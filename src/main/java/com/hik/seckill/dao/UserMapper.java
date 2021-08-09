package com.hik.seckill.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hik.seckill.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SYSTEM
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
