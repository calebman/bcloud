package com.bcloud.server.repo.service;

import com.bcloud.server.repo.out.DataRepoVO;

/**
 * @author JianhuiChen
 * @description 数据仓库服务层
 * @date 2020-03-26
 */
public interface IDataRepoService {

    /**
     * 根据仓库ID查询数据仓库详细信息
     *
     * @param repoId 仓库ID
     * @return 数据仓库详细信息
     */
    DataRepoVO findById(String repoId);

    /**
     * 检查数据仓库是否存在
     *
     * @param repoId 仓库ID
     * @return true:存在 false:不存在
     */
    boolean checkRepoExist(String repoId);
}
