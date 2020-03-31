package com.bcloud.server.repo;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.repo.IDataRepoDocDaoFactory;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
@Slf4j
class DataRepoDocDaoTests {

    @Resource
    private IDataRepoDocDaoFactory dataRepoDocDaoFactory;

    @Test
    void contextLoads() {
        IDataRepoDocDaoFactory.IDataRepoDocDao dataRepoDocDao = dataRepoDocDaoFactory.getDaoByRepo("test_data_repo");
        if (dataRepoDocDao.count() < 100) {
            for (int i = 0; i < 100; i++) {
                HashMap<String, Object> document = new HashMap<>();
                document.put("appName", "测试应用" + i);
                document.put("appDesc", "测试描述");
                document.put("appKey", "appKey" + i);
                dataRepoDocDao.save(document);
            }
        }
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        filters.put("appName", Pair.of(FilterLogic.AND, FilterOperator.EQ), "测试应用3");
        log.info("Filter result {}", dataRepoDocDao.findAll(filters));
        filters = HashBasedTable.create();
        filters.put("appName", Pair.of(FilterLogic.AND, FilterOperator.LIKE), "测试应用3");
        log.info("Filter result {}", dataRepoDocDao.findAll(filters));
    }
}
