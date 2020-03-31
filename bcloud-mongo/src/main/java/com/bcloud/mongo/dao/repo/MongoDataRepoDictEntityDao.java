package com.bcloud.mongo.dao.repo;

import com.bcloud.common.dao.repo.IDataRepoDictEntityDao;
import com.bcloud.common.entity.repo.DataRepoDictEntity;
import com.bcloud.mongo.dao.AbsMongoDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author JianhuiChen
 * @description 数据仓库字典项操作接口实现
 * @date 2020-03-26
 */
public class MongoDataRepoDictEntityDao extends AbsMongoDao<DataRepoDictEntity> implements IDataRepoDictEntityDao {
    public MongoDataRepoDictEntityDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
