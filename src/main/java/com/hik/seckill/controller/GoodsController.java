package com.hik.seckill.controller;

import com.hik.seckill.common.resultbean.Result;
import com.hik.seckill.domain.Goods;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.redis.GoodsKey;
import com.hik.seckill.redis.MiaoshaUserKey;
import com.hik.seckill.service.impl.GoodsServiceImpl;
import com.hik.seckill.validator.MobileCheck;
import com.hik.seckill.vo.GoodsDetailVo;
import com.hik.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author SYSTEM
 */
@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController{

    @Autowired
    private GoodsServiceImpl goodsService;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,
                       Model model, MiaoshaUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return render(request, response, model, "goods_list", GoodsKey.getGoodsList, "");
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, @PathVariable("goodsId") long goodsId) {
        Result<GoodsDetailVo> result = Result.build();
        String token = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        //秒杀还没开始
        if (now < startAt) {
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        }
        //秒杀已结束
        else if (now > endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        }
        // 秒杀进行中
        else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        result.setData(vo);
        return result;
    }

}
