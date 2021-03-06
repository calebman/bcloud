package com.bcloud.server.system.controller;

import com.bcloud.server.common.pojo.BaseBody;
import com.bcloud.server.common.security.SessionUser;
import com.bcloud.server.system.in.SystemConfRemakeDTO;
import com.bcloud.server.system.in.UserLoginDTO;
import com.bcloud.server.system.in.UserRegisterDTO;
import com.bcloud.server.system.service.ISystemService;
import com.bcloud.server.system.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author JianhuiChen
 * @description 系统模块控制层
 * @date 2020-03-28
 */
@RestController
@RequestMapping("system")
@RequiredArgsConstructor
@Tag(name = "SystemController", description = "提供用户登录登出注册等接口")
public class SystemController {

    /**
     * 用户服务层接口
     */
    private final IUserService userService;

    /**
     * 系统服务层接口
     */
    private final ISystemService systemService;

    /**
     * 初始化系统
     *
     * @param confRemakeDTO 系统重置配置信息
     * @return 重置结果
     */
    @PostMapping("init")
    @Operation(summary = "重置系统")
    public BaseBody initSystem(@RequestBody @Valid SystemConfRemakeDTO confRemakeDTO) {
        systemService.initSystem(confRemakeDTO);
        return BaseBody.buildSuccess();
    }

    /**
     * 用户登录接口
     *
     * @param loginDTO 登录信息
     * @return 响应体 包含令牌
     */
    @PostMapping("login")
    @Operation(summary = "用户登录")
    public BaseBody<String> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        return BaseBody.buildSuccess(userService.login(loginDTO));
    }

    /**
     * 用户注册接口
     *
     * @param registerDTO 用户注册信息
     * @return 响应体
     */
    @PostMapping("register")
    @Operation(summary = "用户注册")
    public BaseBody registerUser(@RequestBody @Valid UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return BaseBody.buildSuccess();
    }

    /**
     * 用户登出
     *
     * @param user 当前登录用户
     */
    @PostMapping("logout")
    @Operation(summary = "用户登出")
    public void logout(@Parameter(hidden = true) SessionUser user) {
        systemService.logout(user);
    }
}
