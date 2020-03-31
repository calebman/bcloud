package com.bcloud.server.common.pojo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author JianhuiChen
 * @description 系统菜单定义 用于权限管理中菜单部分
 * @date 2020-03-27
 */
@Getter
@RequiredArgsConstructor
public enum SysMenu {
    USER_MANAGER("UserManager", "用户管理", false),
    ROLE_MANAGER("RoleManager", "角色管理", false);

    /**
     * 菜单标志
     */
    private final String key;

    /**
     * 菜单名称
     */
    private final String name;

    /**
     * 是否默认授予
     */
    private final boolean defaultGrant;
}
