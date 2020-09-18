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
inline fun Int.dp() = DimensUtils.dip2px(this.toFloat())
inline fun Float.dp() = DimensUtils.dip2px(this)

inline fun Int.sp() = DimensUtils.sp2px(this.toFloat())
inline fun Float.sp() = DimensUtils.sp2px(this)

inline fun Int.px2dip() = DimensUtils.px2dip(this.toFloat())
inline fun Float.px2dip() = DimensUtils.px2dip(this)

inline fun Int.px2sp() = DimensUtils.px2sp(this.toFloat())
inline fun Float.px2sp() = DimensUtils.px2sp(this)

/**
 * ResUtils
 */
inline fun Int.resColor(context: Context? = null) = if (context == null) ResUtils.getColor(this) else ResUtils.getColor(context, this)
inline fun Int.resDrawable(context: Context? = null) = if (context == null) ResUtils.getDrawable(this) else ResUtils.getDrawable(context, this)
inline fun Int.resString(context: Context? = null) = if (context == null) ResUtils.getString(this) else ResUtils.getString(context, this)
inline fun Int.resDimension(context: Context? = null) = if (context == null) ResUtils.getDimension(this) else ResUtils.getDimension(context, this)

/**
 * Toast
 */

inline fun String.toast(duration: Int = ToastUtils.LENGTH_LONG) = ToastUtils.show(this, duration)
inline fun String.infoToast() = SuperToast.showInfoMessage(this)
inline fun String.errorToast() = SuperToast.showErrorMessage(this)
inline fun String.confirmToast() = SuperToast.showConfirmMessage(this)
inline fun String.warningToast() = SuperToast.showWarningMessage(this)