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
 * crc8
 */
inline val ByteArray.CRC8
    get() = CrcUtils.CRC8.CRC8(this, 0, this.size)
inline val ByteArray.CRC8_DARC
    get() = CrcUtils.CRC8.CRC8_DARC(this, 0, this.size)
inline val ByteArray.CRC8_ITU
    get() = CrcUtils.CRC8.CRC8_ITU(this, 0, this.size)
inline val ByteArray.CRC8_MAXIM
    get() = CrcUtils.CRC8.CRC8_MAXIM(this, 0, this.size)
inline val ByteArray.CRC8_ROHC
    get() = CrcUtils.CRC8.CRC8_ROHC(this, 0, this.size)

/**
 * crc16
 */
inline val ByteArray.CRC16_CCITT_FALSE
    get() = CrcUtils.CRC16.CRC16_CCITT_FALSE(this, 0)
inline val ByteArray.CRC16_CCITT
    get() = CrcUtils.CRC16.CRC16_CCITT(this, 0, this.size)
inline val ByteArray.CRC16_DECT_R
    get() = CrcUtils.CRC16.CRC16_DECT_R(this, 0, this.size)
inline val ByteArray.CRC16_DECT_X
    get() = CrcUtils.CRC16.CRC16_DECT_X(this, 0, this.size)
inline val ByteArray.CRC16_DNP
    get() = CrcUtils.CRC16.CRC16_DNP(this, 0, this.size)
inline val ByteArray.CRC16_GENIBUS
    get() = CrcUtils.CRC16.CRC16_GENIBUS(this, 0, this.size)
inline val ByteArray.CRC16_IBM
    get() = CrcUtils.CRC16.CRC16_IBM(this, 0, this.size)
inline val ByteArray.CRC16_MAXIM
    get() = CrcUtils.CRC16.CRC16_MAXIM(this, 0, this.size)
inline val ByteArray.CRC16_MODBUS
    get() = CrcUtils.CRC16.CRC16_MODBUS(this, 0, this.size)
inline val ByteArray.CRC16_USB
    get() = CrcUtils.CRC16.CRC16_USB(this, 0, this.size)
inline val ByteArray.CRC16_X25
    get() = CrcUtils.CRC16.CRC16_X25(this, 0, this.size)
inline val ByteArray.CRC16_XMODEM
    get() = CrcUtils.CRC16.CRC16_XMODEM(this, 0, this.size)

/**
 * crc32
 */
inline val ByteArray.CRC32
    get() = CrcUtils.CRC32.CRC32(this, 0, this.size)
inline val ByteArray.CRC32_B
    get() = CrcUtils.CRC32.CRC32_B(this, 0, this.size)

inline val ByteArray.CRC32_C
    get() = CrcUtils.CRC32.CRC32_C(this, 0, this.size)

inline val ByteArray.CRC32_D
    get() = CrcUtils.CRC32.CRC32_D(this, 0, this.size)

inline val ByteArray.CRC32_POSIX
    get() = CrcUtils.CRC32.CRC32_POSIX(this, 0, this.size)

inline val ByteArray.CRC32_MPEG_2
    get() = CrcUtils.CRC32.CRC32_MPEG_2(this, 0, this.size)

/**
 * 字符串转换成字节数组
 */
inline val String.hexToBytes
    get() = HexUtils.hexStr2Bytes(this)

/**
 * @param size 可以是1，2，3，4字节,-1:自动
 */
inline fun Int.toByteArray(size: Int = -1, isReversed: Boolean = true) = HexUtils.intToBytes(this, size, isReversed)
inline fun UInt.toByteArray(size: Int = -1, isReversed: Boolean = true) = HexUtils.intToBytes(this.toInt(), size, isReversed)
inline fun Long.toByteArray(size: Int = -1, isReversed: Boolean = true) = HexUtils.intToBytes(this.toInt(), size, isReversed)

/**
 * 0xffff 为4个字节和2个字节的分界线
 */
inline fun Int.toByteArray2Or4(isReversed: Boolean = true) = HexUtils.intToBytes2Or4(this, isReversed)
inline fun UInt.toByteArray2Or4(isReversed: Boolean = true) = HexUtils.intToBytes2Or4(this.toInt(), isReversed)
inline fun Long.toByteArray4Or8(isReversed: Boolean = true) = HexUtils.longToBytes4Or8(this, isReversed)

/**
 * ByteArray -> 16进制字符串
 */
inline val ByteArray.toHexStr
    get() = HexUtils.bytesToHexString(this)

/**
 * ByteArray -> 16进制字符串  空格拼接
 */
inline val ByteArray.toHexStrSpace
    get() = HexUtils.bytesToHexString(this, " ")
inline val Int.toHexStr
    get() = toByteArray2Or4(false).toHexStr
