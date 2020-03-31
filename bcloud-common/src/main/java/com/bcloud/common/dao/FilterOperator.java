package com.bcloud.common.dao;

import lombok.Getter;

/**
 * @author JianhuiChen
 * @description 筛选操作符
 * @date 2020-03-26
 */
@Getter
public enum FilterOperator {
    /**
     * equal
     */
    EQ,
    /**
     * not equal
     */
    NEQ,
    /**
     * less than
     */
    LT,
    /**
     * less than or equal to
     */
    LTE,
    /**
     * greater than
     */
    GT,
    /**
     * greater than or equal to
     */
    GTE,
    /**
     * regex matching
     */
    REGEX,
    /**
     * fuzzy matching
     */
    LIKE,
    /**
     * in list
     */
    IN,
    /**
     * not in list
     */
    NIN;
}
