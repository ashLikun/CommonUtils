package com.ashlikun.utils.ui.extend

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import androidx.annotation.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2023/3/17　9:38
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：资源的一些扩展
 */
inline fun Context.themeWrapper(
    @StyleRes res: Int,
) = ContextThemeWrapper(this, res)

fun Context.resolveDrawable(
    @DrawableRes res: Int? = null, @AttrRes attr: Int? = null, fallback: Drawable? = null
): Drawable? {
    if (attr != null) {
        val a = theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            var d = a.getDrawable(0)
            if (d == null && fallback != null) {
                d = fallback
            }
            return d
        } finally {
            a.recycle()
        }
    }
    if (res == null) return fallback
    return ContextCompat.getDrawable(this, res)
}

@ColorInt
fun Context.resolveColor(
    @ColorRes res: Int? = null, @AttrRes attr: Int? = null, fallback: (() -> Int)? = null
): Int {
    if (attr != null) {
        val a = theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            val result = a.getColor(0, 0)
            if (result == 0 && fallback != null) {
                return fallback()
            }
            return result
        } finally {
            a.recycle()
        }
    }
    return ContextCompat.getColor(this, res ?: 0)
}

fun Context.resolveInt(
    @AttrRes attr: Int, defaultValue: Int = 0
): Int {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getInt(0, defaultValue)
    } finally {
        a.recycle()
    }
}

fun Context.resolveDimen(
    context: Context, @AttrRes attr: Int, defaultValue: (() -> Float)? = null
): Float {
    val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getDimension(0, defaultValue?.invoke() ?: 0f)
    } finally {
        a.recycle()
    }
}