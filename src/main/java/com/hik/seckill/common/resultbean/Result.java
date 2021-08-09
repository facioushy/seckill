package com.hik.seckill.common.resultbean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hik.seckill.common.enums.ResultStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author SYSTEM
 */
public class Result<T> extends AbstractResult implements Serializable {

    private static final long serialVersionUID = 867933019328199779L;

    @Getter @Setter @JsonSerialize @JsonDeserialize
    private T data;

    @Getter @Setter
    private Integer count;

    protected Result(ResultStatus status, String message) {
        super(status, message);
    }

    protected Result(ResultStatus status) {
        super(status);
    }

    public static <T> Result<T> build() {
        return new Result(ResultStatus.SUCCESS, (String)null);
    }

    public static <T> Result<T> build(String message) {
        return new Result(ResultStatus.SUCCESS, message);
    }

    public static <T> Result<T> error(ResultStatus status) {
        return new Result<T>(status);
    }

    public void success(T value) {
        this.success();
        this.data = value;
        this.count = 0;
    }
}
