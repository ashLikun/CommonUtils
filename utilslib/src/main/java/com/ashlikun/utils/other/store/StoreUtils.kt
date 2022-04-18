package com.ashlikun.utils.other.store

import android.content.Context
import android.os.Parcelable
import android.util.Log
import com.ashlikun.utils.main.ProcessUtils
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2021.12.21　16:08
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：轻量级存储的工具类
 */
/**
 * put的时候this是value
 */
inline fun String.putStore(key: String, name: String = StoreUtils.DEFAULT) =
    StoreUtils.putString(key, this, name)

inline fun Int.putStore(key: String, name: String = StoreUtils.DEFAULT) =
    StoreUtils.putInt(key, this, name)

inline fun Boolean.putStore(key: String, name: String = StoreUtils.DEFAULT) =
    StoreUtils.putBoolean(key, this, name)

inline fun Float.putStore(key: String, name: String = StoreUtils.DEFAULT) =
    StoreUtils.putFloat(key, this, name)

inline fun Long.putStore(key: String, name: String = StoreUtils.DEFAULT) =
    StoreUtils.putLong(key, this, name)

inline fun Set<String>.putStore(key: String, name: String = StoreUtils.DEFAULT) =
    StoreUtils.putSet(key, this, name)

/**
 * get的时候this是key
 */
inline fun String.getStore(defaultValue: String = "", name: String = StoreUtils.DEFAULT) =
    StoreUtils.getString(this, defaultValue, name)

inline fun String.getIntStore(defaultValue: Int = 0, name: String = StoreUtils.DEFAULT) =
    StoreUtils.getInt(this, defaultValue, name)

inline fun String.getBoolStore(
    defaultValue: Boolean = false,
    name: String = StoreUtils.DEFAULT
) =
    StoreUtils.getBoolean(this, defaultValue, name)

inline fun String.getLongStore(defaultValue: Long = 0, name: String = StoreUtils.DEFAULT) =
    StoreUtils.getLong(this, defaultValue, name)

inline fun String.getFloatStore(defaultValue: Float = 0f, name: String = StoreUtils.DEFAULT) =
    StoreUtils.getFloat(this, defaultValue, name)

inline fun String.getSetStore(
    defaultValue: Set<String> = setOf(), name: String = StoreUtils.DEFAULT
) = StoreUtils.getSet(this, defaultValue, name)

object StoreUtils {
    const val DEFAULT = "default"
    const val USER = "user"
    val store: IStore by lazy {
        try {
            //判断是否有MMKV库
            MMKVUtils.init()
            MMKVUtils()
        } catch (e: NoClassDefFoundError) {
            SharedPreUtils()
        }
    }

    //可以不用调用
    fun init() {
        store
    }

    fun putString(key: String, value: String, name: String = DEFAULT) =
        store.putString(key, value, name)

    fun getString(key: String, defaultValue: String = "", name: String = DEFAULT) =
        store.getString(key, defaultValue, name)

    fun putInt(key: String, value: Int, name: String = DEFAULT) = store.putInt(key, value, name)

    fun getInt(key: String, defaultValue: Int = 0, name: String = DEFAULT) =
        store.getInt(key, defaultValue, name)

    fun putLong(key: String, value: Long, name: String = DEFAULT) = store.putLong(key, value, name)


    fun getLong(key: String, defaultValue: Long = 0L, name: String = DEFAULT) =
        store.getLong(key, defaultValue, name)


    fun putFloat(key: String, value: Float, name: String = DEFAULT) =
        store.putFloat(key, value, name)


    fun getFloat(key: String, defaultValue: Float = 0f, name: String = DEFAULT) =
        store.getFloat(key, defaultValue, name)

    fun putBoolean(key: String, value: Boolean, name: String = DEFAULT) =
        store.putBoolean(key, value, name)


    fun getBoolean(key: String, defaultValue: Boolean = false, name: String = DEFAULT) =
        store.getBoolean(key, defaultValue, name)


    fun putSet(key: String, value: Set<String>, name: String = DEFAULT) =
        store.putSet(key, value, name)


    fun getSet(key: String, defaultValue: Set<String> = setOf(), name: String = DEFAULT) =
        store.getSet(key, defaultValue, name)

    fun putParcelable(key: String, value: Parcelable, name: String = DEFAULT) =
        store.putParcelable(key, value, name)


    fun <T : Parcelable> getParcelable(key: String, defaultValue: T?, name: String = DEFAULT) =
        store.getParcelable(key, defaultValue as? Parcelable, name)

    fun remove(key: String, name: String = DEFAULT) = store.remove(key, name)

    fun clear(name: String = DEFAULT) = store.clear(name)

    fun testPut(context: Context?) {
        Log.e("testPut", "getCurProcessName = " + ProcessUtils.curProcessName)
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
        Log.e("testGet", "getCurProcessName = " + ProcessUtils.curProcessName)
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