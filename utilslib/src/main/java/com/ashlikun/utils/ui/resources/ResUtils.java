package com.ashlikun.utils.ui.resources;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.AnyRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.ashlikun.utils.AppUtils;

/**
 * 作者　　: 李坤
 * 创建时间:2017/9/6 0006　20:10
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：获取资源的工具类
 */

public class ResUtils {
    /**
     * 是否有这个id
     */
    public static boolean havRes(@ColorRes int resId) {
        TypedValue typedValue = new TypedValue();
        AppUtils.getApp().getResources().getValue(resId, typedValue, true);
        if (typedValue.type >= TypedValue.TYPE_FIRST_INT
                && typedValue.type <= TypedValue.TYPE_LAST_INT) {
            return true;
        } else if (typedValue.type != TypedValue.TYPE_STRING) {
            return false;
        }
        return true;
    }

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

    public static int getDimensionPixelOffset(Context context, @DimenRes int resId) {
        return context.getResources().getDimensionPixelOffset(resId);
    }

    public static int getDimensionPixelOffset(@DimenRes int resId) {
        return AppUtils.getApp().getResources().getDimensionPixelOffset(resId);
    }

    public static int getDimensionPixelSize(Context context, @DimenRes int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }

    public static int getDimensionPixelSize(@DimenRes int resId) {
        return AppUtils.getApp().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 直接获取xml里面的变量值
     * 12dp就返回12.0
     *
     * @param id
     * @return
     */
    public static float getValue(@AnyRes int id) {
        TypedValue typedValue = new TypedValue();
        AppUtils.getApp().getResources().getValue(id, typedValue, true);
        return TypedValue.complexToFloat(typedValue.data);
    }

}
