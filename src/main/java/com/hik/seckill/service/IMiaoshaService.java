package com.hik.seckill.service;

import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.domain.OrderInfo;
import com.hik.seckill.vo.GoodsVo;

import java.awt.image.BufferedImage;

/**
 * @author SYSTEM
 */
public interface IMiaoshaService {

    /**
     * 校验验证码
     * @param verifyCode
     * @return
     */
    boolean checkVerifyCodeRegister(int verifyCode);

    /**
     * 注册时的验证码图片
     * @return
     */
    BufferedImage createVerifyCodeRegister();

    /**
     * 秒杀时的验证码图片
     * @param user
     * @param goodsId
     * @return
     */
    BufferedImage createVerifyCode(MiaoshaUser user, long goodsId);

    /**
     * 校验秒杀时的验证码
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode);

    /**
     * 生成秒杀路径
     * @param user
     * @param goodsId
     * @return
     */
    String createMiaoshaPath(MiaoshaUser user, long goodsId);

    /**
     * 验证地址
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(MiaoshaUser user, long goodsId, String path);

    /**
     * 判断是否秒杀成功
     * @param userId
     * @param goodsId
     * @return
     */
    long getMiaoshaResult(Long userId, long goodsId);

    /**
     * 减库存 下订单 写入秒杀订单
     * @param user
     * @param goods
     * @return
     */
    OrderInfo miaosha(MiaoshaUser user, GoodsVo goods);
}
