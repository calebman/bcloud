package com.bcloud.server.repo.out;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.dao.repo.IDataRepoDictEntityDao;
import com.bcloud.common.entity.repo.DataRepoColumnEntity;
import com.bcloud.common.entity.repo.DataRepoDictEntity;
import com.bcloud.common.entity.repo.DataRepoEntity;
import com.bcloud.server.repo.in.RepoDictDTO;
import com.bcloud.server.repo.service.IDataRepoDictService;
import com.bcloud.common.entity.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author JianhuiChen
 * @description 数据仓库的列定义实体
 * @date 2020-03-25
 */
@Getter
@Setter
public class DataRepoColumnVO extends BaseEntity {

    /**
     * 列标志键
     */
    private String key;

    /**
     * 列名称
     */
    private String name;

    /**
     * 列描述
     */
    private String desc;

    /**
     * 字典集合 如果类型 type = DICTIONARY
     */
    private RepoDictVO dictionary;

    /**
     * 列类型
     */
    private DataRepoColumnEntity.Type type;

    /**
     * VO 实体转换类
     */
    @RequiredArgsConstructor
    public static class VOConverter implements IConverter<DataRepoColumnVO, DataRepoColumnEntity> {

        /**
         * 字典集合信息服务接口
         */
        private final IDataRepoDictEntityDao dataRepoDictEntityDao;

        /**
         * 字典视图层转换
         */
        private RepoDictVO.VOConverter repoDicrVOConverter = new RepoDictVO.VOConverter();

        @Override
        public DataRepoColumnVO doBackward(DataRepoColumnEntity dataRepoColumnEntity) {
            DataRepoColumnVO vo = new DataRepoColumnVO();
            DataRepoDictEntity dataRepoDictEntity = dataRepoDictEntityDao.findOne(dataRepoColumnEntity.getDictionaryKey());
            vo.setDictionary(repoDicrVOConverter.doBackward(dataRepoDictEntity));
            BeanUtils.copyProperties(dataRepoColumnEntity, vo);
            return vo;
        }
    }
}
