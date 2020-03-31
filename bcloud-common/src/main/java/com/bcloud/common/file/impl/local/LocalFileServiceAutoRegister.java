package com.bcloud.common.file.impl.local;

import com.bcloud.common.file.IFileService;
import com.bcloud.common.file.impl.local.LocalFileService;
import com.bcloud.common.file.impl.local.LocalFileServiceConfig;
import com.bcloud.common.util.SpringCtxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author JianhuiChen
 * @description 基于本地磁盘的文件持久层服务自动化配置
 * @date 2020-03-28
 */
@Slf4j
public class LocalFileServiceAutoRegister implements InitializingBean, ApplicationContextAware {

    /**
     * Spring上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Starting to reinject type local file service implements");
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) this.applicationContext;
        // 注入 文件持久层的实现类
        LocalFileServiceConfig fileServiceConfig = ctx.getAutowireCapableBeanFactory().createBean(LocalFileServiceConfig.class);
        Object[] constructorArgs = new Object[]{fileServiceConfig};
        SpringCtxUtils.registerBean(ctx, "fileService", LocalFileService.class, constructorArgs);
        log.info("LocalFileService injection successfully");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
