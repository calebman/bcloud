package com.bcloud.common.dao;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author JianhuiChen
 * @description 通用数据操作接口定义
 * @date 2020-03-25
 */
public interface IDao<T> {

    /**
     * 根据主键查询一个实体
     *
     * @param id 主键ID
     * @return 查询出的对象
     */
    T findOne(final Object id);

    /**
     * 查询所有实体
     *
     * @return 查询出的对象列表
     */
    default List<T> findAll() {
        return findAll(HashBasedTable.create());
    }

    /**
     * 条件查询
     *
     * @param equalFilters 等于条件下的键值对
     * @return 查询出的对象列表
     */
    default List<T> findAll(Map<String, Object> equalFilters) {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        equalFilters.forEach((key, value) -> filters.put(key, Pair.of(FilterLogic.AND, FilterOperator.EQ), value));
        return findAll(filters);
    }

    /**
     * 条件查询
     *
     * @param filters 筛选对象
     * @return 查询出的对象列表
     */
    List<T> findAll(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters);

    default Page<T> findPage() {
        return findPage(HashBasedTable.create());
    }

    default Page<T> findPage(Map<String, Object> equalFilters) {
        return findPage(equalFilters, PageRequest.of(0, getDefaultPageSize()));
    }

    /**
     * 分页查询实体
     *
     * @param equalFilters 等于条件下的键值对
     * @param pageable     分页参数
     * @return 分页实体列表
     */
    default Page<T> findPage(Map<String, Object> equalFilters, Pageable pageable) {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        equalFilters.forEach((key, value) -> filters.put(key, Pair.of(FilterLogic.AND, FilterOperator.EQ), value));
        return findPage(filters, pageable);
    }

    default Page<T> findPage(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        return findPage(filters, PageRequest.of(0, getDefaultPageSize()));
    }

    default Page<T> findPage(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters, Pageable pageable) {
        return findPage(filters, pageable, Sort.unsorted());
    }

    /**
     * 分页排序查询实体
     *
     * @param filters  筛选对象
     * @param pageable 分页参数
     * @param sort     排序参数
     * @return 分页实体列表
     */
    Page<T> findPage(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters, Pageable pageable, Sort sort);

    /**
     * 返回总记录数
     *
     * @return 总记录数
     */
    default long count() {
        return count(HashBasedTable.create());
    }


    /**
     * 根据条件返回总记录数
     *
     * @param equalFilters 等于条件下的键值对
     * @return 总记录数
     */
    default long count(Map<String, Object> equalFilters) {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        equalFilters.forEach((key, value) -> filters.put(key, Pair.of(FilterLogic.AND, FilterOperator.EQ), value));
        return count(filters);
    }

    /**
     * 根据条件返回总记录数
     *
     * @param filters 条件
     * @return 总记录数
     */
    long count(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters);

    /**
     * 保存实体并返回主键
     *
     * @param entity 对象实例
     * @return 查询出的对象
     */
    T save(final T entity);

    /**
     * 批量保存实体
     *
     * @param entities 实体列表
     */
    Collection<T> save(final List<T> entities);

    /**
     * 查询实体是否存在
     *
     * @param entity 对象实例
     * @return 是否存在
     */
    boolean exists(final T entity);

    /**
     * 根据条件查询实体是否存在
     *
     * @param equalFilters 等于条件下的键值对
     * @return 是否存在
     */
    default boolean exists(Map<String, Object> equalFilters) {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        equalFilters.forEach((key, value) -> filters.put(key, Pair.of(FilterLogic.AND, FilterOperator.EQ), value));
        return exists(filters);
    }

    /**
     * 根据条件查询实体是否存在
     *
     * @param filters 条件
     * @return 是否存在
     */
    boolean exists(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters);

    /**
     * 查询实体是否存在
     *
     * @param id 主键ID
     * @return 是否存在
     */
    boolean existsById(final Object id);

    /**
     * 保存并返回实体
     *
     * @param entity 对象实例
     * @return 查询出的对象
     */
    T update(final T entity);

    /**
     * 删除实体
     *
     * @param entity 对象实例
     */
    void delete(final T entity);

    /**
     * 删除实体
     *
     * @param id 对象唯一标志
     */
    void deleteById(final Object id);

    /**
     * 删除实体的集合
     *
     * @param entities 对象实例
     */
    void deleteAll(Collection<T> entities);

    /**
     * 获取默认的分页大小
     *
     * @return 分页大小
     */
    default int getDefaultPageSize() {
        return 10;
    }
}
