package com.ashlikun.utils.ui.image

import android.R
import android.content.res.ColorStateList
import android.graphics.drawable.*
import android.view.View
import android.widget.ImageView
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
inline fun Drawable.toBitmap() = BitmapUtil.drawableToBitmap(this)
inline fun TextView.setColorStateList(
    @ColorRes normalId: Int? = null,
    @ColorRes selectId: Int? = null,
    @ColorRes pressedId: Int? = null,
    @ColorRes enableId: Int? = null,
    @ColorRes focusedId: Int? = null,
    @ColorRes checkedId: Int? = null,
    @ColorInt normal: Int? = null,
    @ColorInt select: Int? = null,
    @ColorInt pressed: Int? = null,
    @ColorInt enable: Int? = null,
    @ColorInt focused: Int? = null,
    @ColorInt checked: Int? = null
) {
    setTextColor(
        DrawableUtils.createColorStateList(
            normal = normal,
            select = select,
            pressed = pressed,
            enable = enable,
            focused = focused,
            checked = checked,
            normalId = normalId,
            selectId = selectId,
            pressedId = pressedId,
            enableId = enableId,
            focusedId = focusedId,
            checkedId = checkedId
        )
    )
}


inline fun View.setGradientDrawable(
    @ColorInt normal: Int? = null,
    @ColorInt strokeColor: Int? = null,
    @ColorRes normalId: Int? = null,
    @ColorRes strokeColorId: Int? = null,
    strokeSizePx: Int = 0,
    strokeSize: Float = 0f,
    radius: Float = 0f,
    radiusPx: Int = 0,
    radiusArr: FloatArray? = null,
    radiusArrPx: IntArray? = null,
    //是否必须background
    mustBackground: Boolean = true
) {
    val bg = DrawableUtils.getGradientDrawable(
        normal = normal,
        strokeColor = strokeColor,
        normalId = normalId,
        strokeColorId = strokeColorId,
        strokeSizePx = strokeSizePx,
        strokeSize = strokeSize,
        radius = radius,
        radiusPx = radiusPx,
        radiusArr = radiusArr,
        radiusArrPx = radiusArrPx,
    )
    if (!mustBackground && this is ImageView) setImageDrawable(bg)
    else background = bg
}

inline fun View.setStateListDrawable(
    normal: Drawable,
    select: Drawable? = null,
    pressed: Drawable? = null,
    enabled: Drawable? = null,
    focused: Drawable? = null,
    checked: Drawable? = null,
    //是否必须background
    mustBackground: Boolean = true
) {
    val bg = DrawableUtils.getStateListDrawable(
        normal = normal, select = select,
        pressed = pressed, enabled = enabled, focused = focused, checked = checked
    )
    if (!mustBackground && this is ImageView) setImageDrawable(bg)
    else background = bg

}

inline fun View.setStateListDrawable(
    @DrawableRes normalId: Int,
    @DrawableRes selectId: Int? = null,
    @DrawableRes pressedId: Int? = null,
    @DrawableRes enabledId: Int? = null,
    @DrawableRes focusedId: Int? = null,
    @DrawableRes checkedId: Int? = null,
    //是否必须background
    mustBackground: Boolean = true
) {
    val bg = DrawableUtils.getStateListDrawable(
        normalId = normalId, selectId = selectId,
        pressedId = pressedId, enabledId = enabledId,
        focusedId = focusedId, checkedId = checkedId,
    )
    if (!mustBackground && this is ImageView) setImageDrawable(bg)
    else background = bg
}

