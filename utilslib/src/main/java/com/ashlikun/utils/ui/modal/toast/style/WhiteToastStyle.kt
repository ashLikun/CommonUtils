package com.ashlikun.utils.ui.modal.toast.style

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.ashlikun.utils.R
import com.ashlikun.utils.other.DimensUtils
import com.ashlikun.utils.ui.resources.ResUtils

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 21:56
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：默认白色样式实现
 */

class WhiteToastStyle : BlackToastStyle() {
    override fun getTextColor(context: Context): Int {
        return ResUtils.getColor(context, R.color.toast_white_text_color)
    }

    override fun getBackgroundDrawable(context: Context): Drawable {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(ResUtils.getColor(context, R.color.toast_white_bg_color))
        // 设置圆角
        drawable.cornerRadius = DimensUtils.dip2px(context, 10f).toFloat()
        return drawable
    }
}