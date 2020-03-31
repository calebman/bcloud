package com.bcloud.server.common.security.cache;

import com.bcloud.server.common.security.SessionUser;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author JianhuiChen
 * @description 基于内存的用户信息缓存
 * @date 2019-08-17
 */
public class MemmoryUserCacheStore implements IUserCacheStore {

    /**
     * 令牌——用户缓存
     */
    private Cache<String, SessionUser> tokenUserCache;

    /**
     * 用户——令牌缓存
     */
    private Cache<String, String> userTokenCache;

    /**
     * 缓存过期时间
     */
    private final long expireSeconds;

    public MemmoryUserCacheStore() {
        this.expireSeconds = 7 * 24 * 60 * 60;
        this.buildCache();
    }

    public MemmoryUserCacheStore(long expireSeconds) {
        this.expireSeconds = expireSeconds;
        this.buildCache();
    }


    @Override
    public void register(SessionUser user) {
        String oldToken;
        if (existUserToken(user.getId())) {
            oldToken = getUserToken(user.getId());
            if (!StringUtils.isEmpty(oldToken)) {
                removeTokenUser(oldToken);
            }
            setTokenUser(user.getToken(), user);
            setUserToken(user.getId(), user.getToken());
        } else {
            setTokenUser(user.getToken(), user);
            setUserToken(user.getId(), user.getToken());
        }
    }

    @Override
    public void unRegister(String token) {
        if (existTokenUser(token)) {
            SessionUser user = getTokenUser(token);
            if (user != null) {
                String uid = user.getId();
                removeUserToken(uid);
            }
            removeTokenUser(token);
        }
    }

    @Override
    public SessionUser getTokenUser(String token) {
        SessionUser sessionUser = tokenUserCache.getIfPresent(token);
        if (sessionUser == null) {
            return null;
        }
        long createAtTime = sessionUser.getCreatAt().getTime();
        long nowTime = new Date().getTime();
        if (nowTime - createAtTime > getExpireSeconds() * 1000) {
            this.unRegister(token);
            return null;
        }
        return sessionUser;
    }

    @Override
    public long getExpireSeconds() {
        return this.expireSeconds;
    }

    /**
     * 构建缓存对象
     */
    private void buildCache() {
        tokenUserCache = CacheBuilder.newBuilder()
                .expireAfterWrite(this.expireSeconds, TimeUnit.SECONDS)
                .maximumSize(1000)
                .build();
        userTokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(this.expireSeconds, TimeUnit.SECONDS)
                .maximumSize(1000)
                .build();
    }

    /**
     * 检测令牌是否有效
     *
     * @param token 令牌
     * @return 是否有效
     */
    private boolean existTokenUser(String token) {
        return tokenUserCache.getIfPresent(token) != null;
    }

    /**
     * 移除令牌
     *
     * @param token 令牌信息
     */
    private void removeTokenUser(String token) {
        tokenUserCache.invalidate(token);
    }

    /**
     * 设置令牌用户
     *
     * @param token 令牌
     * @param user  用户
     */
    private void setTokenUser(String token, SessionUser user) {
        tokenUserCache.put(token, user);
    }

    /**
     * 用户是否存在令牌
     *
     * @param userId 用户ID
     * @return 是否存在
     */
    private boolean existUserToken(String userId) {
        return userTokenCache.getIfPresent(userId) != null;
    }

    /**
     * 移除用户令牌
     *
     * @param userId 用户ID
     */
    private void removeUserToken(String userId) {
        userTokenCache.invalidate(userId);
    }

    /**
     * 设置用户令牌
     *
     * @param userId 用户ID
     * @param token  令牌信息
     */
    private void setUserToken(String userId, String token) {
        userTokenCache.put(userId, token);
    }

    /**
     * 获取用户令牌
     *
     * @param userId 用户ID
     * @return 令牌信息
     */
    private String getUserToken(String userId) {
        return userTokenCache.getIfPresent(userId);
    }
}
