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

    @Override
    public RepoDictVO createDict(String belongRepo, RepoDictDTO repoDictDTO) {
        // converter define
        RepoDictVO.VOConverter repoDicrVOConverter = createComponent(RepoDictVO.VOConverter.class);
        RepoDictDTO.DTOConverter repoDictDTOConverter = createComponent(RepoDictDTO.DTOConverter.class);
        // create
        DataRepoDictEntity repoDictEntity = repoDictDTOConverter.doBackward(repoDictDTO);
        repoDictEntity.setBelongRepo(belongRepo);
        DataRepoDictEntity result = getDataRepoDictEntityDao().save(repoDictEntity);
        return repoDicrVOConverter.doBackward(result);
    }

    @Override
    public RepoDictVO updateDict(String dictId, RepoDictDTO repoDictDTO) {
        // check dict id
        this.checkDictExist(dictId);
        // converter define
        RepoDictVO.VOConverter repoDicrVOConverter = createComponent(RepoDictVO.VOConverter.class);
        RepoDictDTO.DTOConverter repoDictDTOConverter = createComponent(RepoDictDTO.DTOConverter.class);
        // update
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
        // converter define
        RepoDictVO.VOConverter repoDicrVOConverter = createComponent(RepoDictVO.VOConverter.class);
        // find dict
        DataRepoDictEntity result = getDataRepoDictEntityDao().findOne(dictId);
        return repoDicrVOConverter.doBackward(result);
    }

    @Override
    public List<RepoDictVO> getDictsByRepo(String belongRepo) {
        // converter define
        RepoDictVO.VOConverter repoDicrVOConverter = createComponent(RepoDictVO.VOConverter.class);
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
