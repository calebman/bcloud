package com.bcloud.server.system;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.system.IUserEntityDao;
import com.bcloud.common.entity.system.UserEntity;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.util.Collections;

@SpringBootTest
@Slf4j
class MongoUserEntityDaoTests {

    @Resource
    private IUserEntityDao userEntityDao;

    @Test
    void contextLoads() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAccount("test_1");
        userEntity.setEmail("chenjianhui0428@gmail.com");
        userEntity.setExtraInfo(Collections.singletonMap("theme", "light"));
        userEntity.setName("张三");
        userEntity.setPassword("123456");
        userEntity.setSlat("123456");
        userEntity.setStatus(UserEntity.Status.ENABLE);
        UserEntity saveResult = userEntityDao.save(userEntity);
        log.info("Save user entity result {}", saveResult);
        saveResult.setStatus(UserEntity.Status.DISABLED);
        log.info("Update user test {}", userEntityDao.update(saveResult));
        log.info("Exists test {}", userEntityDao.exists(saveResult));
        log.info("Exists by id test {}", userEntityDao.existsById(saveResult.getId()));
        log.info("Find one test {}", userEntityDao.findOne(saveResult.getId()));
        userEntityDao.delete(saveResult);
        log.info("Delete test {}", userEntityDao.findOne(saveResult.getId()));
        log.info("All users {}", userEntityDao.findAll());
    }

    @Test
    void testFilters() {
        if (userEntityDao.count() < 100) {
            for (int i = 0; i < 100; i++) {
                UserEntity userEntity = new UserEntity();
                userEntity.setAccount("test" + i);
                userEntity.setEmail("chenjianhui0428@gmail.com");
                userEntity.setExtraInfo(Collections.singletonMap("theme", "light"));
                userEntity.setName("张三" + i);
                userEntity.setPassword("123456");
                userEntity.setSlat("123456");
                userEntity.setStatus(UserEntity.Status.ENABLE);
                userEntityDao.save(userEntity);
            }
        }
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        filters.put("account", Pair.of(FilterLogic.AND, FilterOperator.EQ), "test3");
        log.info("Filter result {}", userEntityDao.findAll(filters));
        filters = HashBasedTable.create();
        filters.put("account", Pair.of(FilterLogic.AND, FilterOperator.LIKE), "test3");
        log.info("Filter result {}", userEntityDao.findAll(filters));
    }
}
