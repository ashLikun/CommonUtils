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
    public static final String AES = "AES"; //AES 加密
    /**
     * 密钥长度
     */
    public static final int KEY_LENGTH = 16;
    public static final String DEFAULT_TRANSFORMATION = "AES/ECB/PKCS7Padding";
    public static final String TRANSFORMATION_AES_CBC_PKCS = "AES/CBC/PKCS7Padding";
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
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 1, -1);
    }

    public static String encrypt(String content, String password, String transformation) {
        return aes(content, password, Cipher.ENCRYPT_MODE, transformation, 1, -1);
    }

    public static String encrypt(String content, String password, int length) {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 1, length);
    }

    public static String encryptHex(String content, String password) {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 2, -1);
    }

    public static String encryptHex(String content, String password, String transformation) {
        return aes(content, password, Cipher.ENCRYPT_MODE, transformation, 2, -1);
    }

    public static String encryptHex(String content, String password, int length) {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 2, length);
    }

    public static String encryptStr(String content, String password) {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 3, -1);
    }

    public static String encryptStr(String content, String password, String transformation) {
        return aes(content, password, Cipher.ENCRYPT_MODE, transformation, 3, -1);
    }

    public static String encryptStr(String content, String password, int length) {
        return aes(content, password, Cipher.ENCRYPT_MODE, DEFAULT_TRANSFORMATION, 3, length);
    }

    /**
     * Aes解密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    public static String decrypt(String content, String password) {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 1, -1);
    }

    public static String decrypt(String content, String password, String transformation) {
        return aes(content, password, Cipher.DECRYPT_MODE, transformation, 1, -1);
    }

    public static String decrypt(String content, String password, int length) {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 1, length);
    }

    public static String decryptHex(String content, String password) {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 2, -1);
    }

    public static String decryptHex(String content, String password, String transformation) {
        return aes(content, password, Cipher.DECRYPT_MODE, transformation, 2, -1);
    }

    public static String decryptHex(String content, String password, int length) {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 2, length);
    }

    public static String decryptStr(String content, String password) {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 3, -1);
    }

    public static String decryptStr(String content, String password, String transformation) {
        return aes(content, password, Cipher.DECRYPT_MODE, transformation, 3, -1);
    }

    public static String decryptStr(String content, String password, int length) {
        return aes(content, password, Cipher.DECRYPT_MODE, DEFAULT_TRANSFORMATION, 3, length);
    }

    /**
     * Aes加密/解密
     *
     * @param content     字符串
     * @param password    密钥
     * @param type        加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     * @param base64OrHex 1:Base64处理，2：HEX 处理,3普通的String或者getBytes处理
     * @param length      密码的长度 -1代表自动计算,其他负数代表不计算
     * @return 加密/解密结果字符串
     */
    public static String aes(String content, String password, @AESType int type, String transformation, int base64OrHex, int length) {
        try {
            byte[] contentByte;
            if (type == Cipher.ENCRYPT_MODE) {
                //加密
                contentByte = content.getBytes();
            } else {
                //解密
                if (base64OrHex == 1) {
                    contentByte = Base64Utils.decode(content);
                } else if (base64OrHex == 2) {
                    contentByte = hex2byte(content);
                } else {
                    contentByte = content.getBytes();
                }
            }
            password = toMakeKey(password, length, DEFAULT_VALUE);
            SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes("ASCII"), AES);
            byte[] reslutByte = aes(transformation, secretKeySpec, contentByte, type);
            if (reslutByte != null && reslutByte.length > 0) {
                if (type == Cipher.ENCRYPT_MODE) {
                    //加密
                    if (base64OrHex == 1) {
                        return Base64Utils.encodeToStr(reslutByte);
                    } else if (base64OrHex == 2) {
                        return byte2hex(reslutByte);
                    } else {
                        return new String(reslutByte);
                    }
                } else {
                    //解密
                    return new String(reslutByte);
                }
            } else {
                return "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 16进制转byte
     */
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
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
        if (length == -1) {
            int keyLenght = key.length();
            if (keyLenght > 0 && keyLenght <= 16) {
                length = 16;
            } else if (keyLenght > 16 && keyLenght <= 24) {
                length = 24;
            } else if (keyLenght > 24 && keyLenght <= 32) {
                length = 32;
            }
        }
        if (length <= 0) {
            return key;
        }
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

    private static byte[] aes(String transformation, SecretKey key, byte[] content, @AESType int type) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(type, key);
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
