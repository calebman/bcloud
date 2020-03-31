package com.bcloud.mongo.common;

import com.bcloud.common.dao.IDao;
import com.bcloud.common.dao.repo.IDataRepoDocDaoFactory;
import com.bcloud.common.util.PackageUtils;
import com.bcloud.common.util.SpringCtxUtils;
import com.bcloud.common.util.StringUtils;
import com.bcloud.mongo.dao.AbsMongoDao;
import com.bcloud.mongo.dao.MongoDaoServiceConfig;
import com.bcloud.mongo.dao.repo.MongoDataRepoDocDaoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 基于mongodb的数据持久层服务自动化配置
 * @date 2020-03-28
 */
@Slf4j
public class MongoDaoAutoRegister implements InitializingBean, ApplicationContextAware {

    /**
     * Spring上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Starting to reinject type mongo dao implements");
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) this.applicationContext;
        // 构建 mongo db 核心组件配置
        MongoDaoServiceConfig mongoDaoServiceConfig = ctx.getAutowireCapableBeanFactory().createBean(MongoDaoServiceConfig.class);
        MongoClientConfigurtation clientConfigurtation = new MongoClientConfigurtation(mongoDaoServiceConfig);
        MongoTemplate mongoTemplate = clientConfigurtation.mongoTemplate();
        // 移除 现存的数据持久层实现类
        SpringCtxUtils.removeBean(ctx, IDao.class);
        SpringCtxUtils.removeBean(ctx, IDataRepoDocDaoFactory.class);
        // 注入 数据持久层的实现类
        List<String> registerNames = new ArrayList<>();
        Object[] constructorArgs = new Object[]{mongoTemplate};
        PackageUtils.consumeClasses("classpath*:com/bcloud/mongo/dao/*/*.class", clazz -> {
            if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isStatic(clazz.getModifiers())
                    || !AbsMongoDao.class.isAssignableFrom(clazz)) {
                return;
            }
            String beanName = StringUtils.lowerFirstCase(clazz.getSimpleName());
            SpringCtxUtils.registerBean(ctx, beanName, clazz, constructorArgs);
            registerNames.add(beanName);
        });
        SpringCtxUtils.registerBean(ctx, "dataRepoDocDaoFactory", MongoDataRepoDocDaoFactory.class, constructorArgs);
        registerNames.add("dataRepoDocDaoFactory");
        log.info("A total of {} mongo dao registered. the names are [{}].", registerNames.size(), String.join(", ", registerNames));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
