package com.ashlikun.utils.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/**
 * 作者　　: 李坤
 * 创建时间: 13:53 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：SharedPreferences 操作类
 */

public class SharedPreUtils {

    public final static String CACHE_DATA = "cachedata";


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:53
     * <p>
     * 方法功能：保存键值对
     *
     * @param context
     * @param name    文件名
     * @param key     键
     * @param value   值  可以是String，Integer，Boolean，Float，Long，Set<String>
     * @return 是否保存成功
     */
    public static boolean setKeyAndValue(Context context,
                                         String name,
                                         String key,
                                         Object value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set) value);
        }
        return editor.commit();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 17:05
     * <p>
     * 方法功能：同上，只是文件名写成固定
     */

    public static boolean setKeyAndValue(Context context,
                                         String key,
                                         Object value) {
        return setKeyAndValue(context, CACHE_DATA, key, value);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:56
     * <p>
     * 方法功能：获取指定的key 对应的值
     *
     * @param name 文件名
     * @return 对应值
     */

    public static <T> T getValue(Context context,
                                 String name,
                                 String key) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        try {
            return (T) sp.getAll().get(key);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 17:06
     * <p>
     * 方法功能：同上，只是文件名写成固定
     */

    public static <T> T getValue(Context context,
                                 String key) {
        return getValue(context, CACHE_DATA, key);
    }

}
