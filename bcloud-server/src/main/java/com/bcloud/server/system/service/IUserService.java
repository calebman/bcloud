package com.bcloud.server.system.service;

import com.bcloud.server.common.pojo.BaseBody;
import com.bcloud.server.common.security.SessionUser;
import com.bcloud.server.system.in.UserLoginDTO;
import com.bcloud.server.system.out.UserVO;
import com.bcloud.server.system.in.UserRegisterDTO;

/**
 * @author JianhuiChen
 * @description 用户服务接口
 * @date 2020-03-26
 */
public interface IUserService {

    /**
     * 使用账号密码登录
     *
     * @param loginDTO 登录信息
     * @return 用户令牌信息
     */
    String login(UserLoginDTO loginDTO);

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO findById(String userId);

    /**
     * 注册用户
     *
     * @param registerDTO 用户注册信息实体
     */
    void register(UserRegisterDTO registerDTO);
}
