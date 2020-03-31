package com.bcloud.mongo.file;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * @author JianhuiChen
 * @description 本地存储服务配置信息
 * @date 2020-03-23
 */
@Getter
@Setter
@ToString
@PropertySource("service.file.mongo")
public class MongoFileServiceConfig {

    /**
     * 外链的端点地址
     */
    @Value("${endpoint:/service/file/download/}")
    private String endpoint;
}
