package com.bcloud.server.repo.service.impl;

import com.bcloud.common.entity.repo.DataRepoEntity;
import com.bcloud.server.common.sys.CommonProvider;
import com.bcloud.server.repo.out.DataRepoVO;
import com.bcloud.server.repo.service.IDataRepoService;
import org.springframework.stereotype.Service;

/**
 * @author JianhuiChen
 * @description 数据仓库服务层实现
 * @date 2020-03-28
 */
@Service
public class DataRepoService extends CommonProvider implements IDataRepoService {

    @Override
    public DataRepoVO findById(String repoId) {
        DataRepoEntity entity = getDataRepoEntityDao().findOne(repoId);
        if (entity == null) {
            return null;
        }
        return getDataRepoVOConverter().doBackward(entity);
    }

    @Override
    public boolean checkRepoExist(String repoId) {
        return getDataRepoEntityDao().existsById(repoId);
    }

    private DataRepoVO.VOConverter getDataRepoVOConverter() {
        return new DataRepoVO.VOConverter(getUserEntityDao(), getFileService(), getRoleEntityDao(),
                getDataRepoDictEntityDao(), getDataRepoColumnEntityDao());
    }
}
