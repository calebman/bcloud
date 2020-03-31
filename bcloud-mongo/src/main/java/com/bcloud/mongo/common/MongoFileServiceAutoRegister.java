package com.bcloud.mongo.common;

import com.bcloud.common.dao.IDao;
import com.bcloud.common.dao.repo.IDataRepoDocDaoFactory;
import com.bcloud.common.file.IFileService;
import com.bcloud.common.util.PackageUtils;
import com.bcloud.common.util.SpringCtxUtils;
import com.bcloud.common.util.StringUtils;
import com.bcloud.mongo.dao.AbsMongoDao;
import com.bcloud.mongo.dao.MongoDaoServiceConfig;
import com.bcloud.mongo.dao.repo.MongoDataRepoDocDaoFactory;
import com.bcloud.mongo.file.MongoFileService;
import com.bcloud.mongo.file.MongoFileServiceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 基于mongodb的文件持久层服务自动化配置
 * @date 2020-03-28
 */
@Slf4j
public class MongoFileServiceAutoRegister implements InitializingBean, ApplicationContextAware {

    /**
     * Spring上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Starting to reinject type mongo file service implements");
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) this.applicationContext;
        // 构建 mongo db 核心组件配置
        MongoDaoServiceConfig mongoDaoServiceConfig = ctx.getAutowireCapableBeanFactory().createBean(MongoDaoServiceConfig.class);
        MongoClientConfigurtation clientConfigurtation = new MongoClientConfigurtation(mongoDaoServiceConfig);
        GridFsTemplate gridFsTemplate = clientConfigurtation.gridFsTemplate();
        // 注入 文件持久层的实现类
        MongoFileServiceConfig fileServiceConfig = ctx.getAutowireCapableBeanFactory().createBean(MongoFileServiceConfig.class);
        Object[] constructorArgs = new Object[]{gridFsTemplate, fileServiceConfig};
        SpringCtxUtils.registerBean(ctx, "fileService", MongoFileService.class, constructorArgs);
        log.info("MongoFileService injection successfully");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
