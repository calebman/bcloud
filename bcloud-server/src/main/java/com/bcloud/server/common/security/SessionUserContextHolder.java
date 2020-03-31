package com.bcloud.server.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.NonNull;

/**
 * @author JianhuiChen
 * @description 用户详情信息上下文获取
 * 此类的作用是便于service层直接获取当前登录用户 而不需要通过参数传递来获取
 * @date 2019-08-07
 */
@Slf4j
public abstract class SessionUserContextHolder {

    /**
     * 存储资源服务正在访问的认证用户
     */
    private static final ThreadLocal<SessionUser> resUserHolder = new NamedThreadLocal<>("Security user context.");

    /**
     * 在当前线程重设资源用户
     */
    public static void resetUser() {
        log.debug("SessionUserContextHolder now cleared, as request processing completed");
        resUserHolder.remove();
    }

    /**
     * 设置资源服务访问用户
     *
     * @param sessionUser 认证用户对象
     */
    public static void setUser(@NonNull SessionUser sessionUser) {
        log.debug("SessionUserContextHolder set user, as request pre handle");
        if (sessionUser == null) {
            resetUser();
        } else {
            resUserHolder.set(sessionUser);
        }
    }

    /**
     * 获取当前访问的资源用户
     *
     * @return 认证用户
     */
    @NonNull
    public static SessionUser getUser() {
        return resUserHolder.get();
    }
}
