package com.bcloud.common.dao;

import lombok.Getter;

/**
 * @author JianhuiChen
 * @description 筛选逻辑符
 * @date 2020-03-26
 */
@Getter
public enum FilterLogic {
    /**
     * and logic
     */
    AND,
    /**
     * or logic
     */
    OR,
    /**
     * not logic
     */
    NOR;
}
