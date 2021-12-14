package com.ashlikun.utils.ui.image

import android.R
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.*
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import com.ashlikun.utils.ui.extend.dp
import com.ashlikun.utils.ui.extend.resColor
import com.ashlikun.utils.ui.extend.resDrawable
import kotlin.math.max

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/14 21:52
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Drawable 常用的工具
 */
object DrawableUtils {
    /**
     * 获取ColorStateList ，，对TextView设置不同状态时其文字颜色。
     */
    fun createColorStateList(
        @ColorRes normal: Int,
        @ColorRes pressed: Int? = null,
        @ColorRes select: Int? = null,
        @ColorRes enable: Int? = null
    ): ColorStateList {
        val colors = mutableListOf<Int>()
        val states = arrayListOf<IntArray>()
        if (pressed != null) {
            states.add(intArrayOf(R.attr.state_pressed, R.attr.state_enabled))
            colors.add(pressed.resColor)
        }
        if (select != null) {
            states.add(intArrayOf(R.attr.state_selected))
            colors.add(select.resColor)
        }
        if (enable != null) {
            states.add(intArrayOf(-R.attr.state_enabled))
            colors.add(enable.resColor)
        }
        //默认的
        states.add(intArrayOf())
        colors.add(normal.resColor)
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    /**
     * @param normal:填充的颜色
     * @param strokeColorId:边框颜色
     * @param radius:圆角半径   dp
     * @param strokeW:边框宽度   dp
     * @param radiusPx:圆角半径   px
     * @param strokeWPx:边框宽度   px
     */
    fun getGradientDrawable(
        @ColorRes normal: Int,
        @ColorRes strokeColorId: Int? = null,
        radiusPx: Int = 0,
        strokeWPx: Int = 0,
        radius: Float = 0f,
        strokeW: Float = 0f,
    ) = GradientDrawable().apply {
        setColor(normal.resColor)
        if (strokeColorId != null) {
            setStroke(max(strokeW.dp, strokeWPx), strokeColorId.resColor)
        }
        cornerRadius = max(radius.dp.toFloat(), radiusPx.toFloat())
    }

    /**
     * @param normal:填充的颜色
     * @param strokeColorId:边框颜色
     * @param radius:圆角半径[左上，右上，右下，左下]或者8个值也可以4个值得一个 dp
     * @param radiusPx:圆角半径[左上，右上，右下，左下]或者8个值也可以4个值得一个 px
     * @param strokeWidthPx:边框宽度  px
     * @param strokeWidth:边框宽度  dp
     */
    fun getGradientDrawable(
        @ColorRes normal: Int,
        @ColorRes strokeColorId: Int? = null,
        radiusPx: IntArray = intArrayOf(),
        strokeWidthPx: Int = 0,
        radius: FloatArray = floatArrayOf(),
        strokeWidth: Float = 0f
    ) = GradientDrawable().apply {
        setColor(normal.resColor)
        if (strokeColorId != null) {
            setStroke(max(strokeWidth.dp, strokeWidthPx), strokeColorId.resColor)
        }
        if (radius.size == 8) {
            cornerRadii = radius.map { it.dp.toFloat() }.toFloatArray()
        } else if (radius.size == 4) {
            cornerRadii = floatArrayOf(
                radius[0].dp.toFloat(), radius[0].dp.toFloat(),
                radius[1].dp.toFloat(), radius[1].dp.toFloat(),
                radius[2].dp.toFloat(), radius[2].dp.toFloat(),
                radius[3].dp.toFloat(), radius[3].dp.toFloat()
            )
        }
        if (radiusPx.size == 8) {
            cornerRadii = radiusPx.map { it.toFloat() }.toFloatArray()
        } else if (radiusPx.size == 4) {
            cornerRadii = floatArrayOf(
                radiusPx[0].toFloat(), radiusPx[0].toFloat(),
                radiusPx[1].toFloat(), radiusPx[1].toFloat(),
                radiusPx[2].toFloat(), radiusPx[2].toFloat(),
                radiusPx[3].toFloat(), radiusPx[3].toFloat()
            )
        }
    }


    /**
     * 获取StateListDrawable实例
     *
     * @param normal  默认的资源
     * @param pressed 按下的资源
     * @param select 选择的资源
     * @param enabled 不可用的资源
     */
    fun getStateListDrawable(
        normal: Drawable,
        pressed: Drawable? = null,
        select: Drawable? = null,
        enabled: Drawable? = null
    ) = StateListDrawable().apply {
        addState(intArrayOf(R.attr.state_pressed, R.attr.state_enabled), pressed)
        addState(intArrayOf(R.attr.state_selected), select)
        addState(intArrayOf(-R.attr.state_enabled), enabled)
        addState(intArrayOf(), normal)
    }

    /**
     * 获取StateListDrawable实例
     *
     * @param normal  默认的资源
     * @param pressed 按下的资源
     * @param select 选择的资源
     * @param enabled 不可用的资源
     * @param strokeColorId  边框颜色
     * @param cornerRadius 圆角半径  DP
     * @param strokeWidth  边框宽度 dP
     */
    @SuppressLint("ResourceType")
    fun getStateListDrawable(
        @DrawableRes normal: Int,
        @DrawableRes pressed: Int? = null,
        @DrawableRes select: Int? = null,
        @DrawableRes enabled: Int? = null,
        @DrawableRes strokeColorId: Int,
        radiusPx: Int = 0,
        strokeWPx: Int = 0,
        radius: Float = 0f,
        strokeW: Float = 0f,
    ): StateListDrawable {
        val normal = getGradientDrawable(
            normal,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeWPx = strokeWPx,
            radius = radius,
            strokeW = strokeW
        )
        val pressed = if (pressed == null) null else getGradientDrawable(
            pressed,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeWPx = strokeWPx,
            radius = radius,
            strokeW = strokeW
        )
        val select = if (select == null) null else getGradientDrawable(
            select,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeWPx = strokeWPx,
            radius = radius,
            strokeW = strokeW
        )
        val enabled = if (enabled == null) null else getGradientDrawable(
            enabled,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeWPx = strokeWPx,
            radius = radius,
            strokeW = strokeW
        )
        return getStateListDrawable(
            normal = normal,
            pressed = pressed,
            select = select,
            enabled = enabled
        )
    }


    /**
     * 把drawable渲染成指定的颜色
     */
    fun getTintDrawable(drawable: Drawable, @ColorInt color: Int) =
        DrawableCompat.wrap(drawable).mutate().setTint(color)

    /**
     * 改变Drawable大小
     */
    fun changDrawSize(drawable: Drawable, width: Int, height: Int) =
        DrawableCompat.wrap(drawable).mutate().apply { setBounds(0, 0, width, height) }

    /**
     * 等比
     * 高度被设置了，那么久按照比例设置宽度
     */
    fun changDrawSizeWidth(drawable: Drawable, width: Int): Drawable {
        val drawWidth = drawable.minimumWidth.toFloat()
        val drawHeight = drawable.minimumHeight.toFloat()
        return changDrawSize(drawable, width, (width / drawWidth * drawHeight).toInt())
    }

    fun changDrawHeight(drawable: Drawable, height: Int): Drawable {
        //高度被设置了，那么久按照比例设置宽度
        val drawWidth = drawable.minimumWidth.toFloat()
        val drawHeight = drawable.minimumHeight.toFloat()
        return changDrawSize(drawable, (height / drawHeight * drawWidth).toInt(), height)
    }

    /**
     * 创建一个TextView的上下左右Drawable
     */
    fun createTextDraw(
        textView: TextView,
        @DrawableRes drawableId: Int? = null,
        /**
         * 左：1,上：2,右：3,下：4
         * 默认 右
         */
        location: Int = 3,
        width: Int = 0,
        height: Int = 0,
        tintColor: Int? = null,
        drawable: Drawable? = null,
    ) = TextDrawUtils(textView, drawableId, location, width, height, tintColor, drawable)

    fun createGradientDrawable(color: Int): BuilderGradient {
        return BuilderGradient(color)
    }
}