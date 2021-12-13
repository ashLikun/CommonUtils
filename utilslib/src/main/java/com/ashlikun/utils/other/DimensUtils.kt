package com.ashlikun.utils.other

import android.content.Context
import com.ashlikun.utils.AppUtils.app

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:06
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：尺寸转换类
 */

object DimensUtils {
    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:25
     *
     *
     * 方法功能：将px值转换为dip或dp值，保证尺寸大小不变
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:25
     *
     *
     * 方法功能：将dip或dp值转换为px值，保证尺寸大小不变
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:26
     *
     *
     * 方法功能：将px值转换为sp值，保证文字大小不变
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:26
     *
     *
     * 方法功能：将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:25
     *
     *
     * 方法功能：将px值转换为dip或dp值，保证尺寸大小不变
     */
    fun px2dip(pxValue: Float): Int {
        val scale = app.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:25
     *
     *
     * 方法功能：将dip或dp值转换为px值，保证尺寸大小不变
     */
    fun dip2px(dipValue: Float): Int {
        val scale = app.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:26
     *
     *
     * 方法功能：将px值转换为sp值，保证文字大小不变
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = app.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:26
     *
     *
     * 方法功能：将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(spValue: Float): Int {
        val fontScale = app.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}