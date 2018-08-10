package com.ashlikun.utils.other;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
            if (type instanceof Class<?>) {
                return (Class<?>) type;
            }
        } else if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }


    /**
     * 是静态常量或者内部结构属性
     */
    public static boolean isInvalid(Field f) {
        return (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) || f.isSynthetic();
    }

    /**
     * 是否有Class注解
     *
     * @param clazz           a {@link java.lang.Class} object.
     * @param annotationClass a {@link java.lang.Class} object.
     * @return a boolean.
     */
    public static boolean hasClassAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return getClassAnnotation(clazz, annotationClass) != null;
    }

    /**
     * 是否有字段注解
     *
     * @param clazz           a {@link java.lang.Class} object.
     * @param annotationClass a {@link java.lang.Class} object.
     * @param fieldName       a {@link java.lang.String} object.
     * @return a boolean.
     */
    public static boolean hasFieldAnnotation(Class<?> clazz,
                                             Class<? extends Annotation> annotationClass, String fieldName) throws Exception {
        return getFieldAnnotation(clazz, annotationClass, fieldName) != null;
    }

    /**
     * 是否有字段注解
     *
     * @param clazz           a {@link java.lang.Class} object.
     * @param annotationClass a {@link java.lang.Class} object.
     * @param methodName      a {@link java.lang.String} object.
     * @param paramType       a {@link java.lang.Class} object.
     * @return a boolean.
     */
    public static boolean hasMethodAnnotation(Class<?> clazz,
                                              Class<? extends Annotation> annotationClass, String methodName, Class<?>... paramType) throws Exception {
        return getMethodAnnotation(clazz, annotationClass, methodName, paramType) != null;
    }

    /**
     * 获取类注解
     *
     * @param clazz           类
     * @param annotationClass 注解类
     * @return a A object.
     */


    public static <A extends Annotation> A getClassAnnotation(Class<?> clazz, Class<A> annotationClass) {
        return clazz.getAnnotation(annotationClass);

    }

    /**
     * 获取类成员注解
     *
     * @param clazz           类
     * @param annotationClass 注解类
     * @param fieldName       成员属性名
     * @return a A object.
     */


    public static <A extends Annotation> A getFieldAnnotation(Class<?> clazz,
                                                              Class<A> annotationClass, String fieldName) throws Exception {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (field == null) {

                throw new Exception("no such field[" + fieldName + "] in " + clazz.getCanonicalName());

            }
            return field.getAnnotation(annotationClass);

        } catch (SecurityException e) {
            e.printStackTrace();

            throw new Exception("access error: field[" + fieldName + "] in " + clazz.getCanonicalName(), e);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();

            throw new Exception("no such field[" + fieldName + "] in " + clazz.getCanonicalName());

        }

    }

    /**
     * 获取类方法上的注解
     *
     * @param clazz           类
     * @param annotationClass 注解类
     * @param methodName      方法名
     * @param paramType       方法参数
     * @return a A object.
     */
    public static <A extends Annotation> A getMethodAnnotation(Class<?> clazz,
                                                               Class<A> annotationClass, String methodName, Class<?>... paramType)
            throws Exception {
        try {
            Method method = clazz.getDeclaredMethod(methodName, paramType);
            if (method == null) {
                throw new Exception("access error: method[" + methodName + "] in " + clazz.getCanonicalName());
            }
            return method.getAnnotation(annotationClass);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw new Exception("access error: method[" + methodName + "] in " + clazz.getCanonicalName(), e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new Exception("no such method[" + methodName + "] in " + clazz.getCanonicalName(), e);
        }
    }
}