object DrawableUtils {
    /**
     * 获取ColorStateList ，，对TextView设置不同状态时其文字颜色。
     */
    fun createColorStateList(
        @ColorRes normalId: Int? = null,
        @ColorRes selectId: Int? = null,
        @ColorRes pressedId: Int? = null,
        @ColorRes enableId: Int? = null,
        @ColorRes focusedId: Int? = null,
        @ColorRes checkedId: Int? = null,
        @ColorInt normal: Int? = null,
        @ColorInt select: Int? = null,
        @ColorInt pressed: Int? = null,
        @ColorInt enable: Int? = null,
        @ColorInt focused: Int? = null,
        @ColorInt checked: Int? = null
    ): ColorStateList {
        val colors = mutableListOf<Int>()
        val states = arrayListOf<IntArray>()
        if (pressed != null || pressedId != null) {
            states.add(intArrayOf(R.attr.state_pressed, R.attr.state_enabled))
            colors.add(pressed ?: pressedId?.resColor!!)
        }
        if (select != null || selectId != null) {
            states.add(intArrayOf(R.attr.state_selected))
            colors.add(select ?: selectId?.resColor!!)
        }
        if (enable != null || enableId != null) {
            states.add(intArrayOf(-R.attr.state_enabled))
            colors.add(enable ?: enableId?.resColor!!)
        }
        if (focused != null || focusedId != null) {
            states.add(intArrayOf(R.attr.state_focused))
            colors.add(focused ?: focusedId?.resColor!!)
        }
        if (checked != null || checkedId != null) {
            states.add(intArrayOf(R.attr.state_checked))
            colors.add(checked ?: checkedId?.resColor!!)
        }
        //默认的
        states.add(intArrayOf())
        colors.add(
            normal ?: normalId?.resColor ?: throw RuntimeException("normal 和 normalId 必须选择一个")
        )
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    /**
     * @param normal:填充的颜色
     * @param strokeColor:边框颜色
     * @param strokeSizePx:边框宽度  px
     * @param strokeSize:边框宽度  dp
     * @param radius:圆角半径 dp
     * @param radiusPx:圆角半径 px
     * @param radiusArr:圆角半径[左上，右上，右下，左下]或者8个值也可以4个值得一个 dp
     * @param radiusArrPx:圆角半径[左上，右上，右下，左下]或者8个值也可以4个值得一个 px
     */
    fun getGradientDrawable(
        @ColorInt normal: Int? = null,
        @ColorInt strokeColor: Int? = null,
        @ColorRes normalId: Int? = null,
        @ColorRes strokeColorId: Int? = null,
        strokeSizePx: Int = 0,
        strokeSize: Float = 0f,
        radius: Float = 0f,
        radiusPx: Int = 0,
        radiusArr: FloatArray? = null,
        radiusArrPx: IntArray? = null,
    ) = GradientDrawable().apply {
        setColor(normal ?: normalId?.resColor ?: throw RuntimeException("normal 和 normalId 必须选择一个"))
        if (strokeColorId != null || strokeColor != null) {
            setStroke(
                max(strokeSize.dp, strokeSizePx),
                (strokeColor ?: strokeColorId?.resColor)!!
            )
        }
        val radis = max(radius.dp.toFloat(), radiusPx.toFloat())
        when {
            //使用全角
            radis > 0 -> cornerRadius = radis
            radiusArr?.size == 8 -> cornerRadii = radiusArr.map { it.dp.toFloat() }.toFloatArray()
            radiusArr?.size == 4 -> cornerRadii = floatArrayOf(
                radiusArr[0].dp.toFloat(), radiusArr[0].dp.toFloat(),
                radiusArr[1].dp.toFloat(), radiusArr[1].dp.toFloat(),
                radiusArr[2].dp.toFloat(), radiusArr[2].dp.toFloat(),
                radiusArr[3].dp.toFloat(), radiusArr[3].dp.toFloat()
            )
            radiusArrPx?.size == 8 -> cornerRadii = radiusArrPx.map { it.toFloat() }.toFloatArray()
            radiusArrPx?.size == 4 -> cornerRadii = floatArrayOf(
                radiusArrPx[0].toFloat(), radiusArrPx[0].toFloat(),
                radiusArrPx[1].toFloat(), radiusArrPx[1].toFloat(),
                radiusArrPx[2].toFloat(), radiusArrPx[2].toFloat(),
                radiusArrPx[3].toFloat(), radiusArrPx[3].toFloat()
            )
        }
    }


    /**
     * 获取StateListDrawable实例
     *
     * @param normal  默认的资源
     * @param select 选择的资源
     * @param pressed 按下的资源
     * @param enabled 不可用的资源
     */

    fun getStateListDrawable(
        normal: Drawable,
        select: Drawable? = null,
        pressed: Drawable? = null,
        enabled: Drawable? = null,
        focused: Drawable? = null,
        checked: Drawable? = null
    ) = StateListDrawable().apply {
        addState(intArrayOf(R.attr.state_pressed, R.attr.state_enabled), pressed)
        addState(intArrayOf(R.attr.state_selected), select)
        addState(intArrayOf(-R.attr.state_enabled), enabled)
        addState(intArrayOf(R.attr.state_focused), focused)
        addState(intArrayOf(R.attr.state_checked), checked)
        addState(intArrayOf(), normal)
    }

    /**
     * 获取StateListDrawable实例
     *
     * @param normal  默认的资源
     * @param select 选择的资源
     * @param pressed 按下的资源
     * @param enabled 不可用的资源
     */
    fun getStateListDrawable(
        @DrawableRes normalId: Int,
        @DrawableRes selectId: Int? = null,
        @DrawableRes pressedId: Int? = null,
        @DrawableRes enabledId: Int? = null,
        @DrawableRes focusedId: Int? = null,
        @DrawableRes checkedId: Int? = null
    ) = getStateListDrawable(
        normal = normalId.resDrawable,
        select = selectId?.resDrawable,
        pressed = pressedId?.resDrawable,
        enabled = enabledId?.resDrawable,
        focused = focusedId?.resDrawable,
        checked = checkedId?.resDrawable
    )

    /**
     * 获取StateListDrawable实例
     *
     * @param normal  默认的资源
     * @param pressed 按下的资源
     * @param select 选择的资源
     * @param enabled 不可用的资源
     * @param strokeColorId  边框颜色
     * @param cornerRadius 圆角半径  DP
     * @param strokeSizePx  边框宽度 px
     * @param strokeSize  边框宽度 dP
     */
    fun getStateListDrawableColor(
        @ColorRes normal: Int,
        @ColorRes select: Int? = null,
        @ColorRes pressed: Int? = null,
        @ColorRes enabled: Int? = null,
        @ColorRes focusedId: Int? = null,
        @ColorRes checkedId: Int? = null,
        @ColorRes strokeColorId: Int,
        radiusPx: Int = 0,
        strokeSizePx: Int = 0,
        radius: Float = 0f,
        strokeSize: Float = 0f,
    ): StateListDrawable {
        val normal = getGradientDrawable(
            normalId = normal,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeSizePx = strokeSizePx,
            radius = radius,
            strokeSize = strokeSize
        )

        val select = if (select == null) null else getGradientDrawable(
            normalId = select,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeSizePx = strokeSizePx,
            radius = radius,
            strokeSize = strokeSize
        )
        val pressed = if (pressed == null) null else getGradientDrawable(
            normalId = pressed,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeSizePx = strokeSizePx,
            radius = radius,
            strokeSize = strokeSize
        )
        val enabled = if (enabled == null) null else getGradientDrawable(
            normalId = enabled,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeSizePx = strokeSizePx,
            radius = radius,
            strokeSize = strokeSize
        )
        val focused = if (focusedId == null) null else getGradientDrawable(
            normalId = focusedId,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeSizePx = strokeSizePx,
            radius = radius,
            strokeSize = strokeSize
        )
        val checked = if (checkedId == null) null else getGradientDrawable(
            normalId = checkedId,
            strokeColorId = strokeColorId,
            radiusPx = radiusPx,
            strokeSizePx = strokeSizePx,
            radius = radius,
            strokeSize = strokeSize
        )
        return getStateListDrawable(
            normal = normal,
            select = select,
            pressed = pressed,
            enabled = enabled,
            focused = focused,
            checked = checked
        )
    }

    /**
     * 把drawable渲染成指定的颜色
     */
    fun getTintDrawable(drawable: Drawable, @ColorInt color: Int) =
        DrawableCompat.wrap(drawable).mutate().apply { setTint(color) }

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
     * @param size:px 宽度高度px, 取最大值
     * @param width:px
     * @param height:px
     * @param tintColor: 颜色值
     */
    fun createTextDraw(
        textView: TextView,
        @DrawableRes drawableId: Int? = null,
        //左：1,上：2,右：3,下：4      默认 右
        location: Int = 3,
        //宽度高度px, 取最大值
        size: Int? = null,
        width: Int = 0,
        height: Int = 0,
        @ColorInt tintColor: Int? = null,
        drawable: Drawable? = null,
    ) = TextDrawUtils(textView, drawableId, location, size, width, height, tintColor, drawable)

    fun createGradientDrawable(color: Int): BuilderGradient {
        return BuilderGradient(color)
    }
}