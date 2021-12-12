package com.ashlikun.utils.encryption

import androidx.annotation.IntDef
import java.io.UnsupportedEncodingException
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * 作者　　: 李坤
 * 创建时间: 2016/6/23 17:38
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：AES,加密
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 * 对称加密
 */
@IntDef(Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE)
@Retention(RetentionPolicy.SOURCE)
internal annotation class AESType
object AESUtils {
    const val AES = "AES" //AES 加密

    /**
     * 密钥长度
     */
    const val KEY_LENGTH = 16
    const val DEFAULT_TRANSFORMATION = "AES/ECB/PKCS7Padding"
    const val TRANSFORMATION_AES_CBC_PKCS = "AES/CBC/PKCS7Padding"

    /**
     * 默认填充位数
     */
    const val DEFAULT_VALUE = "0"

    /**
     * 对密钥进行处理
     */
    @Throws(Exception::class)
    fun getRawKey(seed: ByteArray?, keySizeInBytes: Int): ByteArray {
        return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(seed, keySizeInBytes)
    }

    /**
     * Aes加密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    fun encrypt(content: String, password: String): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 1, -1)
    }

    fun encrypt(content: String, password: String, transformation: String?): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, transformation, 1, -1)
    }

    fun encrypt(content: String, password: String, length: Int): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 1, length)
    }

    fun encryptHex(content: String, password: String): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 2, -1)
    }

    fun encryptHex(content: String, password: String, transformation: String?): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, transformation, 2, -1)
    }

    fun encryptHex(content: String, password: String, length: Int): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 2, length)
    }

    fun encryptStr(content: String, password: String): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 3, -1)
    }

    fun encryptStr(content: String, password: String, transformation: String?): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, transformation, 3, -1)
    }

    fun encryptStr(content: String, password: String, length: Int): String? {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 3, length)
    }

    /**
     * Aes解密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    fun decrypt(content: String, password: String): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 1, -1)
    }

    fun decrypt(content: String, password: String, transformation: String?): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, transformation, 1, -1)
    }

    fun decrypt(content: String, password: String, length: Int): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 1, length)
    }

    fun decryptHex(content: String, password: String): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 2, -1)
    }

    fun decryptHex(content: String, password: String, transformation: String?): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, transformation, 2, -1)
    }

    fun decryptHex(content: String, password: String, length: Int): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 2, length)
    }

    fun decryptStr(content: String, password: String): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 3, -1)
    }

    fun decryptStr(content: String, password: String, transformation: String?): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, transformation, 3, -1)
    }

    fun decryptStr(content: String, password: String, length: Int): String? {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 3, length)
    }

    /**
     * Aes加密/解密
     *
     * @param content     字符串
     * @param password    密钥
     * @param type        加密：[Cipher.ENCRYPT_MODE]，解密：[Cipher.DECRYPT_MODE]
     * @param base64OrHex 1:Base64处理，2：HEX 处理,3普通的String或者getBytes处理
     * @param length      密码的长度 -1代表自动计算,其他负数代表不计算
     * @return 加密/解密结果字符串
     */
    fun aes(
        content: String,
        password: String,
        @AESType type: Int,
        transformation: String?,
        base64OrHex: Int,
        length: Int
    ): String? {
        var password = password
        try {
            val contentByte: ByteArray? = if (type == Cipher.ENCRYPT_MODE) {
                //加密
                content.toByteArray()
            } else {
                //解密
                if (base64OrHex == 1) {
                    Base64Utils.decode(content)
                } else if (base64OrHex == 2) {
                    hex2byte(content)
                } else {
                    content.toByteArray()
                }
            }
            password = toMakeKey(password, length, DEFAULT_VALUE)
            val secretKeySpec = SecretKeySpec(password.toByteArray(charset("ASCII")), AES)
            val reslutByte = aes(transformation, secretKeySpec, contentByte, type)
            return if (reslutByte != null && reslutByte.isNotEmpty()) {
                if (type == Cipher.ENCRYPT_MODE) {
                    //加密
                    if (base64OrHex == 1) {
                        Base64Utils.encodeToStr(reslutByte)
                    } else if (base64OrHex == 2) {
                        byte2hex(reslutByte)
                    } else {
                        String(reslutByte)
                    }
                } else {
                    //解密
                    String(reslutByte)
                }
            } else {
                ""
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 16进制转byte
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
        return hs.uppercase()
    }

    /**
     * 密钥key ,默认补的数字，补全16位数，以保证安全补全至少16位长度,android和ios对接通过
     *
     * @param key    密钥key
     * @param length 密钥应有的长度 16  24  32
     * @param text   默认补的文本
     * @return 密钥
     */
    fun toMakeKey(key: String, length: Int, text: String?): String {
        var key = key
        var length = length
        if (length == -1) {
            val keyLenght = key.length
            if (keyLenght in 1..16) {
                length = 16
            } else if (keyLenght in 17..24) {
                length = 24
            } else if (keyLenght in 25..32) {
                length = 32
            }
        }
        if (length <= 0) {
            return key
        }
        // 获取密钥长度
        val strLen = key.length
        // 判断长度是否小于应有的长度
        if (strLen < length) {
            // 补全位数
            val builder = StringBuilder()
            // 将key添加至builder中
            builder.append(key)
            // 遍历添加默认文本
            for (i in 0 until length - strLen) {
                builder.append(text)
            }
            // 赋值
            key = builder.toString()
        }
        return key.substring(0, length)
    }

    private fun aes(
        transformation: String?,
        key: SecretKey,
        content: ByteArray?,
        @AESType type: Int
    ): ByteArray? {
        try {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(type, key)
            return cipher.doFinal(content)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun main(args: Array<String>) {

    }

}

fun main(args: Array<String>) {
    val aa =
        "8xxxSRkzLfjuzFkhbP4YYrijDYm5v5ZTgve79+C7ozXhE/d70RGlfyxI6PRBStX7XexXcbD/QlEerr5clbbDzQ=="
    val decrypt = AESUtils.decrypt(aa, "8d68a9777b8b7115364452c712837616")
    println("decrypt : $decrypt")
}