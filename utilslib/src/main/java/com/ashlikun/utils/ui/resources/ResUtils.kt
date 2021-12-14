package com.ashlikun.utils.ui.resources

import com.ashlikun.utils.AppUtils.app
import androidx.annotation.ColorRes
import android.util.TypedValue
import com.ashlikun.utils.AppUtils
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.annotation.AnyRes

/**
 * 作者　　: 李坤
 * 创建时间:2017/9/6 0006　20:10
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：获取资源的工具类
 */
object ResUtils {
    /**
     * 是否有这个id
     */
    fun havRes(@ColorRes resId: Int): Boolean {
        val typedValue = TypedValue()
        app.resources.getValue(resId, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_INT
            && typedValue.type <= TypedValue.TYPE_LAST_INT
        ) {
            return true
        } else if (typedValue.type != TypedValue.TYPE_STRING) {
            return false
        }
        return true
    }

    fun getColor(context: Context, @ColorRes color: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.theme != null) {
            context.resources.getColor(color, context.theme)
        } else context.resources.getColor(color)
    }

    fun getColor(@ColorRes color: Int): Int {
        return app.resources.getColor(color)
    }

    fun getDrawable(context: Context, @DrawableRes drawable: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.resources.getDrawable(drawable, context.theme)
        } else {
            context.resources.getDrawable(drawable)
        }
    }

    fun getDrawable(@DrawableRes drawable: Int): Drawable {
        return app.resources.getDrawable(drawable)
    }

    fun getDimension(context: Context, @DimenRes dimen: Int): Float {
        return context.resources.getDimension(dimen)
    }

    fun getDimension(@DimenRes dimen: Int): Float {
        return app.resources.getDimension(dimen)
    }

    fun getString(context: Context, @StringRes str: Int): String {
        return context.resources.getString(str)
    }

    fun getString(@StringRes str: Int): String {
        return app.resources.getString(str)
    }

    fun getDimensionPixelOffset(context: Context, @DimenRes resId: Int): Int {
        return context.resources.getDimensionPixelOffset(resId)
    }

    fun getDimensionPixelOffset(@DimenRes resId: Int): Int {
        return app.resources.getDimensionPixelOffset(resId)
    }

    fun getDimensionPixelSize(context: Context, @DimenRes resId: Int): Int {
        return context.resources.getDimensionPixelSize(resId)
    }

    fun getDimensionPixelSize(@DimenRes resId: Int): Int {
        return app.resources.getDimensionPixelSize(resId)
    }

    /**
     * 直接获取xml里面的变量值
     * 12dp就返回12.0
     *
     * @param id
     * @return
     */
    fun getValue(@AnyRes id: Int): Float {
        val typedValue = TypedValue()
        app.resources.getValue(id, typedValue, true)
        return TypedValue.complexToFloat(typedValue.data)
    }
}