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
    fun putString(key: String, value: String?): Boolean {
        return putString(DEFAULT, key, value)
    }

    fun putString(name: String, key: String, value: String?): Boolean {
        return setKeyAndValue(name, key, value)
    }

    fun getString(key: String, defaultValue: String = "", name: String = DEFAULT): String {
        return getValue(name, key, String::class.java, defaultValue) as String
    }

    fun getStringName(name: String, key: String): String {
        return getValue(name, key, String::class.java, "")
    }

    fun putInt(key: String, value: Int): Boolean {
        return putInt(DEFAULT, key, value)
    }

    fun putInt(name: String, key: String, value: Int): Boolean {
        return setKeyAndValue(name, key, value)
    }

    fun getInt(key: String): Int {
        return getInt(key, -1)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return getInt(DEFAULT, key, defaultValue)
    }

    fun getInt(name: String, key: String, defaultValue: Int): Int {
        return getValue(name, key, Int::class.java, defaultValue) as Int
    }

    fun getInt(name: String, key: String): Int {
        return getValue(name, key, Int::class.java, -1) as Int
    }

    fun putLong(key: String, value: Long): Boolean {
        return putLong(DEFAULT, key, value)
    }

    fun putLong(name: String, key: String, value: Long): Boolean {
        return setKeyAndValue(name, key, value)
    }

    fun getLong(key: String): Long {
        return getLong(key, -1)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return getLong(DEFAULT, key, defaultValue)
    }

    fun getLong(name: String, key: String, defaultValue: Long): Long {
        return getValue(name, key, Long::class.java, defaultValue) as Long
    }

    fun getLong(name: String, key: String): Long {
        return getValue(name, key, Long::class.java, -1) as Long
    }

    fun putFloat(key: String, value: Float): Boolean {
        return putFloat(DEFAULT, key, value)
    }

    fun putFloat(name: String, key: String, value: Float): Boolean {
        return setKeyAndValue(name, key, value)
    }

    fun getFloat(key: String): Float {
        return getFloat(key, -1f)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return getFloat(DEFAULT, key, defaultValue)
    }

    fun getFloat(name: String, key: String): Float {
        return getValue(name, key, Float::class.java, -1) as Float
    }

    fun getFloat(name: String, key: String, defaultValue: Float): Float {
        return getValue(name, key, Float::class.java, defaultValue) as Float
    }

    fun putBoolean(key: String, value: Boolean): Boolean {
        return putBoolean(DEFAULT, key, value)
    }

    fun putBoolean(name: String, key: String, value: Boolean): Boolean {
        return setKeyAndValue(name, key, value)
    }

    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }

    fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean {
        return getValue(DEFAULT, key, Boolean::class.java, defaultValue) as Boolean
    }

    fun getBoolean(
        name: String, key: String,
        defaultValue: Boolean
    ): Boolean {
        return getValue(name, key, Boolean::class.java, defaultValue) as Boolean
    }

    fun getBoolean(name: String, key: String): Boolean {
        return getValue(name, key, Boolean::class.java, false) as Boolean
    }

    fun putSet(key: String, value: MutableSet<String>?): Boolean {
        return putSet(DEFAULT, key, value)
    }

    fun putSet(name: String, key: String, value: MutableSet<String>?): Boolean {
        return setKeyAndValue(name, key, value)
    }

    fun getSet(key: String): Set<String>? {
        return getSet(key, mutableSetOf<String>())
    }

    fun getSet(
        key: String,
        defaultValue: MutableSet<String>?
    ): MutableSet<String>? {
        return getValue(DEFAULT, key, MutableSet::class.java, defaultValue) as MutableSet<String>?
    }

    fun getSet(name: String, key: String): MutableSet<String>? {
        return getValue(name, key, MutableSet::class.java, null) as MutableSet<String>?
    }

    fun getSet(name: String, key: String, defaultValue: MutableSet<String>?): MutableSet<String>? {
        return getValue(name, key, MutableSet::class.java, defaultValue) as MutableSet<String>?
    }

    fun remove(key: String): Boolean {
        return remove(DEFAULT, key)
    }

    fun remove(name: String, key: String): Boolean {
        return if (isMainProcess) {
            val editor = getSP(name).edit()
            editor.remove(key)
            editor.commit()
        } else {
            //其他进程，使用ContentProvider
            ImpSpProvider.removeToProvider(AppUtils.app, name, key)
        }
    }

    fun clear(name: String = DEFAULT): Boolean {
        if (isMainProcess) {
            val sp = getSP(name)
            val editor = sp.edit()
            editor.clear()
            return editor.commit()
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
    inline fun <reified T> getValue(name: String, key: String, defaule: T): T {
        if (isMainProcess) {
            val sp = getSP(name) ?: return defaule
            return when {
                T::class.java.isAssignableFrom(String::class.java) -> sp.getString(
                    key,
                    defaule.toString()
                )
                T::class.java.isAssignableFrom(Int::class.java) -> sp.getInt(
                    key,
                    (defaule as Int?)!!
                )
                T::class.java.isAssignableFrom(Boolean::class.java) -> sp.getBoolean(
                    key,
                    defaule as Boolean
                )
                T::class.java.isAssignableFrom(Float::class.java) -> sp.getFloat(
                    key,
                    (defaule as Float)
                )
                T::class.java.isAssignableFrom(Long::class.java) -> sp.getLong(
                    key,
                    defaule as Long
                )
                T::class.java.isAssignableFrom(MutableSet::class.java) -> sp.getStringSet(
                    key,
                    defaule as MutableSet<String>
                )
                else -> {
                    sp.getString(
                        key,
                        defaule.toString()
                    )
                }
            } as T
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.getValueToProvider(AppUtils.app, name, key, defaule)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return defaule
    }

    fun setKeyAndValue(key: String, value: Any?): Boolean {
        return setKeyAndValue(DEFAULT, key, value)
    }

    fun setKeyAndValue(name: String, key: String, value: Any?): Boolean {
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
                return ImpSpProvider.setValueToProvider(AppUtils.app, name, key, value)
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
        Log.e("testGet", "getString = " + getString("sp_testString", null))
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