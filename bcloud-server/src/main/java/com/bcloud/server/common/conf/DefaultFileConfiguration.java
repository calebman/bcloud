package com.bcloud.server.common.conf;

import com.bcloud.common.file.IFileService;
import com.bcloud.common.file.endpoint.FileEndpoint;
import com.bcloud.common.file.impl.local.LocalFileService;
import com.bcloud.common.file.impl.local.LocalFileServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JianhuiChen
 * @description 默认的文件服务配置
 * @date 2020-03-29
 */
@Configuration
public class DefaultFileConfiguration {

    /**
     * 配置文件端点控制层
     *
     * @return 文件服务端点
     */
    @Bean
    public FileEndpoint fileEndpoint() {
        return new FileEndpoint();
    }
}
