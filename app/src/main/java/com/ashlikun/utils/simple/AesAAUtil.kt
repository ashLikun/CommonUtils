package com.ashlikun.utils.simple

import com.ashlikun.utils.encryption.Base64Utils.encodeToStr
import com.ashlikun.utils.other.LogUtils.e
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * @author shen_feng 加密解密
 */
object AesAAUtil {
    // static String sKey = "HOPERUN.COM";
    /**
     * 密钥如超过16位，截至16位，不足16位，补/000至16位
     *
     * @return 新密钥
     */
    fun secureBytes(key: String): String {
        var key = key
        if (key.length > 32) {
            key = key.substring(0, 32)
        } else if (key.length < 32) {
            for (i in key.length - 1..14) {
                key += "\u0000"
            }
        }
        return key
    }

    /**
     * AES解密 用于数据库储存
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    fun decryptCode(sSrc: String?, key: String): String? {
        var sKey = secureBytes(key)
        return try {
            // 判断Key是否正确
            if (sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null
            }
            // 判断Key是否为16位
            if (sKey.length != 32) {
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey)
            }
            val raw = sKey.toByteArray(charset("ASCII"))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            val encrypted1 = hex2byte(sSrc)
            try {
                val original = cipher.doFinal(encrypted1)
                String(original, Charset.forName("GBK"))
            } catch (e: Exception) {
                null
            }
        } catch (ex: Exception) {
            null
        }
    }

    /**
     * AES解密 用于数据库储存
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    fun decrypt(sSrc: String?, key: String): String? {
        var sKey = secureBytes(key)
        return try {
            // 判断Key是否正确
            if (sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null
            }
            // 判断Key是否为16位
            if (sKey.length != 32) {
                println("长度不是16")
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey)
            }
            val raw = sKey.toByteArray(charset("ASCII"))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            val encrypted1 = hex2byte(sSrc)
            try {
                val original = cipher.doFinal(encrypted1)
                // String originalString = new String(original, "GBK");
                String(original, Charset.forName("utf-8"))
            } catch (e: Exception) {
                null
            }
        } catch (ex: Exception) {
            null
        }
    }

    fun encrypt4Contacts(sSrc: String): String {
        return sSrc
    }

    /**
     * AES加密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    fun encrypt(sSrc: String, key: String): String? {

//        String sKey = secureBytes(key);
        return try {
//            if (sSrc == null || key == null) {
//                // LogUtil.d("AesUtil", "Key为空null");
//                return null;
//            }
//            // 判断Key是否为16位
//            if (sKey.length() != 16) {
//                // LogUtil.d("AesUtil", "Key长度不是16位");
//                sKey = secureBytes(sKey);
//            }
            val raw = key.toByteArray()
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
            val encrypted = cipher.doFinal(sSrc.toByteArray())
            e(encodeToStr(encrypted))
            byte2hex(encrypted).toLowerCase()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }

    /**
     * @param strhex
     * @return
     */
    fun hex2byte(strhex: String?): ByteArray? {
        if (strhex == null) {
            return null
        }
        val l = strhex.length
        if (l % 2 == 1) {
            return null
        }
        val b = ByteArray(l / 2)
        for (i in 0 until l / 2) {
            b[i] = strhex.substring(i * 2, i * 2 + 2).toInt(
                16
            ).toByte()
        }
        return b
    }

    /**
     * @param b
     * @return
     */
    fun byte2hex(b: ByteArray): String {
        var hs = ""
        var stmp = ""
        for (n in b.indices) {
            stmp = Integer.toHexString((b[n] and 0XFF.toByte()).toInt())
            hs = if (stmp.length == 1) {
                hs + "0" + stmp
            } else {
                hs + stmp
            }
        }
        return hs.toUpperCase()
    }
}