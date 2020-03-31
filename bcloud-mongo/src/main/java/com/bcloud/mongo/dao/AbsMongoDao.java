package com.bcloud.mongo.dao;

import com.bcloud.common.entity.BaseEntity;
import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.IDao;
import com.bcloud.common.util.StringUtils;
import com.google.common.collect.Table;
import lombok.Getter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.util.Pair;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author JianhuiChen
 * @description 基于MongoDB的通用数据操作层实现
 * @date 2020-03-25
 */
@Getter
public abstract class AbsMongoDao<T> implements IDao<T> {

    /**
     * MongoDB数据操作模板类
     */
    private final MongoTemplate mongoTemplate;

    /**
     * 文档数据操作的集合名称
     */
    private final String collectionName;

    public AbsMongoDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = StringUtils.toLine(getTClass().getSimpleName());
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
    }

    public AbsMongoDao(MongoTemplate mongoTemplate, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
    }

    private Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T findOne(Object id) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, getTClass(), getCollectionName());
    }

    @Override
    public List<T> findAll(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        if (null == filters || filters.isEmpty()) {
            return mongoTemplate.findAll(getTClass(), getCollectionName());
        }
        Query query = this.transferFiltersToQuery(filters);
        return mongoTemplate.find(query, getTClass(), getCollectionName());
    }

    @Override
    public Page<T> findPage(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters, Pageable pageable, Sort sort) {
        if (sort == null) {
            sort = Sort.unsorted();
        }
        if (pageable == null) {
            pageable = PageRequest.of(0, getDefaultPageSize());
        }
        Query query = new Query().with(sort).with(pageable);
        if (null != filters && !filters.isEmpty()) {
            query = this.transferFiltersToQuery(filters).with(sort).with(pageable);
        }
        final List<T> pageList = mongoTemplate.find(query, getTClass(), getCollectionName());
        final long totalCnt = mongoTemplate.count(query, getTClass(), getCollectionName());
        return PageableExecutionUtils.getPage(pageList, pageable, () -> totalCnt);
    }

    @Override
    public long count(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        if (null != filters && !filters.isEmpty()) {
            Query query = this.transferFiltersToQuery(filters);
            return mongoTemplate.count(query, getTClass(), getCollectionName());
        }
        return mongoTemplate.count(new Query(), getTClass(), getCollectionName());
    }

    @Override
    public T save(T entity) {
        return mongoTemplate.save(entity, getCollectionName());
    }

    @Override
    public Collection<T> save(List<T> entities) {
        return mongoTemplate.insert(entities, getCollectionName());
    }

    @Override
    public boolean exists(T entity) {
        return mongoTemplate.exists(getIdQuery(entity), entity.getClass(), getCollectionName());
    }

    @Override
    public boolean exists(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        if (null != filters && !filters.isEmpty()) {
            Query query = this.transferFiltersToQuery(filters);
            return mongoTemplate.exists(query, getTClass(), getCollectionName());
        }
        return false;
    }

    @Override
    public boolean existsById(Object id) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.exists(query, getTClass(), getCollectionName());
    }

    @Override
    public T update(T entity) {
        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setUpdateAt(new Date());
        }
        return mongoTemplate.save(entity, getCollectionName());
    }

    @Override
    public void delete(T entity) {
        mongoTemplate.remove(getIdQuery(entity), entity.getClass(), getCollectionName());
    }

    @Override
    public void deleteById(Object id) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, getTClass(), getCollectionName());
    }

    @Override
    public void deleteAll(Collection<T> entities) {
        entities.forEach(this::delete);
    }


    /**
     * 将筛选对象转换为mongodb所需的query对象
     *
     * @param filters 筛选对象
     * @return query对象
     */
    public Query transferFiltersToQuery(Table<String, Pair<FilterLogic, FilterOperator>, Object> filters) {
        Query query = new Query();
        Criteria start = new Criteria();
        for (Table.Cell<String, Pair<FilterLogic, FilterOperator>, Object> cell : filters.cellSet()) {
            buildCriteria(cell).ifPresent(criteria -> {
                Pair<FilterLogic, FilterOperator> operator = cell.getColumnKey();
                assert operator != null;
                switch (operator.getFirst()) {
                    case AND:
                        start.andOperator(criteria);
                        break;
                    case OR:
                        start.orOperator(criteria);
                        break;
                    case NOR:
                        start.norOperator(criteria);
                        break;
                }
            });
        }
        query.addCriteria(start);
        return query;
    }

    /**
     * 根据筛选对象的单元格构建查询条件
     *
     * @param cell 筛选单元格信息
     * @return 查询条件对象的包装
     */
    private Optional<Criteria> buildCriteria(Table.Cell<String, Pair<FilterLogic, FilterOperator>, Object> cell) {
        String queryKey = cell.getRowKey();
        Object value = cell.getValue();
        Pair<FilterLogic, FilterOperator> operator = cell.getColumnKey();
        if (queryKey == null || "".equals(queryKey) || operator == null) {
            return Optional.empty();
        }
        switch (operator.getSecond()) {
            case EQ:
                return Optional.of(Criteria.where(queryKey).is(value));
            case NEQ:
                return Optional.of(Criteria.where(queryKey).ne(value));
            case LT:
                return Optional.of(Criteria.where(queryKey).lt(value));
            case LTE:
                return Optional.of(Criteria.where(queryKey).lte(value));
            case GT:
                return Optional.of(Criteria.where(queryKey).gt(value));
            case GTE:
                return Optional.of(Criteria.where(queryKey).gte(value));
            case REGEX:
                if (value != null) {
                    return Optional.of(Criteria.where(queryKey).regex(Pattern.compile(value.toString())));
                }
            case LIKE:
                if (value != null) {
                    return Optional.of(Criteria.where(queryKey).regex(value.toString()));
                }
            case IN:
                if (value instanceof Collection) {
                    Collection list = (Collection) value;
                    return Optional.of(Criteria.where(queryKey).in(list));
                }
            case NIN:
                if (value instanceof Collection) {
                    Collection list = (Collection) value;
                    return Optional.of(Criteria.where(queryKey).nin(list));
                }
        }
        return Optional.empty();
    }

    /**
     * 获取实体的ID信息
     *
     * @param t 实体对象
     * @return ID信息
     */
    private Query getIdQuery(T t) {
        Pair<String, Object> id = this.extractIdPropertyAndValue(t);
        return new Query(Criteria.where(id.getFirst()).is(id.getSecond()));
    }

    /**
     * 获取实体的ID键值对
     *
     * @param t 实体对象
     * @return ID键值对
     */
    private Pair<String, Object> extractIdPropertyAndValue(T t) {
        Assert.notNull(t, "Id cannot be extracted from 'null'.");
        Class<?> objectType = t.getClass();
        if (t instanceof Document) {
            return Pair.of("_id", ((Document) t).get("_id"));
        } else {
            MongoPersistentEntity<?> entity = mongoTemplate.getConverter().getMappingContext().getPersistentEntity(objectType);
            if (entity != null && entity.hasIdProperty()) {
                MongoPersistentProperty idProperty = entity.getIdProperty();
                if (idProperty == null) {
                    throw new MappingException("No id property found for object of type " + objectType);
                }
                Object value = entity.getPropertyAccessor(t).getProperty(idProperty);
                if (value == null) {
                    throw new MappingException("No id property " + idProperty.getFieldName() + " value for object of type " + objectType);
                }
                return Pair.of(idProperty.getFieldName(), value);
            } else {
                throw new MappingException("No id property found for object of type " + objectType);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Mongo database dao implementation, Using the database %s", this.getMongoTemplate().getDb().getName());
    }
}
