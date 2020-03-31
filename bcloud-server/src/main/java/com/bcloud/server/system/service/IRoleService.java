package com.bcloud.server.system.service;

import com.bcloud.server.system.out.RoleVO;

import java.util.Collection;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 角色服务接口
 * @date 2020-03-26
 */
public interface IRoleService {

    /**
     * 根据角色ID列表获取角色信息列表
     *
     * @param roleIds 角色ID列表
     * @return 角色列表信息
     */
    List<RoleVO> findByIds(Collection<String> roleIds);
}
