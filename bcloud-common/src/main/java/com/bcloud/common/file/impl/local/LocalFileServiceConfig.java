package com.bcloud.common.file.impl.local;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@PropertySource("service.file.local")
public class LocalFileServiceConfig {

    /**
     * 外链的端点地址
     */
    @Value("${endpoint:/service/file/download/}")
    private String endpoint;

    /**
     * 文件存储的文件夹
     */
    @Value("${folder:/data/upload}")
    private String folder;
}
