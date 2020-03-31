package com.bcloud.server.system.in;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.entity.system.UserEntity;
import com.bcloud.common.util.RandomUtils;
import com.bcloud.server.common.security.encoder.PasswordEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author JianhuiChen
 * @description 用户注册传输层实体
 * @date 2020-03-27
 */
@Getter
@Setter
public class UserRegisterDTO {

    /**
     * 账号
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-z]\\w{3,15}$", message = "账号由字母、数字、下划线组成，字母开头，4-16位")
    private String account;

    /**
     * 邮箱
     */
    @NotBlank
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "邮箱格式不合法")
    private String email;

    /**
     * 密码
     */
    @NotBlank
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$", message = "密码强度不够，至少8个字符且包含字母与数字")
    private String password;

    /**
     * 姓名
     */
    @NotBlank
    private String name;

    /**
     * 传输层实体转换
     */
    @RequiredArgsConstructor
    public static class DTOConverter implements IConverter<UserEntity, UserRegisterDTO> {

        /**
         * 密码加密组件
         */
        private final PasswordEncoder passwordEncoder;

        /**
         * 默认的头像key
         */
        private final String DEFAULT_AVATAR_KEY = "default_avatar";

        /**
         * 默认的角色key
         */
        private final Set<String> DEFAULT_ROLE_IDS = new HashSet<>(Collections.singletonList("default_role"));

        @Override
        public UserEntity doBackward(UserRegisterDTO userRegisterDTO) {
            UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(userRegisterDTO, userEntity);
            String slat = RandomUtils.getRandomStr(false, 6);
            userEntity.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword() + slat));
            userEntity.setStatus(UserEntity.Status.ENABLE);
            userEntity.setSlat(slat);
            userEntity.setAvatarKey(DEFAULT_AVATAR_KEY);
            userEntity.setRoleIds(DEFAULT_ROLE_IDS);
            return userEntity;
        }
    }
}
