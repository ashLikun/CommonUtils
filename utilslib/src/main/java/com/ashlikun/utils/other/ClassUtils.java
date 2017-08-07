package com.ashlikun.utils.other;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/7　14:17
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：Class反射工具
 */

public class ClassUtils {
    /**
     * 获取全部Field，包括父类
     *
     * @param claxx
     * @return
     */
    public static LinkedList<Field> getAllDeclaredFields(Class<?> claxx) {
        // find all field.
        LinkedList<Field> fieldList = new LinkedList<Field>();
        while (claxx != null && claxx != Object.class) {
            Field[] fs = claxx.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                if (!isInvalid(f)) {
                    fieldList.addLast(f);
                }
            }
            claxx = claxx.getSuperclass();
        }
        return fieldList;
    }

    /**
     * 获取域的泛型类型，如果不带泛型返回null
     */
    public static Class<?> getGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (type instanceof Class<?>) return (Class<?>) type;
        } else if (type instanceof Class<?>) return (Class<?>) type;
        return null;
    }


    /**
     * 是静态常量或者内部结构属性
     */
    public static boolean isInvalid(Field f) {
        return (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) || f.isSynthetic();
    }
}
