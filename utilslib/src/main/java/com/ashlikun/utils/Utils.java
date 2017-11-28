package com.ashlikun.utils;

import android.app.Application;

/**
 * 作者　　: 李坤
 * 创建时间:2016/12/30　9:59
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class Utils {
    private static boolean isDebug;
    private static Application myApp;

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/7 10:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：一定要在Application里面调用
     */

    public static void init(Application myApp) {
        Utils.myApp = myApp;
    }

    public static void setDebug(boolean isDebug) {
        Utils.isDebug = isDebug;
    }


    public static Application getApp() {
        return Utils.myApp;
    }

    public static boolean isDebug() {
        return Utils.isDebug;
    }

}
