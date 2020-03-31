package com.bcloud.server.common.sys;

import java.util.Map;

/**
 * @author JianhuiChen
 * @description 系统数据持久层配置
 * @date 2020-03-27
 */
public interface SystemDaoConf {

    /**
     * @return 持久层类型
     */
    StorageType getStorageType();

    /**
     * @return 配置信息
     */
    Map<String, Object> getProperties();

    /**
     * 测试配置信息
     *
     * @throws RuntimeException 测试失败
     */
    void test() throws RuntimeException;

    /**
     * 持久层类型
     */
    enum StorageType {
        MYSQL,
        MONGO
    }
}
