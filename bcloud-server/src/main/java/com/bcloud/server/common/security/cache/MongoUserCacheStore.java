package com.bcloud.server.common.security.cache;

import com.bcloud.server.common.security.SessionUser;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author JianhuiChen
 * @description 基于mongodb实现的用户缓存策略
 * @date 2020-03-27
 */
public class MongoUserCacheStore implements IUserCacheStore {

    /**
     * 缓存过期时间
     */
    private final long expireSeconds;

    /**
     * mongodb数据操作模板
     */
    private final MongoTemplate mongoTemplate;

    /**
     * token查询用户缓存key
     */
    private static final String TOKEN_USER_KEY = "sys_token_cache";

    /**
     * 用户查询token缓存key
     */
    private static final String USER_TOKEN_KEY = "sys_user_cache";

    public MongoUserCacheStore(MongoTemplate mongoTemplate) {
        this.expireSeconds = 7 * 24 * 60 * 60;
        this.mongoTemplate = mongoTemplate;
        mongoTemplate.createCollection(TOKEN_USER_KEY);
        mongoTemplate.createCollection(USER_TOKEN_KEY);
    }

    public MongoUserCacheStore(long expireSeconds, MongoTemplate mongoTemplate) {
        this.expireSeconds = expireSeconds;
        this.mongoTemplate = mongoTemplate;
        mongoTemplate.createCollection(TOKEN_USER_KEY);
        mongoTemplate.createCollection(USER_TOKEN_KEY);
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
        Query query = Query.query(Criteria.where("token").is(token));
        SessionUser sessionUser = mongoTemplate.findOne(query, SessionUser.class, USER_TOKEN_KEY);
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
     * 检测令牌是否有效
     *
     * @param token 令牌
     * @return 是否有效
     */
    private boolean existTokenUser(String token) {
        Query query = Query.query(Criteria.where("token").is(token));
        return mongoTemplate.exists(query, TOKEN_USER_KEY);
    }

    /**
     * 移除令牌
     *
     * @param token 令牌信息
     */
    private void removeTokenUser(String token) {
        Query query = Query.query(Criteria.where("token").is(token));
        mongoTemplate.remove(query, TOKEN_USER_KEY);
    }

    /**
     * 设置令牌用户
     *
     * @param token 令牌
     * @param user  用户
     */
    private void setTokenUser(String token, SessionUser user) {
        mongoTemplate.save(user, TOKEN_USER_KEY);
    }

    /**
     * 用户是否存在令牌
     *
     * @param userId 用户ID
     * @return 是否存在
     */
    private boolean existUserToken(String userId) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        return mongoTemplate.exists(query, USER_TOKEN_KEY);
    }

    /**
     * 移除用户令牌
     *
     * @param userId 用户ID
     */
    private void removeUserToken(String userId) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        mongoTemplate.remove(query, USER_TOKEN_KEY);
    }

    /**
     * 设置用户令牌
     *
     * @param userId 用户ID
     * @param token  令牌信息
     */
    private void setUserToken(String userId, String token) {
        Document document = new Document();
        document.put("userId", userId);
        document.put("token", token);
        mongoTemplate.save(document, USER_TOKEN_KEY);
    }

    /**
     * 获取用户令牌
     *
     * @param userId 用户ID
     * @return 令牌信息
     */
    private String getUserToken(String userId) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        Document document = mongoTemplate.findOne(query, Document.class, USER_TOKEN_KEY);
        if (document == null) {
            return null;
        }
        return document.getString("userId");
    }
}
