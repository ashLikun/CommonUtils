package com.ashlikun.utils.ui.shadow

import android.graphics.drawable.Drawable
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.view.View
import android.graphics.Color
import android.os.Build

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 16:10
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：阴影效果
 * 优点：使用setShadowLayer绘制阴影,效果好
 * 缺点：会生成bitmap占用空间,滚动的时候卡顿（关闭了硬件加速）
 */

class CanShadowDrawable(
    bgColor: Int,
    shadowRange: Float,
    shadowDx: Float,
    shadowDy: Float,
    shadowColor: Int
) : Drawable() {
    private val paint: Paint
    private val paintNoShadow: Paint
    private var drawRect: RectF

    //  圆角半径
    private var radius = 0
    private var corners = 0
    var offsetLeft = 0f
    var offsetTop = 0f
    var offsetRight = 0f
    var offsetBottom = 0f
    private var bitmap: Bitmap? = null
    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        if (bounds.right - bounds.left > 0 && bounds.bottom - bounds.top > 0) {
            val width = bounds.right - bounds.left
            val height = bounds.bottom - bounds.top
            drawRect = RectF(offsetLeft, offsetTop, width - offsetRight, height - offsetBottom)
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
            }
            drawBitmap()
            invalidateSelf()
        }
    }

    /**
     * 因为使用setShadowLayer 必须关闭硬件加速，所以这边只能绘制到bitmap上，然后再绘制到View上
     */
    private fun drawBitmap() {
        val canvas = Canvas(bitmap!!)
        canvas.drawRoundRect(
            drawRect,
            radius.toFloat(), radius.toFloat(),
            paint
        )
        if (corners == 0) {
            corners = CORNER_ALL
        }
        val notRoundedCorners = corners xor CORNER_ALL
        //哪个角不是圆角我再把你用矩形画出来
        if (notRoundedCorners and CORNER_TOP_LEFT != 0) {
            canvas.drawRect(
                offsetLeft,
                offsetTop,
                radius + offsetLeft,
                radius + offsetTop,
                paintNoShadow
            )
        }
        if (notRoundedCorners and CORNER_TOP_RIGHT != 0) {
            canvas.drawRect(
                drawRect.right - radius,
                offsetTop,
                drawRect.right,
                radius + offsetTop,
                paintNoShadow
            )
        }
        if (notRoundedCorners and CORNER_BOTTOM_LEFT != 0) {
            canvas.drawRect(
                offsetLeft,
                drawRect.bottom - radius,
                radius + offsetLeft,
                drawRect.bottom,
                paintNoShadow
            )
        }
        if (notRoundedCorners and CORNER_BOTTOM_RIGHT != 0) {
            canvas.drawRect(
                drawRect.right - radius,
                drawRect.bottom - radius,
                drawRect.right,
                drawRect.bottom,
                paintNoShadow
            )
        }
    }

    fun setCorners(corners: Int) {
        this.corners = corners
    }

    override fun draw(canvas: Canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        }
    }

    fun getRadius(): Float {
        return radius.toFloat()
    }

    fun setRadius(radius: Int) {
        this.radius = radius
    }

    fun setColor(color: Int): CanShadowDrawable {
        paint.color = color
        return this
    }

    override fun setAlpha(i: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    class Builder(private val view: View) {
        //      paddingLeft
        private var isSetPadding = false
        private var offsetLeft = 0f

        //        paddingTop
        private var offsetTop = 0f

        //        paddingRight
        private var offsetRight = 0f

        //        paddingBottom
        private var offsetBottom = 0f

        //       圆角半径
        private var radius = 0
        private var corners = 0

        //       阴影范围
        private var shadowRange = 0f

        //       阴影x轴偏移
        private var shadowDx = 0f

        //        阴影y轴偏移
        private var shadowDy = 0f

        //        阴影颜色
        private var shadowColor = Color.GRAY

        //        背景颜色
        private var bgColor = Color.WHITE

        /**
         * PaddingLeft
         */
        fun offsetLeft(offsetLeft: Float): Builder {
            this.offsetLeft = offsetLeft
            isSetPadding = true
            return this
        }

        /**
         * PaddingTop
         */
        fun offsetTop(offsetTop: Float): Builder {
            this.offsetTop = offsetTop
            isSetPadding = true
            return this
        }

        /**
         * PaddingRight
         */
        fun offsetRight(offsetRight: Float): Builder {
            this.offsetRight = offsetRight
            isSetPadding = true
            return this
        }

        /**
         * PaddingBottom
         */
        fun offsetBottom(offsetBottom: Float): Builder {
            this.offsetBottom = offsetBottom
            isSetPadding = true
            return this
        }

        /**
         * 指定圆角
         */
        fun corners(corners: Int): Builder {
            this.corners = corners
            return this
        }

        /**
         * 圆角半径
         */
        fun radius(radius: Int): Builder {
            this.radius = radius
            return this
        }

        /**
         * 阴影范围
         */
        fun shadowRange(shadowRange: Float): Builder {
            this.shadowRange = shadowRange
            return this
        }

        /**
         * 阴影x轴偏移
         */
        fun shadowDx(shadowDx: Float): Builder {
            this.shadowDx = shadowDx
            return this
        }

        /**
         * 阴影y轴偏移
         */
        fun shadowDy(shadowDy: Float): Builder {
            this.shadowDy = shadowDy
            return this
        }

        /**
         * 阴影颜色
         */
        fun shadowColor(shadowColor: Int): Builder {
            this.shadowColor = shadowColor
            return this
        }

        /**
         * 背景颜色
         */
        fun bgColor(bgColor: Int): Builder {
            this.bgColor = bgColor
            return this
        }

        fun create(): CanShadowDrawable {
            val shadowViewDrawable =
                CanShadowDrawable(bgColor, shadowRange, shadowDx, shadowDy, shadowColor)
            if (offsetLeft == 0f) {
                offsetLeft = view.paddingLeft.toFloat()
            }
            if (offsetTop == 0f) {
                offsetTop = view.paddingTop.toFloat()
            }
            if (offsetRight == 0f) {
                offsetRight = view.paddingRight.toFloat()
            }
            if (offsetBottom == 0f) {
                offsetBottom = view.paddingBottom.toFloat()
            }
            if (isSetPadding) {
                view.setPadding(
                    view.paddingLeft + offsetLeft.toInt(),
                    view.paddingTop + offsetTop.toInt(),
                    view.paddingRight + offsetRight.toInt(),
                    view.paddingBottom + offsetBottom.toInt()
                )
            }
            shadowViewDrawable.offsetLeft = offsetLeft
            shadowViewDrawable.offsetTop = offsetTop
            shadowViewDrawable.offsetBottom = offsetBottom
            shadowViewDrawable.offsetRight = offsetRight
            shadowViewDrawable.setRadius(radius)
            shadowViewDrawable.setCorners(corners)
            view.background = shadowViewDrawable
            return shadowViewDrawable
        }

        companion object {
            fun on(view: View): Builder {
                return Builder(view)
            }
        }
    }

    companion object {
        const val CORNER_TOP_LEFT = 1
        const val CORNER_TOP_RIGHT = 1 shl 1
        const val CORNER_BOTTOM_LEFT = 1 shl 2
        const val CORNER_BOTTOM_RIGHT = 1 shl 3
        const val CORNER_ALL =
            CORNER_TOP_LEFT or CORNER_TOP_RIGHT or CORNER_BOTTOM_LEFT or CORNER_BOTTOM_RIGHT
    }

    init {
        paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.style = Paint.Style.FILL
        paint.color = bgColor
        //      设置阴影
        paint.setShadowLayer(shadowRange, shadowDx, shadowDy, shadowColor)
        paintNoShadow = Paint()
        paintNoShadow.isAntiAlias = true
        paintNoShadow.isFilterBitmap = true
        paintNoShadow.isDither = true
        paintNoShadow.style = Paint.Style.FILL
        paintNoShadow.color = bgColor
        drawRect = RectF()
    }
}