package com.ashlikun.utils.ui.shadow

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.ColorUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 16:11
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：渐变形式实现阴影，性能好，但是效果有一点点差距
 */

class RoundShadowDrawable(
    shadowColor: Int,
    bgColor: Int,
    radius: Float, shadowSize: Float
) : Drawable() {
    // 额外的阴影，以避免卡和阴影之间的差距
    private val minShadowSize = 10f
    private val bounds: RectF
    private val shadowCornerPaint: Paint
    private val shadowEdgePaint: Paint
    private var bgPaint: Paint? = null

    //小的真实的半径
    private var cornerRadius: Float

    //大的半径
    var shadowRadius = 0f
        private set
    private var cornerShadowPath: Path? = null

    //3个颜色数组
    private val shadowColors: IntArray
    private var dirty = true
    override fun setAlpha(alpha: Int) {
        shadowCornerPaint.alpha = alpha
        shadowEdgePaint.alpha = alpha
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        dirty = true
    }

    fun configShadowSize(cornerRadius: Float, shadowSize: Float) {
        var shadowSize = shadowSize
        require(shadowSize >= 0f) {
            "Invalid shadow size " + shadowSize +
                    ". Must be >= 0"
        }
        if (shadowSize < minShadowSize) {
            shadowSize = minShadowSize
        }
        shadowRadius = shadowSize + cornerRadius
        dirty = true
        invalidateSelf()
    }

    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun draw(canvas: Canvas) {
        if (dirty) {
            buildComponents(getBounds())
            dirty = false
        }
        drawShadow(canvas)
    }

    private fun buildComponents(bounds: Rect) {
        this.bounds[bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat()] =
            bounds.bottom.toFloat()
        buildShadowCorners()
    }

    private fun buildShadowCorners() {
        val cornerRecf = RectF(
            shadowRadius - cornerRadius, shadowRadius - cornerRadius,
            shadowRadius + cornerRadius, shadowRadius + cornerRadius
        )
        val shadowCornerRecf = RectF(0f, 0f, 2 * cornerRadius, 2 * cornerRadius)
        if (cornerShadowPath == null) {
            cornerShadowPath = Path()
        } else {
            cornerShadowPath!!.reset()
        }
        cornerShadowPath!!.setFillType(Path.FillType.EVEN_ODD)
        if (cornerRadius == 0f) {
            //没有圆角
            cornerShadowPath!!.addRect(0f, 0f, shadowRadius, shadowRadius, Path.Direction.CW)
        } else {
            cornerShadowPath!!.moveTo(shadowRadius - cornerRadius, shadowRadius)
            // 内半圆，如果cornerRadius 为零时，将绘制不出阴影的内圆角
            cornerShadowPath!!.arcTo(cornerRecf, 180f, 90f, false)
            cornerShadowPath!!.lineTo(shadowRadius, 0f)
            cornerShadowPath!!.lineTo(cornerRadius, 0f)
            // 外半圆，始终都会绘制shadow的圆角
            cornerShadowPath!!.arcTo(shadowCornerRecf, 270f, -90f, false)
            cornerShadowPath!!.lineTo(0f, shadowRadius)
            cornerShadowPath!!.lineTo(shadowRadius - cornerRadius, shadowRadius)
        }
        cornerShadowPath!!.close()
        val startPosition = cornerRadius / shadowRadius
        shadowCornerPaint.shader = RadialGradient(
            shadowRadius, shadowRadius,
            shadowRadius, shadowColors, floatArrayOf(0f, startPosition, 1f), Shader.TileMode.CLAMP
        )
        shadowEdgePaint.shader = LinearGradient(
            shadowRadius, shadowRadius,
            shadowRadius, 0f,
            shadowColors, floatArrayOf(0f, startPosition, 1f), Shader.TileMode.CLAMP
        )
        shadowEdgePaint.isAntiAlias = false
    }

    private fun drawShadow(canvas: Canvas) {
        val drawHorizontalEdges = bounds.width() - 2 * shadowRadius > 0
        val drawVerticalEdges = bounds.height() - 2 * shadowRadius > 0
        //背景
        if (bgPaint != null) {
            if (drawHorizontalEdges || drawVerticalEdges) {
                val shadowSize = shadowRadius - cornerRadius
                val rectF = RectF(
                    shadowSize,
                    shadowSize,
                    bounds.width() - shadowSize,
                    bounds.height() - shadowSize
                )
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, bgPaint!!)
            }
        }
        // 上边
        var saved = canvas.save()
        canvas.drawPath(cornerShadowPath!!, shadowCornerPaint)
        if (drawHorizontalEdges) {
            canvas.drawRect(
                shadowRadius, 0f,
                bounds.width() - shadowRadius, shadowRadius - cornerRadius,
                shadowEdgePaint
            )
        }
        canvas.restoreToCount(saved)
        // 下边
        saved = canvas.save()
        canvas.translate(bounds.right, bounds.bottom)
        canvas.rotate(180f)
        canvas.drawPath(cornerShadowPath!!, shadowCornerPaint)
        if (drawHorizontalEdges) {
            canvas.drawRect(
                shadowRadius, 0f,
                bounds.width() - shadowRadius, shadowRadius - cornerRadius,
                shadowEdgePaint
            )
        }
        canvas.restoreToCount(saved)
        // 左边
        saved = canvas.save()
        canvas.translate(bounds.left, bounds.bottom)
        canvas.rotate(270f)
        canvas.drawPath(cornerShadowPath!!, shadowCornerPaint)
        if (drawVerticalEdges) {
            canvas.drawRect(
                shadowRadius, 0f,
                bounds.height() - shadowRadius, shadowRadius - cornerRadius,
                shadowEdgePaint
            )
        }
        canvas.restoreToCount(saved)
        // 右边
        saved = canvas.save()
        canvas.translate(bounds.right, bounds.top)
        canvas.rotate(90f)
        canvas.drawPath(cornerShadowPath!!, shadowCornerPaint)
        if (drawVerticalEdges) {
            canvas.drawRect(
                shadowRadius, 0f,
                bounds.height() - shadowRadius, shadowRadius - cornerRadius,
                shadowEdgePaint
            )
        }
        canvas.restoreToCount(saved)
    }

    fun getCornerRadius(): Float {
        return cornerRadius
    }

    fun setCornerRadius(radius: Float) {
        var radius = radius
        require(radius >= 0f) { "Invalid radius $radius. Must be >= 0" }
        radius = (radius + .5f)
        if (cornerRadius == radius) {
            return
        }
        cornerRadius = radius
        dirty = true
        invalidateSelf()
    }

    fun getMaxShadowAndCornerPadding(into: Rect?) {
        getPadding(into!!)
    }

    fun setShadowSize(size: Float) {
        configShadowSize(cornerRadius, size)
    }

    val minWidth: Float
        get() {
            val content = 2 *
                    Math.max(shadowRadius, cornerRadius + minShadowSize + shadowRadius / 2)
            return content + (shadowRadius + minShadowSize) * 2
        }
    val minHeight: Float
        get() {
            val content = 2 * Math.max(
                shadowRadius, cornerRadius + minShadowSize
                        + shadowRadius / 2
            )
            return content + (shadowRadius + minShadowSize) * 2
        }

    init {
        shadowColors = intArrayOf(
            ColorUtils.setAlphaComponent(shadowColor, 220),
            ColorUtils.setAlphaComponent(shadowColor, 121),
            ColorUtils.setAlphaComponent(shadowColor, 0)
        )
        shadowCornerPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        shadowCornerPaint.style = Paint.Style.FILL
        cornerRadius = radius
        bounds = RectF()
        shadowEdgePaint = Paint(shadowCornerPaint)
        shadowEdgePaint.isAntiAlias = false
        if (bgColor != Color.TRANSPARENT) {
            bgPaint = Paint(shadowCornerPaint)
            bgPaint?.isAntiAlias = true
            bgPaint?.color = bgColor
        }
        configShadowSize(radius, shadowSize)
    }
}