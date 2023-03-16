package com.ashlikun.utils.ui.keyboard

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.ashlikun.utils.AppUtils.initAppEditMode
import com.ashlikun.utils.R
import com.ashlikun.utils.ui.extend.getActivity

/**
 * 作者　　: 李坤
 * 创建时间: 2022/7/6　16:03
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */

typealias  OnMaxParentHeightChange = (height: Int) -> Unit

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 11:34
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：自动适应软键盘高度的布局
 */
open class SoftKeyHeightLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SoftKeyboardSizeWatchLayout(
    context, attrs, defStyleAttr
) {
    var isPaddingBottom = false
    var bottomDuration = 200
    var maxParentHeightChange: OnMaxParentHeightChange? = null

    var maxParentHeight = 0
    var softKeyboardHeight = 0
    var configurationChangedFlag = false


    init {
        initAppEditMode()
        if (isInEditMode) {
            softKeyboardHeight = 200
        } else {
            softKeyboardHeight = KeyboardUtils.getDefKeyboardHeight()
        }
        onSoftPops.add {
            onSoftPop(it)
        }
        onSoftClose.add {
            onSoftClose()
        }
        val a = context.obtainStyledAttributes(attrs, R.styleable.SoftKeyHeightLayout)
        isPaddingBottom = a.getBoolean(R.styleable.SoftKeyHeightLayout_skh_is_padding_bottom, isPaddingBottom)
        bottomDuration = a.getInt(R.styleable.SoftKeyHeightLayout_skh_bottom_duration, bottomDuration)
        a.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        onSoftKeyboardHeightChanged(softKeyboardHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (maxParentHeight == 0) {
            maxParentHeight = h
        }
    }

    fun updateMaxParentHeight(maxParentHeight: Int) {
        this.maxParentHeight = maxParentHeight
        maxParentHeightChange?.invoke(maxParentHeight)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChangedFlag = true
        screenHeight = 0
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (configurationChangedFlag) {
            configurationChangedFlag = false
            val r = Rect()
            context.getActivity()?.window?.decorView?.getWindowVisibleDisplayFrame(r)
            if (screenHeight == 0) {
                screenHeight = r.bottom
            }
            val mNowh = screenHeight - r.bottom
            maxParentHeight = mNowh
        }
        if (maxParentHeight != 0) {
            val heightMode = MeasureSpec.getMode(heightMeasureSpec)
            val expandSpec = MeasureSpec.makeMeasureSpec(maxParentHeight, heightMode)
            super.onMeasure(widthMeasureSpec, expandSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    val bottomView by lazy {
        View(context)
    }

    open fun onSoftPop(height: Int) {
        if (softKeyboardHeight != height) {
            softKeyboardHeight = height
            KeyboardUtils.setDefKeyboardHeight(context, softKeyboardHeight)
            onSoftKeyboardHeightChanged(softKeyboardHeight)
        }
        if (isPaddingBottom) {
            ValueAnimator.ofInt(0, height).apply {
                addUpdateListener {
                    val w = it.animatedValue as Int
                    setPadding(paddingLeft, paddingTop, paddingRight, w)
                }
            }.apply {
                duration = bottomDuration.toLong()
                interpolator = LinearInterpolator()
                start()
            }
        }
    }

    open fun onSoftClose() {
        if (isPaddingBottom) {
            ValueAnimator.ofInt(paddingBottom, 0).apply {
                addUpdateListener {
                    val w = it.animatedValue as Int
                    setPadding(paddingLeft, paddingTop, paddingRight, w)
                }
            }.apply {
                duration = bottomDuration.toLong()
                interpolator = LinearInterpolator()
                start()
            }
        }
    }

    /**
     * 键盘高度改变了
     */
    open fun onSoftKeyboardHeightChanged(height: Int) {}


}