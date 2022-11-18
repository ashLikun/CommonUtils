package com.ashlikun.utils.other

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

fun String.reversalEvery2Charts(hasSpace: Boolean = false): String {
    val hex = this.addSpaceEvery2Charts()
    return hex.split(" ").reversed().joinToString(if (hasSpace) " " else "")
}

fun String.addSpaceEvery2Charts(): String {
    val hex = this.replace(" ", "")
    val sb = StringBuilder()
    for (i in 0 until hex.length / 2) {
        sb.append(hex.substring(i * 2, i * 2 + 2))
        sb.append(" ")
    }
    return sb.toString().trim()
}
/**
 * ByteArray->String
 */
fun ByteArray.toHexString(hasSpace: Boolean = true) = this.joinToString("") {
    (it.toInt() and 0xFF).toString(16).padStart(2, '0').toUpperCase() + if (hasSpace) " " else ""
}

/**
 * ByteArray -> AsciiStr
 */
fun ByteArray.toAsciiString(hasSpace: Boolean = false) =
    this.map { it.toInt().toChar() }.joinToString(if (hasSpace) " " else "")


/**
 * 读取Byte8位,可能为负数
 */
fun ByteArray.readByte8(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 1)) return null
    return this[offset].toInt()
}

/**
 * 读取Byte8位,无符号
 */
fun ByteArray.readUByte8(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 1)) return null
    return this[offset].toUByte().toInt()
}

/**
 * 读取Byte 4位
 */
fun ByteArray.readByte4(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 1)) return null
    return (this[offset].toInt() and 0x0F)
}

/**
 * 读取Byte8位,无符号
 */
fun ByteArray.readUByte4(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 1)) return null
    return (this[offset].toUInt().toInt() and 0x0F)
}

/**
 * 读取Byte 某些位
 */
fun ByteArray.readByteRange(offset: Int = 0, start: Int = 0, end: Int = 0): Int? {
    if (throwOffestError(this, offset, 1)) return null
    return this[offset].readByteRange(start, end)
}

/**
 * 读取8位
 */
fun ByteArray.readInt8(offset: Int = 0): Int? {
    return readByte8(offset)
}

/**
 * 读取int 4位
 */
fun ByteArray.readInt4(offset: Int = 0): Int? {
    return readByte4(offset)
}


fun ByteArray.readUInt8(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 1)) return null
    return this[offset].toInt() and 0xFF
}

fun ByteArray.readInt16BE(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 2)) return null
    return (this[offset].toInt() shl 8) + (this[offset + 1].toInt() and 0xFF)
}

fun ByteArray.readUInt16BE(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 2)) return null
    // 方式一
    return ((this[offset].toInt() and 0xFF) shl 8) or (this[offset + 1].toInt() and 0xFF)
    // 方式二
//    return readUnsigned(this, 2, offset, false).toInt()
}


fun ByteArray.readInt16LE(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 2)) return null
    return (this[offset + 1].toInt() shl 8) + (this[offset].toInt() and 0xFF)
}

fun ByteArray.readUInt16LE(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 2)) return null
    return ((this[offset + 1].toInt() and 0xFF) shl 8) or (this[offset].toInt() and 0xFF)
//    return readUnsigned(this, 2, offset, true).toInt()
}

fun ByteArray.readInt32BE(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 4)) return null
    return (this[offset].toInt()) shl 24 or
            ((this[offset + 1].toInt() and 0xFF) shl 16) or
            ((this[offset + 2].toInt() and 0xFF) shl 8) or
            (this[offset + 3].toInt() and 0xFF)
//    return (this[offset].toInt() shl 24) + (this[offset + 1].toUByte().toInt() shl 16) + (this[offset + 2].toUByte().toInt() shl 8) + this[offset + 3].toUByte().toInt()
}

