package com.hik.seckill.controller;

import com.hik.seckill.common.enums.ResultStatus;
import com.hik.seckill.common.resultbean.Result;
import com.hik.seckill.service.impl.MiaoshaServiceImpl;
import com.hik.seckill.service.impl.MiaoshaUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author SYSTEM
 */
@Controller
@RequestMapping("/user")
public class RegisterController {

    private static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private MiaoshaUserServiceImpl miaoshaUserService;

    @Autowired
    private MiaoshaServiceImpl miaoshaService;

    @RequestMapping("/do_register")
    public String registerIndex() {
        return "register";
    }

    @RequestMapping("/register")
    @ResponseBody
    public Result<String> register(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("verifyCode") String verifyCode,
                                   @RequestParam("salt") String salt,
                                   HttpServletResponse response) {
        Result<String> result = Result.build();

        boolean check = miaoshaService.checkVerifyCodeRegister(Integer.valueOf(verifyCode));
        if (!check) {
            result.withError(ResultStatus.CODE_FAIL.getCode(),
            ResultStatus.CODE_FAIL.getMessage());
            return result;
        }

        boolean registerInfo = miaoshaUserService.register(response, username, password, salt);
        if (!registerInfo) {
            result.withError(ResultStatus.RESIGETER_FAIL.getCode(), ResultStatus.RESIGETER_FAIL.getMessage());
            return result;
        }
        return result;
    }

}
