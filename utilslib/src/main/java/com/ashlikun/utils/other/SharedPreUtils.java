package com.ashlikun.utils.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.provider.ImpSpProvider;
import com.ashlikun.utils.ui.ActivityUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author　　: 李坤
 * 创建时间: 2018/5/30 0030 下午 4:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：Sp工具类
 */

public class SharedPreUtils {

    public static final String DEFAULT = "default";
    public static final String USER = "user";


    public static boolean putString(Context context, String key, String value) {
        return putString(context, DEFAULT, key, value);
    }

    public static boolean putString(Context context, String name, String key, String value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key,
                                   String defaultValue) {
        return getString(context, DEFAULT, key, defaultValue);
    }

    public static String getString(Context context, String name, String key, String defaultValue) {
        return (String) getValue(context, name, key, String.class, defaultValue);
    }

    public static String getStringName(Context context, String name, String key) {
        return (String) getValue(context, name, key, String.class, null);
    }

    public static boolean putInt(Context context, String key, int value) {
        return putInt(context, DEFAULT, key, value);
    }

    public static boolean putInt(Context context, String name, String key, int value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getInt(context, DEFAULT, key, defaultValue);
    }

    public static int getInt(Context context, String name, String key, int defaultValue) {
        return (int) getValue(context, name, key, Integer.class, defaultValue);
    }

    public static int getInt(Context context, String name, String key) {
        return (int) getValue(context, name, key, Integer.class, -1);
    }

    public static boolean putLong(Context context, String key, long value) {
        return putLong(context, DEFAULT, key, value);
    }

    public static boolean putLong(Context context, String name, String key, long value) {
        return setKeyAndValue(context, name, key, value);
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getLong(context, DEFAULT, key, defaultValue);
    }

    public static long getLong(Context context, String name, String key, long defaultValue) {
        return (long) getValue(context, name, key, Long.class, defaultValue);
    }

    public static long getLong(Context context, String name, String key) {
        return (long) getValue(context, name, key, Long.class, -1);
    }


    public static boolean putFloat(Context context, String key, float value) {
        return putFloat(context, DEFAULT, key, value);
    }

    public static boolean putFloat(Context context, String name, String key, float value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }


    public static float getFloat(Context context, String key, float defaultValue) {
        return getFloat(context, DEFAULT, key, defaultValue);
    }

    public static float getFloat(Context context, String name, String key) {
        return (float) getValue(context, name, key, Float.class, -1);
    }

    public static float getFloat(Context context, String name, String key, float defaultValue) {
        return (float) getValue(context, name, key, Float.class, defaultValue);
    }


    public static boolean putBoolean(Context context, String key, boolean value) {
        return putBoolean(context, DEFAULT, key, value);
    }

    public static boolean putBoolean(Context context, String name, String key, boolean value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key,
                                     boolean defaultValue) {
        return (boolean) getValue(context, DEFAULT, key, Boolean.class, defaultValue);
    }

    public static boolean getBoolean(Context context, String name, String key,
                                     boolean defaultValue) {
        return (boolean) getValue(context, name, key, Boolean.class, defaultValue);
    }

    public static boolean getBoolean(Context context, String name, String key) {
        return (boolean) getValue(context, name, key, Boolean.class, false);
    }


    public static boolean putSet(Context context, String key, Set value) {
        return putSet(context, DEFAULT, key, value);
    }

    public static boolean putSet(Context context, String name, String key, Set value) {
        return setKeyAndValue(context, name, key, value);
    }


    public static Set getSet(Context context, String key) {
        return getSet(context, key, new HashSet<String>());
    }

    public static Set getSet(Context context, String key,
                             Set<String> defaultValue) {
        return (Set) getValue(context, DEFAULT, key, Set.class, defaultValue);
    }

    public static Set getSet(Context context, String name, String key) {
        return (Set) getValue(context, name, key, Set.class, null);
    }

    public static Set getSet(Context context, String name, String key,
                             Set defaultValue) {
        return (Set) getValue(context, name, key, Set.class, defaultValue);
    }

