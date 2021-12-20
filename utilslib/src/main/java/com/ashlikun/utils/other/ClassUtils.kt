package com.ashlikun.utils.other

import java.lang.reflect.*

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:53
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：Class反射工具
 */
/**
 * 获取全部Field，包括父类
 */
inline fun Class<*>.getAllFields() = ClassUtils.getAllFields(this)

/**
 * 获取全部Method，包括父类
 */
inline fun Class<*>.getAllMethods() = ClassUtils.getAllMethods(this)

/**
 * 获取指定的字段
 */
inline fun Class<*>.getField(fieldName: String) = ClassUtils.getField(this, fieldName)
inline fun Class<*>.getMethod(fieldName: String, vararg parameterTypes: Class<*>?) =
    ClassUtils.getMethod(this, fieldName, *parameterTypes)

/**
 * 反射字段 设置值
 */
inline fun Any.getFieldValue(fieldName: String) = ClassUtils.getFieldValue(this, fieldName)
inline fun Any.setFieldValue(fieldName: String, value: Any?) =
    ClassUtils.setFieldValue(this, fieldName, value)

/**
 * 反射方法
 *
 * @param object      要反射的对象
 * @param methodNames 要反射的方法名称
 * @param parameterTypes 参数类型
 * @param args 值
 */
inline fun Any.callMethod(
    methodName: String, parameterTypes: Array<Class<*>?>? = null,
    vararg args: Any?
) = ClassUtils.callMethod(this, methodName, parameterTypes, *args)

inline fun Class<*>.callStaticMethod(
    methodName: String, parameterTypes: Array<Class<*>?>? = null,
    vararg args: Any?
) = ClassUtils.callStaticMethod(this, methodName, parameterTypes, *args)

/**
 * 获取泛型类型 第一个，如果不带泛型返回null,
 */
inline fun Class<*>.getGenericType() = ClassUtils.getGenericType(this)

object ClassUtils {
    /**
     * 获取全部Field，包括父类
     */
    fun getAllFields(claxx: Class<*>): MutableList<Field> {
        var claxx: Class<*>? = claxx
        val fieldList = mutableListOf<Field>()
        while (claxx != null && claxx != Any::class.java) {
            val fs = claxx.declaredFields
            for (i in fs.indices) {
                val f = fs[i]
                if (!isInvalid(f)) {
                    fieldList.add(f)
                }
            }
            claxx = claxx.superclass
        }
        return fieldList
    }

