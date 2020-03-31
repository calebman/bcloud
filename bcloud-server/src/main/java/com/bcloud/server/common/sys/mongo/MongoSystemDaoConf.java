package com.bcloud.server.common.sys.mongo;

import com.bcloud.server.common.sys.SystemDaoConf;
import lombok.ToString;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @author JianhuiChen
 * @description 基于mongodb的数据持久层配置
 * @date 2020-03-29
 */
@ToString
public class MongoSystemDaoConf extends BaseMongoConf implements SystemDaoConf {

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
