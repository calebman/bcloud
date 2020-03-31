package com.bcloud.server.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author JianhuiChen
 * @description 数据持久层类型校验
 * @date 2019-08-12
 */
@Constraint(validatedBy = OptionsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Options {

    String message() default "数据不合法";

    Class<? extends Enum> enumClass();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
