package com.ashlikun.utils.simple

import android.R
import android.content.Context
import android.graphics.Rect
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.ashlikun.utils.ui.text.FocusLinkMovementMethod

/**
 * 作者　　: 李坤
 * 创建时间:2017/9/2 0002　14:53
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：textView的兼容
 * 1：lineSpacingExtra   兼容
 */
class TextViewCompat @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    var mSpacingAdd = 0f
    var movementMethodClick = false
    private fun initView(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, intArrayOf(R.attr.lineSpacingExtra))
        mSpacingAdd = a.getDimensionPixelSize(0, 0).toFloat()
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        a.recycle()
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        super.setLineSpacing(add, mult)
        mSpacingAdd = add
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight - calculateExtraSpace())
    }

    //计算需要兼容的底部多余的高度
    fun calculateExtraSpace(): Int {
        var result = 0
        val lastLineIndex = lineCount - 1
        if (lastLineIndex >= 0) {
            val layout = layout
            if (measuredHeight - paddingTop - paddingBottom == getLayout().height) {
                val mRect = Rect()
                val baseline = getLineBounds(lastLineIndex, mRect)
                result = mRect.bottom - (baseline + layout.paint.fontMetricsInt.bottom)
            }
        }
        return result
    }

    /**
     * 设置了ClickableSpan导致的上层View点击事件无法响应解决方案
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val movementMethod = movementMethod
        return if (movementMethod != null && movementMethod is FocusLinkMovementMethod) {
            movementMethodClick = false
            val result = super.onTouchEvent(event)
            movementMethodClick = movementMethod.clickUp
            result
        } else {
            super.onTouchEvent(event)
        }
    }

    /**
     * 设置了ClickableSpan导致的上层View点击事件无法响应解决方案
     */
    fun setMovementMethods(movement: MovementMethod?) {
        val focusable = isFocusable
        val isClickable = isClickable
        val isLongClickable = isLongClickable
        super.setMovementMethod(movement)
        isFocusable = focusable
        setClickable(isClickable)
        setLongClickable(isLongClickable)
    }

    /**
     * 设置了ClickableSpan导致的上层View点击事件无法响应解决方案
     */
    override fun performClick(): Boolean {
        return if (!movementMethodClick) {
            super.performClick()
        } else false
    }

    init {
        initView(context, attrs)
    }
}