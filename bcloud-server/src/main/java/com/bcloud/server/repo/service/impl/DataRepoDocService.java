package com.bcloud.server.repo.service.impl;

import com.bcloud.server.common.sys.CommonProvider;
import com.bcloud.server.repo.service.IDataRepoDocService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JianhuiChen
 * @description 数据仓库中的文档操作服务实现
 * @date 2020-03-26
 */
@Service
public class DataRepoDocService extends CommonProvider implements IDataRepoDocService {

    @Override
    public Map<String, Object> save(String repoId, Map<String, Object> data) {
        this.checkRepoExist(repoId);
        HashMap<String, Object> insertDocument = new HashMap<>();
        getDataRepoColumnEntityDao().findByRepo(repoId).forEach(col -> {
            String key = col.getKey();
            insertDocument.put(key, data.get(key));
        });
        return getDataRepoDocDaoFactory().getDaoByRepo(repoId).save(insertDocument);
    }

    @Override
    public Map<String, Object> update(String repoId, String dataId, Map<String, Object> data) {
        this.checkRepoExist(repoId);
        HashMap<String, Object> updateDocument = new HashMap<>();
        getDataRepoColumnEntityDao().findByRepo(repoId).forEach(col -> {
            String key = col.getKey();
            updateDocument.put(key, data.get(key));
        });
        return getDataRepoDocDaoFactory().getDaoByRepo(repoId).save(updateDocument);
    }

    @Override
    public void delete(String repoId, String dataId) {
        this.checkRepoExist(repoId);
        getDataRepoDocDaoFactory().getDaoByRepo(repoId).deleteById(dataId);
    }

    @Override
    public Map<String, Object> findById(String repoId, String dataId) {
        this.checkRepoExist(repoId);
        return getDataRepoDocDaoFactory().getDaoByRepo(repoId).findOne(dataId);
    }

    /**
     * 检查数据仓库是否存在 不存在抛出异常终止后续执行
     *
     * @param repoId 数据仓库ID
     */
    private void checkRepoExist(String repoId) {
        if (!getDataRepoEntityDao().existsById(repoId)) {
            throw new RuntimeException("无法操作一个不存在的数据仓库");
        }
    }
}
