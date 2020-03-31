package com.bcloud.server.common.valid;

import com.bcloud.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

/**
 * @author JianhuiChen
 * @description 数据持久层类型校验
 * @date 2019-08-12
 */
@Getter
@Setter
public class OptionsValidator implements ConstraintValidator<Options, String> {

    /**
     * 前缀提示信息
     */
    private String prefixMessage;

    /**
     * 枚举对象
     */
    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(Options options) {
        this.prefixMessage = options.message();
        this.enumClass = options.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        Enum[] enums = enumClass.getEnumConstants();
        String msg = String.format("%s，可选值为[%s]", getPrefixMessage(), StringUtils.join(",", enums));
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        return Stream.of(enums).anyMatch(o -> o.toString().equals(value));
    }
}
