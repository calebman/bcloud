package com.bcloud.mongo.dao.repo;

import com.bcloud.common.dao.repo.IDataRepoDocDaoFactory;
import com.bcloud.mongo.dao.AbsMongoDao;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 数据仓库文档数据操作接口的工厂
 * @date 2020-03-26
 */
public class MongoDataRepoDocDaoFactory extends ConcurrentHashMap<String, IDataRepoDocDaoFactory.IDataRepoDocDao> implements IDataRepoDocDaoFactory {

    /**
     * mongodb数据操作模板对象
     */
    private final MongoTemplate mongoTemplate;

    public MongoDataRepoDocDaoFactory(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public IDataRepoDocDao getDaoByRepo(String repoId) {
        if (this.containsKey(repoId)) {
            return this.get(repoId);
        }
        IDataRepoDocDao dataRepoDocDao = new MongoDataRepoDocDao(mongoTemplate, repoId);
        this.put(repoId, dataRepoDocDao);
        return dataRepoDocDao;
    }


    /**
     * 数据仓库中的文档数据操作接口实现
     */
    @Getter
    public static class MongoDataRepoDocDao extends AbsMongoDao<HashMap<String, Object>> implements IDataRepoDocDao {

        /**
         * 数据仓库ID
         */
        private final String repoId;

        /**
         * 文档创建时间key
         */
        private final String createAtKey = "_createAt";

        /**
         * 文档更新时间key
         */
        private final String updateAtKey = "_updateAt";

        MongoDataRepoDocDao(MongoTemplate mongoTemplate, String repoId) {
            super(mongoTemplate, String.format("data_repo_%s", repoId));
            this.repoId = repoId;
        }

        @Override
        public HashMap<String, Object> save(HashMap<String, Object> entity) {
            entity.put(createAtKey, new Date());
            entity.put(updateAtKey, new Date());
            return super.save(entity);
        }

        @Override
        public Collection<HashMap<String, Object>> save(List<HashMap<String, Object>> entities) {
            entities = entities.stream().peek(o -> {
                o.put(createAtKey, new Date());
                o.put(updateAtKey, new Date());
            }).collect(Collectors.toList());
            return super.save(entities);
        }

        @Override
        public HashMap<String, Object> update(HashMap<String, Object> entity) {
            entity.put(updateAtKey, new Date());
            return super.update(entity);
        }
    }
}
