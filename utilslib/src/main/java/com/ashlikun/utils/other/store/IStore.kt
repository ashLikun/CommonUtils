package com.ashlikun.utils.other.store

import android.os.Parcelable
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.main.ProcessUtils
import com.ashlikun.utils.provider.ImpSpProvider
import kotlin.reflect.KClass

/**
 * 作者　　: 李坤
 * 创建时间: 2021.12.21　16:09
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：轻量级存储的接口
 */
interface IStore {

    fun putString(key: String, value: String, name: String = StoreUtils.DEFAULT): Boolean

    fun getString(
        key: String,
        defaultValue: String = "",
        name: String = StoreUtils.DEFAULT
    ): String

    fun putInt(key: String, value: Int, name: String = StoreUtils.DEFAULT): Boolean

    fun getInt(key: String, defaultValue: Int = 0, name: String = StoreUtils.DEFAULT): Int

    fun putLong(key: String, value: Long, name: String = StoreUtils.DEFAULT): Boolean


    fun getLong(key: String, defaultValue: Long = 0L, name: String = StoreUtils.DEFAULT): Long


    fun putFloat(key: String, value: Float, name: String = StoreUtils.DEFAULT): Boolean


    fun getFloat(
        key: String, defaultValue: Float = 0f, name: String = StoreUtils.DEFAULT
    ): Float

    fun putDouble(key: String, value: Double, name: String = StoreUtils.DEFAULT): Boolean


    fun getDouble(
        key: String, defaultValue: Double = 0.0, name: String = StoreUtils.DEFAULT
    ): Double

    fun putBoolean(key: String, value: Boolean, name: String = StoreUtils.DEFAULT): Boolean


    fun getBoolean(
        key: String, defaultValue: Boolean = false, name: String = StoreUtils.DEFAULT
    ): Boolean

    fun putByteArray(key: String, value: ByteArray, name: String = StoreUtils.DEFAULT): Boolean

    fun getByteArray(
        key: String, defaultValue: ByteArray = ByteArray(0), name: String = StoreUtils.DEFAULT
    ): ByteArray

    fun putSet(key: String, value: Set<String>, name: String = StoreUtils.DEFAULT): Boolean

    fun getSet(
        key: String, defaultValue: Set<String> = setOf(), name: String = StoreUtils.DEFAULT
    ): Set<String>

    fun putParcelable(key: String, value: Parcelable, name: String): Boolean

    fun <T : Parcelable> getParcelable(key: String, defaultValue: T?, cls: Class<T>, name: String): T?

    fun remove(key: String, name: String = StoreUtils.DEFAULT): Boolean

    fun clear(name: String = StoreUtils.DEFAULT): Boolean

}