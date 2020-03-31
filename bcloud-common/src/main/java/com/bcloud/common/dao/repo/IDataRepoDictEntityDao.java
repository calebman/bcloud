package com.bcloud.common.dao.repo;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.IDao;
import com.bcloud.common.entity.repo.DataRepoDictEntity;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 数据仓库字典操作接口
 * @date 2020-03-26
 */
public interface IDataRepoDictEntityDao extends IDao<DataRepoDictEntity> {

    /**
     * 根据字典所属仓库查询字典列表
     *
     * @param belongRepo 所属数据仓库
     * @return 字典集合列表
     */
    default List<DataRepoDictEntity> findByRepo(String belongRepo) {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        filters.put("belongRepo", Pair.of(FilterLogic.AND, FilterOperator.EQ), belongRepo);
        return findAll(filters);
    }
}
