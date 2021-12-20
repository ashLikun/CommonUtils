package com.ashlikun.utils.provider

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.SharedPreUtils.getSP
import com.ashlikun.utils.provider.mode.SpMode
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/30 0030　下午 5:14
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：内容提供者用到的sp,只处理[BaseContentProvider.HANDLE_SP]类型
 */
class ImpSpProvider : IContentProvider {
    fun contains(name: String): Boolean {
        val sp = getSP()
        return sp != null && sp.contains(name)
    }

    fun remove(name: String, key: String): Boolean {
        val editor = getSP(name).edit()
        editor.remove(key)
        return editor.commit()
    }

    fun clear(name: String): Boolean {
        val sp = getSP(name)
        val editor = sp.edit()
        editor.clear()
        return editor.commit()
    }

    /**
     * sp: path: sp/type/name/key---->1:sp
     * content://包名.ContentProvider/handle_sp/string/user/nickname/
     */
    override fun query(uri: Uri): Cursor? {
        val mode = SpMode(uri)
        if (!mode.isException) {
            //构建一个游标
            val cursor = MatrixCursor(
                arrayOf(
                    SP_CURSOR_COLUMN_NAME,
                    SP_CURSOR_COLUMN_KEY,
                    SP_CURSOR_COLUMN_VALUE
                )
            )
            val value = getValue(mode.key, mode.name, mode.type)
            if (value != null) {
                val rows = arrayOfNulls<Any>(3)
                rows[0] = mode.name
                rows[1] = mode.key
                rows[2] = value
                //向游标里面添加数据
                cursor.addRow(rows)
                return cursor
            }
        }
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val mode = SpMode(uri)
        var obj = values!![VALUE]
        if (!mode.isException && obj != null) {
            //set类型特殊处理
            if (TYPE_STRING_SET == mode.type && obj is String) {
                val setStrs = obj
                if (setStrs != null) {
                    val setStr = setStrs.split(COMMA_REPLACEMENT).toTypedArray()
                    if (setStr != null) {
                        val set = HashSet<String>()
                        for (s in setStr) {
                            set.add(s)
                        }
                        obj = set
                    }
                }
            }
            return if (setValue(mode.key, obj, mode.name)) Uri.parse("true") else null
        }
        return null
    }

    override fun delete(uri: Uri): Int {
        val mode = SpMode(uri)
        if (!mode.isException && mode.type == TYPE_CLEAN) {
            return if (clear(mode.name)) 1 else 0
        }
        return if (remove(mode.name, mode.key)) 1 else 0
    }

    override fun getType(uri: Uri): String? {
        val mode = SpMode(uri)
        return if (!mode.isException && mode.handle != null) {
            mode.handle
        } else null
    }

    /**
     * 获取sp内容
     */
    inline fun getValue(
        key: String, name: String, type: String
    ): Any? {
        val sp = getSP(name)
        //如果没有这个值，就返回null，调用者自己返回默认值
        if (!sp.contains(key)) {
            return null
        }
        when (type) {
            TYPE_STRING -> return sp.getString(key, null)
            TYPE_INT -> return sp.getInt(key, -1)
            TYPE_BOOLEAN -> return sp.getBoolean(key, false)
            TYPE_FLOAT -> return sp.getFloat(key, -1f)
            TYPE_LONG -> return sp.getLong(key, -1)
            TYPE_STRING_SET -> return sp.getStringSet(key, null)
        }
        return null
    }

    /**
     * 保存sp内容
     */
    @Synchronized
    fun <T> setValue(key: String, value: T, name: String): Boolean {
        val sp = getSP(name) ?: return false
        val editor = sp.edit()
        if (value is String) {
            editor.putString(key, value)
        } else if (value is Int) {
            editor.putInt(key, value)
        } else if (value is Boolean) {
            editor.putBoolean(key, value)
        } else if (value is Float) {
            editor.putFloat(key, value)
        } else if (value is Long) {
            editor.putLong(key, value)
        } else if (value is Set<*>) {
            editor.putStringSet(key, value as Set<String>)
        }
        return editor.commit()
    }

