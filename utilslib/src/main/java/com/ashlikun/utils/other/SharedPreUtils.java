package com.ashlikun.utils.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

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

    private static void clean(Context context,
                              String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(key)) {
            sp.edit().clear().commit();
        } else {
            sp.edit().remove(key).commit();
        }
    }

    public static void clean(Context context,
                             String name) {
        clean(context, name, null);
    }

    public static void clean(Context context) {
        clean(context, CACHE_DATA, null);
    }

    public static void remove(Context context,
                              String name, String key) {
        clean(context, name, key);
    }

    public static void remove(Context context,
                              String key) {
        clean(context, CACHE_DATA, key);
    }

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
                Context.MODE_PRIVATE);
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

    public static Object getValue(Context context,
                                  String name,
                                  String key, Class type) {
        SharedPreferences sp = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        if (type.isAssignableFrom(Integer.class)) {
            return sp.getInt(key, 0);
        } else if (type.isAssignableFrom(String.class)) {
            return sp.getString(key, "");
        } else if (type.isAssignableFrom(Boolean.class)) {
            return sp.getBoolean(key, false);
        } else if (type.isAssignableFrom(Float.class)) {
            return sp.getFloat(key, 0);
        } else if (type.isAssignableFrom(Set.class)) {
            return sp.getStringSet(key, null);
        }
        return null;
    }

    public static int getInt(Context context,
                             String name,
                             String key) {
        return (int) getValue(context, name, key, Integer.class);
    }

    public static String getString(Context context,
                                   String name,
                                   String key) {
        return (String) getValue(context, name, key, String.class);
    }

    public static boolean getBoolean(Context context,
                                     String name,
                                     String key) {
        return (boolean) getValue(context, name, key, Boolean.class);
    }

    public static float getFloat(Context context,
                                 String name,
                                 String key) {
        return (float) getValue(context, name, key, Float.class);
    }

    public static Set<String> getSet(Context context,
                                     String name,
                                     String key) {
        return (Set<String>) getValue(context, name, key, Set.class);
    }

    public static int getInt(Context context,
                             String key) {
        return (int) getValue(context, CACHE_DATA, key, Integer.class);
    }

    public static String getString(Context context,
                                   String key) {
        return (String) getValue(context, CACHE_DATA, key, String.class);
    }

    public static boolean getBoolean(Context context,
                                     String key) {
        return (boolean) getValue(context, CACHE_DATA, key, Boolean.class);
    }

    public static float getFloat(Context context,
                                 String key) {
        return (float) getValue(context, CACHE_DATA, key, Float.class);
    }

    public static Set<String> getSet(Context context,
                                     String key) {
        return (Set<String>) getValue(context, CACHE_DATA, key, Set.class);
    }

}
