package com.ashlikun.utils.other.spannable;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/3　16:20
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：居上对齐Span
 */
public class CustomAlignSpan extends ReplacementSpan {

    float alignTopOffset;

    public CustomAlignSpan(float alignTopOffset) {
        this.alignTopOffset = alignTopOffset;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        return (int) paint.measureText(text.toString());
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        text = text.subSequence(start, end);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        //此处重新计算y坐标，使字体对其
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) - (bottom + top)) + alignTopOffset, paint);
    }

}
