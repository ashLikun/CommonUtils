package com.ashlikun.utils.ui.image

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import androidx.annotation.ColorRes
import com.ashlikun.utils.ui.extend.resColor

/**
 * 作者　　: 李坤
 * 创建时间: 2021/12/14　23:04
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：RippleDrawable 的工具
 * 创建水波纹效果
 */

class RippleUtils() {
    var rippleColor: ColorStateList? = null
    var normalDrawable: Drawable? = null
    var pressedDrawable: Drawable? = null
    var selectDrawable: Drawable? = null
    var enableDrawable: Drawable? = null

    /**
     * 设置失效的资源
     */
    fun setEnableDrawable(enableDrawable: Drawable): RippleUtils {
        this.enableDrawable = enableDrawable
        return this
    }

    fun setSelectDrawable(selectDrawable: Drawable): RippleUtils {
        this.selectDrawable = selectDrawable
        return this
    }

    fun setEnableColor(@ColorRes enable: Int): RippleUtils {
        enableDrawable = ColorDrawable(enable.resColor)
        return this
    }

    /**
     * 设置涟漪的颜色
     */
    fun setRippleColor(rippleColor: ColorStateList): RippleUtils {
        this.rippleColor = rippleColor
        return this
    }

    /**
     * 设置默认
     */
    fun setNormalDrawable(normalDrawable: Drawable): RippleUtils {
        this.normalDrawable = normalDrawable
        return this
    }

    fun setNormalColor(@ColorRes normalColor: Int): RippleUtils {
        normalDrawable = ColorDrawable(normalColor.resColor)
        return this
    }

    /**
     * 设置按下
     */
    fun setPressedDrawable(pressedDrawable: Drawable): RippleUtils {
        this.pressedDrawable = pressedDrawable
        return this
    }

    fun setPressedColor(@ColorRes pressedColor: Int): RippleUtils {
        pressedDrawable = ColorDrawable(pressedColor.resColor)
        if (rippleColor == null) {
            rippleColor = ColorStateList.valueOf(pressedColor.resColor)
        }
        return this
    }

    fun create(): Drawable {
        if (normalDrawable == null) {
            normalDrawable = ColorDrawable(Color.TRANSPARENT)
        }
        if (enableDrawable == null) {
            enableDrawable = ColorDrawable(Color.GRAY)
        }
        if (rippleColor == null) {
            rippleColor = ColorStateList.valueOf(Color.LTGRAY)
        }
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(-R.attr.state_enabled), enableDrawable)
        drawable.addState(
            intArrayOf(R.attr.state_selected),
            if (selectDrawable == null) pressedDrawable else selectDrawable
        )
        drawable.addState(intArrayOf(), normalDrawable)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable(rippleColor!!, drawable, normalDrawable)
        } else {
            if (pressedDrawable != null) {
                drawable.addState(
                    intArrayOf(R.attr.state_pressed),
                    pressedDrawable
                )
                drawable.addState(
                    intArrayOf(R.attr.state_focused),
                    pressedDrawable
                )
            }
            drawable
        }
    }
}
