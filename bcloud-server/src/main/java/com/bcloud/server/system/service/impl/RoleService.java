package com.bcloud.server.system.service.impl;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.server.common.sys.CommonProvider;
import com.bcloud.server.system.out.RoleVO;
import com.bcloud.server.system.service.IRoleService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 角色服务接口实现
 * @date 2020-03-27
 */
@Slf4j
@Service
public class RoleService extends CommonProvider implements IRoleService {

    @Override
    public List<RoleVO> findByIds(Collection<String> roleIds) {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        filters.put("_id", Pair.of(FilterLogic.AND, FilterOperator.IN), roleIds);
        RoleVO.VOConverter roleVOConverter = getRoleVOConverter();
        return getRoleEntityDao().findAll(filters).stream().map(roleVOConverter::doBackward).collect(Collectors.toList());
    }

    public RoleVO.VOConverter getRoleVOConverter() {
        return new RoleVO.VOConverter();
    }
}
