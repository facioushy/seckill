package com.hik.seckill.controller;

import com.hik.seckill.common.resultbean.Result;
import com.hik.seckill.dao.OrderMapper;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.domain.OrderInfo;
import com.hik.seckill.redis.MiaoshaUserKey;
import com.hik.seckill.redis.OrderKey;
import com.hik.seckill.redis.RedisService;
import com.hik.seckill.service.impl.GoodsServiceImpl;
import com.hik.seckill.service.impl.MiaoshaUserServiceImpl;
import com.hik.seckill.service.impl.OrderServiceImpl;
import com.hik.seckill.utils.CookieUtils;
import com.hik.seckill.vo.GoodsVo;
import com.hik.seckill.vo.OrderDetailVo;
import org.apache.ibatis.reflection.wrapper.BaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static com.hik.seckill.common.enums.ResultStatus.ORDER_NOT_EXIST;
import static com.hik.seckill.common.enums.ResultStatus.SESSION_ERROR;

/**
 * @author fanbinhai
 */
@RequestMapping("/order")
@Controller
public class OrderController {

    @Autowired
    MiaoshaUserServiceImpl userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    GoodsServiceImpl goodsService;

    @Autowired
    OrderMapper orderMapper;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(@RequestParam("orderId") long orderId,
                                      HttpServletRequest request) {
        Result<OrderDetailVo> result = Result.build();
        String token = CookieUtils.getTokenFromCookies(request);
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            result.withError(ORDER_NOT_EXIST.getCode(), ORDER_NOT_EXIST.getMessage());
            return result;
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(orderInfo);
        vo.setGoods(goods);
        result.setData(vo);
        return result;
    }

    @RequestMapping("/pay")
    @ResponseBody
    public Result<String> pay(@RequestParam("goodsId") long goodsId,
                              @RequestParam("orderId") String orderId,
                              HttpServletRequest request) {
        Result<String> result = Result.build();
        String token = CookieUtils.getTokenFromCookies(request);
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user == null) {
            result.withError(SESSION_ERROR.getCode(), SESSION_ERROR.getMessage());
            return result;
        }
        //redisService.delete(OrderKey.getMiaoshaOrderByUidGid, user.getId()+"_"+goodsId);

        return result;
    }
}
