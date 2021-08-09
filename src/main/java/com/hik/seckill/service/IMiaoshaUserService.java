package com.hik.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hik.seckill.domain.MiaoshaUser;
import com.hik.seckill.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author SYSTEM
 */
public interface IMiaoshaUserService extends IService<MiaoshaUser> {

    /**
     * 根据token获取用户
     * @param response
     * @param token
     * @return
     */
    MiaoshaUser getByToken(HttpServletResponse response, String token);

    /**
     * 根据昵称获取用户
     * @param nickname
     * @return
     */
    MiaoshaUser getByNickName(String nickname);

    /**
     * 更改密码
     * @param token
     * @param nickname
     * @param formPass
     * @return
     */
    boolean updatePassword(String token, String nickname, String formPass);

    /**
     * 注册
     * @param response
     * @param username
     * @param password
     * @param salt
     * @return
     */
    boolean register(HttpServletResponse response, String username, String password, String salt);

    /**
     * 登录
     * @param response
     * @param loginVo
     * @return
     */
    boolean login(HttpServletResponse response, LoginVo loginVo);

    /**
     * 生成token
     * @param response
     * @param loginVo
     * @return
     */
    String createToken(HttpServletResponse response, LoginVo loginVo);

    /**
     * 把cookie加到token中
     * @param response
     * @param token
     */
}
