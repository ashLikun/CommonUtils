package com.ashlikun.utils.other.spannable

import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/3　16:21
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：点击事件Span
 * 点击后的背景颜色(HighLightColor)属于TextView的属性，Android4.0以上默认是淡绿色，低版本的是黄色。
 */
abstract class XClickableSpan(
    //文字颜色
    open var color: Int? = null,
    //下划线
    open var underlineText: Boolean = false,
) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        if (color != null) {
            //设定的是span超链接的文本颜色，而不是点击后的颜色，点击后的背景颜色(HighLightColor)属于TextView的属性，Android4.0以上默认是淡绿色，低版本的是黄色。
            ds.color = color!!
        }
        ds.isUnderlineText = underlineText
        ds.clearShadowLayer()
    }
}