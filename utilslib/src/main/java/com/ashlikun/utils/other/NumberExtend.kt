package com.ashlikun.utils.other

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2023/3/3　15:53
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
inline fun Boolean.toInt() = if (this) 1 else 0

/**
 *  1=是 0=否
 */
inline fun Int.toBoolean() = this == 1
inline fun String.toBooleanInt() = this == "1"

inline fun Int?.orEmpty(defaultValue: Int = 0): Int =
    this ?: defaultValue

inline fun Float?.orEmpty(defaultValue: Float = 0f): Float =
    (if (this?.isNaN() == true) null else this) ?: defaultValue

inline fun Double?.orEmpty(defaultValue: Double = 0.0): Double =
    (if (this?.isNaN() == true) null else this) ?: defaultValue

inline fun Long?.orEmpty(defaultValue: Long = 0): Long =
    this ?: defaultValue

inline fun Number?.toString() = this?.toString() ?: "0"

inline fun String?.toIntOrEmpty(defaultValue: Int = 0): Int =
    this?.toIntOrNull() ?: defaultValue

inline fun String?.toFloatOrEmpty(defaultValue: Float = 0f): Float =
    this?.toFloatOrNull() ?: defaultValue

inline fun String?.toDoubleOrEmpty(defaultValue: Double = 0.0): Double =
    this?.toDoubleOrNull() ?: defaultValue

inline fun String?.toLongOrEmpty(defaultValue: Long = 0): Long =
    this?.toLongOrNull() ?: defaultValue

/**
 * 保留小数后几位位，四舍五入
 * 10.0 -->10
 * 10.1 -->10.1
 * 10.88 -->10.9
 * @param isJumpInt 整数的时候直接返回
 */
fun Number.toFormat45(wei: Int = 1, isJumpInt: Boolean = false): String {
    if (isJumpInt && (this is Int || this is Long)) {
        return this.toString()
    }
    val format = DecimalFormat("#" + if (wei == 0) "" else "." + MutableList(wei) { "#" }.joinToString("") { it }, DecimalFormatSymbols(Locale.CHINA))
    //舍弃规则，RoundingMode.HALF_EVEN表示直接舍弃。
    format.roundingMode = RoundingMode.HALF_UP
    return format.format(this)
}

/**
 * 保留小数后几位位，四舍五入, 这里保留0
 * 10.0 -->10.0
 * 10.1 -->10.1
 * 10.88 -->10.9
 * @param isJumpInt 整数的时候直接返回
 */
fun Number.toFormat45Or0(wei: Int = 1, isJumpInt: Boolean = false): String {
    if (isJumpInt && (this is Int || this is Long)) {
        return this.toString()
    }
    val format = DecimalFormat("0" + if (wei == 0) "" else "." + MutableList(wei) { "0" }.joinToString("") { it }, DecimalFormatSymbols(Locale.CHINA))
    //舍弃规则，RoundingMode.HALF_EVEN表示直接舍弃。
    format.roundingMode = RoundingMode.HALF_UP
    return format.format(this)
}

/**
 * 保留小数后几位位，不用四舍五入
 * 2.0-> 2
 * 2.222->2.2
 * 2.292->2.2
 * 2->2
 * @param isJumpInt 整数的时候直接返回
 */
fun Number.toFormat(wei: Int = 1, isJumpInt: Boolean = false): String {
    if (isJumpInt && (this is Int || this is Long)) {
        return this.toString()
    }
    val format =
        DecimalFormat("#" + if (wei == 0) "" else "." + MutableList(wei) { "#" }.joinToString("") { it }, DecimalFormatSymbols(Locale.CHINA))
    //舍弃规则，RoundingMode.FLOOR表示直接舍弃。
    format.roundingMode = RoundingMode.FLOOR
    return format.format(this)
}
