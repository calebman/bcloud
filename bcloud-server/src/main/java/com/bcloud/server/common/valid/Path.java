package com.bcloud.server.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author JianhuiChen
 * @description 磁盘路径校验注解
 * @date 2019-08-12
 */
@Constraint(validatedBy = PathValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {

    String message() default "路径不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
