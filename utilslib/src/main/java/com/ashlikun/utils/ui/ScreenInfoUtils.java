package com.ashlikun.utils.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ashlikun.utils.AppUtils;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/22 15:07
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：屏幕信息工具类
 */
public class ScreenInfoUtils {
    private static DisplayMetrics metric;


    public static int getWidth() {
        init();
        return metric.widthPixels;
    }

    private static void init() {
        if (metric == null) {
            metric = new DisplayMetrics();
            WindowManager wm = (WindowManager) AppUtils.getApp().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
        }
    }


    public static int getHeight() {
        init();
        return metric.heightPixels;
    }


    public static float getDensity() {
        init();
        return metric.density;
    }


    public static DisplayMetrics getDisplayMetrics() {
        init();
        return metric;
    }

    public static int getDensityDpi() {
        init();
        return metric.densityDpi;
    }

}
