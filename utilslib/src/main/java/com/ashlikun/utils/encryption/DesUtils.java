/* 
 * @(#)SecurityUtils.java    Created on 2004-10-9
 * Copyright (c) 2004 ZDSoft Networks, Inc. All rights reserved.
 * $Id: SecurityUtils.java 33450 2012-12-13 08:34:03Z xuan $
 */
package com.ashlikun.utils.encryption;

import android.annotation.SuppressLint;
import androidx.annotation.IntDef;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import static com.ashlikun.utils.other.StringUtils.parseByte2HexStr;
import static com.ashlikun.utils.other.StringUtils.parseHexStr2Byte;


/**
 * 作者　　: 李坤
 * 创建时间: 14:48 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍： DES加密算法, 对称性得
 */

public abstract class DesUtils {

    @IntDef({Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE})
    @Retention(RetentionPolicy.SOURCE)
    @interface DESType {
    }

    /**
     * Aes加密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    public static String encrypt(String content, String password) {
        return des(content, password, Cipher.ENCRYPT_MODE);
    }

    /**
     * Aes解密
     *
     * @param content  字符串
     * @param password 密钥
     * @return 加密/解密结果字符串
     */
    public static String decrypt(String content, String password) {
        return des(content, password, Cipher.DECRYPT_MODE);
    }

    /**
     * Des加密/解密
     *
     * @param content  字符串内容
     * @param password 密钥
     * @param type     加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     * @return 加密/解密结果
     */
    public static String des(String content, String password, @DESType int type) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("DES");
            cipher.init(type, keyFactory.generateSecret(desKey), random);

            if (type == Cipher.ENCRYPT_MODE) {
                byte[] byteContent = content.getBytes("utf-8");
                return parseByte2HexStr(cipher.doFinal(byteContent));
            } else {
                byte[] byteContent = parseHexStr2Byte(content);
                return new String(cipher.doFinal(byteContent));
            }
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException |
                UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException |
                InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}
