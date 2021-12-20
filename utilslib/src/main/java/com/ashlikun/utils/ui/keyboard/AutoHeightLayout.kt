package com.ashlikun.utils.ui.keyboard

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.util.AttributeSet
import com.ashlikun.utils.ui.extend.getActivity

typealias  OnMaxParentHeightChange = (height: Int) -> Unit

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 11:34
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：自动适应软键盘高度的布局
 */
class AutoHeightLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SoftKeyboardSizeWatchLayout(
    context, attrs, defStyleAttr
) {
    var maxParentHeightChange: OnMaxParentHeightChange? = null

    var maxParentHeight = 0
    var softKeyboardHeight = KeyboardUtils.getDefKeyboardHeight()
    var configurationChangedFlag = false


    init {
        onSoftPops.add {
            onSoftPop(it)
        }
        onSoftClose.add {
            onSoftClose()
        }
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

    open fun onSoftPop(height: Int) {
        if (softKeyboardHeight != height) {
            softKeyboardHeight = height
            KeyboardUtils.setDefKeyboardHeight(context, softKeyboardHeight)
            onSoftKeyboardHeightChanged(softKeyboardHeight)
        }
    }

    open fun onSoftClose() {}

    /**
     * 键盘高度改变了
     */
    fun onSoftKeyboardHeightChanged(height: Int) {}


}