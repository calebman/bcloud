package com.bcloud.server.repo.service.impl;

import com.bcloud.server.common.sys.CommonProvider;
import com.bcloud.server.repo.out.DataRepoColumnVO;
import com.bcloud.server.repo.service.IDataRepoColumnService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 数据仓库服务层实现
 * @date 2020-03-28
 */
@Service
public class DataRepoColumnService extends CommonProvider implements IDataRepoColumnService {

    @Override
    public List<DataRepoColumnVO> findByRepo(String repoId) {
        return getDataRepoColumnEntityDao().findAll(Collections.singletonMap("belongRepo", repoId))
                .stream()
                .map(getDataRepoColumnVOConverter()::doBackward)
                .collect(Collectors.toList());
    }

    private DataRepoColumnVO.VOConverter getDataRepoColumnVOConverter() {
        return new DataRepoColumnVO.VOConverter(getDataRepoDictEntityDao());
    }
}
