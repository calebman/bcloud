package com.bcloud.mongo.dao.repo;

import com.bcloud.common.entity.repo.DataRepoEntity;
import com.bcloud.common.dao.repo.IDataRepoEntityDao;
import com.bcloud.mongo.dao.AbsMongoDao;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author JianhuiChen
 * @description 数据仓库操作接口实现
 * @date 2020-03-25
 */
public class MongoDataRepoEntityDao extends AbsMongoDao<DataRepoEntity> implements IDataRepoEntityDao {

    public MongoDataRepoEntityDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
