package com.ashlikun.utils.ui.image

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.ui.extend.resDrawable
import kotlin.math.max

/**
 * 作者　　: 李坤
 * 创建时间: 2021/12/14　23:06
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：TextView的上下左右Drawable，兼容大小
 */
/**
 * 创建一个TextView的上下左右Drawable
 * @param size:px 宽度高度px, 取最大值
 * @param width:px
 * @param height:px
 * @param tintColor: 颜色值
 */
class TextDrawUtils(
    var textView: TextView,
    @DrawableRes var drawableId: Int? = null,
    //左：1,上：2,右：3,下：4      默认 右
    var location: Int = 3,
    //宽度高度px, 取最大值
    var size: Int? = null,
    var width: Int = 0,
    var height: Int = 0,
    @ColorInt var tintColor: Int? = null,
    drawable: Drawable? = null,
) {
    var mDrawable: Drawable

    init {
        width = max(width, size ?: 0)
        height = max(height, size ?: 0)
        mDrawable = (drawable ?: drawableId?.resDrawable)!!
        createDrawable()
        val yiyou = textView.compoundDrawables
        when (location) {
            1 -> textView.setCompoundDrawables(mDrawable, yiyou[1], yiyou[2], yiyou[3])
            2 -> textView.setCompoundDrawables(yiyou[0], mDrawable, yiyou[2], yiyou[3])
            3 -> textView.setCompoundDrawables(yiyou[0], yiyou[1], mDrawable, yiyou[3])
            4 -> textView.setCompoundDrawables(yiyou[0], yiyou[1], yiyou[2], mDrawable)
        }
    }

    fun createDrawable(): Drawable {
        //是否改变宽高
        var isChang = true
        val drawWidth = mDrawable.minimumWidth.toFloat()
        val drawHeight = mDrawable.minimumHeight.toFloat()
        if (width == 0 && height == 0) {
            width = drawWidth.toInt()
            height = drawHeight.toInt()
            isChang = false
        } else if (width == 0) {
            //高度被设置了，那么久按照比例设置宽度
            width = (height / drawHeight * drawWidth).toInt()
        } else if (height == 0) {
            //高度被设置了，那么久按照比例设置宽度
            height = (width / drawWidth * drawHeight).toInt()
        }
        //如果使用tint，必须使用DrawableCompat.wrap
        if (isChang || tintColor != null) {
            mDrawable = DrawableCompat.wrap(mDrawable).mutate()
            if (tintColor != null) {
                DrawableCompat.setTint(mDrawable, tintColor!!)
            }
        }
        mDrawable.setBounds(0, 0, width, height)
        return mDrawable
    }
}