package com.bcloud.mysql.dao;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.IDao;
import com.google.common.collect.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 基于mysqldb实现的通用数据访问层
 * @date 2020-03-29
 */
@RequiredArgsConstructor
public class AbsMysqlDao<T> implements IDao<T> {

    /**
     * jdbc操作模板
     */
    private final JdbcTemplate jdbcTemplate;

    @Override
    public T findOne(Object id) {
        return null;
    }

    @Override
    public List<T> findAll(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        return null;
    }

    @Override
    public Page<T> findPage(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters, Pageable pageable, Sort sort) {
        return null;
    }

    @Override
    public long count(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        return 0;
    }

    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public Collection<T> save(List<T> entities) {
        return null;
    }

    @Override
    public boolean exists(T entity) {
        return false;
    }

    @Override
    public boolean exists(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        return false;
    }

    @Override
    public boolean existsById(Object id) {
        return false;
    }

    @Override
    public T update(T entity) {
        return null;
    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public void deleteById(Object id) {

    }

    @Override
    public void deleteAll(Collection<T> entities) {

    }
}
