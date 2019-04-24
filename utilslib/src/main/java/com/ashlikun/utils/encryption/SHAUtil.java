package com.ashlikun.utils.encryption;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/10/17　14:04
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：sha加密
 * 单向加密
 */

public class SHAUtil {

    public final static String SHA224 = "sha-224";
    public final static String SHA256 = "sha-256";
    public final static String SHA384 = "sha-384";
    public final static String SHA512 = "sha-512";

    @StringDef({SHA224, SHA256, SHA384, SHA512})
    @Retention(RetentionPolicy.SOURCE)
    @interface SHAType {
    }

    public static String getSha(String string) {
        return getSha(string, SHA256);
    }

    /**
     * Sha加密
     *
     * @param string 加密字符串
     * @param type   加密类型 ：{@link #SHA224}，{@link #SHA256}，{@link #SHA384}，{@link #SHA512}
     * @return SHA加密结果字符串
     */
    public static String getSha(String string, @Nullable @SHAType String type) {
        if (TextUtils.isEmpty(string)) return "";
        if (TextUtils.isEmpty(type)) type = SHA256;

        try {
            MessageDigest md5 = MessageDigest.getInstance(type);
            byte[] bytes = md5.digest((string).getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
