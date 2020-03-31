package com.bcloud.server.common.pojo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author JianhuiChen
 * @description 系统资源定义 用于权限管理中request api部分
 * @date 2020-03-27
 */
@Getter
@RequiredArgsConstructor
public enum SysResource {
    USER_MANAGER_STATUS("UserManagerStatus", "用户启用/禁用", "用户管理"),
    ROLE_MANAGE_ADD("RoleManagerAdd", "新增角色", "角色管理"),
    ROLE_MANAGE_UPDATE("RoleManagerUpdate", "编辑角色", "角色管理"),
    ROLE_MANAGE_DELETE("RoleManagerDelete", "删除角色", "角色管理"),
    ROLE_MANAGE_STATUS("RoleManagerStatus", "角色启用/禁用", "角色管理");

    /**
     * 资源标志
     */
    private final String key;

    /**
     * 资源名称
     */
    private final String name;

    /**
     * 资源分组
     */
    private final String group;
}
