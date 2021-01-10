package com.bcloud.server;

import com.alibaba.fastjson.JSONObject;
import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

@SpringBootTest
@Slf4j
class ServerApplicationTests {

    @Test
    void contextLoads() {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        filters.put("account", Pair.of(FilterLogic.AND, FilterOperator.EQ), "test3");
        String json = JSONObject.toJSONString(filters.rowMap());
        log.info("Filter json {}", json);
    }

}
