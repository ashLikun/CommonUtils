package com.ashlikun.utils.encryption

import com.ashlikun.utils.other.StringUtils
import java.util.regex.Matcher
import com.ashlikun.utils.encryption.UnicodeUtils
import java.lang.StringBuffer
import java.lang.Exception
import java.util.regex.Pattern

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 22:49
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Unicode编码
 */

object UnicodeUtils {
    val reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{2,4})")

    /**
     * unicode 转字符串
     */
    fun unicode2String(unicode: String): String? {
        if (unicode.isEmpty()) {
            return null
        }
        val m = reUnicode.matcher(unicode)
        val sb = StringBuffer(unicode.length)
        while (m.find()) {
            try {
                m.appendReplacement(
                    sb,
                    Character.toString(m.group(1).toInt(16).toChar())
                )
            } catch (e: Exception) {
            }
        }
        m.appendTail(sb)
        return sb.toString()
    }

    /**
     * 字符串转换unicode
     */
    fun string2Unicode(string: String): String {
        val unicode = StringBuffer()
        var hex: String
        for (i in 0 until string.length) {
            // 取出每一个字符
            val c = string[i]
            // 转换为unicode
            hex = Integer.toHexString(c.code)
            if (hex.length == 4) {
                unicode.append("\\u")
            } else if (hex.length == 2) {
                unicode.append("\\u00")
            } else if (hex.length == 3) {
                unicode.append("\\u0")
            } else if (hex.length == 1) {
                unicode.append("\\u0")
            } else {
                unicode.append("\\u")
            }
            unicode.append(hex)
        }
        return unicode.toString()
    }
}