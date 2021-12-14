package com.ashlikun.utils.ui.keyboard

import kotlin.jvm.JvmOverloads
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import java.util.ArrayList
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.graphics.Rect

/**
 * 软键盘弹起
 */
typealias  OnSoftPop = (height: Int) -> Unit
/**
 * 软键盘关闭
 */
typealias  OnSoftClose = () -> Unit

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
open class SoftKeyboardSizeWatchLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    var oldh = -1
    var nowh = -1
    var screenHeight = 0
    var isSoftKeyboardPop = false
        protected set

    /**
     * 软键盘监听
     */
    var onSoftPops: MutableList<OnSoftPop> = mutableListOf()
    var onSoftClose: MutableList<OnSoftClose> = mutableListOf()

    init {
        viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            screenHeight = if (screenHeight == 0) {
                r.bottom
            } else {
                //防止第一次的时候获取的是假的
                Math.max(screenHeight, r.bottom)
            }
            nowh = screenHeight - r.bottom
            if (oldh != -1 && nowh != oldh) {
                if (nowh > 0) {
                    isSoftKeyboardPop = true
                    onSoftPops.forEach {
                        it.invoke(nowh)
                    }
                } else {
                    isSoftKeyboardPop = false
                    onSoftClose.forEach {
                        it.invoke()
                    }
                }
            }
            oldh = nowh
        }
    }
}