package com.bcloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

/**
 * @author JianhuiChen
 * @description Spring bean 操作工具
 * @date 2020-03-29
 */
@Slf4j
public class SpringCtxUtils {


    /**
     * 注入配置到Spring上下文
     *
     * @param ctx    上下文
     * @param prefix 配置前缀
     * @param props  系统配置重置事件
     */
    public static void injectProperty(ConfigurableApplicationContext ctx, String prefix, Map<String, Object> props) {
        ConfigurableEnvironment env = ctx.getEnvironment();
        PropertySource propertySource = new MapPropertySource(prefix, props);
        env.getPropertySources().addLast(propertySource);
    }

    /**
     * 注入bean至Spring容器中
     *
     * @param ctx      上下文
     * @param beanName 注册实体的名称
     * @param clazz    类型
     */
    public static <T> T registerBean(ConfigurableApplicationContext ctx, String beanName, Class<T> clazz, Object... constructorArgs) {
        removeBean(ctx, beanName);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object constructorArg : constructorArgs) {
            beanDefinitionBuilder.addConstructorArgValue(constructorArg);
        }
        beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
        Object bean = beanFactory.getBean(beanName);
        beanFactory.applyBeanPostProcessorsAfterInitialization(bean, beanName);
        log.debug("Register the bean {}:{} from the context", beanName, bean);
        return ctx.getBean(beanName, clazz);
    }

    public static <T> T registerBean(ConfigurableApplicationContext ctx, String beanName, Class<T> clazz) {
        return registerBean(ctx, beanName, clazz, new Object[]{});
    }

    public static void registerBean(ConfigurableApplicationContext ctx, Object obj) {
        String beanName = obj.getClass().getSimpleName();
        registerBean(ctx, beanName, obj);
    }

    /**
     * 注入bean至Spring容器中
     *
     * @param ctx      上下文
     * @param beanName 注册实体的名称
     * @param obj      bean对象
     */
    public static void registerBean(ConfigurableApplicationContext ctx, String beanName, Object obj) {
        Class<?> clazz = obj.getClass();
        removeBean(ctx, clazz);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        beanFactory.registerSingleton(beanName, obj);
        beanFactory.applyBeanPostProcessorsAfterInitialization(obj, beanName);
        log.debug("Register the bean {}:{} from the context", beanName, obj);
    }

    /**
     * 配置类的加载
     *
     * @param ctx   上下文
     * @param clazz 类型
     */
    public static void configureBean(ConfigurableApplicationContext ctx, Class<?> clazz) {
        removeBean(ctx, clazz);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        String beanName = StringUtils.lowerFirstCase(clazz.getSimpleName());
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        ConfigurationClassPostProcessor postProcessor = ctx.getBeanFactory().getBean(ConfigurationClassPostProcessor.class);
        postProcessor.processConfigBeanDefinitions((BeanDefinitionRegistry) ctx.getBeanFactory());
    }

    /**
     * 移除Spring容器中的bean
     *
     * @param ctx           上下文
     * @param existBeanName 名称
     */
    public static void removeBean(ConfigurableApplicationContext ctx, String existBeanName) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        if (beanFactory.containsBean(existBeanName)) {
            if (beanFactory.isSingleton(existBeanName)) {
                beanFactory.destroySingleton(existBeanName);
            }
            if (beanFactory.containsBeanDefinition(existBeanName)) {
                beanFactory.removeBeanDefinition(existBeanName);
            }
        }
    }

    /**
     * 移除Spring容器中的bean
     *
     * @param ctx   上下文
     * @param clazz 类型
     */
    public static void removeBean(ConfigurableApplicationContext ctx, Class<?> clazz) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        for (String existBeanName : beanFactory.getBeanNamesForType(clazz)) {
            try {
                if (beanFactory.isSingleton(existBeanName)) {
                    beanFactory.destroySingleton(existBeanName);
                }
                if (beanFactory.containsBeanDefinition(existBeanName)) {
                    beanFactory.removeBeanDefinition(existBeanName);
                }
                log.debug("Remove bean {}:{} from spring context", existBeanName, clazz.getName());
            } catch (Exception ex) {
                log.warn("Failed to remove bean {}", existBeanName, ex);
            }
        }
    }
}
