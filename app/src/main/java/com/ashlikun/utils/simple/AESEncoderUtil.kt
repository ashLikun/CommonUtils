package com.ashlikun.utils.simple

import com.ashlikun.utils.encryption.Base64Utils
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

/**
 * 作者　　: 李坤
 * 创建时间: 2021/2/23　17:46
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
object AESEncoderUtil {
    private const val AES = "AES"
    private const val SP = "SHA1PRNG"
    private const val CHARACTER = "utf-8"

    /**
     * 对原字符串串进⾏行行加密
     *
     * @param content
     * 原字符串串
     * @return
     */
    fun encode(content: String, encodeRules: String): String {
        return try {
            val kgt = KeyGenerator.getInstance(AES)
            val secureRandom = SecureRandom.getInstance(SP)
            secureRandom.setSeed(encodeRules.toByteArray())
            kgt.init(128, secureRandom)
            val secretKey = kgt.generateKey()
            val enCodeFormat = secretKey.encoded
            val key = SecretKeySpec(enCodeFormat, AES)
            val cipher = Cipher.getInstance(AES)
            val byteContent = content.toByteArray(charset(CHARACTER))
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val result = cipher.doFinal(byteContent)
            // 对sign进⾏行行编码，防⽌止加号变为空格
            URLEncoder.encode(parseByte2HexStr(result), "UTF-8")
        } catch (e: Exception) {
            throw RuntimeException("encode error")
        }
    }

    /**
     * 对加密的字符串串解密
     *
     * @param content
     * 加密的字符串串
     * @return
     */
    fun decode(content: String?, encodeRules: String): String? {
        return try {
            // 对sign进⾏行行解码
            val decryptFrom = parseHexStr2Byte(URLDecoder.decode(content, "UTF-8"))
            val kgt = KeyGenerator.getInstance(AES)
            val secureRandom = SecureRandom.getInstance(SP)
            secureRandom.setSeed(encodeRules.toByteArray())
            kgt.init(128, secureRandom)
            val secretKey = kgt.generateKey()
            val enCodeFormat = secretKey.encoded
            val key = SecretKeySpec(enCodeFormat, AES)
            val cipher = Cipher.getInstance(AES) // 创建密码器器
            cipher.init(Cipher.DECRYPT_MODE, key) // 初始化
            val result = cipher.doFinal(decryptFrom)
            String(result) // 加密
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将指定字符串做hash算法处理
     * @param datastr    需要被处理的字符串
     * @return
     */
    private fun tohash256Deal(datastr: String): ByteArray {
        return try {
            /**
             * MessageDigest : 该类是个抽象类，此类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值。
             * getInstance(String algorithm) ： 生成实现指定摘要算法的 MessageDigest 对象。
             * algorithm : 所请求算法的名称。
             * 该对象通过使用 update 方法处理数据。任何时候都可以调用 reset 方法重置摘要。一旦所有需要更新的数据都已经被更新了，应该调用 digest 方法之一完成哈希计算。
             */
            val digester = MessageDigest.getInstance("SHA-256")
            /**
             * update(byte[] input) : 使用指定的字节数组更新摘要。
             */
            digester.update(datastr.toByteArray())
            /**
             * hex : 存放哈希值结果的字节数组。
             */
            digester.digest()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e.message)
        }
    }

    /**
     * 将⼆二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private fun parseByte2HexStr(buf: ByteArray): String {
        return String(Base64Utils.encode(buf))
    }

    /**
     * 将16进制转换为⼆二进制
     *
     * @param hexStr
     * @return
     */
    private fun parseHexStr2Byte(hexStr: String): ByteArray {
        return Base64Utils.decode(hexStr)
    }

    @JvmStatic
    fun main(args: Array<String>) {
//        String CDNO = "{\"companyCode\": \"test\",\"auth\":1555407227,\"username\": \"123456789\"}";
//        String encodeRules = "8N3U2C3I4B5C2J7P83A";
//        String encode = AESEncoderUtil.encode(CDNO, encodeRules);
//        System.out.println("test encode:" + encode);
//        System.out.println("demo encode:bHwXyaQzi6B80L01X%2FdDVhzT9wg58rR3vP6NpDmqnN14KCKgmsX%2BmcqTPz5%2F79w7Ko2PKmqk6hupFSqDFlbcLbTMRnhataKrZKUOkvxGnpg%3D");
//        String decode = AESEncoderUtil.decode("bHwXyaQzi6B80L01X%2FdDVhzT9wg58rR3vP6NpDmqnN14KCKgmsX%2BmcqTPz5%2F79w7sC%2BgOav9kPwfJ9SzfbPMBZS6mO6RMYO0IAuZeM1npE8%3D", encodeRules);
//        System.out.println("decode:" + decode);
        val aaa = encode("1111111111111111111111111111111111", "8d68a9777b8b7115364452c712837616")
        println("encode:$aaa")
        val decode = decode(
            "qv%2BfJZ0lOMy9ynchtDtDO6cnsl%2B%2FRF8XSALM40iS6Sk9s%2FZyT4b8IU74YCCwvaxLL%2FCoon%2FLByUaNODrZenMig%3D%3D",
            "8d68a9777b8b7115364452c712837616"
        )
        println("decode:$decode")
        System.out.printf("完成\n\r")
    }
}