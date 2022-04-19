package com.ashlikun.utils.encryption

import android.text.TextUtils
import androidx.annotation.StringDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 22:46
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：sha加密
 * 单向加密
 */

object SHAUtil {
    const val SHA224 = "sha-224"
    const val SHA256 = "sha-256"
    const val SHA384 = "sha-384"
    const val SHA512 = "sha-512"
    fun getSha(string: String): String {
        return getSha(string, SHA256)
    }

    /**
     * Sha加密
     *
     * @param string 加密字符串
     * @param type   加密类型 ：[.SHA224]，[.SHA256]，[.SHA384]，[.SHA512]
     * @return SHA加密结果字符串
     */
    fun getSha(string: String, @SHAType type: String): String {
        var type = type
        if (TextUtils.isEmpty(string)) return ""
        if (TextUtils.isEmpty(type)) type = SHA256
        try {
            val md5 = MessageDigest.getInstance(type)
            val bytes = md5.digest(string.toByteArray())
            var result = ""
            for (b in bytes) {
                result += String.format("%02x", b and 0xFF.toByte())
            }
            return result
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}

@StringDef(SHAUtil.SHA224, SHAUtil.SHA256, SHAUtil.SHA384, SHAUtil.SHA512)
@Retention(RetentionPolicy.SOURCE)
internal annotation class SHAType