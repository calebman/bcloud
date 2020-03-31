package com.bcloud.common.entity.system;

import com.bcloud.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author JianhuiChen
 * @description 系统角色实体
 * @date 2020-03-27
 */
@Getter
@Setter
@ToString
public class RoleEntity extends BaseEntity {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String desc;

    /**
     * 角色当前的状态
     */
    private Status status;

    /**
     * 拥有的菜单标志列表
     */
    private Set<String> menuKeys;

    /**
     * 拥有的资源标志列表
     */
    private Set<String> resourceKeys;

    /**
     * 角色状态
     */
    public enum Status {
        ENABLE,
        DISABLED
    }
}