    companion object {
        /**
         * 数据类型
         */
        const val TYPE_STRING = "string"
        const val TYPE_INT = "int"
        const val TYPE_LONG = "long"
        const val TYPE_FLOAT = "float"
        const val TYPE_BOOLEAN = "boolean"
        const val TYPE_STRING_SET = "string_set"
        const val COMMA_REPLACEMENT = "__COMMA__"
        const val VALUE = "VALUE"

        /**
         * 构建后的游标属性
         */
        const val SP_CURSOR_COLUMN_NAME = "cursor_name"
        const val SP_CURSOR_COLUMN_KEY = "cursor_key"
        const val SP_CURSOR_COLUMN_VALUE = "cursor_value"

        /**
         * sp操作类型
         */
        const val TYPE_CLEAN = "clean"

        /**
         * 设置Provider内容
         */
        fun setValueToProvider(
            key: String,
            value: Any,
            name: String,
        ): Boolean {
            val cv = ContentValues()
            if (value is String) {
                cv.put(VALUE, value)
            } else if (value is Int) {
                cv.put(VALUE, value)
            } else if (value is Boolean) {
                cv.put(VALUE, value)
            } else if (value is Float) {
                cv.put(VALUE, value)
            } else if (value is Long) {
                cv.put(VALUE, value)
            } else if (value is Set<*>) {
                val builder = StringBuilder()
                var isAdd = false
                for (string in value as Set<String>) {
                    if (isAdd) {
                        builder.append(COMMA_REPLACEMENT)
                    }
                    builder.append(string)
                    isAdd = true
                }
                cv.put(VALUE, builder.toString())
            }
            val uri = SpMode(name, key, value.javaClass).uri
            if (uri != null) {
                val cr = AppUtils.app.contentResolver
                return cr.update(uri, cv, null, null) > 0
            }
            return false
        }

        /**
         * 获取provider内容
         */
        fun <T> getValueToProvider(
            key: String, defaultValue: T, name: String, type: Class<T>
        ): T {
            val uri = SpMode(name, key, type).uri
            if (uri != null) {
                val cr = AppUtils.app.contentResolver
                val cursor = cr.query(uri, null, null, null, null) ?: return defaultValue
                var result: T? = null
                if (cursor.moveToNext()) {
                    if (type.isAssignableFrom(String::class.java)) {
                        result =
                            cursor.getString(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE)) as T
                    } else if (type.isAssignableFrom(Int::class.java)) {
                        result = cursor.getInt(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE)) as T
                    } else if (type.isAssignableFrom(Boolean::class.java)) {
                        result = java.lang.Boolean.valueOf(
                            cursor.getString(
                                cursor.getColumnIndex(
                                    SP_CURSOR_COLUMN_VALUE
                                )
                            )
                        ) as T
                    } else if (type.isAssignableFrom(Float::class.java)) {
                        result = cursor.getFloat(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE)) as T
                    } else if (type.isAssignableFrom(Long::class.java)) {
                        result = cursor.getLong(cursor.getColumnIndex(SP_CURSOR_COLUMN_VALUE)) as T
                    } else if (type.isAssignableFrom(MutableSet::class.java)) {
                        val setStrs = cursor.getString(
                            cursor.getColumnIndex(
                                SP_CURSOR_COLUMN_VALUE
                            )
                        )
                        if (setStrs != null) {
                            val setStr = setStrs.split(COMMA_REPLACEMENT).toTypedArray()
                            if (setStr != null) {
                                val set = mutableSetOf<String>()
                                set.addAll(setStr)
                                result = set as T
                            }
                        }
                    }
                }
                return (result ?: defaultValue)
            }
            return defaultValue
        }

        fun removeToProvider(context: Context?, name: String?, key: String?): Boolean {
            if (context == null || name == null || key == null) {
                return false
            }
            val uri = SpMode(name, key, TYPE_CLEAN).uri
            if (uri != null) {
                val cr = context.contentResolver
                return cr.delete(uri, null, null) > 0
            }
            return false
        }

        fun clearToProvider(context: Context?, name: String?): Boolean {
            if (context == null || name == null) {
                return false
            }
            val uri = SpMode(name, TYPE_CLEAN, TYPE_CLEAN).uri
            if (uri != null) {
                val cr = context.contentResolver
                return cr.delete(uri, null, null) > 0
            }
            return false
        }
    }
}