    /**
     * 获取指定的字段
     */
    fun getField(claxx: Class<*>, fieldName: String): Field? {
        var claxx: Class<*>? = claxx
        if (fieldName.isEmpty()) {
            return null
        }
        while (claxx != null && claxx != Any::class.java) {
            try {
                val f = claxx.getDeclaredField(fieldName)
                if (f != null) {
                    return f
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
            claxx = claxx.superclass
        }
        return null
    }

    /**
     *  反射字段 的值
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    fun getFieldValue(obj: Any, fieldName: String): Any? {
        if (fieldName.isEmpty()) {
            return null
        }
        try {
            val field = getField(obj.javaClass, fieldName)
            if (field != null) {
                field.isAccessible = true
                return field[obj]
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 反射字段 设置值
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    fun setFieldValue(obj: Any, fieldName: String, value: Any?): Field? {
        if (fieldName.isEmpty()) {
            return null
        }
        try {
            val field = getField(obj.javaClass, fieldName)
            if (field != null) {
                field.isAccessible = true
                field[obj] = value
                return field
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取全部Method，包括父类
     */
    fun getAllMethods(claxx: Class<*>): MutableList<Method> {
        var claxx: Class<*>? = claxx
        val fieldList = mutableListOf<Method>()
        while (claxx != null && claxx != Any::class.java) {
            val fs = claxx.declaredMethods
            for (i in fs.indices) {
                val f = fs[i]
                fieldList.add(f)
            }
            claxx = claxx.superclass
        }
        return fieldList
    }

    /**
     * 获取指定的方法
     */
    fun getMethod(
        claxx: Class<*>, methodName: String, vararg parameterTypes: Class<*>?
    ): Method? {
        var claxx: Class<*>? = claxx
        if (methodName.isEmpty()) {
            return null
        }
        while (claxx != null && claxx != Any::class.java) {
            try {
                val f = claxx.getDeclaredMethod(methodName, *parameterTypes)
                if (f != null) {
                    return f
                }
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
            claxx = claxx.superclass
        }
        return null
    }

    /**
     * 反射方法
     *
     * @param object      要反射的对象
     * @param methodNames 要反射的方法名称
     * @param parameterTypes 参数类型
     * @param args 值
     */
    fun callMethod(
        obj: Any,
        methodName: String,
        parameterTypes: Array<Class<*>?>? = null,
        vararg args: Any?
    ): Any? {
        if (methodName.isEmpty()) {
            return null
        }
        try {
            val method = if (parameterTypes == null) getMethod(
                obj.javaClass,
                methodName
            ) else getMethod(obj.javaClass, methodName, *parameterTypes)
            if (method != null) {
                method.isAccessible = true
                return method.invoke(obj, *args)
            }
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 反射静态方法
     *
     * @param claxx          要反射的对象
     * @param methodName     要反射的方法名称
     * @param parameterTypes 参数类型
     * @param args           参数
     * @return
     */
    fun callStaticMethod(
        claxx: Class<*>,
        methodName: String,
        parameterTypes: Array<Class<*>?>?,
        vararg args: Any?
    ): Any? {
        if (methodName.isEmpty()) {
            return null
        }
        try {
            val method = getMethod(claxx, methodName, *parameterTypes!!)
            if (method != null) {
                method.isAccessible = true
                return method.invoke(null, *args)
            }
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取泛型类型 第一个，如果不带泛型返回null,
     */
    fun getGenericType(cls: Class<*>): Type? {
        val types: Type? = cls.genericSuperclass
        var parentypes: Array<Type?> //泛型类型集合
        if (types is ParameterizedType) {
            parentypes = types.actualTypeArguments
        } else {
            parentypes = cls.getGenericInterfaces()
            for (childtype in parentypes) {
                if (childtype is ParameterizedType) {
                    val rawType = childtype.rawType
                    //实现的接口是Callback
                    if (rawType is Class<*>) {
                        //Callback里面的类型
                        parentypes = childtype.actualTypeArguments
                    }
                }
            }
        }
        return parentypes.getOrNull(0)
    }

    /**
     * 是静态常量或者内部结构属性
     */
    fun isInvalid(f: Field) =
        Modifier.isStatic(f.modifiers) && Modifier.isFinal(f.modifiers) || f.isSynthetic

    /**
     * 是否有Class注解
     */
    fun hasClassAnnotation(clazz: Class<*>, annotationClass: Class<out Annotation>) =
        getClassAnnotation(clazz, annotationClass) != null

    /**
     * 是否有字段注解
     */
    fun hasFieldAnnotation(
        clazz: Class<*>,
        annotationClass: Class<out Annotation>?, fieldName: String
    ) = getFieldAnnotation(clazz, annotationClass, fieldName) != null

    /**
     * 是否有字段注解
     *
     * @param clazz           a [Class] object.
     * @param annotationClass a [Class] object.
     * @param methodName      a [String] object.
     * @param paramType       a [Class] object.
     * @return a boolean.
     */
    fun hasMethodAnnotation(
        clazz: Class<*>,
        annotationClass: Class<out Annotation>?, methodName: String, vararg paramType: Class<*>?
    ) = getMethodAnnotation(clazz, annotationClass, methodName, *paramType) != null

    /**
     * 获取类注解
     *
     * @param clazz           类
     * @param annotationClass 注解类
     * @return a A object.
     */
    fun <A : Annotation?> getClassAnnotation(clazz: Class<*>, annotationClass: Class<A>): A? {
        return clazz.getAnnotation(annotationClass)
    }

    /**
     * 获取类成员注解
     *
     * @param clazz           类
     * @param annotationClass 注解类
     * @param fieldName       成员属性名
     * @return a A object.
     */
    fun <A : Annotation> getFieldAnnotation(
        clazz: Class<*>,
        annotationClass: Class<A>?, fieldName: String
    ): A? {
        return try {
            val field = clazz.getDeclaredField(fieldName)
            field.getAnnotation(annotationClass)
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            null
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
    fun <A : Annotation> getMethodAnnotation(
        clazz: Class<*>,
        annotationClass: Class<A>?, methodName: String, vararg paramType: Class<*>?
    ): A? {
        return try {
            val method = clazz.getDeclaredMethod(methodName, *paramType)
            method.getAnnotation(annotationClass)
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            null
        }
    }
}