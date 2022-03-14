package com.ashlikun.utils.ui.text

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.TextView

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.15 15:18
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 * 设置TextView(setClickSpan)点击时候的背景，手中抬起背景颜色还在问题
 * 是否设置选择--->TextViewCompat || widget.getLineSpacingExtra() > 0 || widget.getLineSpacingMultiplier() != 1
 * 满足一个就不设置
 */

class FocusLinkMovementMethod(val isOpenSelect:Boolean = true) : LinkMovementMethod(), OnLongClickListener {
    var clickDown = false

    @JvmField
    var clickUp = false
    override fun onLongClick(v: View): Boolean {
        return clickUp
    }


    override fun onTouchEvent(
        widget: TextView, buffer: Spannable,
        event: MotionEvent
    ): Boolean {
        clickUp = false
        val action = event.action
        if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_DOWN
        ) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val links = buffer.getSpans(off, off, ClickableSpan::class.java)
            if (links.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    if (isOpenSelect) {
                        Selection.removeSelection(buffer)
                    }
                    links[0].onClick(widget)
                    clickUp = true
                    return false
                } else if (action == MotionEvent.ACTION_DOWN) {
                    if (isOpenSelect) {
                        Selection.setSelection(
                            buffer,
                            buffer.getSpanStart(links[0]),
                            buffer.getSpanEnd(links[0])
                        )
                    }
                    clickDown = true
                }
                return true
            } else {
                if (isOpenSelect) {
                    Selection.removeSelection(buffer)
                }
            }
        } else if (action != MotionEvent.ACTION_MOVE) {
            Selection.removeSelection(buffer)
        }
        clickDown = false
        //true :调用TextView,false 事件结束
        return widget.isClickable && super.onTouchEvent(widget, buffer, event)
    }

    companion object {
        private var sInstance: FocusLinkMovementMethod? = null
        val instance: MovementMethod?
            get() {
                if (sInstance == null) {
                    sInstance = FocusLinkMovementMethod()
                }
                return sInstance
            }
    }
}