package com.bcloud.common.entity.repo;

import com.bcloud.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author JianhuiChen
 * @description 数据仓库实体
 * @date 2020-03-25
 */
@Getter
@Setter
@ToString
public class DataRepoEntity extends BaseEntity {

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 仓库描述
     */
    private String desc;

    /**
     * 仓库所属用户
     */
    private String belongUser;
}
