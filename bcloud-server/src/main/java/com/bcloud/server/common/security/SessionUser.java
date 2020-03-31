package com.bcloud.server.common.security;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.dao.system.IRoleEntityDao;
import com.bcloud.common.entity.system.UserEntity;
import com.bcloud.common.file.IFileService;
import com.bcloud.server.system.out.RoleVO;
import com.bcloud.server.system.out.UserVO;
import com.bcloud.server.system.service.IRoleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * @author JianhuiChen
 * @description 缓存用户信息 结合SpringSecurity框架使用 可直接在Controller层作为参数注入
 * @date 2019/5/27
 */
@Getter
@Setter
public class SessionUser extends UserVO {

    /**
     * 用户令牌信息
     */
    private String token;

    /**
     * 拥有的菜单标志列表
     */
    private Set<String> menuKeys;

    /**
     * 拥有的资源标志列表
     */
    private Set<String> resourceKeys;

    /**
     * 将用户实体信息转换为带权限的用户信息
     */
    @RequiredArgsConstructor
    public static class UserConverter implements IConverter<SessionUser, UserEntity> {

        /**
         * 文件服务 用于头像外链转换
         */
        private final IFileService fileService;

        /**
         * 角色服务
         */
        private final IRoleEntityDao roleEntityDao;

        @Override
        public SessionUser doBackward(UserEntity userEntity) {
            SessionUser sessionUser = new SessionUser();
            IConverter<UserVO, UserEntity> voConverter = new VOConverter(fileService, roleEntityDao);
            UserVO vo = voConverter.doBackward(userEntity);
            // 补全基本信息
            BeanUtils.copyProperties(vo, sessionUser);
            // 补全权限信息
            Set<String> menuKeys = new HashSet<>();
            Set<String> resourceKeys = new HashSet<>();
            for (RoleVO role : vo.getRoles()) {
                menuKeys.addAll(role.getMenuKeys());
                resourceKeys.addAll(role.getResourceKeys());
            }
            sessionUser.setMenuKeys(menuKeys);
            sessionUser.setResourceKeys(resourceKeys);
            // 补全令牌信息
            sessionUser.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
            return sessionUser;
        }
    }
}
