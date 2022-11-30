package com.ashlikun.utils.ui.extend

import android.animation.*
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.util.TypedValue
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.view.*
import androidx.core.widget.TextViewCompat
import com.ashlikun.utils.R
import com.ashlikun.utils.other.DimensUtils
import com.ashlikun.utils.ui.OnSizeListener
import com.ashlikun.utils.ui.UiUtils
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

val View.layoutInflater
    get() = LayoutInflater.from(context)


/**
 * 设置view大小
 * @param duration 动画执行时间 > 0 就启用动画
 * @param interpolator 动画的插值
 */
inline fun View?.setViewSize(width: Int? = null, height: Int? = null, duration: Long = 0, interpolator: TimeInterpolator? = null) {
    if (width != null || height != null) {
        this?.run {
            var params: ViewGroup.LayoutParams? = layoutParams
            if (params == null) {
                params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            if (duration > 0) {
                val currentHeight = if (this?.height > 0) this?.height else this?.measuredHeight
                val currentWidth = if (this?.width > 0) this?.width else this?.measuredWidth
                val animSet = AnimatorSet()
                val animList = mutableListOf<Animator>()
                if (width != null) {
                    animList.add(ValueAnimator.ofInt(currentWidth, width).apply {
                        addUpdateListener {
                            val w = it.animatedValue as Int
                            params.width = w
                            layoutParams = params
                        }
                    })
                }
                if (height != null) {
                    animList.add(ValueAnimator.ofInt(currentHeight, height).apply {
                        addUpdateListener {
                            val h = it.animatedValue as Int
                            params.height = h
                            layoutParams = params
                        }
                    })
                }
                animSet.playTogether(animList)
                animSet.duration = duration
                animSet.interpolator = interpolator ?: LinearInterpolator()
                animSet.start()

            } else {
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
}

/**
 * 获取view大小
 *
 * @param onSizeListener 监听回调
 */
inline fun View?.getViewSize(noinline onSizeListener: OnSizeListener) {
    this?.run {
        UiUtils.getViewSize(this, onSizeListener)
    }
}

/**
 * 从资源文件获取一个view
 */
inline fun Context.getInflaterView(
    res: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = parent != null
) = LayoutInflater.from(this).inflate(
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
 * @param duration 动画执行时间 > 0 就启用动画
 * @param interpolator 动画的插值
 * @param bili (w/h)
 */
inline fun View?.scaleViewByWidth(bili: Float, duration: Long = 0, interpolator: TimeInterpolator? = null) {
    this?.getViewSize { width, height ->
        setViewSize(width, (width / bili).toInt(), duration, interpolator)
    }
}

/**
 * 按照原始的高度，根据比例，缩放
 * @param duration 动画执行时间 > 0 就启用动画
 * @param interpolator 动画的插值
 * @param bili (w/h)
 */
inline fun View?.scaleViewByHeight(bili: Float, duration: Long = 0, interpolator: TimeInterpolator? = null) {
    getViewSize { width, height ->
        setViewSize((height * bili).toInt(), height, duration, interpolator)
    }
}

inline fun View.getMarginLeft() =
    if (this.layoutParams is ViewGroup.MarginLayoutParams) (this.layoutParams as ViewGroup.MarginLayoutParams).leftMargin else 0

inline fun View.getMarginTop() =
    if (this.layoutParams is ViewGroup.MarginLayoutParams) (this.layoutParams as ViewGroup.MarginLayoutParams).topMargin else 0

inline fun View.getMarginRight() =
    if (this.layoutParams is ViewGroup.MarginLayoutParams) (this.layoutParams as ViewGroup.MarginLayoutParams).rightMargin else 0

inline fun View.getMarginBottom() =
    if (this.layoutParams is ViewGroup.MarginLayoutParams) (this.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin else 0

/**
 * 设置view   Padding
 * @param duration 动画执行时间 > 0 就启用动画
 * @param interpolator 动画的插值
 */
inline fun View?.setPaddings(
    padding: Int? = null, leftPadding: Int? = null, topPadding: Int? = null,
    rightPadding: Int? = null, bottomPadding: Int? = null, duration: Long = 0, interpolator: TimeInterpolator? = null
) {
    this?.run {
        UiUtils.setPaddings(this, padding, leftPadding, topPadding, rightPadding, bottomPadding, duration, interpolator)
    }
}

/**
 * 设置view   Margin
 * @param duration 动画执行时间 > 0 就启用动画
 * @param interpolator 动画的插值
 */
inline fun View.setMargin(
    leftMargin: Int? = null, topMargin: Int? = null,
    rightMargin: Int? = null, bottomMargin: Int? = null, duration: Long = 0, interpolator: TimeInterpolator? = null
) = UiUtils.setViewMargin(this, leftMargin, topMargin, rightMargin, bottomMargin, duration, interpolator)

/**
 * 获取最大的Padding
 */
inline fun View.padding() =
    paddingTop.coerceAtLeast(paddingBottom).coerceAtLeast(paddingLeft.coerceAtLeast(paddingRight))

/**
 * RoundShadowDrawable 阴影
 */
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
inline fun View.setVisibility(visibility: Boolean, duration: Long = 0, interpolator: TimeInterpolator? = null) {
    if (duration > 0) {
        clearAnimation()
        animate().alpha(if (visibility) 1f else 0f)
            .apply {
                setDuration(duration)
                setInterpolator(interpolator)
                setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        isVisible = true
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        isVisible = visibility
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        super.onAnimationCancel(animation)
                        isVisible = visibility
                    }
                })
            }
            .start()
    } else {
        this.visibility = if (visibility) View.VISIBLE else View.GONE
    }
}

/**
 * 是否Gone
 */
inline fun View.isGone() = this?.visibility == View.GONE

inline fun View.isInvisible() = this?.visibility == View.INVISIBLE

/**
 * 相同不刷新
 */
inline var View.isVisibleX: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        val result = if (value) View.VISIBLE else View.GONE
        if (result != visibility)
            visibility = result
    }

/**
 * 相同不刷新
 */
inline var TextView.textX: CharSequence
    get() = text
    set(value) {
        if (text != value) {
            text = value
        }
    }

/**
 * 相同不刷新
 */
inline var View.isSelectedX: Boolean
    get() = isSelected
    set(value) {
        if (isSelected != value) {
            isSelected = value
        }
    }

/**
 * 相同不刷新
 */
inline var ImageView.imageResourceX: Int
    get() = (getTag("imageResourceX".hashCode()) as? Int) ?: 0
    set(value) {
        if (getTag("imageResourceX".hashCode()) != value) {
            setTag("imageResourceX".hashCode(), value)
            setImageResource(value)
        }
    }


/**
 * 相同不刷新
 */
inline var View.isEnabledX: Boolean
    get() = isEnabled
    set(value) {
        if (isEnabled != value) {
            isEnabled = value
        }
    }

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


/**
 * 设置文字大小 Px
 */
inline var TextView.textSizePx: Float
    get() = textSize
    set(value) {
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, value.dp.toFloat())
    }

/**
 * 设置文字大小 Dp
 */
inline var TextView.textSizeDp: Float
    get() = textSize.px2dip.toFloat()
    set(value) {
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, value)
    }

