package com.hik.seckill.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanbinhai
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoshaOrder {

    /**
     * 秒杀订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long goodsId;
}
