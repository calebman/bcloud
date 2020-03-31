package com.bcloud.mongo.dao.system;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.system.IUserEntityDao;
import com.bcloud.common.entity.system.UserEntity;
import com.bcloud.mongo.dao.AbsMongoDao;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 用户信息数据操作实现
 * @date 2020-03-25
 */
public class MongoUserEntityDao extends AbsMongoDao<UserEntity> implements IUserEntityDao {

    public MongoUserEntityDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
