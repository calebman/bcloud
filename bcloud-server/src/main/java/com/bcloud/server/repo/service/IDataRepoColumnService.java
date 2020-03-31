package com.bcloud.server.repo.service;

import com.bcloud.server.repo.out.DataRepoColumnVO;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 数据仓库数据列服务层接口
 * @date 2020-03-26
 */
public interface IDataRepoColumnService {

    /**
     * 根据仓库ID查询其数据列
     *
     * @param repoId 仓库ID
     * @return 数据列集合
     */
    List<DataRepoColumnVO> findByRepo(String repoId);
}