fun ByteArray.readUInt32BE(offset: Int = 0): Long? {
    if (throwOffestError(this, offset, 4)) return null
    return (((this[offset].toInt() and 0xFF).toLong() shl 24) or
            ((this[offset + 1].toInt() and 0xFF).toLong() shl 16) or
            ((this[offset + 2].toInt() and 0xFF).toLong() shl 8) or
            (this[offset + 3].toInt() and 0xFF).toLong())
//    return readUnsigned(this, 4, offset, false)
}

fun ByteArray.readInt32LE(offset: Int = 0): Int? {
    if (throwOffestError(this, offset, 4)) return null
    return (this[offset + 3].toInt()) shl 24 or
            ((this[offset + 2].toInt() and 0xFF) shl 16) or
            ((this[offset + 1].toInt() and 0xFF) shl 8) or
            (this[offset].toInt() and 0xFF)
//    return (this[offset + 3].toInt() shl 24) + (this[offset + 2].toUByte().toInt() shl 16) + (this[offset + 1].toUByte().toInt() shl 8) + this[offset].toUByte().toInt()
}

fun ByteArray.readUInt32LE(offset: Int = 0): Long? {
    if (throwOffestError(this, offset, 4)) return null
    return (((this[offset + 3].toInt() and 0xFF).toLong() shl 24) or
            ((this[offset + 2].toInt() and 0xFF).toLong() shl 16) or
            ((this[offset + 1].toInt() and 0xFF).toLong() shl 8) or
            (this[offset].toInt() and 0xFF).toLong())
//    return readUnsigned(this, 4, offset, true)
}

// 四字节 float
fun ByteArray.readFloatBE(offset: Int = 0) = this.readInt32BE(offset)?.let { java.lang.Float.intBitsToFloat(it) }

// 四字节 float
fun ByteArray.readFloatLE(offset: Int = 0) = this.readInt32LE(offset)?.let { java.lang.Float.intBitsToFloat(it) }

//
//// 四字节 时间
@SuppressLint("SimpleDateFormat")
fun ByteArray.readTimeBE(offset: Int = 0, pattern: String = "yyyy-MM-dd HH:mm:ss") =
    SimpleDateFormat(pattern).format((this.readUInt32BE(offset) ?: 0) * 1000)

@SuppressLint("SimpleDateFormat")
fun ByteArray.readTimeLE(offset: Int = 0, pattern: String = "yyyy-MM-dd HH:mm:ss") =
    SimpleDateFormat(pattern).format((this.readUInt32LE(offset) ?: 0) * 1000)

// 读取ByteArray
fun ByteArray.readByteArrayBE(offset: Int, byteLength: Int): ByteArray {
    if (throwOffestError(this, offset)) return ByteArray(0)
    return this.copyOfRange(offset, if ((offset + byteLength) > this.size) this.size else offset + byteLength)
}

fun ByteArray.readByteArrayLE(offset: Int, byteLength: Int): ByteArray {
    if (throwOffestError(this, offset)) return ByteArray(0)
    return this.readByteArrayBE(offset, byteLength).reversedArray()
}

// 读取指定范围输出字符串
fun ByteArray.readStringBE(
    offset: Int, byteLength: Int, encoding: String = "hex", hasSpace: Boolean = encoding.toLowerCase() == "hex"
): String {
    return when (encoding.toLowerCase()) {
        "hex" -> this.readByteArrayBE(offset, byteLength).toHexString(hasSpace)
        "ascii" -> this.readByteArrayBE(offset, byteLength).map { it.toChar() }.joinToString(if (hasSpace) " " else "")
        else -> ""
    }
}

fun ByteArray.readStringLE(
    offset: Int,
    byteLength: Int,
    encoding: String = "hex",
    hasSpace: Boolean = encoding.toLowerCase() == "hex"
): String {
    return when (encoding.toLowerCase()) {
        "hex" -> this.readByteArrayLE(offset, byteLength).toHexString(hasSpace)
        "ascii" -> this.readByteArrayLE(offset, byteLength).map { it.toChar() }.joinToString(if (hasSpace) " " else "")
        else -> ""
    }
}

