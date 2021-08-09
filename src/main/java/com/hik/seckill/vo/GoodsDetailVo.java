package com.hik.seckill.vo;

import com.hik.seckill.domain.MiaoshaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanbinhai
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailVo {
    /**
     * 秒杀状态
     */
    private int miaoshaStatus = 0;

    /**
     * 剩余时间
     */
    private int remainSeconds = 0;

    /**
     * 商品
     */
    private GoodsVo goods ;

    /**
     * 用户
     */
    private MiaoshaUser user;
}
