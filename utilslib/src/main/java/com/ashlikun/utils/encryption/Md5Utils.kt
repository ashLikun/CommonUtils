package com.ashlikun.utils.encryption

import android.text.TextUtils
import com.ashlikun.utils.other.hex.toHexStr
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 18:23
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Md5加密  不可逆算法
 */

object Md5Utils {

    /**
     * MD5加密(多次)
     */
    fun getMD5(string: String, times: Int): String {
        if (TextUtils.isEmpty(string)) return ""
        var md5 = string
        for (i in 0 until times) md5 = getMD5(md5)
        return md5
    }

    /**
     * MD5加密(加盐)
     *
     * @param string 加密字符串
     * @param slat   加密盐值key
     * @return 加密结果字符串
     */
    fun getMD5(string: String, slat: String = "", uppercase: Boolean = true): String {
        if (TextUtils.isEmpty(string)) return ""
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val result = md5.digest((string + slat).toByteArray()).toHexStr
            return if (uppercase) result.uppercase() else result
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * MD5加密(文件)
     * 可用于文件校验。
     *
     * @param file 加密文件
     * @return md5 数值
     */
    fun getMD5(file: File): String {
        if (!file.isFile) {
            return ""
        }
        var fis: FileInputStream? = null
        try {
            val digest = MessageDigest.getInstance("MD5")
            fis = FileInputStream(file)
            val buffer = ByteArray(1024)
            var len: Int
            while (fis.read(buffer, 0, 1024).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            val bigInt = BigInteger(1, digest.digest())
            return bigInt.toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        } finally {
            try {
                fis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
}