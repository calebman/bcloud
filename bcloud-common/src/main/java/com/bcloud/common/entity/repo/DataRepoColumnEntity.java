package com.bcloud.common.entity.repo;

import com.bcloud.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author JianhuiChen
 * @description 数据仓库的列定义实体
 * @date 2020-03-25
 */
@Getter
@Setter
@ToString
public class DataRepoColumnEntity extends BaseEntity {

    /**
     * 列标志键
     */
    private String key;

    /**
     * 列名称
     */
    private String name;

    /**
     * 列描述
     */
    private String desc;

    /**
     * 列类型
     */
    private Type type;

    /**
     * 字典项的key
     */
    private String dictionaryKey;

    /**
     * 列所属数据仓库
     */
    private String belongRepo;

    /**
     * 列类型定义
     */
    public enum Type {
        // 文本
        TEXT,
        // 数字
        NUNBER,
        // 字典
        DICTIONARY,
        // 时间
        DATE,
        // 文件
        FILE
    }
}
