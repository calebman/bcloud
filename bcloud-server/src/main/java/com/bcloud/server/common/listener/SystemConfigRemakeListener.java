package com.bcloud.server.common.listener;

import com.bcloud.common.file.IFileService;
import com.bcloud.common.file.impl.local.LocalFileServiceAutoRegister;
import com.bcloud.common.util.SpringCtxUtils;
import com.bcloud.mongo.common.MongoDaoAutoRegister;
import com.bcloud.mongo.common.MongoFileServiceAutoRegister;
import com.bcloud.server.common.events.SystemConfigRemakeEvent;
import com.bcloud.server.common.sys.SystemDaoConf;
import com.bcloud.server.common.sys.SystemFileConf;
import com.bcloud.server.system.service.ISystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author JianhuiChen
 * @description dao层配置信息重置事件监听
 * @date 2020-03-28
 */
@Slf4j
@Component
public class SystemConfigRemakeListener implements ApplicationListener<SystemConfigRemakeEvent> {

    @Override
    public void onApplicationEvent(SystemConfigRemakeEvent event) {
        log.info("Start resetting the basic configuration of the system with event {}", event);
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) event.getApplicationContext();
        this.initDaoStorage(ctx, event.getSystemDaoConf());
        this.initFileStorage(ctx, event.getSystemFileConf());
        this.initSystemResource(ctx);
        log.info("System reset successfully successfully, dao and file layers have been replaced.");
    }

    /**
     * 初始化数据持久化组件库
     *
     * @param ctx     上下文
     * @param daoConf 配置信息
     */
    private void initDaoStorage(ConfigurableApplicationContext ctx, SystemDaoConf daoConf) {
        String autoRegisterBeanName = "daoAutoRegister";
        switch (daoConf.getStorageType()) {
            case MONGO:
                SpringCtxUtils.injectProperty(ctx, "service.dao.mongo", daoConf.getProperties());
                SpringCtxUtils.registerBean(ctx, autoRegisterBeanName, MongoDaoAutoRegister.class);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化文件持久化组件库
     *
     * @param ctx      上下文
     * @param fileConf 配置信息
     */
    private void initFileStorage(ConfigurableApplicationContext ctx, SystemFileConf fileConf) {
        String autoRegisterBeanName = "fileServiceAutoRegister";
        switch (fileConf.getStorageType()) {
            case LOCAL:
                SpringCtxUtils.injectProperty(ctx, "service.file.local", fileConf.getProperties());
                SpringCtxUtils.registerBean(ctx, autoRegisterBeanName, LocalFileServiceAutoRegister.class);
                break;
            case MONGO:
                SpringCtxUtils.injectProperty(ctx, "service.file.mongo", fileConf.getProperties());
                SpringCtxUtils.registerBean(ctx, autoRegisterBeanName, MongoFileServiceAutoRegister.class);
                break;
        }
    }

    /**
     * 重置系统初始化的资源 默认头像、角色等
     *
     * @param ctx 应用上下文
     */
    private void initSystemResource(ConfigurableApplicationContext ctx) {
        ISystemService systemService = ctx.getBean(ISystemService.class);
        systemService.initResource();
    }
}