    public static boolean remove(Context context, String key) {
        return remove(context, DEFAULT, key);
    }

    public static boolean remove(Context context, String name, String key) {
        if (AppUtils.isMainProcess()) {
            SharedPreferences.Editor editor = getSP(context, name).edit();
            editor.remove(key);
            return editor.commit();
        } else {
            //其他进程，使用ContentProvider
            return ImpSpProvider.removeToProvider(context, name, key);
        }
    }

    public static boolean clear(Context context) {
        return clear(context, DEFAULT);
    }


    public static boolean clear(Context context, String name) {

        if (AppUtils.isMainProcess()) {
            SharedPreferences sp = getSP(context, name);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            return editor.commit();
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.clearToProvider(context, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
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
                                  String key, Class type, Object defaule) {
        if (context == null) {
            return false;
        }
        if (AppUtils.isMainProcess()) {
            SharedPreferences sp = getSP(context, name);
            if (sp == null) {
                return defaule;
            }
            if (type.isAssignableFrom(String.class)) {
                return sp.getString(key, (String) defaule);
            } else if (type.isAssignableFrom(Integer.class)) {
                return sp.getInt(key, (Integer) defaule);
            } else if (type.isAssignableFrom(Boolean.class)) {
                return sp.getBoolean(key, false);
            } else if (type.isAssignableFrom(Float.class)) {
                return sp.getFloat(key, (Float) defaule);
            } else if (type.isAssignableFrom(Long.class)) {
                return sp.getLong(key, (Long) defaule);
            } else if (type.isAssignableFrom(Set.class)) {
                return sp.getStringSet(key, (Set<String>) defaule);
            }
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.getValueToProvider(context, name, key, type, defaule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaule;
    }

    public static boolean setKeyAndValue(Context context, String key, Object value) {
        return setKeyAndValue(context, DEFAULT, key, value);
    }

    public static boolean setKeyAndValue(Context context, String name, String key, Object value) {
        if (context == null) {
            return false;
        }
        if (AppUtils.isMainProcess()) {
            SharedPreferences sp = getSP(context, name);
            if (sp == null) {
                return false;
            }
            SharedPreferences.Editor editor = sp.edit();
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
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.setValueToProvider(context, name, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    public static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(
                DEFAULT, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSP(Context context, String name) {
        return context.getSharedPreferences(
                name, Context.MODE_PRIVATE);
    }

    public static void testPut(Context context) {
        Log.e("testPut", "getCurProcessName = " + ActivityUtils.getCurProcessName());
        SharedPreUtils.putLong(context, "sp_testLong", 123);
        SharedPreUtils.putInt(context, "sp_testInt", 12322222);
        SharedPreUtils.putString(context, "sp_testString", "putString");
        SharedPreUtils.putBoolean(context, "sp_testBoolean", true);
        SharedPreUtils.putFloat(context, "sp_testFloat", 123.369f);
        Set<String> strings = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            strings.add("putSet + " + i);
        }
        SharedPreUtils.putSet(context, "sp_testSet", strings);
    }

    public static void testGet(Context context) {
        Log.e("testGet", "getCurProcessName = " + ActivityUtils.getCurProcessName());
        Log.e("testGet", "getLong = " + SharedPreUtils.getLong(context, "sp_testLong", 999));
        Log.e("testGet", "getInt = " + SharedPreUtils.getInt(context, "sp_testInt"));
        Log.e("testGet", "getString = " + SharedPreUtils.getString(context, "sp_testString", null));
        Log.e("testGet", "getBoolean = " + SharedPreUtils.getBoolean(context, "sp_testBoolean"));
        Log.e("testGet", "getFloat = " + SharedPreUtils.getFloat(context, "sp_testFloat"));

        StringBuilder sb = new StringBuilder("  sp_testSet  == ");
        Set<String> strings = SharedPreUtils.getSet(context, "sp_testSet");
        if (strings != null) {
            for (String s : strings) {
                sb.append(s);
                sb.append(",,,");
            }
        }
        Log.e("testGet", "getSet = " + sb.toString());
    }
}
