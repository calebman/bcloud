package com.bcloud.server.common.sys.mongo;

import com.bcloud.server.common.sys.SystemFileConf;
import lombok.ToString;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @author JianhuiChen
 * @description 基于Mongo DB 文件持久化配置
 * @date 2020-03-30
 */
@ToString
public class MongoSystemFileConf extends BaseMongoConf implements SystemFileConf {

    @Override
    public StorageType getStorageType() {
        return StorageType.MONGO;
    }

    @Override
    public Map<String, Object> getProperties() {
        return BeanMap.create(this);
    }

    @Override
    public void test() throws RuntimeException {

    }
}
