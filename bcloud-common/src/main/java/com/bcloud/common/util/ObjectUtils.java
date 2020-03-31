package com.bcloud.common.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JianhuiChen
 * @description 对象工具
 * @date 2020-03-29
 */
public class ObjectUtils {

    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return 数据集合
     */
    public static Map<String, Object> objToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Map转成实体对象
     *
     * @param map  map实体对象包含属性
     * @param bean 实体对象
     */
    public static void populate(Map<String, Object> map, Object bean) {
        if (map == null) {
            map = new HashMap<>();
        }
        Map<String, Object> finalMap = map;
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            field.setAccessible(true);
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                return;
            }
            field.setAccessible(true);
            field.set(bean, finalMap.get(field.getName()));
        });
    }

}
