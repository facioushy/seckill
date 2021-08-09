package com.hik.seckill.rabbitmq;

import com.hik.seckill.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author fanbinhai
 */
@Data
public class MiaoshaMessage {

    private MiaoshaUser user;

    private long goodsId;




}
