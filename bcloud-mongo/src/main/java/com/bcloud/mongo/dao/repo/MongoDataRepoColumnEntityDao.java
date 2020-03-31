package com.bcloud.mongo.dao.repo;

import com.bcloud.mongo.dao.AbsMongoDao;
import com.bcloud.common.dao.repo.IDataRepoColumnEntityDao;
import com.bcloud.common.entity.repo.DataRepoColumnEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 数据仓库列操作接口实现
 * @date 2020-03-25
 */
public class MongoDataRepoColumnEntityDao extends AbsMongoDao<DataRepoColumnEntity> implements IDataRepoColumnEntityDao {

    public MongoDataRepoColumnEntityDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
