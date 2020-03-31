package com.bcloud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.util.ProxyUtils;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author JianhuiChen
 * @description 包操作工具
 * @date 2020-03-28
 */
@Slf4j
public class PackageUtils {

    /**
     * 根据包路径查询所有的类信息
     *
     * @param packagePath 包路径
     * @return 类信息列表
     * @throws IOException            IO操作异常
     * @throws ClassNotFoundException 类无法找到
     */
    public static List<Class<?>> getClasses(String packagePath) throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        // 加载系统所有类资源
        Resource[] resources = resourcePatternResolver.getResources(packagePath);
        List<Class<?>> result = new ArrayList<>();
        // 把每一个class文件找出来
        for (Resource r : resources) {
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(r);
            Class<?> clazz = ClassUtils.forName(metadataReader.getClassMetadata().getClassName(), null);
            result.add(clazz);
        }
        return result;
    }

    /**
     * 消费包内的所有类
     *
     * @param packagePath 包路径
     * @param action      类消费对象
     */
    public static void consumeClasses(String packagePath, Consumer<Class<?>> action) {
        try {
            getClasses(packagePath).forEach(clazz -> {
                try {
                    action.accept(clazz);
                } catch (Exception e) {
                    log.error("Failed to consume class {}", clazz.getName(), e);
                }
            });
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to load classes in {}", packagePath, e);
            e.printStackTrace();
        }
    }
}
