package com.bcloud.mongo.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * @author JianhuiChen
 * @description mongodb配置信息
 * @date 2020-03-23
 */
@Getter
@Setter
@ToString
@PropertySource("service.dao.mongo")
public class MongoDaoServiceConfig {

    /**
     * 连接地址
     */
    @Value("${uri:mongodb://localhost}")
    private String uri;

    /**
     * 数据库
     */
    @Value("${dbName:default}")
    private String dbName;
}
