package com.bcloud.server.system.service.impl;

import com.bcloud.common.entity.system.UserEntity;
import com.bcloud.server.common.sys.CommonProvider;
import com.bcloud.server.common.security.AccessDeniedException;
import com.bcloud.server.common.security.SessionUser;
import com.bcloud.server.common.security.cache.IUserCacheStore;
import com.bcloud.server.common.security.encoder.PasswordEncoder;
import com.bcloud.server.system.in.UserLoginDTO;
import com.bcloud.server.system.out.UserVO;
import com.bcloud.server.system.service.IUserService;
import com.bcloud.server.system.in.UserRegisterDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * @author JianhuiChen
 * @description 用户服务接口实现
 * @date 2020-03-27
 */
@Slf4j
@Getter
@Service
public class UserService extends CommonProvider implements IUserService {

    /**
     * 用户缓存策略接口
     */
    private final IUserCacheStore userCacheStore;

    /**
     * 密码编码器
     */
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, IUserCacheStore userCacheStore) {
        this.passwordEncoder = passwordEncoder;
        this.userCacheStore = userCacheStore;
    }

    @Override
    public String login(UserLoginDTO loginDTO) {
        log.debug("用户{}进行登录认证操作", loginDTO.getUsername());
        UserEntity userEntity = getUserEntityDao().findByAccount(loginDTO.getUsername());
        // 验证阶段
        if (userEntity == null) {
            throw new AccessDeniedException("账号或密码错误");
        }
        if (userEntity.getStatus() == UserEntity.Status.DISABLED) {
            throw new AccessDeniedException("账户被禁用");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword() + userEntity.getSlat(), userEntity.getPassword())) {
            throw new AccessDeniedException("账号或密码错误");
        }
        if (userEntity.getRoleIds().isEmpty()) {
            throw new AccessDeniedException("账号未被分配任何角色，暂时为不可用状态");
        }
        // 将数据库的用户实体 转换成 带权限的缓存用户
        SessionUser.UserConverter sessionUserConverter = createComponent(SessionUser.UserConverter.class);
        SessionUser sessionUser = sessionUserConverter.doBackward(userEntity);
        userCacheStore.register(sessionUser);
        return sessionUser.getToken();
    }

    @Override
    public UserVO findById(String userId) {
        UserVO.VOConverter userVoConverter = createComponent(UserVO.VOConverter.class);
        return userVoConverter.doBackward(getUserEntityDao().findOne(userId));
    }

    @Override
    public void register(UserRegisterDTO registerDTO) {
        Map<String, Object> filters = Collections.singletonMap("email", registerDTO.getEmail());
        if (getUserEntityDao().exists(filters)) {
            throw new RuntimeException("注册邮箱已存在");
        }
        filters = Collections.singletonMap("account", registerDTO.getAccount());
        if (getUserEntityDao().exists(filters)) {
            throw new RuntimeException("注册账户已存在");
        }
        UserRegisterDTO.DTOConverter reigsterDtoConverter = createComponent(UserRegisterDTO.DTOConverter.class);
        UserEntity userEntity = reigsterDtoConverter.doBackward(registerDTO);
        getUserEntityDao().save(userEntity);
    }
}
