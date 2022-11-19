package com.ashlikun.utils.other.hex

import com.ashlikun.utils.other.hex.CrcUtils
import kotlin.experimental.and

/**
 * 作者　　: 李坤
 * 创建时间: 2022.4.6　17:19
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：16进制工具
 */
/**
 * crc16
 */
inline val ByteArray.CRC16_CCITT_FALSE
    get() = CrcUtils.CRC16.CRC16_CCITT_FALSE(this, 0)

/**
 * crc32
 */
inline val ByteArray.CRC32
    get() = CrcUtils.CRC32.CRC32(this, 0, this.size)

/**
 * crc32
 */
inline val ByteArray.CRC32_B
    get() = CrcUtils.CRC32.CRC32_B(this, 0, this.size)

/**
 * crc32
 */
inline val ByteArray.CRC32_C
    get() = CrcUtils.CRC32.CRC32_C(this, 0, this.size)

/**
 * crc32
 */
inline val ByteArray.CRC32_D
    get() = CrcUtils.CRC32.CRC32_D(this, 0, this.size)

/**
 * crc32
 */
inline val ByteArray.CRC32_POSIX
    get() = CrcUtils.CRC32.CRC32_POSIX(this, 0, this.size)

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
inline val Int.toHexStr
    get() = toByteArray2Or4(false).toHexStr

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

/**
 * ByteArray -> Long
 * 不翻转
 */
inline val ByteArray.byteToUIntLE
    get() = HexUtils.byteToUInt(this, false)

/**
 * ByteArray -> ASCII字符串
 *
 */
inline val ByteArray.byteToASCIIStr
    get() = HexUtils.byteToASCIIStr(this, false, false)

inline val ByteArray.byteToASCIIStrSub00
    get() = HexUtils.byteToASCIIStr(this, false, true)

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
            b.append(String.format("%02X", src[i] and 0xFF.toByte()))
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

    /**
     * 将byte[]转化成uint
     *
     * @param res 要转化的byte[]
     * @return 对应的整数
     */
    fun byteToUInt(res: ByteArray, reversed: Boolean = true): Long {
        //翻转一波，保证一样的输出
        val res = if (reversed) res.reversedArray() else res
        return (((res.getOrElse(0) { 0 }).toInt()) and 0xff).toLong() or
                (((res.getOrElse(1) { 0 }).toInt() and 0xff).toLong() shl 8) or
                (((res.getOrElse(2) { 0 }).toInt() and 0xff).toLong() shl 16) or
                (((res.getOrElse(3) { 0 }).toInt() and 0xff).toLong() shl 24)
    }

    /**
     * 将byte[]转化成ASCII字符串
     *
     * @param res 要转化的byte[]
     * @param isSub00 是否遇到00就去除之后的byte
     * @return 对应的字符串
     */
    fun byteToASCIIStr(res: ByteArray, reversed: Boolean = true, isSub00: Boolean = false): String {
        //翻转一波，保证一样的输出
        val res = if (reversed) res.reversedArray() else res
        var is00 = false
        return res.map {
            if (isSub00 && !is00) is00 = it.toInt() == 0
            if (is00) null else it.toInt().toChar()
        }.filterNotNull().joinToString("")
    }
}