inline val UInt.toHexStr
    get() = toByteArray2Or4(false).toHexStr
inline val Long.toHexStr
    get() = toUInt().toByteArray2Or4(false).toHexStr
inline val Long.toHexStr4Or8
    get() = toByteArray4Or8(false).toHexStr

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
 * ByteArray -> Uint
 * 不翻转
 */
inline val ByteArray.byteToUIntLE
    get() = HexUtils.byteToUInt(this, false)

/**
 * ByteArray -> Uint
 * 不翻转
 */
inline val ByteArray.byteToUIntBE
    get() = HexUtils.byteToUInt(this, true)

/**
 * ByteArray -> ASCII字符串
 *
 */
inline val ByteArray.byteToASCIIStr
    get() = HexUtils.byteToASCIIStr(this, false, false)

/**
 * 删除00和后续
 */
inline val ByteArray.byteToASCIIStrSub00
    get() = HexUtils.byteToASCIIStr(this, false, true)

/**
 * 删除控制字符和 00和后续
 */
inline val ByteArray.byteToASCIIStrDelCtrlOr00
    get() = HexUtils.byteToASCIIStr(this, false, true, true)

/**
 * 移除控制字符
 */
inline val ByteArray.byteToASCIIStrDelCtrl
    get() = HexUtils.byteToASCIIStr(this, false, false, true)

object HexUtils {

    /**
     * 十六进制String转换成Byte[]
     */
    fun hexStr2Bytes(str: String?): ByteArray {
        if (str.isNullOrEmpty()) {
            return ByteArray(0)
        }
        //替换空格
        val str = str.replace(" ", "")
        val byteArray = ByteArray(str.length / 2)
        for (i in byteArray.indices) {
            val subStr = str.substring(2 * i, 2 * i + 2)
            byteArray[i] = subStr.toInt(16).toByte()
        }
        return byteArray
    }

    /**
     * 16进制Char 转成 Byte  0-15
     */
    private fun charToByte(c: Char): Byte {
        return "0123456789ABCDEF".indexOf(c).toByte()
    }

    /**
     * 将字节数组转换为16进制字符串
     * @param joint 两位拼接的字符串，一般是空格
     */
    fun bytesToHexString(src: ByteArray?, joint: String? = null): String {
        if (src == null || src.isEmpty()) {
            return ""
        }
        val b = StringBuilder()
        for (i in src.indices) {
            b.append(String.format("%02X", src[i] and 0xFF.toByte()))
            if (joint != null) {
                b.append(joint)
            }
        }
        return b.toString()
    }

    /**
     * 将int转化成byte[]
     *
     * @param res 要转化的整数
     * @return 对应的byte[]
     */
    fun intToBytes(res: Int, size: Int = -1, isReversed: Boolean = true): ByteArray {
        var size = size
        if (size <= 0 || size > 4) {
            size = if (res.toUInt() > 0xFFFFFFU) 4
            else if (res.toUInt() > 0xFFFFU) 3
            else if (res.toUInt() > 0xFFU) 2
            else 1
        } else size
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
            targets[3] = (res ushr 24 and 0xff).toByte() // 最高位,无符号右移。
        }
        if (!isReversed) {
            targets.reverse()
        }
        return targets
    }

    /**
     * long 转 ByteArray  8字节和4字节
     */
    fun longToBytes4Or8(res: Long, isReversed: Boolean = true): ByteArray {
        val targets = ByteArray(if (res.toUInt() > 0xFFFFFFFFU) 8 else 4)
        targets[0] = (res and 0xff).toByte() // 最低位
        targets[1] = (res shr 8 and 0xff).toByte() // 次低位
        targets[2] = (res shr 16 and 0xff).toByte() // 次高位
        targets[3] = (res shr 24 and 0xff).toByte() // 最高位
        if (targets.size > 4) {
            targets[4] = (res shr 32 and 0xff).toByte() // 次高位
            targets[5] = (res shr 40 and 0xff).toByte() // 最高位
            targets[6] = (res shr 48 and 0xff).toByte() // 最高位
            targets[7] = (res ushr 56 and 0xff).toByte() // 最高位
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
     * long 好计算
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
     * @param isRemoveControl 是否去除控制符
     * @return 对应的字符串
     */
    fun byteToASCIIStr(res: ByteArray, reversed: Boolean = true, isSub00: Boolean = false, isRemoveControl: Boolean = false): String {
        var res = res
        //翻转一波，保证一样的输出
        res = if (reversed) res.reversedArray() else res
        var result = ""
        for (it in res) {
            if (isSub00 && it.toInt() == 0) break
            if (isRemoveControl && it < 32) continue
            result += it.toInt().toChar()
        }
        return result
    }

}