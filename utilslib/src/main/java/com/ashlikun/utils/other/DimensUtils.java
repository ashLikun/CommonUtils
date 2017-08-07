package com.ashlikun.utils.other;

import android.content.Context;

/**
 * 作者　　: 李坤
 * 创建时间:2016/10/12　13:50
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：尺寸转换类
 */

public class DimensUtils {


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:25
     * <p>
     * 方法功能：将px值转换为dip或dp值，保证尺寸大小不变
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:25
     * <p>
     * 方法功能：将dip或dp值转换为px值，保证尺寸大小不变
     */

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:26
     * <p>
     * 方法功能：将px值转换为sp值，保证文字大小不变
     */

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:26
     * <p>
     * 方法功能：将sp值转换为px值，保证文字大小不变
     */

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * fontScale + 0.5f);
    }
}
