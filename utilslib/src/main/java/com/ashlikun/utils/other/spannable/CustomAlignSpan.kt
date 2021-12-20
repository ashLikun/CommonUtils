package com.ashlikun.utils.other.spannable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.TextPaint
import android.text.style.ReplacementSpan

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:33
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：居上对齐Span
 */
class CustomAlignSpan(
    var alignTopOffset: Float,
    var foregroundColor: Int?,
    var backgroundColor: Int?
) : ReplacementSpan() {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        var text = text
        text = text.subSequence(start, end)
        return paint.measureText(text.toString()).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        var text = text
        if (foregroundColor != null) {
            paint.color = foregroundColor!!
        }
        if (backgroundColor != null) {
            if (paint is TextPaint) {
                paint.bgColor = backgroundColor!!
            }
        }
        text = text.subSequence(start, end)
        val fm = paint.fontMetricsInt
        //此处重新计算y坐标，使字体对其
        canvas.drawText(
            text.toString(),
            x,
            y - (y + fm.descent + y + fm.ascent - (bottom + top)) + alignTopOffset,
            paint
        )
    }
}