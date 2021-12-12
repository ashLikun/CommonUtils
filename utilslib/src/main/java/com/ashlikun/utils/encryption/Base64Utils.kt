package com.ashlikun.utils.encryption

import android.util.Base64
import com.ashlikun.utils.other.file.FileUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 18:21
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：CRLF：这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
 * DEFAULT：这个参数是默认，使用默认的方法来加密
 * NO_PADDING：这个参数是略去加密字符串最后的“=”
 * NO_WRAP：这个参数意思是略去所有的换行符（设置后CRLF就没用了）
 * URL_SAFE：这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
 */
object Base64Utils {
    /**
     * 加密
     */
    fun encode(data: ByteArray): ByteArray {
        return Base64.encode(data, Base64.NO_WRAP)
    }

    fun encode(data: String): ByteArray {
        return Base64.encode(data.toByteArray(), Base64.NO_WRAP)
    }

    fun encodeToStr(data: ByteArray): String {
        return String(encode(data), Charsets.UTF_8)
    }

    fun encodeToStr(data: String): String {
        return String(encode(data), Charsets.UTF_8)
    }

    fun getFileToBase64(path: String): String {
        return Base64.encodeToString(FileUtils.readByte(path), Base64.NO_WRAP)
    }

    /**
     * 解密
     */
    fun decode(str: String): ByteArray {
        return Base64.decode(str, Base64.NO_WRAP)
    }

    /**
     * 解密 转成字符串
     */
    fun decodeToStr(str: String): String {
        return String(decode(str), Charsets.UTF_8)
    }

    /**
     * 解密重载
     */
    fun decode(data: ByteArray): ByteArray {
        return Base64.decode(data, Base64.NO_WRAP)
    }
}