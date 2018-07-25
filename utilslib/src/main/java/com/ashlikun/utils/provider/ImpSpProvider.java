package com.ashlikun.utils.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.ashlikun.utils.other.SharedPreUtils;
import com.ashlikun.utils.provider.mode.SpMode;

import java.util.HashSet;
import java.util.Set;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/30 0030　下午 5:14
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：内容提供者用到的sp,只处理{@link BaseContentProvider#HANDLE_SP}类型
 */
public class ImpSpProvider implements IContentProvider {
    /**
     * 数据类型
     */
    public static final String TYPE_STRING = "string";
    public static final String TYPE_INT = "int";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_BOOLEAN = "boolean";

    public static final String TYPE_STRING_SET = "string_set";

    public static final String COMMA_REPLACEMENT = "__COMMA__";
    public static final String VALUE = "VALUE";
    /**
     * 构建后的游标属性
     */
    public static final String SP_CURSOR_COLUMN_NAME = "cursor_name";
    public static final String SP_CURSOR_COLUMN_KEY = "cursor_key";
    public static final String SP_CURSOR_COLUMN_VALUE = "cursor_value";
    /**
     * sp操作类型
     */
    public static final String TYPE_CLEAN = "clean";


    boolean contains(Context context, String name) {
        SharedPreferences sp = SharedPreUtils.getSP(context);
        return sp != null && sp.contains(name);
    }

    boolean remove(Context context, String name, String key) {
        SharedPreferences.Editor editor = SharedPreUtils.getSP(context, name).edit();
        editor.remove(key);
        return editor.commit();
    }

    boolean clear(Context context, String name) {
        SharedPreferences sp = SharedPreUtils.getSP(context, name);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        return editor.commit();
    }


    /**
     * sp: path: sp/type/name/key---->1:sp
     * content://包名.ContentProvider/handle_sp/string/user/nickname/
     */
    @Override
    public Cursor query(Context context, Uri uri) {
        SpMode mode = new SpMode(uri);

        if (!mode.isException) {
            //构建一个游标
            MatrixCursor cursor = new MatrixCursor(new String[]{SP_CURSOR_COLUMN_NAME, SP_CURSOR_COLUMN_KEY, SP_CURSOR_COLUMN_VALUE});
            Object value = getValue(context, mode.name, mode.key, mode.type);
            if (value != null) {
                Object[] rows = new Object[3];
                rows[0] = mode.name;
                rows[1] = mode.key;
                rows[2] = value;
                //向游标里面添加数据
                cursor.addRow(rows);
                return cursor;
            }
        }
        return null;
    }

    @Override
    public Uri insert(Context context, Uri uri, ContentValues values) {
        SpMode mode = new SpMode(uri);
        Object obj = values.get(VALUE);
        if (!mode.isException && obj != null) {
            //set类型特殊处理
            if (TYPE_STRING_SET.equals(mode.type) && obj instanceof String) {
                String setStrs = (String) obj;
                if (setStrs != null) {
                    String[] setStr = setStrs.split(COMMA_REPLACEMENT);
                    if (setStr != null) {
                        HashSet<String> set = new HashSet<>();
                        for (String s : setStr) {
                            set.add(s);
                        }
                        obj = set;
                    }
                }
            }
            return setValue(context, mode.name, mode.key, obj) ? Uri.parse("true") : null;
        }
        return null;
    }

    @Override
    public int delete(Context context, Uri uri) {
        SpMode mode = new SpMode(uri);
        if (!mode.isException && mode.type.equals(TYPE_CLEAN)) {
            return clear(context, mode.name) ? 1 : 0;
        }
        return remove(context, mode.name, mode.key) ? 1 : 0;
    }

    @Override
    public String getType(Context context, Uri uri) {
        SpMode mode = new SpMode(uri);
        if (!mode.isException && mode.handle != null) {
            return mode.handle;
        }
        return null;
    }


    /**
     * 获取sp内容
     */
    Object getValue(Context context,
                    String name,
                    String key, String type) {
        SharedPreferences sp = SharedPreUtils.getSP(context, name);
        if (sp == null) {
            return null;
        }
        //如果没有这个值，就返回null，调用者自己返回默认值
        if (!sp.contains(key)) {
            return null;
        }
        switch (type) {
            case TYPE_STRING:
                return sp.getString(key, null);
            case TYPE_INT:
                return sp.getInt(key, -1);
            case TYPE_BOOLEAN:
                return sp.getBoolean(key, false);
            case TYPE_FLOAT:
                return sp.getFloat(key, -1);
            case TYPE_LONG:
                return sp.getLong(key, -1);
            case TYPE_STRING_SET:
                return sp.getStringSet(key, null);
        }
        return null;
    }

    /**
     * 保存sp内容
     */
    synchronized <T> boolean setValue(Context context, String name, String key, T value) {
        SharedPreferences sp = SharedPreUtils.getSP(context, name);
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
    }

    /**
     * 设置Provider内容
     */
    public static boolean setValueToProvider(Context context, String name, String key, Object value) {
        if (context == null || name == null || key == null || value == null) {
            return false;
        }
        ContentValues cv = new ContentValues();
        if (value instanceof String) {
            cv.put(VALUE, (String) value);
        } else if (value instanceof Integer) {
            cv.put(VALUE, (Integer) value);
        } else if (value instanceof Boolean) {
            cv.put(VALUE, (Boolean) value);
        } else if (value instanceof Float) {
            cv.put(VALUE, (Float) value);
        } else if (value instanceof Long) {
            cv.put(VALUE, (Long) value);
        } else if (value instanceof Set) {
            StringBuilder builder = new StringBuilder();
            boolean isAdd = false;
            for (String string : ((Set<String>) value)) {
                if (isAdd) {
                    builder.append(COMMA_REPLACEMENT);
                }
                builder.append(string);
                isAdd = true;
            }
            cv.put(VALUE, builder.toString());
        }
        Uri uri = new SpMode(name, key, value.getClass()).uri;
        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            return cr.update(uri, cv, null, null) > 0;
        }
        return false;
    }

    /**
     * 获取provider内容
     */
    public static Object getValueToProvider(Context context,
                                            String name,
                                            String key, Class type, Object defaultValue) {
        if (context == null || name == null || key == null || type == null) {
            return defaultValue;
        }
        Uri uri = new SpMode(name, key, type).uri;
        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            if (cursor == null) {
                return defaultValue;
            }
            Object result = null;
            if (cursor.moveToNext()) {
                if (type.isAssignableFrom(String.class)) {
                    result = cursor.getString(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE));
                } else if (type.isAssignableFrom(Integer.class)) {
                    result = cursor.getInt(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE));
                } else if (type.isAssignableFrom(Boolean.class)) {
                    result = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE)));
                } else if (type.isAssignableFrom(Float.class)) {
                    result = cursor.getFloat(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE));
                } else if (type.isAssignableFrom(Long.class)) {
                    result = cursor.getLong(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE));
                } else if (type.isAssignableFrom(Set.class)) {
                    String setStrs = cursor.getString(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE));
                    if (setStrs != null) {
                        String[] setStr = setStrs.split(COMMA_REPLACEMENT);
                        if (setStr != null) {
                            HashSet<String> set = new HashSet<>();
                            for (String s : setStr) {
                                set.add(s);
                            }
                            result = set;
                        }
                    }
                }
            }
            if (result == null) {
                return defaultValue;
            }
            return result;
        }
        return defaultValue;
    }

    public static boolean removeToProvider(Context context, String name, String key) {
        if (context == null || name == null || key == null) {
            return false;
        }
        Uri uri = new SpMode(name, key, TYPE_CLEAN).uri;
        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            return cr.delete(uri, null, null) > 0;
        }
        return false;
    }

    public static boolean clearToProvider(Context context, String name) {
        if (context == null || name == null) {
            return false;
        }
        Uri uri = new SpMode(name, TYPE_CLEAN, TYPE_CLEAN).uri;
        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            return cr.delete(uri, null, null) > 0;
        }
        return false;
    }
}
