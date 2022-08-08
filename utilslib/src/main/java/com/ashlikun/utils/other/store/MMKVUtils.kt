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

        val kv: MMKV by lazy {
            MMKV.defaultMMKV()
        }

        fun init() {
            MMKV.initialize(AppUtils.base)
        }
    }

    override fun putString(key: String, value: String, name: String) =
        setKeyAndValue(key, value)

    override fun getString(key: String, defaultValue: String, name: String) =
        getValue(key, defaultValue)!!

    override fun putInt(key: String, value: Int, name: String) =
        setKeyAndValue(key, value)!!

    override fun getInt(key: String, defaultValue: Int, name: String) =
        getValue(key, defaultValue)!!

    override fun putLong(key: String, value: Long, name: String) =
        setKeyAndValue(key, value)


    override fun getLong(key: String, defaultValue: Long, name: String) =
        getValue(key, defaultValue)!!


    override fun putFloat(key: String, value: Float, name: String) =
        setKeyAndValue(key, value)


    override fun getFloat(key: String, defaultValue: Float, name: String) =
        getValue(key, defaultValue)!!

    override fun putDouble(key: String, value: Double, name: String) = setKeyAndValue(key, value)

    override fun getDouble(key: String, defaultValue: Double, name: String) = getValue(key, defaultValue)!!

    override fun putBoolean(key: String, value: Boolean, name: String) = setKeyAndValue(key, value)


    override fun getBoolean(key: String, defaultValue: Boolean, name: String) =
        getValue(key, defaultValue)!!

    override fun putByteArray(key: String, value: ByteArray, name: String) = setKeyAndValue(key, value)
    override fun getByteArray(key: String, defaultValue: ByteArray, name: String) = getValue(key, defaultValue)!!

    override fun putSet(key: String, value: Set<String>, name: String) =
        setKeyAndValue(key, value)

    override fun getSet(
        key: String, defaultValue: Set<String>, name: String) = getValue(key, defaultValue)!!

    override fun putParcelable(key: String, value: Parcelable, name: String) =
        setKeyAndValue(key, value)


    override fun <T : Parcelable> getParcelable(key: String, defaultValue: T?, cls: Class<T>, name: String): T? {
        return kv.decodeParcelable(key, cls)
    }

    override fun remove(key: String, name: String): Boolean {
        kv.removeValueForKey(key)
        return true
    }

    override fun clear(name: String): Boolean {
        kv.clearAll()
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
        default: T?,
    ) = getValueType(key, default, T::class)

    fun <T : Any> getValueType(key: String, default: T?, type: KClass<T>): T? {
        return when (type) {
            String::class -> kv.decodeString(key, default.toString())
            Int::class -> kv.decodeInt(key, default as Int)
            Boolean::class -> kv.decodeBool(key, default as Boolean)
            Float::class -> kv.decodeFloat(key, default as Float)
            Long::class -> kv.decodeLong(key, default as Long)
            Double::class -> kv.decodeDouble(key, default as Double)
            ByteArray::class -> kv.decodeBytes(key, default as ByteArray)
            Set::class -> kv.decodeStringSet(key, default as? Set<String>)
            Parcelable::class -> kv.decodeParcelable(
                key, type.java as? Class<Parcelable>, default as? Parcelable?
            )
            else -> kv.decodeString(key, default.toString())
        } as? T?
    }

    fun setKeyAndValue(key: String, value: Any): Boolean {
        return when (value) {
            is String -> kv.encode(key, value)
            is Int -> kv.encode(key, value)
            is Boolean -> kv.encode(key, value)
            is Float -> kv.encode(key, value)
            is Long -> kv.encode(key, value)
            is Double -> kv.encode(key, value)
            is Parcelable -> kv.encode(key, value)
            is ByteArray -> kv.encode(key, value)
            is Set<*> -> kv.encode(key, value as Set<String>)
            else -> kv.encode(key, value.toString())
        }
    }
}