// ********************************************** 写 **********************************************
/**
 * 写Byte 低 4位
 * @param bitOffset 0-7,位的偏移量
 */
fun ByteArray.writeBit(value: Boolean, offset: Int = 0, bitOffset: Int = 0): Boolean {
    if (throwOffestError(this, offset)) return false
    this[offset] = this[offset].writeBool(value, bitOffset)
    return true
}

/**
 * 写Byte 低 4位
 */
fun ByteArray.writeByte4(value: Int, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset)) return false
    this[offset] = this[offset].writeLow4(value)
    return true
}

/**
 * 写Byte 高 4位
 */
fun ByteArray.writeByteHigh4(value: Byte, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset)) return false
    this[offset] = this[offset].writeHigh4(value)
    return true
}

fun ByteArray.writeInt8(value: Int, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset)) return false
    // 无符号Int 执行写入
    this[offset] = value.toByte()
    return true
}

fun ByteArray.writeInt16BE(value: Int, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset, 2)) return false
    // 执行写入
    this[offset] = (value and 0xff00 ushr 8).toByte()
    this[offset + 1] = (value and 0xff).toByte()
    return true
}

fun ByteArray.writeInt16LE(value: Int, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset, 2)) return false
    // 无符号Int 执行写入
    this[offset] = (value and 0xff).toByte()
    this[offset + 1] = (value and 0xff00 ushr 8).toByte()
    return true
}

fun ByteArray.writeInt32BE(value: Long, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset, 4)) return false
    // 无符号Int 执行写入
    this[offset + 3] = (value and 0xff).toByte()
    this[offset + 2] = (value and 0xff00 ushr 8).toByte()
    this[offset + 1] = (value and 0xff0000 ushr 16).toByte()
    this[offset] = (value and 0xff000000 ushr 24).toByte()
    return true
}

fun ByteArray.writeInt32LE(value: Long, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset, 4)) return false
    // 无符号Int 执行写入
    this[offset] = (value and 0xff).toByte()
    this[offset + 1] = (value and 0xff00 ushr 8).toByte()
    this[offset + 2] = (value and 0xff0000 ushr 16).toByte()
    this[offset + 3] = (value and 0xff000000 ushr 24).toByte()
    return true
}

// 写入Float类型
fun ByteArray.writeFloatBE(value: Float, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset, 4)) return false
    this.writeInt32BE(java.lang.Float.floatToIntBits(value).toLong(), offset)
    return true
}

fun ByteArray.writeFloatLE(value: Float, offset: Int = 0): Boolean {
    if (throwOffestError(this, offset, 4)) return false
    this.writeInt32LE(java.lang.Float.floatToIntBits(value).toLong(), offset)
    return true
}

// 写入时间
fun ByteArray.writeTimeBE(time: String, offset: Int = 0, pattern: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
    return this.writeInt32BE(SimpleDateFormat(pattern).parse(time).time / 1000, offset)
}

fun ByteArray.writeTimeLE(time: String, offset: Int = 0, pattern: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
    return this.writeInt32LE(SimpleDateFormat(pattern).parse(time).time / 1000, offset)
}

// 指定位置写入ByteArray
fun ByteArray.writeByteArrayBE(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size): Boolean {
    return this.writeStringBE(byteArray.toHexString(), offset, length)
}

fun ByteArray.writeByteArrayLE(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size): Boolean {
    return this.writeStringLE(byteArray.toHexString(), offset, length)
}

// 指定位置插入ByteArray
fun ByteArray.insertByteArrayBE(
    insertArray: ByteArray, origrinalIndex: Int = 0, insertArrayOffset: Int = 0, insertArrayLength: Int = insertArray.size - insertArrayOffset
): ByteArray {
    val byteArrayPre = this.copyOfRange(0, origrinalIndex)
    val byteArrayLast = this.copyOfRange(origrinalIndex, this.size)
    val insertFinalArray = insertArray.copyOfRange(insertArrayOffset, insertArrayOffset + insertArrayLength)
    return byteArrayPre.plus(insertFinalArray).plus(byteArrayLast)
}

