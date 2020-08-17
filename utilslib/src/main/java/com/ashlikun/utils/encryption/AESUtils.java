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
import javax.crypto.spec.SecretKeySpec;

import static com.ashlikun.utils.other.StringUtils.parseByte2HexStr;
import static com.ashlikun.utils.other.StringUtils.parseHexStr2Byte;

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
        return aes(content, password, Cipher.ENCRYPT_MODE, 128);
    }

    public static String encrypt(String content, String password, int bits) {
        return aes(content, password, Cipher.ENCRYPT_MODE, bits);
    }

    /**
     * Aes解密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    public static String decrypt(String content, String password) {
        return aes(content, password, Cipher.DECRYPT_MODE, 128);
    }

    public static String decrypt(String content, String password, int bits) {
        return aes(content, password, Cipher.DECRYPT_MODE, bits);
    }

    /**
     * Aes加密/解密
     *
     * @param content  字符串
     * @param password 密钥
     * @param type     加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     * @return 加密/解密结果字符串
     */
    public static String aes(String content, String password, @AESType int type, int bits) {
        try {

            SecretKeySpec key = new SecretKeySpec(getRawKey(password.getBytes(), bits / 8), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(type, key);
            if (type == Cipher.ENCRYPT_MODE) {
                byte[] byteContent = content.getBytes("utf-8");
                return parseByte2HexStr(cipher.doFinal(byteContent));
            } else {
                byte[] byteContent = parseHexStr2Byte(content);
                return new String(cipher.doFinal(byteContent));
            }
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException |
                UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
