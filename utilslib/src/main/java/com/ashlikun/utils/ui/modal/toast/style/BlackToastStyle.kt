package com.ashlikun.utils.ui.modal.toast.style

import com.ashlikun.utils.ui.modal.toast.config.IToastStyle
import android.widget.TextView
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.os.Build
import android.view.ViewGroup
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.graphics.drawable.GradientDrawable
import com.ashlikun.utils.other.DimensUtils
import com.ashlikun.utils.ui.resources.ResUtils
import com.ashlikun.utils.R

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 21:49
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：默认黑色样式实现
 */
open class BlackToastStyle : IToastStyle<TextView> {
    override fun createView(context: Context): TextView {
        val textView = TextView(context)
        textView.id = android.R.id.message
        textView.gravity = getTextGravity(context)
        textView.setTextColor(getTextColor(context))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(context).toFloat())
        val horizontalPadding = getHorizontalPadding(context)
        val verticalPadding = getVerticalPadding(context)

        // 适配布局反方向特性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setPaddingRelative(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
            )
        } else {
            textView.setPadding(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
            )
        }
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val background = getBackgroundDrawable(context)
        // 设置背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.background = background
        } else {
            textView.setBackgroundDrawable(background)
        }

        // 设置 Z 轴阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.z = getTranslationZ(context)
        }

        // 设置最大显示行数
        textView.maxLines = getMaxLines(context)
        return textView
    }

    protected fun getTextGravity(context: Context): Int {
        return Gravity.CENTER
    }

    protected open fun getTextColor(context: Context): Int {
        return ResUtils.getColor(context, R.color.toast_black_text_color)
    }

    protected fun getTextSize(context: Context) = DimensUtils.sp2px(context, 14f)

    protected fun getHorizontalPadding(context: Context) = DimensUtils.dip2px(context, 24f)

    protected fun getVerticalPadding(context: Context) = DimensUtils.dip2px(context, 10f)

    protected open fun getBackgroundDrawable(context: Context): Drawable {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(ResUtils.getColor(context, R.color.toast_black_bg_color))
        // 设置圆角
        drawable.cornerRadius = DimensUtils.dip2px(context, 10f).toFloat()
        return drawable
    }

    protected fun getTranslationZ(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            context.resources.displayMetrics
        )
    }

    protected fun getMaxLines(context: Context?): Int {
        return 5
    }
}