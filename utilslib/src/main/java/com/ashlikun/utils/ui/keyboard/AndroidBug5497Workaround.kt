package com.ashlikun.utils.ui.keyboard

import android.R
import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import com.ashlikun.utils.other.DimensUtils.dip2px
import com.ashlikun.utils.ui.ScreenInfoUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 18:55
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：功能介绍：
 * 在非全屏模式下，将activity的windowSoftInputMode的属性设置为
 * ：adjustResize。同时在View的onSizeChanged(int w, int h, int oldw, int oldh)里可以得到变化后的尺寸，
 * 然后根据前后变化的结果来计算屏幕需要移动的距离。
 *
 *
 * 在你的Activity的oncreate()方法里调用AndroidBug5497Workaround.assistActivity(this);即可。注意：在setContentView(R.layout.xxx)之后调用。
 */

class AndroidBug5497Workaround private constructor(window: Window) {
    private val mChildOfContent: View
    private var usableHeightPrevious = 0
    private val frameLayoutParams: FrameLayout.LayoutParams
    private var contentHeight = 0
    private var isfirst = true
    private val statusBarHeight: Int

    /**
     * 软键盘监听
     */
    var onSoftPop: OnSoftPop? = null
    var onSoftClose: OnSoftClose? = null

    /**
     * 重新调整跟布局的高度
     */
    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        //当前可见高度和上一次可见高度不一致 布局变动
        if (usableHeightNow != usableHeightPrevious) {
            //int usableHeightSansKeyboard2 = mChildOfContent.getHeight();//兼容华为等机型
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                    frameLayoutParams.height =
                        usableHeightSansKeyboard - heightDifference + statusBarHeight
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
                }
            } else {
                frameLayoutParams.height = contentHeight
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
            if (ScreenInfoUtils.getHeight() - usableHeightNow > dip2px(150f)) {
                onSoftPop?.invoke(ScreenInfoUtils.getHeight() - usableHeightNow)
            } else {
                onSoftClose?.invoke()
            }
        }
    }

    /**
     * 计算mChildOfContent可见高度     ** @return
     */
    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top
    }


    companion object {
        operator fun get(activity: Activity): AndroidBug5497Workaround {
            return AndroidBug5497Workaround(activity.window)
        }

        operator fun get(window: Window): AndroidBug5497Workaround {
            return AndroidBug5497Workaround(window)
        }
    }

    init {
        //获取状态栏的高度
        val resourceId =
            window.decorView.resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = window.decorView.resources.getDimensionPixelSize(resourceId)
        val content = window.findViewById<View>(R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)

        //界面出现变动都会调用这个监听事件
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener {
            if (isfirst) {
                //兼容华为等机型
                contentHeight = mChildOfContent.height
                isfirst = false
            }
            possiblyResizeChildOfContent()
        }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }
}