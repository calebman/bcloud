package com.bcloud.server.system.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author JianhuiChen
 * @description 用户登陆传输层实体
 * @date 2020-03-27
 */
@Getter
@Setter
@Schema(title = "用户登录信息")
public class UserLoginDTO {

    /**
     * 用户名
     */
    @Schema(title = "用户名", required = true)
    @NotBlank(message = "登录用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @Schema(title = "密码", required = true)
    @NotBlank(message = "登录密码不能为空")
    private String password;
}
