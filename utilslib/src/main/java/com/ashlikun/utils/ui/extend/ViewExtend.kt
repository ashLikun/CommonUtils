package com.ashlikun.utils.ui.extend

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import com.ashlikun.utils.R
import com.ashlikun.utils.other.DimensUtils
import com.ashlikun.utils.ui.image.BitmapUtil
import com.ashlikun.utils.ui.resources.ResUtils
import com.ashlikun.utils.ui.shadow.CanShadowDrawable
import com.ashlikun.utils.ui.shadow.RoundShadowDrawable
import com.ashlikun.utils.ui.status.StatusBarCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2019/10/18　16:02
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：view的一些扩展方法
 */
typealias OnSizeListener = (width: Int, height: Int) -> Unit

/**
 * 设置view大小
 */
inline fun View?.setViewSize(width: Int? = null, height: Int? = null) {
    if (width != null || height != null) {
        this?.run {
            var params: ViewGroup.LayoutParams? = layoutParams
            if (params == null) {
                params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            if (height != null) {
                params.height = height
            }
            if (width != null) {
                params.width = width
            }
            layoutParams = params
        }
    }
}


/**
 * 获取view大小
 *
 * @param onSizeListener 监听回调
 */
inline fun View?.getViewSize(crossinline onSizeListener: OnSizeListener) {
    this?.run {
        if (measuredWidth > 0 || measuredHeight > 0) {
            onSizeListener.invoke(measuredWidth, measuredHeight)
            return
        }
        val observer = viewTreeObserver
        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredHeight <= 0 && measuredWidth <= 0) {
                    return
                }
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                onSizeListener.invoke(width, height)
            }
        })
    }

}

/**
 * 从资源文件获取一个view
 */
inline fun Context.getInflaterView(
    res: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = parent != null
) = (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
    res,
    parent,
    attachToRoot
)

/**
 * 设置ImageView渲染（Tint）
 */
inline fun ImageView?.setImageViewTint(@ColorRes color: Int) =
    this?.setColorFilter(ResUtils.getColor(context, color))

/**
 * 按照原始的宽度，根据比例，缩放
 *
 * @param bili (w/h)
 */
inline fun View?.scaleViewByWidth(bili: Float) {
    this?.getViewSize { width, height ->
        getViewSize { width, height ->
            setViewSize(width, (width / bili).toInt())
        }
    }
}

/**
 * 按照原始的高度，根据比例，缩放
 *
 * @param bili (w/h)
 */
inline fun View?.scaleViewByHeight(bili: Float) {
    getViewSize { width, height ->
        setViewSize((height * bili).toInt(), height)
    }
}

inline fun View?.getMarginLeft() =
    if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.leftMargin
        ?: 0 else 0

inline fun View?.getMarginTop() =
    if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.topMargin
        ?: 0 else 0

inline fun View?.getMarginRight() =
    if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.rightMargin
        ?: 0 else 0

inline fun View?.getMarginBottom() =
    if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.bottomMargin
        ?: 0 else 0

/**
 * 设置view   Padding
 */
inline fun View?.setPaddings(
    leftPadding: Int = this?.paddingLeft
        ?: 0, topPadding: Int = this?.paddingTop ?: 0,
    rightPadding: Int = this?.paddingRight
        ?: 0, bottomPadding: Int = this?.paddingBottom
        ?: 0
) {
    this?.run {
        setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
    }
}

/**
 * 设置view   Margin
 */
inline fun View?.setMargin(
    leftMargin: Int = getMarginLeft(), topMargin: Int = getMarginTop(),
    rightMargin: Int = getMarginRight(), bottomMargin: Int = getMarginBottom()
) {
    this?.run {
        val params = layoutParams
        if (params != null && params is ViewGroup.MarginLayoutParams) {
            params.leftMargin = leftMargin
            params.topMargin = topMargin
            params.rightMargin = rightMargin
            params.bottomMargin = bottomMargin
            layoutParams = params
        }
    }
}


inline fun View.padding() =
    Math.max(Math.max(paddingTop, paddingBottom), Math.max(paddingLeft, paddingRight))

inline fun View?.shadow(
    range: Int = this?.padding() ?: DimensUtils.dip2px(5f),
    color: Int = R.color.lib_shadow_default,
    bgColor: Int = R.color.lib_shadow_default_bg,
    radius: Float = 0f
) {
    if (this != null) {
        if (getTag(999999881) != range || getTag(999999882) != color || getTag(999999883) != bgColor) {
            setTag(999999881, range)
            setTag(999999882, color)
            setTag(999999883, bgColor)
            ViewCompat.setBackground(
                this, RoundShadowDrawable(
                    context.resColor(color),
                    ResUtils.getColor(context, bgColor),
                    DimensUtils.dip2px(radius).toFloat(),
                    range.toFloat()
                )
            )
        }
    }
}

/**
 * 阴影 效果好，但是被关闭硬件加速
 */
inline fun View?.shadowNoHardware(
    range: Int = this?.padding() ?: DimensUtils.dip2px(5f),
    color: Int = R.color.lib_shadow_default,
    bgColor: Int = R.color.lib_shadow_default_bg,
    radius: Float = 0f,
    corners: Int = CanShadowDrawable.CORNER_ALL
) {
    if (this != null) {
        if (getTag(999999881) != range || getTag(999999882) != color || getTag(999999883) != bgColor) {
            setTag(999999881, range)
            setTag(999999882, color)
            setTag(999999883, bgColor)
            CanShadowDrawable.Builder.on(this)
                .bgColor(ResUtils.getColor(context, bgColor))
                .shadowColor(ResUtils.getColor(context, color))
                .radius(DimensUtils.dip2px(radius))
                .shadowRange(range.toFloat())
                .corners(corners)
                .create()
        }
    }
}

/**
 * 设置隐藏与显示
 */
inline fun View.setVisibility(visibility: Boolean) {
    this?.visibility = if (visibility) View.VISIBLE else View.GONE
}

/**
 * 是否Gone
 */
inline fun View.isGone() = this?.visibility == View.GONE

inline fun View.isVisible() = this?.visibility == View.VISIBLE
inline fun View.isInvisible() = this?.visibility == View.INVISIBLE

/**
 * 截取viewGroup内容，生成图片
 *
 * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
 * @return 图片bitmap
 */
inline fun View.bitmap(scale: Float = 1f) = BitmapUtil.getViewBitmap(this, scale)

/**
 * 设置View的饱和度
 */
inline fun View.setViewSaturation(sat: Float = 0f) {
    val paint = Paint()
    val filter = ColorMatrix()
    filter.setSaturation(sat)
    paint.colorFilter = ColorMatrixColorFilter(filter)
    if (layerType == View.LAYER_TYPE_NONE) {
        setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    } else {
        setLayerType(layerType, paint)
    }
}

/**
 * 设置状态栏高度
 */
inline fun View.setStatusHeight(isNeedAndroidMHalf: Boolean = false) =
    StatusBarCompat.setEmptyHeight(this, isNeedAndroidMHalf)

/**
 * 设置状态栏view的padding
 */
inline fun View.setStatusPadding() =
    StatusBarCompat.setTransparentViewPadding(this)

/**
 * 设置状态栏view的Margin
 */
inline fun View.setStatusMargin() =
    StatusBarCompat.setTransparentViewMargin(this)