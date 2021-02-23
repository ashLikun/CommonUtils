package com.ashlikun.utils.other.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/3　16:21
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：点击事件Span
 */
public abstract class XClickableSpan extends ClickableSpan {

    private int color = 0;
    private boolean underlineText = false;
    private boolean colorIsSet = false;

    public XClickableSpan(int color) {
        this.color = color;
        colorIsSet = true;
    }

    public XClickableSpan() {
    }

    public XClickableSpan(boolean underlineText) {
        this.underlineText = underlineText;
    }

    public XClickableSpan(int color, boolean underlineText) {
        this.color = color;
        this.underlineText = underlineText;
        colorIsSet = true;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (colorIsSet) {
            //设定的是span超链接的文本颜色，而不是点击后的颜色，点击后的背景颜色(HighLightColor)属于TextView的属性，Android4.0以上默认是淡绿色，低版本的是黄色。
            ds.setColor(color);
        }
        ds.setUnderlineText(underlineText);
        ds.clearShadowLayer();
    }
}
