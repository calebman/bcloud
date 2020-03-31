package com.bcloud.server.common.security;

/**
 * @author JianhuiChen
 * @description 访问被拒绝异常
 * @date 2020-03-28
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
