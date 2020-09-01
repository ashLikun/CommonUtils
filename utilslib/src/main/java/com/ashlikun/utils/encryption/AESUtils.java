package com.ashlikun.utils.encryption;

import androidx.annotation.IntDef;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/6/23 17:38
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：AES,加密
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 * 对称加密
 */
public class AESUtils {
    private static final String AES = "AES"; //AES 加密
    /**
     * 密钥长度
     */
    public static final int KEY_LENGTH = 16;
    /**
     * 默认填充位数
     */
    public static final String DEFAULT_VALUE = "0";

    @IntDef({Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE})
    @Retention(RetentionPolicy.SOURCE)
    @interface AESType {
    }

    /**
     * 对密钥进行处理
     */
    public static byte[] getRawKey(byte[] seed, int keySizeInBytes) throws Exception {
        return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(seed, keySizeInBytes);
    }

    /**
     * Aes加密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    public static String encrypt(String content, String password) {
        return aes(content, password, Cipher.ENCRYPT_MODE, KEY_LENGTH);
    }

    public static String encrypt(String content, String password, int length) {
        return aes(content, password, Cipher.ENCRYPT_MODE, length);
    }

    /**
     * Aes解密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    public static String decrypt(String content, String password) {
        return aes(content, password, Cipher.DECRYPT_MODE, KEY_LENGTH);
    }

    public static String decrypt(String content, String password, int length) {
        return aes(content, password, Cipher.DECRYPT_MODE, length);
    }

    /**
     * Aes加密/解密
     *
     * @param content  字符串
     * @param password 密钥
     * @param type     加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     * @return 加密/解密结果字符串
     */
    public static String aes(String content, String password, @AESType int type, int length) {
        try {
            byte[] contentByte;
            if (type == Cipher.ENCRYPT_MODE) {
                contentByte = content.getBytes();
            } else {
                //解密的时候是否吧字符串用base64
                contentByte = Base64Utils.decode(content);
            }
            password = toMakeKey(password, length, DEFAULT_VALUE);
            IvParameterSpec iv = new IvParameterSpec(password.getBytes());
            return new String(aes("AES/CBC/PKCS7Padding",
                    new SecretKeySpec(password.getBytes("ASCII"), AES), iv, contentByte, type));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 密钥key ,默认补的数字，补全16位数，以保证安全补全至少16位长度,android和ios对接通过
     *
     * @param key    密钥key
     * @param length 密钥应有的长度 16  24  32
     * @param text   默认补的文本
     * @return 密钥
     */
    public static String toMakeKey(String key, int length, String text) {
        // 获取密钥长度
        int strLen = key.length();
        // 判断长度是否小于应有的长度
        if (strLen < length) {
            // 补全位数
            StringBuilder builder = new StringBuilder();
            // 将key添加至builder中
            builder.append(key);
            // 遍历添加默认文本
            for (int i = 0; i < length - strLen; i++) {
                builder.append(text);
            }
            // 赋值
            key = builder.toString();
        }
        return key.substring(0, length);
    }

    private static byte[] aes(String transformation, SecretKey key, IvParameterSpec iv, byte[] content, @AESType int type) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(type, key, iv);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException |
                InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
