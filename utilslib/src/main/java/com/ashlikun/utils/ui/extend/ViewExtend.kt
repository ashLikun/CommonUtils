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
import com.ashlikun.utils.ui.BitmapUtil
import com.ashlikun.utils.ui.ResUtils
import com.ashlikun.utils.ui.shadow.CanShadowDrawable
import com.ashlikun.utils.ui.shadow.RoundShadowDrawable

/**
 * 作者　　: 李坤
 * 创建时间: 2019/10/18　16:02
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：view的一些扩展方法
 */
typealias OnSizeListener = (width: Int, height: Int) -> Unit

typealias OnAttachedChangeWindow = () -> Unit

/**
 * 设置view大小
 */
fun View?.setViewSize(width: Int? = null, height: Int? = null) {
    if (width != null || height != null) {
        this?.run {
            var params: ViewGroup.LayoutParams? = layoutParams
            if (params == null) {
                params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
fun View?.getViewSize(onSizeListener: OnSizeListener) {
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
 * view树绘制的回调
 *
 * @param onDraw 监听回调
 * @param isOneToRemove 是否只监听一次
 */
fun View?.addOnDrawListener(listener: () -> Unit, isOneToRemove: Boolean = true) {
    this?.run {
        viewTreeObserver.addOnDrawListener(object : ViewTreeObserver.OnDrawListener {
            override fun onDraw() {
                if (isOneToRemove) {
                    viewTreeObserver.removeOnDrawListener(this)
                }
                listener.invoke()
            }
        })
    }
}

/**
 * view树绘制前的回调
 *
 * @param onDraw 监听回调
 * @param isOneToRemove 是否只监听一次
 */
fun View?.addOnPreDrawListener(listener: () -> Boolean, isOneToRemove: Boolean = true) {
    this?.run {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (isOneToRemove) {
                    viewTreeObserver.removeOnPreDrawListener(this)
                }
                return listener.invoke()
            }
        })
    }
}

/**
 * view树布局的回调
 *
 * @param onDraw 监听回调
 * @param isOneToRemove 是否只监听一次
 */
fun View?.addOnGlobalLayoutListener(listener: () -> Unit, isOneToRemove: Boolean = true) {
    this?.run {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (isOneToRemove) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                listener.invoke()
            }
        })
    }
}

/**
 * view树 窗口获取焦点的回调
 *
 * @param onDraw 监听回调
 * @param isOneToRemove 是否只监听一次
 */
fun View?.addOnWindowFocusChangeListener(listener: (hasFocus: Boolean) -> Unit, isOneToRemove: Boolean = true) {
    this?.run {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            viewTreeObserver.addOnWindowFocusChangeListener(object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (isOneToRemove) {
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                    listener.invoke(hasFocus)
                }
            })
        }
    }
}

/**
 * 从资源文件获取一个view
 */
fun Context.getInflaterView(res: Int, parent: ViewGroup? = null, attachToRoot: Boolean = parent != null) = (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(res, parent, attachToRoot)

/**
 * 设置ImageView渲染（Tint）
 */
fun ImageView?.setImageViewTint(@ColorRes color: Int) = this?.setColorFilter(ResUtils.getColor(context, color))

/**
 * 按照原始的宽度，根据比例，缩放
 *
 * @param bili (w/h)
 */
fun View?.scaleViewByWidth(bili: Float) {
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
fun View?.scaleViewByHeight(bili: Float) {
    getViewSize { width, height ->
        setViewSize((height * bili).toInt(), height)
    }
}

fun View?.getMarginLeft() = if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.leftMargin
        ?: 0 else 0

fun View?.getMarginTop() = if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.topMargin
        ?: 0 else 0

fun View?.getMarginRight() = if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.rightMargin
        ?: 0 else 0

fun View?.getMarginBottom() = if (this?.layoutParams is ViewGroup.MarginLayoutParams?) (this?.layoutParams as ViewGroup.MarginLayoutParams?)?.bottomMargin
        ?: 0 else 0

/**
 * 设置view   Padding
 */
fun View?.setPaddings(leftPadding: Int = this?.paddingLeft
        ?: 0, topPadding: Int = this?.paddingTop ?: 0,
                      rightPadding: Int = this?.paddingRight
                              ?: 0, bottomPadding: Int = this?.paddingBottom
                ?: 0) {
    this?.run {
        setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
    }
}

/**
 * 设置view   Margin
 */
fun View?.setMargin(leftMargin: Int = getMarginLeft(), topMargin: Int = getMarginTop(),
                    rightMargin: Int = getMarginRight(), bottomMargin: Int = getMarginBottom()) {
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

/**
 * 监听AttachedToWindow状态
 */
fun View?.getAttachedToWindow(onAttachedChangeWindow: OnAttachedChangeWindow) {
    this?.run {
        if (ViewCompat.isAttachedToWindow(this)) {
            onAttachedChangeWindow.invoke()
            return
        }
        this?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                onAttachedChangeWindow.invoke()
            }

            override fun onViewDetachedFromWindow(v: View) {
            }
        })
    }
}

/**
 * 监听onViewDetachedFromWindow状态
 */
fun View?.getDetachedFromWindow(view: View, onAttachedChangeWindow: OnAttachedChangeWindow) {
    this?.run {

        this?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                onAttachedChangeWindow.invoke()
            }
        })
    }
}


fun View.getPadding() = Math.max(Math.max(paddingTop, paddingBottom), Math.max(paddingLeft, paddingRight))

fun View?.shadow(range: Int = this?.getPadding() ?: DimensUtils.dip2px(5f),
                 color: Int = R.color.lib_shadow_default,
                 bgColor: Int = R.color.lib_shadow_default_bg,
                 radius: Float = 0f) {
    if (this != null) {
        if (getTag(999999881) != range || getTag(999999882) != color || getTag(999999883) != bgColor) {
            setTag(999999881, range)
            setTag(999999882, color)
            setTag(999999883, bgColor)
            ViewCompat.setBackground(this, RoundShadowDrawable(context.resColor(color),
                    ResUtils.getColor(context, bgColor),
                    DimensUtils.dip2px(radius).toFloat(),
                    range.toFloat()))
        }
    }
}

/**
 * 阴影 效果好，但是被关闭硬件加速
 */
fun View?.shadowNoHardware(range: Int = this?.getPadding() ?: DimensUtils.dip2px(5f),
                           color: Int = R.color.lib_shadow_default,
                           bgColor: Int = R.color.lib_shadow_default_bg,
                           radius: Float = 0f,
                           corners: Int = CanShadowDrawable.CORNER_ALL) {
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
fun View.setVisibility(visibility: Boolean) {
    this?.visibility = if (visibility) View.VISIBLE else View.GONE
}

/**
 * 是否Gone
 */
fun View.isGone() = this?.visibility == View.GONE

fun View.isVisible() = this?.visibility == View.VISIBLE
fun View.isInvisible() = this?.visibility == View.INVISIBLE

/**
 * 截取viewGroup内容，生成图片
 *
 * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
 * @return 图片bitmap
 */
fun View.getToBitmap(scale: Float = 1f) = BitmapUtil.getViewBitmap(this, scale)

/**
 * 设置View的饱和度
 */
fun View.setViewSaturation(sat: Float = 0f) {
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
