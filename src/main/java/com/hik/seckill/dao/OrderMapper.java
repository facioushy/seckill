package com.hik.seckill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hik.seckill.domain.MiaoshaOrder;
import com.hik.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author fanbinhai
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {

    /**
     * 根据用户ID和商品ID获取秒杀订单
     * @param userNickName
     * @param goodsId
     * @return
     */
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userNickName") long userNickName,
                                                @Param("goodsId") long goodsId);

    /**
     * 新建秒杀订单
     * @param miaoshaOrder
     * @return
     */
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    /**
     * 根据ID获取订单
     * @param orderId
     * @return
     */
    OrderInfo getOrderById(@Param("orderId") long orderId);

    /**
     * 根据创建时间获取所有订单
     * @param status
     * @param createDate
     * @return
     */
    List<OrderInfo> selectOrderStatusByCreateTime(@Param("status")Integer status, @Param("createDate") String createDate);

    /**
     * 关闭订单
     * @return
     */
    int closeOrderByOrderInfo();

}
