package com.hik.seckill.exception;

import com.hik.seckill.common.enums.ResultStatus;

/**
 * @author SYSTEM
 */
public class GlobalException extends RuntimeException{

    private ResultStatus status;

    public GlobalException(ResultStatus status) {
        super();
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
