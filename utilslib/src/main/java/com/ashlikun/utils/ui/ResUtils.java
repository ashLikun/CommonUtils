package com.ashlikun.utils.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.ashlikun.utils.AppUtils;

/**
 * 作者　　: 李坤
 * 创建时间:2017/9/6 0006　20:10
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：获取资源的工具类
 */

public class ResUtils {
    public int getColor(Context context, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.getTheme() != null) {
            return context.getResources().getColor(color, context.getTheme());
        }
        return context.getResources().getColor(color);
    }

    public int getColor(@ColorRes int color) {
        return AppUtils.getApp().getResources().getColor(color);
    }

    public Drawable getDrawable(Context context, @DrawableRes int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(drawable, context.getTheme());
        } else {
            return context.getResources().getDrawable(drawable);
        }
    }

    public Drawable getDrawable(@DrawableRes int drawable) {
        return AppUtils.getApp().getResources().getDrawable(drawable);
    }

    public float getDimension(Context context, @DimenRes int color) {
        return context.getResources().getDimension(color);
    }

    public float getDimension(@DimenRes int color) {
        return AppUtils.getApp().getResources().getDimension(color);
    }

    public String getString(Context context, @StringRes int str) {
        return context.getResources().getString(str);
    }

    public String getString(@StringRes int str) {
        return AppUtils.getApp().getResources().getString(str);
    }
}
