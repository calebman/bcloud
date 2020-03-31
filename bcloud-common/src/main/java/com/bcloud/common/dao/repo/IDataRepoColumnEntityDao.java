package com.bcloud.common.dao.repo;

import com.bcloud.common.dao.IDao;
import com.bcloud.common.entity.repo.DataRepoColumnEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 数据仓库列操作接口
 * @date 2020-03-25
 */
public interface IDataRepoColumnEntityDao extends IDao<DataRepoColumnEntity> {


    /**
     * 根据数据仓库查询列集合
     *
     * @param repoId 数据仓库ID
     * @return 列集合
     */
    default List<DataRepoColumnEntity> findByRepo(String repoId) {
        return this.findAll(Collections.singletonMap("belongRepo", repoId));
    }
}
