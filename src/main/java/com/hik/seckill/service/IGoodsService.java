package com.hik.seckill.service;

import com.hik.seckill.vo.GoodsVo;

import java.util.List;

/**
 * @author SYSTEM
 */
public interface IGoodsService {

    /**
     * 获得商品列表
     * @return
     */
    List<GoodsVo> listGoodsVo();

    /**
     * 根据商品ID获得商品
     * @param goodsId
     * @return
     */
    GoodsVo getGoodsVoByGoodsId(long goodsId);

    /**
     * 减少
     * @param goods
     * @return
     */
    boolean reduceStock(GoodsVo goods);
}
