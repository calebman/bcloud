package com.bcloud.server.common.security.encoder;

/**
 * @author JianhuiChen
 * @description 密码编码器
 * @date 2020-03-28
 */
public interface PasswordEncoder {

    /**
     * 对原始密码进行编码
     */
    String encode(String rawPassword);

    /**
     * 验证从存储中获得的编码密码与提交的原始密码是否匹配
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 编码密码
     * @return true代表匹配成功
     */
    boolean matches(String rawPassword, String encodedPassword);
}
