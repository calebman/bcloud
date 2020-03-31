package com.bcloud.server.common.security.encoder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author JianhuiChen
 * @description 基于 BCrypt 算法的密码编码器
 * @date 2020-03-28
 */
@Slf4j
public class BCryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}