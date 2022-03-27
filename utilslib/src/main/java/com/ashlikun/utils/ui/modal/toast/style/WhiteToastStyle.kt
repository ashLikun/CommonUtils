package com.ashlikun.utils.ui.modal.toast.style

import com.ashlikun.utils.ui.modal.toast.style.BlackToastStyle
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import com.ashlikun.utils.other.DimensUtils

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 21:56
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：默认白色样式实现
 */

class WhiteToastStyle : BlackToastStyle() {
    override fun getTextColor(context: Context): Int {
        return 0XBB000000.toInt()
    }

    override fun getBackgroundDrawable(context: Context): Drawable {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(0XFFEAEAEA.toInt())
        // 设置圆角
        drawable.cornerRadius = DimensUtils.sp2px(context, 8f).toFloat()
        return drawable
    }
}