package com.hik.seckill.controller;

import com.hik.seckill.access.AccessLimit;
import com.hik.seckill.common.enums.ResultStatus;
import com.hik.seckill.common.resultbean.Result;
import com.hik.seckill.domain.MiaoshaOrder;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.rabbitmq.MQSender;
import com.hik.seckill.rabbitmq.MiaoshaMessage;
import com.hik.seckill.redis.GoodsKey;
import com.hik.seckill.redis.MiaoshaUserKey;
import com.hik.seckill.redis.RedisService;
import com.hik.seckill.service.impl.GoodsServiceImpl;
import com.hik.seckill.service.impl.MiaoshaServiceImpl;
import com.hik.seckill.service.impl.OrderServiceImpl;
import com.hik.seckill.utils.CommonUtil;
import com.hik.seckill.utils.CookieUtils;
import com.hik.seckill.vo.GoodsVo;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static com.hik.seckill.common.enums.ResultStatus.*;

/**
 * @author SYSTEM
 */
@Controller
@Slf4j
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    private String COOKIE_ID = "";

    private Long GOODS_ID;

    private MiaoshaUser verifyUser;

    @Autowired
    private GoodsServiceImpl goodsService;

    @Autowired
    private MiaoshaServiceImpl miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private MQSender mqSender;

    private HashMap<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @ResponseBody
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    public Result<Integer> miaosha(@PathVariable("path") String path,
                                   @RequestParam("goodsId") long goodsId,
                                   HttpServletRequest request) {
        Result<Integer> result = Result.build();
        String token = CookieUtils.getTokenFromCookies(request);
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        // 验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
            return result;
        }
        //是否已经秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserGoodsId(user.getId(), goodsId);
        if (order != null) {
            result.withError(REPEATE_MIAOSHA.getCode(), REPEATE_MIAOSHA.getMessage());
            return result;
        }
        // 内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            result.withError(MIAO_SHA_OVER.getCode(),MIAO_SHA_OVER.getMessage());
            return result;
        }
        // 预见库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            result.withError(MIAO_SHA_OVER.getCode(),MIAO_SHA_OVER.getMessage());
            return result;
        }
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setGoodsId(goodsId);
        mm.setUser(user);
        mqSender.sendMiaoshaMessage(mm);
        return result;
    }

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> miaoshaResult(Model model, HttpServletRequest request,
                                      @RequestParam("goodsId") long goodsId) {
        Result<String> result = Result.build();
        String token = CookieUtils.getTokenFromCookies(request);
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        model.addAttribute("user", user);
        Long miaoshaResult = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        System.out.println(miaoshaResult);
        result.setData(miaoshaResult+"");
        System.out.println(CommonUtil.bean2String(result));
        return result;
    }


    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode) {
        Result<String> result = Result.build();
        String token = CookieUtils.getTokenFromCookies(request);
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            result.withError(REQUEST_ILLEGAL.getCode(), REQUEST_ILLEGAL.getMessage());
            return result;
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        result.setData(path);
        return result;
    }

    @RequestMapping(value = "/verifyCodeRegister", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response) {
        Result<String> result = Result.build();
        try {
            BufferedImage image = miaoshaService.createVerifyCodeRegister();
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return result;
        } catch (Exception e){
            log.error("生成验证码错误=======注册:{}", e);
            result.withError(ResultStatus.MIAOSHA_FAIL.getCode(),
                    ResultStatus.MIAOSHA_FAIL.getMessage());
            return result;
        }
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam("goodsId") Long goodsId) {
        Result<String> result = Result.build();
        String token = CookieUtils.getTokenFromCookies(request);
        this.COOKIE_ID = token;
        this.GOODS_ID = goodsId;
        verifyUser = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, this.COOKIE_ID, MiaoshaUser.class);
        if (verifyUser == null) {
            result.withError(SESSION_ERROR.getCode(),
                    SESSION_ERROR.getMessage());
            return result;
        }
        return result;
    }

    @RequestMapping("/verifyCodeGenerator")
    @ResponseBody
    public Result<String> verifyCodeGenerator(HttpServletResponse response) {
        Result<String> result = Result.build();
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, this.COOKIE_ID, MiaoshaUser.class);
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, this.GOODS_ID);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return result;
        } catch (Exception e) {
            log.error("生成验证码错误=======goodsId:{}", this.GOODS_ID, e);
            result.withError(ResultStatus.MIAOSHA_FAIL.getCode(), ResultStatus.MIAOSHA_FAIL.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
