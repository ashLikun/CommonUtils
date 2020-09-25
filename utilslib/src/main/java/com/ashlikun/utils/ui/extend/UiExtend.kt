package com.ashlikun.utils.ui.extend

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.ashlikun.utils.other.DimensUtils
import com.ashlikun.utils.ui.ResUtils
import com.ashlikun.utils.ui.SuperToast
import com.ashlikun.utils.ui.ToastUtils

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

inline val Int.sp
    get() = DimensUtils.sp2px(this.toFloat())
inline val Float.sp
    get() = DimensUtils.sp2px(this)

inline val Int.px2dip
    get() = DimensUtils.px2dip(this.toFloat())
inline val Float.px2dip
    get() = DimensUtils.px2dip(this)

inline val Int.px2sp
    get() = DimensUtils.px2sp(this.toFloat())
inline val Float.px2sp
    get() = DimensUtils.px2sp(this)

/**
 * ResUtils
 */
inline val Int.resColor
    get() = ResUtils.getColor(this)

inline fun Int.resColor(context: Context) = ResUtils.getColor(context, this)

inline val Int.resDrawable
    get() = ResUtils.getDrawable(this)

inline fun Int.resDrawable(context: Context) = ResUtils.getDrawable(context, this)

inline val Int.resString
    get() = ResUtils.getString(this)

inline fun Int.resString(context: Context) = ResUtils.getString(context, this)

inline val Int.resDimension
    get() = ResUtils.getDimension(this)

inline fun Int.resDimension(context: Context) = ResUtils.getDimension(context, this)

/**
 * Toast
 */

inline fun String.toast(duration: Int = ToastUtils.LENGTH_LONG) = ToastUtils.show(this, duration)
inline fun String.infoToast() = SuperToast.showInfoMessage(this)
inline fun String.errorToast() = SuperToast.showErrorMessage(this)
inline fun String.confirmToast() = SuperToast.showConfirmMessage(this)
inline fun String.warningToast() = SuperToast.showWarningMessage(this)