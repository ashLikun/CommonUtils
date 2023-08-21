package com.ashlikun.utils.other

import kotlin.math.min

/**
 * 作者　　: 李坤
 * 创建时间: 2022.4.7　17:57
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
/**
 * 某一位转成bool
 */
inline fun Byte.toBit(offset: Int = 0): Boolean {
    return (toInt() shr offset) and 1 == 1
}

inline fun Int.toBit(offset: Int = 0): Boolean {
    return (this shr offset) and 1 == 1
}

/**
 * 读取Byte 某些位
 * @param start 0-7
 * @param end 0-7
 * 0-3：0000 1111
 * 2-3：0000 1100
 * 6-7：1100 0000
 */
inline fun Byte.readByteRange(start: Int = 0, end: Int = 0): Int {
    val start = min(start, end)
    val model = ((0xff shl (7 - (end - start))).toUByte().toInt() shr (7 - end)).toUByte().toInt()
    return (this.toUByte().toInt() and model) shr start
}

/**
 * 写入某一位值
 * 1111 1101
 */
inline fun Byte.writeBool(value: Boolean, offset: Int = 0): Byte {
    return if (value) {
        (this.toInt() or (1 shl offset)).toByte()
    } else {
        (this.toInt() and ((1 shl offset) xor 0xFF)).toByte()
    }
//    return this and (1 shl offset) == (1 shl offset)
}


/**
 * 写入低4位
 * 0000 1101
 */
inline fun Byte.writeLow4(value: Int): Byte {
    return ((this.toInt() and 0xf0) + (value and 0x0f)).toByte()
}

/**
 * 写入高4位
 * 1101 0000
 */
inline fun Byte.writeHigh4(value: Byte): Byte {
    val gao = (value.toInt() shl 4) and 0xf0
    return (gao + (this.toInt() and 0x0f)).toByte()
}

