package com.bcloud.server.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author JianhuiChen
 * @description 磁盘路径校验类
 * @date 2019-08-12
 */
public class PathValidator implements ConstraintValidator<Path, String> {

    /**
     * windows系统的路径正则匹配
     */
    private final Pattern windowsPattern = Pattern.compile("^[a-zA-Z]:(//[^///:\"<>/|]+)+$");

    /**
     * linux系统的路径正则匹配
     */
    private final Pattern linxPattern = Pattern.compile("^/([\\w.-]+/?)+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.toLowerCase().startsWith("windows")) {
            return windowsPattern.matcher(value).matches();
        } else {
            return linxPattern.matcher(value).matches();
        }
    }
}
