package com.bcloud.server.repo.out;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.entity.BaseEntity;
import com.bcloud.common.entity.repo.DataRepoDictEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 数据仓库字典集合视图层实体
 * @date 2020-03-26
 */
@Getter
@Setter
@Schema(title = "数据仓库字典集合")
public class RepoDictVO extends BaseEntity {

    @Schema(title = "字典集合名称")
    private String name;

    @Schema(title = "字典集合描述")
    private String desc;

    @Schema(title = "字典选项")
    private List<String> items;

    @Schema(title = "所属数据仓库")
    private DataRepoVO belongRepo;

    /**
     * 将持久层实体转换为视图层实体
     */
    public static class VOConverter implements IConverter<RepoDictVO, DataRepoDictEntity> {

        @Override
        public RepoDictVO doBackward(DataRepoDictEntity dataRepoDictEntity) {
            RepoDictVO vo = new RepoDictVO();
            BeanUtils.copyProperties(dataRepoDictEntity, vo);
            return vo;
        }
    }
}
