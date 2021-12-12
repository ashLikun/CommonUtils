package com.ashlikun.utils.ui.extend

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.view.ViewCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2021/4/10　3:15
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */

typealias OnAttachedChangeWindow = () -> Unit


/**
 * view树绘制的回调
 *
 * @param onDraw 监听回调
 * @param isOneToRemove 是否只监听一次
 */
inline fun View?.addOnDrawListener(crossinline listener: () -> Unit, isOneToRemove: Boolean = true) {
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
inline fun View?.addOnPreDrawListener(crossinline listener: () -> Boolean, isOneToRemove: Boolean = true) {
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
inline fun View?.addOnGlobalLayoutListener(crossinline listener: () -> Unit, isOneToRemove: Boolean = true) {
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
inline fun View?.addOnWindowFocusChangeListener(crossinline listener: (hasFocus: Boolean) -> Unit, isOneToRemove: Boolean = true) {
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
 * 监听AttachedToWindow状态
 */
inline fun View?.getAttachedToWindow(crossinline onAttachedChangeWindow: OnAttachedChangeWindow) {
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
inline fun View?.getDetachedFromWindow(view: View, crossinline onAttachedChangeWindow: OnAttachedChangeWindow) {
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

/**
 * 点击
 */
inline fun View?.click(delay: Long = 500, crossinline onClick: (view: View) -> Unit) {
    this?.setOnClickListener {
        if (delay > 0) {
            this.isClickable = false
        }
        onClick(this)
        if (delay > 0) {
            this.postDelayed({
                this.isClickable = true
            }, delay)
        }
    }
}

/**
 * 长击
 */
inline fun View?.clickLong(crossinline onClick: (view: View) -> Boolean) {
    this?.setOnLongClickListener {
        onClick(this)

    }
}

/**
 * 文本改变事件
 */
fun TextView?.textChang(
        before: ((s: CharSequence, start: Int, count: Int, after: Int) -> Unit)? = null,
        after: ((s: Editable) -> Unit)? = null,
        chang: (s: CharSequence, start: Int, before: Int, count: Int) -> Unit
) {
    this?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            before?.invoke(s ?: "", start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            chang?.invoke(s ?: "", start, before, count)
        }

        override fun afterTextChanged(s: Editable) {
            after?.invoke(s)
        }

    })
}

/**
 * 键盘事件
 */
inline fun View?.key(crossinline key: (view: View, code: Int, event: KeyEvent) -> Boolean) {
    this?.setOnKeyListener { v, keyCode, event ->
        key(v, keyCode, event)
    }
}

/**
 * CheckBox事件
 */
inline fun CompoundButton?.checkedChang(crossinline checked: (view: CompoundButton, isChecked: Boolean) -> Boolean) {
    this?.setOnCheckedChangeListener { buttonView, isChecked -> checked(buttonView, isChecked) }
}

/**
 * 触摸
 */
inline fun View?.touch(crossinline touch: (view: View, event: MotionEvent) -> Boolean) {
    this?.setOnTouchListener { v, event ->
        touch(v, event)
    }
}

/**
 * 焦点改变
 */
inline fun View?.focusChang(crossinline focus: (view: View, hasFocus: Boolean) -> Unit) {
    this?.setOnFocusChangeListener { v, hasFocus ->
        focus(v, hasFocus)
    }
}