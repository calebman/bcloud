package com.bcloud.server.common.conf;

import com.bcloud.common.util.PackageUtils;
import com.bcloud.common.util.SpringCtxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JianhuiChen
 * @description 默认的dao层定义
 * 利用jdk动态代理生成默认的dao层实现
 * 防止Spring容器中因为找不带依赖bean而启动失败
 * @date 2020-03-30
 */
@Slf4j
@Configuration
public class DefaultDaoConfiguration implements ApplicationContextAware, InitializingBean {

    /**
     * 上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Start injecting the default dao implementation");
        List<String> registerNames = new ArrayList<>();
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) applicationContext;
        final Object target = new Object();
        PackageUtils.consumeClasses("classpath*:com/bcloud/common/dao/*/*.class", clazz -> {
            try {
                Class<?> proxyClazz = Proxy.getProxyClass(clazz.getClassLoader(), clazz);
                Constructor<?> constructor = proxyClazz.getConstructor(InvocationHandler.class);
                Object bean = constructor.newInstance((InvocationHandler) (proxy, method, args) -> method.invoke(target, args));
                SpringCtxUtils.registerBean(ctx, bean);
                registerNames.add(clazz.getSimpleName());
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                log.error("Failed to register bean {}", clazz.getSimpleName(), e);
            }
        });
        log.info("A total of {} default dao registered. the classes are [{}].", registerNames.size(), String.join(", ", registerNames));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
