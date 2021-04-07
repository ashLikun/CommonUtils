package com.ashlikun.utils.simple;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者　　: 李坤
 * 创建时间: 2021/2/23　17:46
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class AESEncoderUtil {
    private static final String AES = "AES";
    private static final String SP = "SHA1PRNG";
    private static final String CHARACTER = "utf-8";

    /**
     * 对原字符串串进⾏行行加密
     *
     * @param content
     *            原字符串串
     * @return
     */
    public static String encode(String content, String encodeRules) {
        try {
            KeyGenerator kgt = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance(SP);
            secureRandom.setSeed(encodeRules.getBytes());
            kgt.init(128, secureRandom);
            SecretKey secretKey = kgt.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(AES);
            byte[] byteContent = content.getBytes(CHARACTER);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            // 对sign进⾏行行编码，防⽌止加号变为空格
            return URLEncoder.encode(parseByte2HexStr(result), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("encode error");
        }
    }

    /**
     * 对加密的字符串串解密
     *
     * @param content
     *            加密的字符串串
     * @return
     */
    public static String decode(String content, String encodeRules) {
        try {
            // 对sign进⾏行行解码
            byte[] decryptFrom = parseHexStr2Byte(URLDecoder.decode(content, "UTF-8"));
            KeyGenerator kgt = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom = SecureRandom.getInstance(SP);
            secureRandom.setSeed(encodeRules.getBytes());
            kgt.init(128, secureRandom);
            SecretKey secretKey = kgt.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(AES);// 创建密码器器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(decryptFrom);
            return new String(result); // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将指定字符串做hash算法处理
     * @param datastr	需要被处理的字符串
     * @return
     */
    private static byte[] tohash256Deal(String datastr) {
        try {
            /**
             * MessageDigest : 该类是个抽象类，此类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。信息摘要是安全的单向哈希函数，它接收任意大小的数据，输出固定长度的哈希值。
             * getInstance(String algorithm) ： 生成实现指定摘要算法的 MessageDigest 对象。
             * algorithm : 所请求算法的名称。
             * 该对象通过使用 update 方法处理数据。任何时候都可以调用 reset 方法重置摘要。一旦所有需要更新的数据都已经被更新了，应该调用 digest 方法之一完成哈希计算。
             */
            MessageDigest digester=MessageDigest.getInstance("SHA-256");
            /**
             * update(byte[] input) : 使用指定的字节数组更新摘要。
             */
            digester.update(datastr.getBytes());
            /**
             * digest() : 通过执行诸如填充之类的最终操作完成哈希计算。调用此方法后摘要被重置。
             */
            byte[] hex=digester.digest();
            /**
             * hex : 存放哈希值结果的字节数组。
             */
            return hex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 将⼆二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        return new String(Base64.getEncoder().encodeToString(buf));
    }

    /**
     * 将16进制转换为⼆二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        return Base64.getDecoder().decode(hexStr);
    }

    public static void main(String[] args) {
//        String CDNO = "{\"companyCode\": \"test\",\"auth\":1555407227,\"username\": \"123456789\"}";
//        String encodeRules = "8N3U2C3I4B5C2J7P83A";
//        String encode = AESEncoderUtil.encode(CDNO, encodeRules);
//        System.out.println("test encode:" + encode);
//        System.out.println("demo encode:bHwXyaQzi6B80L01X%2FdDVhzT9wg58rR3vP6NpDmqnN14KCKgmsX%2BmcqTPz5%2F79w7Ko2PKmqk6hupFSqDFlbcLbTMRnhataKrZKUOkvxGnpg%3D");
//        String decode = AESEncoderUtil.decode("bHwXyaQzi6B80L01X%2FdDVhzT9wg58rR3vP6NpDmqnN14KCKgmsX%2BmcqTPz5%2F79w7sC%2BgOav9kPwfJ9SzfbPMBZS6mO6RMYO0IAuZeM1npE8%3D", encodeRules);
//        System.out.println("decode:" + decode);

        String aaa = AESEncoderUtil.encode("1111111111111111111111111111111111", "8d68a9777b8b7115364452c712837616");
        System.out.println("encode:" + aaa);

        String decode = AESEncoderUtil.decode("qv%2BfJZ0lOMy9ynchtDtDO6cnsl%2B%2FRF8XSALM40iS6Sk9s%2FZyT4b8IU74YCCwvaxLL%2FCoon%2FLByUaNODrZenMig%3D%3D", "8d68a9777b8b7115364452c712837616");
        System.out.println("decode:" + decode);

        System.out.printf("完成\n\r");
    }


}
