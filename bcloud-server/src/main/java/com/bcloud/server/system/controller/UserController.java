package com.bcloud.server.system.controller;

import com.bcloud.server.common.pojo.BaseBody;
import com.bcloud.server.common.security.SessionUser;
import com.bcloud.server.system.in.UserLoginDTO;
import com.bcloud.server.system.in.UserRegisterDTO;
import com.bcloud.server.system.service.IUserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author JianhuiChen
 * @description 用户模块控制器
 * @date 2020-03-25
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "提供用户相关的接口信息")
public class UserController {

    /**
     * 获取当前登录的用户信息
     *
     * @param user 缓存用户对象
     * @return 响应体
     */
    @GetMapping("me")
    @Operation(summary = "获取当前登录的用户信息")
    public BaseBody<SessionUser> getCurrentUser(@Parameter(hidden = true) SessionUser user) {
        return BaseBody.buildSuccess(user);
    }
}
