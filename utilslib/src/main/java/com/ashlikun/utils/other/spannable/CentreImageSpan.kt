package com.ashlikun.utils.other.spannable

import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Rect
import android.graphics.Canvas

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:31
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：居中对齐的Image
 */
class CentreImageSpan(d: Drawable) : ImageSpan(d) {
    /**
     * 是否改变大小和文字一样大
     */
    var isChangSizeToText = false
    var isChangOk = false

    /**
     * 0：居中
     * 1:上
     * 2：下
     */
    var imageAlign = 0
    override fun getSize(
        paint: Paint, text: CharSequence, start: Int, end: Int,
        fm: FontMetricsInt?
    ): Int {
        val d = drawable
        val rect = d.bounds
        if (fm != null) {
            val fmPaint = paint.fontMetricsInt
            if (isChangSizeToText && !isChangOk) {
                val drawWidth = d.minimumWidth.toFloat()
                val drawHeight = d.minimumHeight.toFloat()
                var fmsize = Math.ceil((fm.descent - fm.top).toDouble()).toInt()
                if (fmsize <= 0) {
                    fmsize = Math.ceil((fmPaint.descent - fmPaint.top).toDouble()).toInt()
                }
                if (fmsize > 0) {
                    val newHeight = ((fmsize + 2) * 0.7).toInt()
                    d.setBounds(0, 0, (newHeight / drawHeight * drawWidth).toInt(), newHeight)
                }
            }
            val fontHeight = fmPaint.bottom - fmPaint.top
            val drHeight = rect.bottom - rect.top
            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4
            fm.ascent = -bottom
            fm.top = -bottom
            fm.bottom = top
            fm.descent = top
        }
        return rect.right
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val d = drawable
        canvas.save()
        val fontMetricsInt = paint.fontMetricsInt
        val fontTop = y + fontMetricsInt.top
        val fontMetricsHeight = fontMetricsInt.bottom - fontMetricsInt.top
        val iconHeight = d.bounds.bottom - d.bounds.top
        var transY = 0
        if (imageAlign == 0) {
            //居中
            transY = fontTop + (fontMetricsHeight - iconHeight) / 2
        } else if (imageAlign == 1) {
            //上
            transY = paint.fontMetricsInt.descent
        } else if (imageAlign == 2) {
            //下
            transY = bottom - d.bounds.bottom - paint.fontMetricsInt.descent
        }
        canvas.translate(x, transY.toFloat())
        d.draw(canvas)
        canvas.restore()
    }
}