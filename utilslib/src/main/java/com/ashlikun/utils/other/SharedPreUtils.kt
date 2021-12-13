package com.ashlikun.utils.other

import com.ashlikun.utils.main.ProcessUtils.isMainProcess
import com.ashlikun.utils.main.ProcessUtils.curProcessName
import android.content.Context
import com.ashlikun.utils.other.SharedPreUtils
import java.util.HashSet
import com.ashlikun.utils.main.ProcessUtils
import android.content.SharedPreferences.Editor
import com.ashlikun.utils.provider.ImpSpProvider
import kotlin.jvm.JvmOverloads
import android.content.SharedPreferences
import java.lang.Exception
import java.lang.Class
import android.util.Log
import com.ashlikun.utils.AppUtils
import java.lang.StringBuilder

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:45
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Sp工具类
 */

object SharedPreUtils {
    const val DEFAULT = "default"
    const val USER = "user"

    fun putString(key: String, value: String, name: String = DEFAULT) =
        setKeyAndValue(key, value, name)

    fun getString(key: String, defaultValue: String = "", name: String = DEFAULT) =
        getValue(key, defaultValue, name)

    fun putInt(key: String, value: Int, name: String = DEFAULT) = setKeyAndValue(key, value, name)

    fun getInt(key: String, defaultValue: Int = -1, name: String = DEFAULT) =
        getValue(key, defaultValue, name)

    fun putLong(key: String, value: Long, name: String = DEFAULT) = setKeyAndValue(key, value, name)


    fun getLong(key: String, defaultValue: Long = -1L, name: String = DEFAULT) =
        getValue(key, defaultValue, name)


    fun putFloat(key: String, value: Float, name: String = DEFAULT) =
        setKeyAndValue(key, value, name)


    fun getFloat(key: String, defaultValue: Float = -1f, name: String = DEFAULT) =
        getValue(key, defaultValue, name)


    fun putBoolean(key: String, value: Boolean, name: String = DEFAULT): Boolean {
        return setKeyAndValue(key, value, name)
    }


    fun getBoolean(key: String, defaultValue: Boolean = false, name: String = DEFAULT) =
        getValue(key, defaultValue, name)


    fun putSet(key: String, value: Set<String>, name: String = DEFAULT) =
        setKeyAndValue(key, value, name)


    fun getSet(
        key: String,
        defaultValue: MutableSet<String> = mutableSetOf(),
        name: String = DEFAULT
    ) = getValue(key, defaultValue, name)


    fun remove(key: String, name: String = DEFAULT): Boolean {
        return if (isMainProcess) {
            getSP(name).edit().remove(key).commit()
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.removeToProvider(AppUtils.app, name, key)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    fun clear(name: String = DEFAULT): Boolean {
        if (isMainProcess) {
            return getSP(name).edit().clear().commit()
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.clearToProvider(AppUtils.app, name)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     * 获取指定的key 对应的值
     *
     * @param name 文件名
     * @return 对应值
     */
    inline fun <reified T> getValue(key: String, defaule: T, name: String = DEFAULT) =
        getValueType(key, defaule, name, T::class.java)

    inline fun <T> getValueType(key: String, defaule: T, name: String, type: Class<T>): T {
        if (isMainProcess) {
            val sp = getSP(name) ?: return defaule
            return when {
                type.isAssignableFrom(String::class.java) -> sp.getString(
                    key, defaule.toString()
                )
                type.isAssignableFrom(Int::class.java) -> sp.getInt(
                    key, defaule as Int
                )
                type.isAssignableFrom(Boolean::class.java) -> sp.getBoolean(
                    key, defaule as Boolean
                )
                type.isAssignableFrom(Float::class.java) -> sp.getFloat(
                    key, defaule as Float
                )
                type.isAssignableFrom(Long::class.java) -> sp.getLong(
                    key, defaule as Long
                )
                type.isAssignableFrom(MutableSet::class.java) -> sp.getStringSet(
                    key, defaule as MutableSet<String>
                )
                else -> {
                    sp.getString(key, defaule.toString())
                }
            } as T
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.getValueToProvider(key, defaule, name, type)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return defaule
    }


    fun setKeyAndValue(key: String, value: Any, name: String = DEFAULT): Boolean {
        if (isMainProcess) {
            val sp = getSP(name) ?: return false
            val editor = sp.edit()
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Float -> editor.putFloat(key, value)
                is Long -> editor.putLong(key, value)
                is MutableSet<*> -> editor.putStringSet(
                    key,
                    value?.toMutableList() as MutableSet<String>
                )
            }
            return editor.commit()
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.setValueToProvider(key, value, name)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun getSP(name: String = DEFAULT) =
        AppUtils.app.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun testPut(context: Context?) {
        Log.e("testPut", "getCurProcessName = " + curProcessName)
        putLong("sp_testLong", 123)
        putInt("sp_testInt", 12322222)
        putString("sp_testString", "putString")
        putBoolean("sp_testBoolean", true)
        putFloat("sp_testFloat", 123.369f)
        val strings: MutableSet<String> = HashSet()
        for (i in 0..999) {
            strings.add("putSet + $i")
        }
        putSet("sp_testSet", strings)
    }

    fun testGet(context: Context?) {
        Log.e("testGet", "getCurProcessName = " + curProcessName)
        Log.e("testGet", "getLong = " + getLong("sp_testLong", 999))
        Log.e("testGet", "getInt = " + getInt("sp_testInt"))
        Log.e("testGet", "getString = " + getString("sp_testString", ""))
        Log.e("testGet", "getBoolean = " + getBoolean("sp_testBoolean"))
        Log.e("testGet", "getFloat = " + getFloat("sp_testFloat"))
        val sb = StringBuilder("  sp_testSet  == ")
        val strings: Set<String>? = getSet("sp_testSet")
        if (strings != null) {
            for (s in strings) {
                sb.append(s)
                sb.append(",,,")
            }
        }
        Log.e("testGet", "getSet = $sb")
    }
}