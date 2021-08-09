package com.hik.seckill.controller;

import com.hik.seckill.common.resultbean.Result;
import com.hik.seckill.service.impl.MiaoshaServiceImpl;
import com.hik.seckill.service.impl.MiaoshaUserServiceImpl;
import com.hik.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author SYSTEM
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoshaUserServiceImpl miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        //logger.info();
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        Result<Boolean> result = Result.build();
        logger.info(loginVo.toString());
        miaoshaUserService.login(response, loginVo);
        return result;
    }
}
