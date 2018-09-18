package com.ashlikun.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.TypedValue;

import com.ashlikun.utils.AppUtils;

/**
 * 作者　　: 李坤
 * 创建时间:2017/9/6 0006　20:10
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：获取资源的工具类
 */

public class ResUtils {
    public static int getColor(Context context, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.getTheme() != null) {
            return context.getResources().getColor(color, context.getTheme());
        }
        return context.getResources().getColor(color);
    }

    public static int getColor(@ColorRes int color) {
        return AppUtils.getApp().getResources().getColor(color);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(drawable, context.getTheme());
        } else {
            return context.getResources().getDrawable(drawable);
        }
    }

    public static Drawable getDrawable(@DrawableRes int drawable) {
        return AppUtils.getApp().getResources().getDrawable(drawable);
    }

    public static float getDimension(Context context, @DimenRes int dimen) {
        return context.getResources().getDimension(dimen);
    }

    public static float getDimension(@DimenRes int dimen) {
        return AppUtils.getApp().getResources().getDimension(dimen);
    }

    public static String getString(Context context, @StringRes int str) {
        return context.getResources().getString(str);
    }

    public static String getString(@StringRes int str) {
        return AppUtils.getApp().getResources().getString(str);
    }

    /**
     * 直接获取xml里面的变量值
     * 12dp就返回12.0
     *
     * @param id
     * @return
     */
    public static float getFloatValue(@IdRes int id) {
        TypedValue typedValue = new TypedValue();
        AppUtils.getApp().getResources().getValue(id, typedValue, true);
        return TypedValue.complexToFloat(typedValue.data);
    }

}
