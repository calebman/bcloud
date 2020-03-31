package com.bcloud.server.repo.service;

import org.bson.Document;

import java.util.Map;

/**
 * @author JianhuiChen
 * @description 数据仓库中的文档操作服务接口
 * @date 2020-03-25
 */
public interface IDataRepoDocService {

    /**
     * 保存文档
     *
     * @param repoId 仓库ID
     * @param data   文档数据
     * @return 文档保存结果
     */
    Map<String, Object> save(String repoId, Map<String, Object> data);

    /**
     * 更新文档
     *
     * @param repoId 仓库ID
     * @param dataId 文档ID
     * @param data   文档数据
     * @return 文档更新结果
     */
    Map<String, Object> update(String repoId, String dataId, Map<String, Object> data);

    /**
     * 删除文档
     *
     * @param repoId 仓库ID
     * @param dataId 文档ID
     */
    void delete(String repoId, String dataId);

    /**
     * 查询指定文档
     *
     * @param repoId 仓库ID
     * @param dataId 文档ID
     * @return 文档详情
     */
    Map<String, Object> findById(String repoId, String dataId);
}
