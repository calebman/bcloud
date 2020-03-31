package com.bcloud.server.repo.in;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.entity.repo.DataRepoDictEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 字典集合传输实体
 * @date 2020-03-26
 */
@Getter
@Setter
public class RepoDictDTO {

    /**
     * 字典集合名称
     */
    private String name;

    /**
     * 字典集合描述
     */
    private String desc;

    /**
     * 字典选项
     */
    private List<String> items;

    /**
     * 将传输层实体转换为持久层实体
     */
    public static class DTOConverter implements IConverter<DataRepoDictEntity, RepoDictDTO> {

        @Override
        public DataRepoDictEntity doBackward(RepoDictDTO repoDictDTO) {
            DataRepoDictEntity entity = new DataRepoDictEntity();
            BeanUtils.copyProperties(repoDictDTO, entity);
            return entity;
        }
    }
}
