package com.hik.seckill.service;

import com.hik.seckill.domain.MiaoshaOrder;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.domain.OrderInfo;
import com.hik.seckill.vo.GoodsVo;

/**
 * @author fanbinhai
 */
public interface IOrderService {

    /**
     * 根据用户ID和商品ID获取秒杀订单
     * @param userId
     * @param goodsId
     * @return
     */
    MiaoshaOrder getMiaoshaOrderByUserGoodsId(long userId, long goodsId);

    /**
     * 根据订单ID获取订单信息
     * @param orderId
     * @return
     */
    OrderInfo getOrderById(long orderId);

    /**
     * 创建订单
     * @param user
     * @param goods
     * @return
     */
    OrderInfo createOrder(MiaoshaUser user, GoodsVo goods);

    /**
     * 关闭订单
     * @param hour
     */
    void closeOrder(int hour);
}
