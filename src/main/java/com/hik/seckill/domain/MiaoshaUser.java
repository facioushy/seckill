package com.hik.seckill.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author SYSTEM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoshaUser {
    /**
     * 用户id
     */
    @TableId
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值 用来加密
     */
    private String salt;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册日期
     */
    private Date registerDate;

    /**
     * 最后一次登录日期
     */
    private Date lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;
}
