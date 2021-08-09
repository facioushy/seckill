package com.hik.seckill.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hik.seckill.domain.MiaoshaUser;
import jdk.internal.instrumentation.TypeMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author SYSTEM
 */
@TableName("miaosha_user")
@Mapper
public interface MiaoshaUserMapper extends BaseMapper<MiaoshaUser> {

    /**
     * 根据昵称获取用户
     * @param nickname
     * @return
     */
    MiaoshaUser getByNickname(@Param("nickname") String nickname);

}
