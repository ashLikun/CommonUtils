package com.ashlikun.utils.ui.extend

import com.ashlikun.utils.other.DimensUtils

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