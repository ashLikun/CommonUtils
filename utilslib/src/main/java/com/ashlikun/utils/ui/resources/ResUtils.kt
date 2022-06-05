package com.ashlikun.utils.ui.resources

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.ashlikun.utils.AppUtils

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
     * 获取String的时候回调
     */
    var callString: ((resId: Int, value: String) -> String)? = null

    /**
     * 是否有这个id
     */
    fun havRes(@ColorRes resId: Int): Boolean {
        val typedValue = TypedValue()
        AppUtils.appResources.getValue(resId, typedValue, true)
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
        return ContextCompat.getColor(context, color)
    }

    fun getColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(AppUtils.context, color)
    }

    fun getDrawable(context: Context, @DrawableRes drawable: Int): Drawable {
        return ContextCompat.getDrawable(context, drawable)!!
    }

    fun getDrawable(@DrawableRes drawable: Int): Drawable {
        return getDrawable(AppUtils.context, drawable)
    }

    fun getDimension(context: Context, @DimenRes dimen: Int): Float {
        return context.resources.getDimension(dimen)
    }

    fun getDimension(@DimenRes dimen: Int): Float {
        return AppUtils.appResources.getDimension(dimen)
    }

    fun getString(context: Context, @StringRes id: Int): String {
        val value = context.resources.getString(id)
        return callString?.invoke(id, value) ?: value
    }

    fun getString(@StringRes id: Int): String {
        val value = AppUtils.appResources.getString(id)
        return callString?.invoke(id, value) ?: value
    }

    fun getDimensionPixelOffset(context: Context, @DimenRes resId: Int): Int {
        return context.resources.getDimensionPixelOffset(resId)
    }

    fun getDimensionPixelOffset(@DimenRes resId: Int): Int {
        return AppUtils.appResources.getDimensionPixelOffset(resId)
    }

    fun getDimensionPixelSize(context: Context, @DimenRes resId: Int): Int {
        return context.resources.getDimensionPixelSize(resId)
    }

    fun getDimensionPixelSize(@DimenRes resId: Int): Int {
        return AppUtils.appResources.getDimensionPixelSize(resId)
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
        AppUtils.appResources.getValue(id, typedValue, true)
        return TypedValue.complexToFloat(typedValue.data)
    }
}