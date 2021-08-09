package com.hik.seckill.service.impl;

import com.hik.seckill.dao.OrderMapper;
import com.hik.seckill.domain.MiaoshaOrder;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.domain.OrderInfo;
import com.hik.seckill.redis.OrderKey;
import com.hik.seckill.redis.RedisService;
import com.hik.seckill.service.IOrderService;
import com.hik.seckill.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author fanbinhai
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public MiaoshaOrder getMiaoshaOrderByUserGoodsId(long userId, long goodsId) {
        return (MiaoshaOrder) redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId,MiaoshaOrder.class);
    }

    @Override
    public OrderInfo getOrderById(long orderId) {
        return orderMapper.getOrderById(orderId);
    }

    @Override
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(Long.valueOf(user.getNickname()));
        orderMapper.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getNickname()+"_"+goods.getId(), miaoshaOrder);
        return orderInfo;
    }

    @Override
    public void closeOrder(int hour) {

    }
}
