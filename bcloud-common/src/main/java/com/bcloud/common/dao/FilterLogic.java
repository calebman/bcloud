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
     * logic and
     */
    AND,
    /**
     * logic or
     */
    OR,
    /**
     * logic not
     */
    NOR;
}
