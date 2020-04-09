package com.bcloud.server.common.conf;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JianhuiChen
 * @description 接口文档配置信息
 * @date 2020-04-09
 */
@Configuration
public class ApiDocConfiguration {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .setGroup("springshop-public")
                .pathsToMatch("/**")
                .build();
    }
}
