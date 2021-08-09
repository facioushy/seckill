package com.hik.seckill.rabbitmq;

import com.hik.seckill.domain.MiaoshaOrder;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.service.impl.GoodsServiceImpl;
import com.hik.seckill.service.impl.MiaoshaServiceImpl;
import com.hik.seckill.service.impl.OrderServiceImpl;
import com.hik.seckill.utils.CommonUtil;
import com.hik.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fanbinhai
 */
@Slf4j
@Service
public class MQReceiver {

    @Autowired
    private GoodsServiceImpl goodsService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private MiaoshaServiceImpl miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoshaMessage mm = CommonUtil.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return;
        }
        // 判断是否已经秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserGoodsId(user.getId(),goodsId);
        if (order != null) {
            return;
        }
        // 减少库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goodsVo);
    }

}
