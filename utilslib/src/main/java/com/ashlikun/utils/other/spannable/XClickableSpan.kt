package com.ashlikun.utils.other.spannable

import android.text.style.ClickableSpan
import android.text.TextPaint

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/3　16:21
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：点击事件Span
 */
abstract class XClickableSpan(
    open var color: Int = 0,
    open var underlineText: Boolean = false,
    open var colorIsSet: Boolean = true
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        if (colorIsSet) {
            //设定的是span超链接的文本颜色，而不是点击后的颜色，点击后的背景颜色(HighLightColor)属于TextView的属性，Android4.0以上默认是淡绿色，低版本的是黄色。
            ds.color = color
        }
        ds.isUnderlineText = underlineText
        ds.clearShadowLayer()
    }
}