package com.ashlikun.utils.other.store

import android.content.Context
import android.os.Parcelable
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.main.ProcessUtils.isMainProcess
import com.ashlikun.utils.provider.ImpSpProvider
import kotlin.reflect.KClass

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:45
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Sp工具类,数据量大建议使用MMKV  implementation 'com.tencent:mmkv:1.2.11'
 */

internal class SharedPreUtils : IStore {


    override fun putString(key: String, value: String, name: String) =
        setKeyAndValue(key, value, name)

    override fun getString(key: String, defaultValue: String, name: String) =
        getValue(key, defaultValue, name)

    override fun putInt(key: String, value: Int, name: String) =
        setKeyAndValue(key, value, name)

    override fun getInt(key: String, defaultValue: Int, name: String) =
        getValue(key, defaultValue, name)

    override fun putLong(key: String, value: Long, name: String) =
        setKeyAndValue(key, value, name)


    override fun getLong(key: String, defaultValue: Long, name: String) =
        getValue(key, defaultValue, name)


    override fun putFloat(key: String, value: Float, name: String) =
        setKeyAndValue(key, value, name)


    override fun getFloat(key: String, defaultValue: Float, name: String) =
        getValue(key, defaultValue, name)

    override fun putBoolean(key: String, value: Boolean, name: String): Boolean {
        return setKeyAndValue(key, value, name)
    }

    override fun putByteArray(key: String, value: ByteArray, name: String) = setKeyAndValue(key, value, name)
    override fun getByteArray(key: String, defaultValue: ByteArray, name: String) = getValue(key, defaultValue, name)
    override fun getDouble(key: String, defaultValue: Double, name: String) = getValue(key, defaultValue, name)

    override fun putDouble(key: String, value: Double, name: String) = setKeyAndValue(key, value, name)

    override fun getBoolean(key: String, defaultValue: Boolean, name: String) =
        getValue(key, defaultValue, name)


    override fun putSet(key: String, value: Set<String>, name: String) =
        setKeyAndValue(key, value, name)


    override fun getSet(
        key: String,
        defaultValue: Set<String>,
        name: String
    ) = getValue(key, defaultValue, name)

    override fun putParcelable(key: String, value: Parcelable, name: String) =
        throw RuntimeException("SharedPre no Parcelable")


    override fun <T : Parcelable> getParcelable(key: String, defaultValue: T, name: String) =
        throw RuntimeException("SharedPre no Parcelable")

    override fun remove(key: String, name: String): Boolean {
        return if (isMainProcess) {
            getSP(name).edit().remove(key).apply()
            true
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

    override fun clear(name: String): Boolean {
        if (isMainProcess) {
            getSP(name).edit().clear().apply()
            return true
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
    inline fun <reified T : Any> getValue(
        key: String,
        default: T,
        name: String
    ) =
        getValueType(key, default, name, T::class)

    fun <T : Any> getValueType(key: String, default: T, name: String, type: KClass<T>): T {
        if (isMainProcess) {
            val sp = getSP(name) ?: return default
            return when (type) {
                String::class -> sp.getString(key, default.toString())
                Int::class -> sp.getInt(key, default as Int)
                Boolean::class -> sp.getBoolean(key, default as Boolean)
                Float::class -> sp.getFloat(key, default as Float)
                Long::class -> sp.getLong(key, default as Long)
                Set::class -> sp.getStringSet(key, default as Set<String>)
                else -> sp.getString(key, default.toString())
            } as T
        } else {
            //其他进程，使用ContentProvider
            try {
                return ImpSpProvider.getValueToProvider(key, default, name, type)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return default
    }


    fun setKeyAndValue(key: String, value: Any, name: String): Boolean {
        if (isMainProcess) {
            val sp = getSP(name) ?: return false
            val editor = sp.edit()
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Float -> editor.putFloat(key, value)
                is Long -> editor.putLong(key, value)
                is Set<*> -> editor.putStringSet(key, value as Set<String>)
            }
            editor.apply()
            return true
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

    fun getSP(name: String) =
        AppUtils.app.getSharedPreferences(name, Context.MODE_PRIVATE)


}