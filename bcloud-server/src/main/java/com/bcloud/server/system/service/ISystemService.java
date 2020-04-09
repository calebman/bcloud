package com.bcloud.server.system.service;

import com.bcloud.server.common.security.SessionUser;
import com.bcloud.server.system.in.SystemConfRemakeDTO;

/**
 * @author JianhuiChen
 * @description 系统服务层接口
 * @date 2020-03-29
 */
public interface ISystemService {

    /**
     * 初始化系统 数据与文件的持久层
     *
     * @param confRemakeDTO 重置配置传输实体
     */
    void initSystem(SystemConfRemakeDTO confRemakeDTO);

    /**
     * 初始化系统资源
     */
    void initResource();

    /**
     * 用户登出
     *
     * @param user 当前登录用户
     */
    void logout(SessionUser user);
}
