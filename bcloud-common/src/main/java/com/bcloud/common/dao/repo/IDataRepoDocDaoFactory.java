package com.bcloud.common.dao.repo;


import com.bcloud.common.dao.IDao;
import com.bcloud.common.entity.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JianhuiChen
 * @description 数据仓库中的文档数据访问对象工厂
 * @date 2020-03-26
 */
public interface IDataRepoDocDaoFactory {

    /**
     * 根据mongodb的集合名称获取数据访问对象
     *
     * @param repoId 数据仓库ID
     * @return 数据访问对象
     */
    IDataRepoDocDao getDaoByRepo(String repoId);

    /**
     * 数据仓库中的文档数据操作接口
     */
    interface IDataRepoDocDao extends IDao<HashMap<String, Object>> {

        /**
         * 获取数据仓库ID
         *
         * @return 数据仓库ID
         */
        String getRepoId();
    }
}
