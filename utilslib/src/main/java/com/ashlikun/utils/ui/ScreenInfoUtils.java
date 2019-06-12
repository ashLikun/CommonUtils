package com.ashlikun.utils.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.DimensUtils;

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

    public static int width() {
        return widthSub(0);
    }

    /**
     * @param subDp 减去的dp大小
     * @return
     */
    public static int widthSubDp(int subDp) {
        return widthSub(DimensUtils.dip2px(subDp));
    }

    /**
     * @param subPx 减去的px大小
     * @return
     */
    public static int widthSub(int subPx) {
        if (AppUtils.getApp() == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = AppUtils.getApp().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels - subPx;
    }

    public static int height() {
        return heightSub(0);
    }

    /**
     * @param subDp 减去的dp大小
     * @return
     */
    public static int heightSubDp(int subDp) {
        return heightSub(DimensUtils.dip2px(subDp));
    }

    /**
     * @param subPx 减去的px大小
     * @return
     */
    public static int heightSub(int subPx) {
        if (AppUtils.getApp() == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = AppUtils.getApp().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels - subPx;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/5 13:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/5 13:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：获取导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
