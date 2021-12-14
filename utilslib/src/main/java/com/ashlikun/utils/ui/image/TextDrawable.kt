package com.ashlikun.utils.ui.image

import com.ashlikun.utils.other.DimensUtils.dip2px
import com.ashlikun.utils.AppUtils.app
import android.graphics.drawable.GradientDrawable
import android.graphics.Paint
import com.ashlikun.utils.other.DimensUtils
import com.ashlikun.utils.AppUtils
import android.text.TextUtils
import android.graphics.Canvas
import android.graphics.Paint.FontMetricsInt
import com.ashlikun.utils.ui.image.TextDrawable
import androidx.annotation.ColorInt

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/14 23:19
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：显示文字的GradientDrawable, 显示背景，边框，圆角，文字
 */

class TextDrawable : GradientDrawable {
    protected var text: String? = null
    protected var textSize = 0f
    protected var textColor = 0
    protected var paint: Paint? = null
    protected var paddingLeft = 0
    protected var paddingTop = 0
    protected var paddingRight = 0
    protected var paddingBottom = 0
    protected var stokeWidth = 0

    constructor() : super() {
        init()
    }

    constructor(orientation: Orientation, colors: IntArray) : super(orientation, colors) {
        init()
    }

    private fun init() {
        paddingLeft = dip2px(app, 3f)
        paddingRight = dip2px(app, 3f)
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.FILL
        paint!!.textAlign = Paint.Align.CENTER
        setTextSize(12f)
    }

    private fun setDrawableSize() {
        if (TextUtils.isEmpty(text)) {
            setSize(0, 0)
            setBounds(0, 0, 0, 0)
            return
        }
        val textWidth = paint!!.measureText(text)
        val textHeiht = (paint!!.fontMetricsInt.bottom - paint!!.fontMetricsInt.top).toFloat()
        val width = (textWidth + paddingLeft + paddingRight + stokeWidth * 2).toInt()
        val height = (textHeiht + paddingTop + paddingBottom + stokeWidth * 2).toInt()
        setSize(width, height)
        setBounds(0, 0, width, height)
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        super.draw(canvas)
        val fontMetrics = paint!!.fontMetricsInt
        val baseline = (intrinsicHeight - fontMetrics.ascent - fontMetrics.descent) / 2
        canvas.drawText(
            text!!,
            0,
            text!!.length,
            (intrinsicWidth / 2).toFloat(),
            baseline.toFloat(),
            paint!!
        )
    }

    fun setText(text: String?): TextDrawable {
        this.text = text
        setDrawableSize()
        return this
    }

    fun setTextColor(textColor: Int): TextDrawable {
        this.textColor = textColor
        paint!!.color = this.textColor
        setDrawableSize()
        return this
    }

    fun setPadding(paddingLeftDp: Float, paddingRightDp: Float): TextDrawable {
        //本身字体上下已经有一点空白了,这里就不要设置了
        return setPadding(paddingLeftDp, 0f, paddingRightDp, 0f)
    }

    /**
     * @param paddingLeftDp
     * @param paddingTopDp    本身字体上下已经有一点空白了，注意
     * @param paddingRightDp
     * @param paddingBottomDp 本身字体上下已经有一点空白了，注意
     * @return
     */
    fun setPadding(
        paddingLeftDp: Float,
        paddingTopDp: Float,
        paddingRightDp: Float,
        paddingBottomDp: Float
    ): TextDrawable {
        paddingLeft = dip2px(app, paddingLeftDp)
        paddingTop = dip2px(app, paddingTopDp)
        paddingRight = dip2px(app, paddingRightDp)
        paddingBottom = dip2px(app, paddingBottomDp)
        setDrawableSize()
        return this
    }

    fun setTextSize(textSizeDp: Float): TextDrawable {
        textSize = dip2px(app, textSizeDp).toFloat()
        paint!!.textSize = textSize
        setDrawableSize()
        return this
    }

    /**
     * 设置边框
     *
     * @param widthDp
     * @param color
     */
    fun setStrokeNew(widthDp: Float, @ColorInt color: Int): TextDrawable {
        stokeWidth = dip2px(app, widthDp)
        setStroke(stokeWidth, color)
        return this
    }

    fun setColorNew(argb: Int): TextDrawable {
        super.setColor(argb)
        return this
    }

    fun setColorsNew(colors: IntArray?): TextDrawable {
        super.setColors(colors)
        return this
    }

    fun setCornerRadiusNew(radiusDp: Float): TextDrawable {
        super.setCornerRadius(dip2px(app, radiusDp).toFloat())
        return this
    }

    /**
     * 设置8个方向的角标
     *
     * @param radiiDp
     * @return
     */
    fun setCornerRadiuNew(radiiDp: FloatArray): TextDrawable {
        //转成dp
        for (i in radiiDp.indices) {
            radiiDp[i] = dip2px(app, radiiDp[i]).toFloat()
        }
        super.setCornerRadii(radiiDp)
        return this
    }
}