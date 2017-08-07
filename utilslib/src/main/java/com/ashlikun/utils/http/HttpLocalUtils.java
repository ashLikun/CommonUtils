package com.ashlikun.utils.http;

import android.content.Context;

public class HttpLocalUtils {

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/3 15:08
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：判断是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        return NetWorkHelper.isNetworkAvailable(context);
    }
    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/3 15:08
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：判断mobile网络是否可用
     */
    public static boolean isMobileDataEnable(Context context) {
        String TAG = "httpUtils.isMobileDataEnable()";
        try {
            return NetWorkHelper.isMobileDataEnable(context);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/3 15:08
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：判断wifi网络是否可用
     */
    public static boolean isWifiDataEnable(Context context) {
        String TAG = "httpUtils.isWifiDataEnable()";
        try {
            return NetWorkHelper.isWifiDataEnable(context);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/3 14:59
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：判断是否为漫游
     */
    public static boolean isNetworkRoaming(Context context) {
        return NetWorkHelper.isNetworkRoaming(context);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/27 14:17
     * <p>
     * 方法功能：对网络资源文件判断路径 如果是已http开头的就返回这个值 否则在前面加上域名
     */
    public static String getHttpFileUrl(String baseUrl, String url) {
        String res = "";
        if (url != null) {
            if (url.startsWith("http://")) {
                res = url;
            } else if (url.startsWith("/storage") || url.startsWith("storage") || url.startsWith("/data") || url.startsWith("data")) {
                res = url;
            } else if (url.startsWith("/")) {
                res = baseUrl + url;
            } else {
                res = baseUrl + "/" + url;
            }
        }
        return res;
    }
}
