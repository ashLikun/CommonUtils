/* 
 * @(#)SecurityUtils.java    Created on 2004-10-9
 * Copyright (c) 2004 ZDSoft Networks, Inc. All rights reserved.
 * $Id: SecurityUtils.java 33450 2012-12-13 08:34:03Z xuan $
 */
package com.ashlikun.utils.encryption

import android.annotation.SuppressLint
import androidx.annotation.IntDef
import com.ashlikun.utils.other.StringUtils
import java.io.UnsupportedEncodingException
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import javax.crypto.*
import javax.crypto.spec.DESKeySpec

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 18:23
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍： DES加密算法, 对称性得
 */

object DesUtils {
    /**
     * Aes加密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    fun encrypt(content: String, password: String): String {
        return des(content, password, Cipher.ENCRYPT_MODE)
    }

    /**
     * Aes解密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    fun decrypt(content: String, password: String): String {
        return des(content, password, Cipher.DECRYPT_MODE)
    }

    /**
     * Des加密/解密
     *
     * @param content  字符串内容
     * @param password 密钥
     * @param type     加密：[Cipher.ENCRYPT_MODE]，解密：[Cipher.DECRYPT_MODE]
     * @return 加密/解密结果
     */
    fun des(content: String, password: String, @DESType type: Int): String {
        try {
            val random = SecureRandom()
            val desKey = DESKeySpec(password.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance("DES")
            @SuppressLint("GetInstance") val cipher = Cipher.getInstance("DES")
            cipher.init(type, keyFactory.generateSecret(desKey), random)
            return if (type == Cipher.ENCRYPT_MODE) {
                val byteContent = content.toByteArray(charset("utf-8"))
                StringUtils.parseByte2HexStr(cipher.doFinal(byteContent))
            } else {
                val byteContent = StringUtils.parseHexStr2Byte(content)
                String(cipher.doFinal(byteContent))
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }
        return ""
    }

    @IntDef(Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class DESType
}