package com.bcloud.server.system.out;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.entity.BaseEntity;
import com.bcloud.common.entity.system.RoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Set;

/**
 * @author JianhuiChen
 * @description 系统角色视图层实体
 * @date 2020-03-27
 */
@Getter
@Setter
@Schema(title = "系统角色信息")
public class RoleVO extends BaseEntity {

    @Schema(title = "角色名称")
    private String name;

    @Schema(title = "角色描述")
    private String desc;

    @Schema(title = "角色当前的状态")
    private RoleEntity.Status status;

    @Schema(title = "拥有的菜单标志列表")
    private Set<String> menuKeys;

    @Schema(title = "拥有的资源标志列表")
    private Set<String> resourceKeys;

    /**
     * VO 实体转换层
     */
    public static class VOConverter implements IConverter<RoleVO, RoleEntity> {

        @Override
        public RoleVO doBackward(RoleEntity roleEntity) {
            RoleVO vo = new RoleVO();
            BeanUtils.copyProperties(roleEntity, vo);
            return vo;
        }
    }
}
