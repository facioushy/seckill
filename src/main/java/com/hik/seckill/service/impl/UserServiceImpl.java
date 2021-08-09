package com.hik.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hik.seckill.dao.UserMapper;
import com.hik.seckill.domain.User;
import com.hik.seckill.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @author SYSTEM
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
