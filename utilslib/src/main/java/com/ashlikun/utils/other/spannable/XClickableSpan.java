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
    private boolean colorIsSet = false;

    public XClickableSpan(int color) {
        this.color = color;
        colorIsSet = true;
    }

    public XClickableSpan() {
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        if (colorIsSet) {
            ds.setColor(color);
        }
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
