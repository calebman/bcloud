package com.bcloud.common.dao.system;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.IDao;
import com.bcloud.common.entity.system.UserEntity;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @author JianhuiChen
 * @description 系统用户数据操作接口
 * @date 2020-03-25
 */
public interface IUserEntityDao extends IDao<UserEntity> {

    /**
     * 根据账户查询用户实体
     *
     * @param account 账户信息
     * @return 用户实体
     */
    default UserEntity findByAccount(String account) {
        Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
        filters.put("account", Pair.of(FilterLogic.AND, FilterOperator.EQ), account);
        List<UserEntity> userEntities = findAll(filters);
        if (userEntities.size() > 0) {
            return userEntities.get(0);
        }
        return null;
    }

}
