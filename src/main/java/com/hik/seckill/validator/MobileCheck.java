package com.hik.seckill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author SYSTEM
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {MobileValidator.class})
public @interface MobileCheck {
    boolean required() default true;

    String message() default "手机号码格式错误！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
