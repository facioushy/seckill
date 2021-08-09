package com.hik.seckill.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hik.seckill.common.enums.ResultStatus;
import com.hik.seckill.dao.MiaoshaUserMapper;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.exception.GlobalException;
import com.hik.seckill.redis.MiaoshaUserKey;
import com.hik.seckill.redis.RedisService;
import com.hik.seckill.service.IMiaoshaUserService;
import com.hik.seckill.service.IUserService;
import com.hik.seckill.utils.MD5Utils;
import com.hik.seckill.utils.UUIDUtil;
import com.hik.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author SYSTEM
 */
@Service
public class MiaoshaUserServiceImpl extends ServiceImpl<MiaoshaUserMapper, MiaoshaUser> implements IMiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    private static Logger logger = LoggerFactory.getLogger(MiaoshaUserServiceImpl.class);

    @Autowired
    private MiaoshaUserMapper miaoshaUserMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = (MiaoshaUser) redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    @Override
    public MiaoshaUser getByNickName(String nickname) {
        return null;
    }

    @Override
    public boolean updatePassword(String token, String nickname, String formPass) {
        return false;
    }

    @Override
    public boolean register(HttpServletResponse response, String username, String password, String salt) {
        MiaoshaUser miaoshaUser = new MiaoshaUser();
        miaoshaUser.setId(Long.parseLong(username));
        miaoshaUser.setNickname(username);
        String DBPassword = MD5Utils.inputPass2DBPass(password, salt);
        miaoshaUser.setPassword(DBPassword);
        miaoshaUser.setRegisterDate(new Date());
        miaoshaUser.setSalt(salt);
        miaoshaUser.setNickname(username);
        try{
            miaoshaUserMapper.insert(miaoshaUser);
            MiaoshaUser user = miaoshaUserMapper.getByNickname(miaoshaUser.getNickname());
            if (user == null){
                return false;
            }
            String token = UUIDUtil.uuid();
            addCookie(response, token, user);
        } catch (Exception e){
            logger.error("注册失败", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(ResultStatus.SYSTEM_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        MiaoshaUser user = getById(mobile);
        if (user == null) {
            throw new GlobalException(ResultStatus.MOBILE_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        String saltDb = user.getSalt();
        String calcPass = MD5Utils.inputPass2DBPass(password, saltDb);
        if (!calcPass.equals(dbPass)){
            throw new GlobalException(ResultStatus.PASSWORD_ERROR);
        }
        // 生成cookie 将session返回浏览器 分布式session
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    @Override
    public String createToken(HttpServletResponse response, LoginVo loginVo) {
        return null;
    }

    public void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置token有效期
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
