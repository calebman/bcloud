package com.bcloud.common.entity.repo;

import com.bcloud.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 数据仓库字典实体
 * @date 2020-03-26
 */
@Getter
@Setter
@ToString
public class DataRepoDictEntity extends BaseEntity {

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
     * 所属数据仓库
     */
    private String belongRepo;
}
