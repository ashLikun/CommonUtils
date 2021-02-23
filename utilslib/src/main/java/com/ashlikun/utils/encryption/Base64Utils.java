package com.ashlikun.utils.encryption;

import android.util.Base64;

import com.ashlikun.utils.other.file.FileUtils;

import kotlin.text.Charsets;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/7/4 17:49
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 * CRLF：这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
 * DEFAULT：这个参数是默认，使用默认的方法来加密
 * NO_PADDING：这个参数是略去加密字符串最后的“=”
 * NO_WRAP：这个参数意思是略去所有的换行符（设置后CRLF就没用了）
 * URL_SAFE：这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
 */

public class Base64Utils {

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/27 17:47
     * <p>
     * 方法功能：加密
     */
    public static byte[] encode(byte[] data) {
        return Base64.encode(data, Base64.NO_WRAP);
    }

    public static byte[] encode(String data) {
        return Base64.encode(data.getBytes(), Base64.NO_WRAP);
    }

    public static String encodeToStr(byte[] data) {
        return new String(encode(data), Charsets.UTF_8);
    }

    public static String encodeToStr(String data) {
        return new String(encode(data), Charsets.UTF_8);
    }


    public static String getFileToBase64(String path) {
        return Base64.encodeToString(FileUtils.readByte(path), Base64.NO_WRAP);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:30
     * <p>
     * 方法功能：解密
     */

    public static byte[] decode(String str) {
        return Base64.decode(str, Base64.NO_WRAP);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:30
     * <p>
     * 方法功能：解密 转成字符串
     */
    public static String decodeToStr(String str) {
        return new String(decode(str), Charsets.UTF_8);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:30
     * <p>
     * 方法功能：解密重载
     */

    public static byte[] decode(byte[] data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }


}
