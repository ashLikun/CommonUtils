package com.ashlikun.utils.ui.extend

import android.os.Parcelable
import com.ashlikun.utils.other.cache.CacheDiskUtils
import java.io.Serializable
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2019/2/27　16:44
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：字符串扩展函数
 */
fun String?.isPositive() = this?.toDoubleOrNull() ?: 0.0 > 0

/**
 * 转换成double，错误就0
 */
fun String?.double() = this?.toDoubleOrNull() ?: 0.0

/**
 * 转换成int，错误就0
 */
fun String?.int() = this?.toDoubleOrNull() ?: 0

/**
 * 复制 Serializable
 * @return [T] obj
 */
fun <T> Serializable.copy(): T {
    // 写入字节流
    val byte = CacheDiskUtils.serializable2Bytes(this)
    // 读取字节流
    val obs = CacheDiskUtils.bytes2Object(byte)
    return obs as T
}

/**
 * 复制 Parcelable
 * @return [T] obj
 */
fun <T> Parcelable.copy(creator: Parcelable.Creator<T>): T {
    // 写入字节流
    val byte = CacheDiskUtils.parcelable2Bytes(this)
    // 读取字节流
    val obs = CacheDiskUtils.bytes2Parcelable(byte, creator)
    return obs as T
}

/**
 * 出栈
 */
fun <T> Stack<T>.popOrNull(): T? {
    return try {
        this.pop()
    } catch (e: Exception) {
        null
    }
}
/**
 * 出栈
 */
fun <T> Stack<T>.lastElementOrNull(): T? {
    return try {
        this.lastElement()
    } catch (e: Exception) {
        null
    }
}