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
    private static OnNeedListener listener;

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/7 10:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：一定要在Application里面调用
     */

    public static void init(OnNeedListener listener) {
        Utils.listener = listener;
    }


    public interface OnNeedListener {
        public Application getApplication();

        public boolean isDebug();
    }

    public static Application getApp() {
        if (listener == null) {
            throw new RuntimeException("请在Application调用Utils的init方法");
        } else {
            return listener.getApplication();
        }
    }

    public static boolean isDebug() {
        if (listener == null) {
            throw new RuntimeException("请在Application调用Utils的init方法");
        } else {
            return listener.isDebug();
        }
    }

}
