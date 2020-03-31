package com.bcloud.mongo.common;

import com.bcloud.mongo.dao.MongoDaoServiceConfig;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * @author JianhuiChen
 * @description mongo db 核心组件配置类
 * 可使用其快速构建mongo db 的核心组件
 * @date 2020-03-31
 */
@RequiredArgsConstructor
public class MongoClientConfigurtation extends AbstractMongoClientConfiguration {

    /**
     * mongo 持久层配置
     */
    private final MongoDaoServiceConfig mongoDaoServiceConfig;

    @Override
    public MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoDaoServiceConfig.getUri()))
                .build();
        MongoDriverInformation driverInformation = MongoDriverInformation.builder().build();
        return new MongoClientImpl(settings, driverInformation);
    }

    @Override
    protected String getDatabaseName() {
        return mongoDaoServiceConfig.getDbName();
    }

    /**
     * 定义基于mongo实现的GridFS操作模板
     *
     * @return 基于mongo实现的GridFS操作模板
     * @throws Exception 构建时异常
     */
    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }
}
