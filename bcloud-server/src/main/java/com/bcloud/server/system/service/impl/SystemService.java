package com.bcloud.server.system.service.impl;

import com.bcloud.common.dao.system.IRoleEntityDao;
import com.bcloud.common.entity.system.RoleEntity;
import com.bcloud.common.file.IFileService;
import com.bcloud.server.common.sys.CommonProvider;
import com.bcloud.server.common.events.SystemConfigRemakeEvent;
import com.bcloud.server.common.pojo.GlobalConstant;
import com.bcloud.server.common.pojo.SysMenu;
import com.bcloud.server.common.pojo.SysResource;
import com.bcloud.server.common.sys.SystemDaoConf;
import com.bcloud.server.common.sys.SystemFileConf;
import com.bcloud.server.system.in.SystemConfRemakeDTO;
import com.bcloud.server.system.service.ISystemService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author JianhuiChen
 * @description 系统服务层接口实现
 * @date 2020-03-29
 */
@Slf4j
@Getter
@Service
public class SystemService extends CommonProvider implements ISystemService,
        ApplicationContextAware, ApplicationEventPublisherAware {

    /**
     * Spring容器上下文
     */
    private ApplicationContext applicationContext;

    /**
     * Spring事件发布器
     */
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void initSystem(SystemConfRemakeDTO confRemakeDTO) {
        SystemDaoConf systemDaoConf = getDaoConfConverter().doBackward(confRemakeDTO);
        systemDaoConf.test();
        SystemFileConf systemFileConf = getFileConfConverter().doBackward(confRemakeDTO);
        systemFileConf.test();
        SystemConfigRemakeEvent configRemakeEvent = new SystemConfigRemakeEvent(systemDaoConf, systemFileConf, getApplicationContext());
        eventPublisher.publishEvent(configRemakeEvent);
    }

    @Override
    public void initResource() {
        this.initDefaultAvatar();
        this.initSystemRole();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 获取数据持久层的配置转换工具
     */
    private SystemConfRemakeDTO.DaoConfConverter getDaoConfConverter() {
        return new SystemConfRemakeDTO.DaoConfConverter();
    }

    /**
     * 获取文件持久层的配置转换工具
     */
    private SystemConfRemakeDTO.FileConfConverter getFileConfConverter() {
        return new SystemConfRemakeDTO.FileConfConverter();
    }

    /**
     * 初始化系统必要的文件 默认头像
     */
    private void initDefaultAvatar() {
        String avatarKey = GlobalConstant.SYS_DEFAULT_AVATAR_KEY;
        log.debug("Init system default avatar, file key is {}.", avatarKey);
        try {
            Resource resource = new ClassPathResource("static/images/default_avatar.png");
            File avatarFile = resource.getFile();
            IFileService fileService = this.getFileService();
            fileService.save(avatarKey, "default_avatar.png", new FileInputStream(avatarFile));
            log.info("System default avatar initialization successfully with service [{}].", fileService);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to init system default avatar.", e);
        }
    }

    /**
     * 初始化系统必要的角色
     */
    private void initSystemRole() {
        String adminRoleKey = GlobalConstant.SYS_ADMIN_ROLE_KEY;
        String normalRoleKey = GlobalConstant.SYS_DEFAULT_ROLE_KEY;
        log.debug("Init system roles with admin and normal, that keys are {} and {}.", adminRoleKey, normalRoleKey);
        IRoleEntityDao roleEntityDao = this.getRoleEntityDao();
        // admin
        RoleEntity adminRole = new RoleEntity();
        adminRole.setId(adminRoleKey);
        adminRole.setName("管理员");
        adminRole.setDesc("系统最高权限角色");
        adminRole.setMenuKeys(Stream.of(SysMenu.values()).map(SysMenu::getKey).collect(Collectors.toSet()));
        adminRole.setResourceKeys(Stream.of(SysResource.values()).map(SysResource::getKey).collect(Collectors.toSet()));
        adminRole.setStatus(RoleEntity.Status.ENABLE);
        roleEntityDao.save(adminRole);
        // normal
        RoleEntity normalRole = new RoleEntity();
        normalRole.setId(normalRoleKey);
        normalRole.setName("普通角色");
        normalRole.setDesc("系统新注册用户默认角色");
        normalRole.setMenuKeys(Stream.of(SysMenu.values()).filter(SysMenu::isDefaultGrant).map(SysMenu::getKey).collect(Collectors.toSet()));
        normalRole.setResourceKeys(Collections.emptySet());
        normalRole.setStatus(RoleEntity.Status.ENABLE);
        roleEntityDao.save(normalRole);
        log.info("System roles initialization successfully with dao [{}].", roleEntityDao);
    }
}
