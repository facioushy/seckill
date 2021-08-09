package com.hik.seckill.dao;

import com.hik.seckill.domain.MiaoshaGoods;
import com.hik.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author SYSTEM
 */
@Mapper
public interface GoodsMapper {

    /**
     * 列出商品表
     * @return
     */
    List<GoodsVo> listGoodsVo();

    /**
     * 根据商品ID获得商品
     * @param goodsId
     * @return
     */
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 减少库存
     * @param miaoshaGoods
     * @return
     */
    int reduceStock(MiaoshaGoods miaoshaGoods);

}
