package com.bcloud.mongo.dao.system;

import com.bcloud.common.dao.system.IRoleEntityDao;
import com.bcloud.common.entity.system.RoleEntity;
import com.bcloud.mongo.dao.AbsMongoDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author JianhuiChen
 * @description 系统角色数据操作接口实现
 * @date 2020-03-27
 */
public class MongoRoleEntityDao extends AbsMongoDao<RoleEntity> implements IRoleEntityDao {
    public MongoRoleEntityDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }
}
