package com.ashlikun.utils.simple

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.ashlikun.pathanim.PathAnimHelper
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.svg.SvgUtils
import com.ashlikun.utils.ui.extend.dp

/**
 * 作者　　: 李坤
 * 创建时间: 2023/11/14　16:19
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class MyView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    val svgData by lazy {
        SvgUtils.getVectorData(R.drawable.ic_car)!!
    }

    //默认的画笔
    var paintDefault = Paint().apply {
        isAntiAlias = true
//        strokeWidth = 2.toFloat()
//        style = Paint.Style.STROKE
    }
    var animPath: Path? = null
    val anim by lazy {
        PathAnimHelper(svgData.path, animTime = 200) { pathMeasure, animPath, progress ->
            this.animPath = animPath
            invalidate()
        }
    }
    var animValue: Float = 0f

    init {
        ValueAnimator.ofFloat(0f, 0.5f).apply {
            addUpdateListener {
                animValue = it.animatedValue as Float
                invalidate()
            }
            interpolator = LinearInterpolator()
            duration = 500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }
    }

    var motorAnim = false
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        motorAnim = false
        LogUtils.e("gggggggggggg${svgData}")
//        LogUtils.e("gggggggggggg${svgData.scale}")
        svgData.pathDatas.forEachIndexed { index, it ->
            canvas.save()
            canvas.translate(width / 2f - svgData.viewportWidth / 2, height / 2f - svgData.viewportHeight / 2)
            canvas.scale(svgData.scale.first, svgData.scale.second, svgData.viewportWidth / 2, svgData.viewportWidth / 2)
            it.path?.let { path ->
                paintDefault.color = it.fillColor
//                    paintDefault.setShadowLayer(shadowRadius, 0f, 0f, tintColor)
                if (it.name.startsWith("motor")) {
                    if (!motorAnim) {
//                        motorAnim = true
                        canvas.scale(1 + animValue, 1 + animValue, 153.24f, 123.99f)
                    }
                }
                canvas.drawPath(path, paintDefault)

            }
            canvas.restore()
        }
//        animPath?.let { canvas.drawPath(it, paintDefault) }

    }
}