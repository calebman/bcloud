package com.bcloud.server.repo.service.impl;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.entity.repo.DataRepoDictEntity;
import com.bcloud.server.common.sys.CommonProvider;
import com.bcloud.server.repo.in.RepoDictDTO;
import com.bcloud.server.repo.out.RepoDictVO;
import com.bcloud.server.repo.service.IDataRepoDictService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 数据仓库字典集合操作服务接口实现
 * @date 2020-03-27
 */
@Service
public class DataRepoDictService extends CommonProvider implements IDataRepoDictService {

    /**
     * 字典集合 传输层实体转换类
     */
    private final RepoDictDTO.DTOConverter repoDictDTOConverter = new RepoDictDTO.DTOConverter();

    /**
     * 字典集合 视图层实体转换类
     */
    private final RepoDictVO.VOConverter repoDicrVOConverter = new RepoDictVO.VOConverter();

    @Override
    public RepoDictVO createDict(String belongRepo, RepoDictDTO repoDictDTO) {
        DataRepoDictEntity repoDictEntity = repoDictDTOConverter.doBackward(repoDictDTO);
        repoDictEntity.setBelongRepo(belongRepo);
        DataRepoDictEntity result = getDataRepoDictEntityDao().save(repoDictEntity);
        return repoDicrVOConverter.doBackward(result);
    }

    @Override
    public RepoDictVO updateDict(String dictId, RepoDictDTO repoDictDTO) {
        this.checkDictExist(dictId);
        DataRepoDictEntity entity = getDataRepoDictEntityDao().findOne(dictId);
        DataRepoDictEntity repoDictEntity = repoDictDTOConverter.doBackward(repoDictDTO);
        BeanUtils.copyProperties(repoDictEntity, entity);
        DataRepoDictEntity result = getDataRepoDictEntityDao().update(entity);
        return repoDicrVOConverter.doBackward(result);
    }

    @Override
    public void deleteDict(String dictId) {
        this.checkDictExist(dictId);
        getDataRepoDictEntityDao().deleteById(dictId);
    }

    @Override
    public RepoDictVO getDictById(String dictId) {
        DataRepoDictEntity result = getDataRepoDictEntityDao().findOne(dictId);
        return repoDicrVOConverter.doBackward(result);
    }

    @Override
    public List<RepoDictVO> getDictsByRepo(String belongRepo) {
        return getDataRepoDictEntityDao().findByRepo(belongRepo)
                .stream()
                .map(repoDicrVOConverter::doBackward)
                .collect(Collectors.toList());
    }

    /**
     * 检查字典集合是否存在
     *
     * @param dictId 集合ID
     */
    private void checkDictExist(String dictId) {
        if (!getDataRepoDictEntityDao().existsById(dictId)) {
            throw new RuntimeException("无法操作一个不存在的字典集合");
        }
    }
}
