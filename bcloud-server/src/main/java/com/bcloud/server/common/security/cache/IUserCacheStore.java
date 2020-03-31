package com.bcloud.server.common.security.cache;


import com.bcloud.server.common.security.SessionUser;

/**
 * @author JianhuiChen
 * @description 用户缓存仓库
 * @date 2019-08-12
 */
public interface IUserCacheStore {

    /**
     * 注册缓存用户
     *
     * @param user 用户对象
     */
    void register(SessionUser user);

    /**
     * 移除缓存用户
     *
     * @param token 访问令牌
     */
    void unRegister(String token);

    /**
     * 读取用户信息
     *
     * @param token 令牌
     * @return 用户信息
     */
    SessionUser getTokenUser(String token);

    /**
     * 获取缓存的过期时间 单位：秒
     *
     * @return 过期秒数 默认七天
     */
    default long getExpireSeconds() {
        return 7 * 24 * 60 * 60;
    }
}
