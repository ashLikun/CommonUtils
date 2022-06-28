package com.ashlikun.utils.other

import kotlin.experimental.and

/**
 * 作者　　: 李坤
 * 创建时间: 2022.4.6　17:19
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：16进制工具
 */
inline val String.hexToBytes
    get() = HexUtils.hexStr2Bytes(this)

/**
 * 可以是1，2，3，4字节
 */
inline fun Int.toByteArray(isReversed: Boolean = true) = HexUtils.intToBytes(this, isReversed)

/**
 * 0xffff 为4个字节和2个字节的分界线
 */
inline fun Int.toByteArray2Or4(isReversed: Boolean = true) = HexUtils.intToBytes2Or4(this, isReversed)

/**
 * ByteArray -> 16进制字符串
 */
inline val ByteArray.toHexStr
    get() = HexUtils.bytesToHexString(this)

/**
 * ByteArray -> Int
 * 翻转
 */
inline val ByteArray.byteToInt
    get() = HexUtils.byteToInt(this)

/**
 * ByteArray -> Int
 * 不翻转
 */
inline val ByteArray.byteToIntLE
    get() = HexUtils.byteToInt(this, false)

/**
 * 低位在左，高位在右
 */
inline val ByteArray.hexToLowHight
    get() = HexUtils.hexToLowHight(this)


object HexUtils {

    /**
     * 十六进制String转换成Byte[]
     * @param hexString the hex string
     * *
     * @return byte[]
     */
    fun hexStr2Bytes(str: String?): ByteArray {
        if (str.isNullOrEmpty()) {
            return ByteArray(0)
        }
        val byteArray = ByteArray(str.length / 2)
        for (i in byteArray.indices) {
            val subStr = str.substring(2 * i, 2 * i + 2)
            byteArray[i] = subStr.toInt(16).toByte()
        }
        return byteArray
    }

    /**
     * Convert char to byte
     */
    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }

    /**
     * 将字节数组转换为16进制字符串
     */
    fun bytesToHexString(src: ByteArray?): String {
        if (src == null || src.isEmpty()) {
            return ""
        }
        val b = StringBuilder()
        for (i in src.indices) {
            b.append(String.format("%02x", src[i] and 0xFF.toByte()))
        }
        return b.toString()
    }

    /**
     *  低位在左，高位在右
     */
    fun hexToLowHight(src: ByteArray?): ByteArray {
        if (src == null || src.isEmpty()) {
            return ByteArray(0)
        }
        return src.reversedArray()
    }

    /**
     * 将int转化成byte[]
     *
     * @param res 要转化的整数
     * @return 对应的byte[]
     */
    fun intToBytes(res: Int, isReversed: Boolean = true): ByteArray {
        val size = if (res.toUInt() > 0xFFFFFFU) 4
        else if (res.toUInt() > 0xFFFFU) 3
        else if (res.toUInt() > 0xFFU) 2
        else 1
        val targets = ByteArray(size)
        if (size >= 1) {
            targets[0] = (res and 0xff).toByte() // 最低位
        }
        if (size >= 2) {
            targets[1] = (res shr 8 and 0xff).toByte() // 次低位
        }
        if (size >= 3) {
            targets[2] = (res shr 16 and 0xff).toByte() // 次高位
        }
        if (size >= 4) {
            targets[3] = (res ushr 24).toByte() // 最高位,无符号右移。
        }
        if (!isReversed) {
            targets.reverse()
        }
        return targets
    }

    /**
     * int 转 ByteArray  4字节和2字节
     */
    fun intToBytes2Or4(res: Int, isReversed: Boolean = true): ByteArray {
        val targets = ByteArray(if (res.toUInt() > 0xFFFFU) 4 else 2)
        targets[0] = (res and 0xff).toByte() // 最低位
        targets[1] = (res shr 8 and 0xff).toByte() // 次低位
        if (targets.size > 2) {
            targets[2] = (res shr 16 and 0xff).toByte() // 次高位
            targets[3] = (res ushr 24).toByte() // 最高位,无符号右移。
        }
        if (!isReversed) {
            targets.reverse()
        }
        return targets
    }

    /**
     * 将byte[]转化成int
     *
     * @param res 要转化的byte[]
     * @return 对应的整数
     */
    fun byteToInt(res: ByteArray, reversed: Boolean = true): Int {
        //翻转一波，保证一样的输出
        val res = if (reversed) res.reversedArray() else res
        return ((res.getOrNull(0)?.toInt() ?: 0)) and 0xff or
                ((res.getOrNull(1)?.toInt() ?: 0) shl 8 and 0xff00) or
                ((res.getOrNull(2)?.toInt() ?: 0) shl 24 ushr 8) or
                ((res.getOrNull(3)?.toInt() ?: 0) shl 24)
    }


}