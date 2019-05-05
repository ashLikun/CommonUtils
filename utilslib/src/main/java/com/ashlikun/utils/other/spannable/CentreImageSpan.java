package com.ashlikun.utils.other.spannable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/3　16:06
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：居中对齐的Image
 */
public class CentreImageSpan extends ImageSpan {
    /**
     * 是否改变大小和文字一样大
     */
    protected boolean isChangSizeToText = false;
    protected boolean isChangOk = false;
    /**
     * 行间距
     */
    protected float lineSpacingExtra;

    /**
     * 0：居中
     * 1:上
     * 2：下
     */
    int imageAlign;

    public CentreImageSpan(Drawable d) {
        super(d);
    }

    public void setImageAlign(int imageAlign) {
        this.imageAlign = imageAlign;
    }

    public void setChangSizeToText(boolean changSizeToText) {
        isChangSizeToText = changSizeToText;
    }

    public void setLineSpacingExtra(float lineSpacingExtra) {
        this.lineSpacingExtra = lineSpacingExtra;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            if (isChangSizeToText && !isChangOk) {
                float drawWidth = d.getMinimumWidth();
                float drawHeight = d.getMinimumHeight();
                int fmsize = (int) Math.ceil(fm.descent - fm.top);
                if (fmsize <= 0) {
                    fmsize = (int) Math.ceil(fmPaint.descent - fmPaint.top);
                }
                if (fmsize > 0) {
                    int newHeight = (int) ((fmsize + 2) * 0.7);
                    d.setBounds(0, 0, (int) (newHeight / drawHeight * drawWidth), newHeight);
                }
            }

            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;
            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        if (imageAlign == 0) {
            //居中
            transY = (int) (((bottom - top) - b.getBounds().bottom - lineSpacingExtra) / 2 + top);
        } else if (imageAlign == 1) {
            //上
            transY = paint.getFontMetricsInt().descent;
        } else if (imageAlign == 2) {
            //下
            transY = bottom - b.getBounds().bottom - paint.getFontMetricsInt().descent;
        }
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
