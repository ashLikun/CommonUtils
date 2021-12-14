package com.ashlikun.utils.ui.image

import android.graphics.drawable.GradientDrawable

/**
 * 作者　　: 李坤
 * 创建时间: 2021/12/14　23:17
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class BuilderGradient(color: Int) {
    var fillColor: Int?
    var strokeColor: Int? = null
    var roundRadiu: FloatArray? = null
    var roundRadius = 0f
    var strokeWidth = 0
    fun strokeColor(color: Int): BuilderGradient {
        strokeColor = color
        return this
    }

    /**
     * 圆角半径[左上，右上，右下，左下]或者8个值也可以4个值得一个 dp
     */
    fun roundRadiu(roundRadiu: FloatArray): BuilderGradient {
        this.roundRadiu = roundRadiu
        return this
    }

    /**
     * 圆角半径[左上，右上，右下，左下]或者8个值也可以4个值得一个 dp
     */
    fun roundRadiu(roundRadiu: IntArray): BuilderGradient {
        this.roundRadiu = FloatArray(roundRadiu.size)
        for (i in roundRadiu.indices) {
            this.roundRadiu!![i] = roundRadiu[i].toFloat()
        }
        return this
    }

    fun roundRadius(roundRadius: Float): BuilderGradient {
        this.roundRadius = roundRadius
        return this
    }

    fun strokeWidth(strokeWidth: Int): BuilderGradient {
        this.strokeWidth = strokeWidth
        return this
    }

    fun create(): GradientDrawable {
        val drawable = GradientDrawable()
        if (fillColor != null) {
            drawable.setColor(fillColor!!)
        }
        if (strokeColor != null) {
            drawable.setStroke(strokeWidth, strokeColor!!)
        }
        if (roundRadiu != null && roundRadiu!!.size >= 8) {
            drawable.cornerRadii = roundRadiu
        } else if (roundRadiu != null && roundRadiu!!.size == 4) {
            val round = floatArrayOf(
                roundRadiu!![0], roundRadiu!![0],
                roundRadiu!![1], roundRadiu!![1],
                roundRadiu!![2], roundRadiu!![2],
                roundRadiu!![3], roundRadiu!![3]
            )
            drawable.cornerRadii = round
        } else {
            drawable.cornerRadius = roundRadius
        }
        return drawable
    }

    init {
        fillColor = color
    }
}