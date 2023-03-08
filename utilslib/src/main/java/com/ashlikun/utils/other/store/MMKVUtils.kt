package com.ashlikun.utils.other.store

import android.os.Parcelable
import com.ashlikun.utils.AppUtils
import com.tencent.mmkv.MMKV
import kotlin.reflect.KClass


/**
 * 作者　　: 李坤
 * 创建时间: 2021.12.21　15:45
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：MMKV 的操作
 */
internal class MMKVUtils : IStore {
    companion object {
        val map = mutableMapOf<String, MMKV>()

        val kv: MMKV by lazy {
            if (StoreUtils.MMKV_MODE != null) {
                MMKV.defaultMMKV(StoreUtils.MMKV_MODE, null)
            } else {
                MMKV.defaultMMKV()
            }
        }

        @Synchronized
        fun getKv(name: String): MMKV {
            if (!map.contains(name)) {
                map[name] = if (name.isEmpty() || name == StoreUtils.DEFAULT) {
                    kv
                } else {
                    if (StoreUtils.MMKV_MODE != null) {
                        MMKV.mmkvWithID(name, StoreUtils.MMKV_MODE)
                    } else {
                        MMKV.mmkvWithID(name)
                    }
                }
            }
            return map[name]!!
        }

        fun init() {
            MMKV.initialize(AppUtils.base)
        }
    }

    override fun putString(key: String, value: String, name: String) = setKeyAndValue(key, value, name)

    override fun getString(key: String, defaultValue: String, name: String) = getValue(key, defaultValue, name)!!

    override fun putInt(key: String, value: Int, name: String) = setKeyAndValue(key, value, name)!!

    override fun getInt(key: String, defaultValue: Int, name: String) = getValue(key, defaultValue, name)!!

    override fun putLong(key: String, value: Long, name: String) = setKeyAndValue(key, value, name)


    override fun getLong(key: String, defaultValue: Long, name: String) = getValue(key, defaultValue, name)!!


    override fun putFloat(key: String, value: Float, name: String) = setKeyAndValue(key, value, name)


    override fun getFloat(key: String, defaultValue: Float, name: String) = getValue(key, defaultValue, name)!!

    override fun putDouble(key: String, value: Double, name: String) = setKeyAndValue(key, value, name)

    override fun getDouble(key: String, defaultValue: Double, name: String) = getValue(key, defaultValue, name)!!

    override fun putBoolean(key: String, value: Boolean, name: String) = setKeyAndValue(key, value, name)


    override fun getBoolean(key: String, defaultValue: Boolean, name: String) = getValue(key, defaultValue, name)!!

    override fun putByteArray(key: String, value: ByteArray, name: String) = setKeyAndValue(key, value, name)
    override fun getByteArray(key: String, defaultValue: ByteArray, name: String) = getValue(key, defaultValue, name)!!

    override fun putSet(key: String, value: Set<String>, name: String) = setKeyAndValue(key, value, name)

    override fun getSet(
        key: String, defaultValue: Set<String>, name: String
    ) = getValue(key, defaultValue, name)!!

    override fun putParcelable(key: String, value: Parcelable, name: String) = setKeyAndValue(key, value, name)


    override fun <T : Parcelable> getParcelable(key: String, defaultValue: T?, cls: Class<T>, name: String): T? {
        return getKv(name).decodeParcelable(key, cls)
    }

    override fun contains(key: String, name: String) = getKv(name).contains(key)

    override fun remove(key: String, name: String): Boolean {
        getKv(name).removeValueForKey(key)
        return true
    }

    override fun clear(name: String): Boolean {
        getKv(name).clearAll()
        return true
    }

    /**
     * 获取指定的key 对应的值
     *
     * @param name 文件名
     * @return 对应值
     */
    inline fun <reified T : Any> getValue(
        key: String,
        default: T?, name: String
    ) = getValueType(key, default, name, T::class)

    fun <T : Any> getValueType(key: String, default: T?, name: String, type: KClass<T>): T? {
        return when (type) {
            String::class -> getKv(name).decodeString(key, default.toString())
            Int::class -> getKv(name).decodeInt(key, default as Int)
            Boolean::class -> getKv(name).decodeBool(key, default as Boolean)
            Float::class -> getKv(name).decodeFloat(key, default as Float)
            Long::class -> getKv(name).decodeLong(key, default as Long)
            Double::class -> getKv(name).decodeDouble(key, default as Double)
            ByteArray::class -> getKv(name).decodeBytes(key, default as ByteArray)
            Set::class -> getKv(name).decodeStringSet(key, default as? Set<String>)
            Parcelable::class -> getKv(name).decodeParcelable(
                key, type.java as? Class<Parcelable>, default as? Parcelable?
            )
            else -> getKv(name).decodeString(key, default.toString())
        } as? T?
    }

    fun setKeyAndValue(key: String, value: Any, name: String): Boolean {
        return when (value) {
            is String -> getKv(name).encode(key, value)
            is Int -> getKv(name).encode(key, value)
            is Boolean -> getKv(name).encode(key, value)
            is Float -> getKv(name).encode(key, value)
            is Long -> getKv(name).encode(key, value)
            is Double -> getKv(name).encode(key, value)
            is Parcelable -> getKv(name).encode(key, value)
            is ByteArray -> getKv(name).encode(key, value)
            is Set<*> -> getKv(name).encode(key, value as Set<String>)
            else -> getKv(name).encode(key, value.toString())
        }
    }
}