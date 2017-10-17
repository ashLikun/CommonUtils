package com.ashlikun.utils.encryption;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/3/27 16:40
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：Md5加密  不可逆算法
 */
public class Md5Utils {
    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:31
     * <p>
     * 方法功能：md加密
     */
    public static String getMD5(String string) {
        return getMD5(string, "");
    }

    /**
     * MD5加密(多次)
     *
     * @param string 加密字符串
     * @param times  重复加密次数
     * @return 加密结果字符串
     */
    public static String getMD5(@NonNull String string, int times) {
        if (TextUtils.isEmpty(string)) return "";

        String md5 = string;
        for (int i = 0; i < times; i++) md5 = getMD5(md5);
        return md5;
    }

    /**
     * MD5加密(加盐)
     *
     * @param string 加密字符串
     * @param slat   加密盐值key
     * @return 加密结果字符串
     */
    public static String getMD5(@NonNull String string, String slat) {
        if (TextUtils.isEmpty(string)) return "";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
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

    /**
     * MD5加密(文件)
     * 可用于文件校验。
     *
     * @param file 加密文件
     * @return md5 数值
     */
    public static String getMD5(@NonNull File file) {
        if (!file.isFile()) {
            return "";
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (in == null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