fun ByteArray.insertByteArrayLE(
    insertArray: ByteArray,
    origrinalIndex: Int = 0,
    insertArrayOffset: Int = 0,
    insertArrayLength: Int = insertArray.size - insertArrayOffset
): ByteArray {
    insertArray.reverse()
    val byteArrayPre = this.copyOfRange(0, origrinalIndex)
    val byteArrayLast = this.copyOfRange(origrinalIndex, this.size)
    val insertFinalArray = insertArray.copyOfRange(insertArrayOffset, insertArrayOffset + insertArrayLength)
    return byteArrayPre.plus(insertFinalArray).plus(byteArrayLast)
}

// 指定位置写入String
/**
 * @str 写入的字符串
 * @encoding  Hex & ASCII
 */
fun ByteArray.writeStringBE(str: String, offset: Int = 0, encoding: String = "hex"): Boolean {
    if (throwOffestError(this, offset)) return false
    when (encoding.toLowerCase()) {
        "hex" -> {
            val hex = str.replace(" ", "")
            for (i in 0 until hex.length / 2) {
                if (i + offset < this.size) {
                    this[i + offset] = hex.substring(i * 2, i * 2 + 2).toInt(16).toByte()
                }
            }
        }
        "ascii" -> {
            val hex = str.toCharArray().map { it.toInt() }.map { it.toString(16) }.joinToString("")
            this.writeStringBE(hex, offset, "hex")
        }
    }
    return true
}

// length: 写入长度
fun ByteArray.writeStringLE(str: String, offset: Int = 0, encoding: String = "hex"): Boolean {
    when (encoding.toLowerCase()) {
        "hex" -> {
            val hex = str.reversalEvery2Charts()
            this.writeStringBE(hex, offset, encoding)
        }
        "ascii" -> {
            val hex = str.toCharArray().map { it.toInt() }.map { it.toString(16) }.joinToString("")
            this.writeStringLE(hex, offset, "hex")
        }
    }
    return true
}

fun ByteArray.writeStringBE(str: String, offset: Int, length: Int, encoding: String = "hex"): Boolean {
    if (throwOffestError(this, offset, length)) return false
    when (encoding.toLowerCase()) {
        "hex" -> {
            val hex = str.replace(" ", "").padStart(length * 2, '0').substring(0, length * 2)
            this.writeStringBE(hex, offset)
        }
        "ascii" -> {
            val hex = str.toCharArray().map { it.toInt() }.map { it.toString(16) }.joinToString("")
            this.writeStringBE(hex, offset, length, "hex")
        }
    }
    return true
}

fun ByteArray.writeStringLE(str: String, offset: Int, length: Int, encoding: String = "hex"): Boolean {
    when (encoding.toLowerCase()) {
        "hex" -> {
            val hex = str.reversalEvery2Charts().padEnd(length * 2, '0').substring(0, length * 2)
            this.writeStringBE(hex, offset, length, encoding)
        }
        "ascii" -> {
            val hex = str.toCharArray().map { it.toInt() }.map { it.toString(16) }.joinToString("")
            this.writeStringLE(hex, offset, length, "hex")
        }
    }
    return true
}

// 无符号int
private fun readUnsigned(byteArray: ByteArray, len: Int, offset: Int, littleEndian: Boolean): Long {
    var value = 0L
    for (count in 0 until len) {
        val shift = (if (littleEndian) count else (len - 1 - count)) shl 3
        value = value or (0xff.toLong() shl shift and (byteArray[offset + count].toLong() shl shift))
    }
    return value
}


private fun throwOffestError(byteArray: ByteArray, offset: Int, length: Int = 1, byteLength: Int = 0) = offset > byteArray.size - length - byteLength