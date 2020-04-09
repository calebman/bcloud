package com.bcloud.server.repo.in;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.entity.repo.DataRepoDictEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 字典集合传输实体
 * @date 2020-03-26
 */
@Getter
@Setter
@Schema(title = "字典集合传输实体")
public class RepoDictDTO {

    @NotBlank
    @Schema(title = "字典集合名称", required = true)
    private String name;

    @NotBlank
    @Schema(title = "字典集合描述", required = true)
    private String desc;

    @NotEmpty
    @Schema(title = "字典选项", required = true)
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
