package com.ashlikun.utils.ui.extend

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.annotation.FloatRange
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.DimensUtils
import com.ashlikun.utils.ui.ScreenUtils
import com.ashlikun.utils.ui.modal.SuperToast
import com.ashlikun.utils.ui.modal.ToastUtils
import com.ashlikun.utils.ui.resources.ColorUtils
import com.ashlikun.utils.ui.resources.ResUtils

/**
 * 作者　　: 李坤
 * 创建时间: 2020/9/18　21:11
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：ui页面的一些扩展函数
 */
inline val Int.dp
    get() = DimensUtils.dip2px(this.toFloat())
inline val Float.dp
    get() = DimensUtils.dip2px(this)
inline val Double.dp
    get() = DimensUtils.dip2px(this.toFloat())

inline val Int.sp
    get() = DimensUtils.sp2px(this.toFloat())
inline val Float.sp
    get() = DimensUtils.sp2px(this)
inline val Double.sp
    get() = DimensUtils.sp2px(this.toFloat())

inline val Int.px2dip
    get() = DimensUtils.px2dip(this.toFloat())
inline val Float.px2dip
    get() = DimensUtils.px2dip(this)
inline val Double.px2dip
    get() = DimensUtils.px2dip(this.toFloat())

inline val Int.px2sp
    get() = DimensUtils.px2sp(this.toFloat())
inline val Float.px2sp
    get() = DimensUtils.px2sp(this)
inline val Double.px2sp
    get() = DimensUtils.px2sp(this.toFloat())

/**
 * [ScreenUtils]
 * 屏幕宽度的几倍
 * 0.5.sw : 屏幕宽度的一半
 * 1.sw：屏幕宽度
 */
inline val Double.sw
    get() = (ScreenUtils.width() * this).toInt()
inline val Int.sw
    get() = ScreenUtils.width() * this

/**
 * [ScreenUtils]
 * 屏幕高度的几倍
 * 0.5.sh : 屏幕高度的一半
 * 1.sh：屏幕高度
 */
inline val Double.sh
    get() = (ScreenUtils.height() * this).toInt()
inline val Int.sh
    get() = ScreenUtils.height() * this

/**
 * [ScreenUtils]
 * 屏幕宽度减去 dp
 * 10.5.shSubDp : 屏幕宽度 减去 10.5dp
 */
inline val Double.whSubDp
    get() = ScreenUtils.width(this.toInt())
inline val Int.whSubDp
    get() = ScreenUtils.width(this)

/**
 * [ScreenUtils]
 * 屏幕高度减去 dp
 * 10.5.shSubDp : 屏幕高度 减去 10.5dp
 */
inline val Double.shSubDp
    get() = ScreenUtils.height(this.toInt())
inline val Int.shSubDp
    get() = ScreenUtils.height(this)


/**
 * ResUtils
 */
inline val Int.resColor
    get() = ResUtils.getColor(this)

inline fun Int.resColor(context: Context) = ResUtils.getColor(context, this)
inline fun Context.resColor(resId: Int) = ResUtils.getColor(this, resId)
inline fun Int.resColorAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f) = ColorUtils.setColorAlpha(resColor, alpha)


inline val Int.resDrawable
    get() = ResUtils.getDrawable(this)

inline fun Context.resDrawable(resId: Int) = ResUtils.getDrawable(this, resId)
inline fun Int.resDrawable(context: Context) = ResUtils.getDrawable(context, this)
inline fun Int.resDrawable(themeId: Int) = ResUtils.getDrawable(themeId.resContextTheme, this)


inline val Int.resTheme
    get() = resContextTheme.theme
inline val Int.resContextTheme
    get() = AppUtils.context.newContextTheme(this)

inline fun Context.newContextTheme(themeId: Int) = ContextThemeWrapper(this, themeId)
inline fun Context.newTheme(themeId: Int) = ContextThemeWrapper(this, themeId).theme


inline val Int.resString
    get() = ResUtils.getString(this)

inline fun Int.resString(context: Context) = ResUtils.getString(context, this)
inline fun Context.resString(resId: Int) = ResUtils.getString(this, resId)


inline val Int.resDimension
    get() = ResUtils.getDimension(this)

inline fun Int.resDimension(context: Context) = ResUtils.getDimension(context, this)
inline fun Context.resDimension(resId: Int) = ResUtils.getDimension(this, resId)

inline val Int.resDimensionPixelOffset
    get() = ResUtils.getDimensionPixelOffset(this)

inline fun Int.resDimensionPixelOffset(context: Context) =
    ResUtils.getDimensionPixelOffset(context, this)

inline fun Context.resDimensionPixelOffset(resId: Int) =
    ResUtils.getDimensionPixelOffset(this, resId)

inline val Int.resDimensionPixelSize
    get() = ResUtils.getDimensionPixelSize(this)

inline fun Int.resDimensionPixelSize(context: Context) =
    ResUtils.getDimensionPixelSize(context, this)

inline fun Context.resDimensionPixelSize(resId: Int) = ResUtils.getDimensionPixelSize(this, resId)


/**
 * Toast
 */

inline fun String.toast() = ToastUtils.show(this)
inline fun String.toastInfo() = SuperToast.showInfoMessage(this)
inline fun String.toastError() = SuperToast.showErrorMessage(this)
inline fun String.toastConfirm() = SuperToast.showConfirmMessage(this)
inline fun String.toastWarning() = SuperToast.showWarningMessage(this)