package com.hik.seckill.exception;

import com.baomidou.mybatisplus.extension.api.R;
import com.hik.seckill.common.enums.ResultStatus;
import com.hik.seckill.common.resultbean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import java.util.List;

/**
 * 拦截异常
 * @author SYSTEM
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value=Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getStatus());
         }else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            /**
             * 打印堆栈信息
             */
            logger.error(String.format(msg, msg));
            return Result.error(ResultStatus.SESSION_ERROR);
        }else {
            return Result.error(ResultStatus.SYSTEM_ERROR);
        }
    }
}
