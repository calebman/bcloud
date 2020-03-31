package com.bcloud.server.common.sys.mongo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author JianhuiChen
 * @description Mongo db 基础配置信息
 * @date 2020-03-31
 */
@Getter
@Setter
abstract class BaseMongoConf {

    /**
     * mongodb连接地址
     */
    @NotBlank(message = "mongodb连接地址不可为空")
    @Pattern(regexp = "mongodb://([\\w.]+/?)\\S*", message = "mongodb连接地址不合法，示例mongodb://localhost")
    private String uri;

    /**
     * mongodb数据库名称
     */
    @NotBlank(message = "mongodb数据库名称不可为空")
    @Pattern(regexp = "[^%&',;=?$\\x22]+", message = "数据库名称不可包含特殊字符")
    private String dbName;
}
