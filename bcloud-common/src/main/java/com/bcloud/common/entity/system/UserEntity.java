package com.bcloud.common.entity.system;

import com.bcloud.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * @author JianhuiChen
 * @description 系统用户实体
 * @date 2020-03-25
 */
@Getter
@Setter
@ToString
public class UserEntity extends BaseEntity {

    /**
     * 账号
     */
    private String account;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String slat;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像 文件key值
     */
    private String avatarKey;

    /**
     * 用户当前的状态
     */
    private Status status;

    /**
     * 用户拥有的角色标志列表
     */
    private Set<String> roleIds;

    /**
     * 额外信息 如系统采用主题、样式等等
     */
    private Map<String, String> extraInfo;

    /**
     * 用户状态
     */
    public enum Status {
        ENABLE,
        DISABLED
    }
}
