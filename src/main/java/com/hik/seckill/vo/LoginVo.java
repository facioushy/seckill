package com.hik.seckill.vo;

import com.hik.seckill.validator.MobileCheck;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author SYSTEM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {

    @NotNull
    @MobileCheck
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