/**
 * 创建 StaticLayout
 */
inline fun TextView.createStaticLayout(text: CharSequence = textX, width: Int): StaticLayout {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return createStaticLayoutBuild(text, width).build()
    } else {
        return StaticLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineSpacingExtra, includeFontPadding)
    }
}

/**
 * 创建 StaticLayout.Builder
 */
@RequiresApi(Build.VERSION_CODES.M)
inline fun TextView.createStaticLayoutBuild(text: CharSequence = textX, width: Int): StaticLayout.Builder {
    return StaticLayout.Builder.obtain(text, 0, text.length, paint, width).also {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            it.setTextDirection(textDirectionHeuristic)
        }
        it.setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
        it.setIncludePad(includeFontPadding)
        it.setBreakStrategy(breakStrategy)
        it.setHyphenationFrequency(hyphenationFrequency)
        it.setMaxLines(if (maxLines == -1) Int.MAX_VALUE else maxLines)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it.setJustificationMode(justificationMode)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            it.setUseLineSpacingFromFallbacks(isFallbackLineSpacing)
        }
    }
}


/**
 * 水平的padding+Margin
 */
inline val View.paddingAndMarginX: Int
    get() = paddingLeft + paddingRight + marginLeft + marginRight

/**
 * 垂直的padding+Margin
 */
inline val View.paddingAndMarginY: Int
    get() = paddingTop + paddingBottom + marginTop + marginBottom

