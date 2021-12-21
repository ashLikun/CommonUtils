package com.ashlikun.utils.ui.keyboard

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.IBinder
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.DimensUtils.dip2px
import com.ashlikun.utils.other.store.StoreUtils
import kotlin.math.abs

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 16:07
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：软键盘工具
 */

object KeyboardUtils {
    private const val EXTRA_DEF_KEYBOARDHEIGHT = "DEF_KEYBOARDHEIGHT"
    private const val DEF_KEYBOARD_HEAGH_WITH_DP = 300
    private var sDefKeyboardHeight = -1
    fun getDefKeyboardHeight(): Int {
        if (sDefKeyboardHeight < 0) {
            sDefKeyboardHeight = dip2px(AppUtils.app, DEF_KEYBOARD_HEAGH_WITH_DP.toFloat())
        }
        val height = StoreUtils.getInt(EXTRA_DEF_KEYBOARDHEIGHT, 0)
        return if (height > 0 && sDefKeyboardHeight != height) height else sDefKeyboardHeight.also {
            sDefKeyboardHeight = it
        }
    }

    fun setDefKeyboardHeight(context: Context?, height: Int) {
        if (sDefKeyboardHeight != height) {
            StoreUtils.putInt(EXTRA_DEF_KEYBOARDHEIGHT, height)
            sDefKeyboardHeight = height
        }
    }

    private fun exitOrShowInput(iBinder: IBinder?, view: View?) {
        val inputMethodManager =
            AppUtils.app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (iBinder != null) {
            inputMethodManager.hideSoftInputFromWindow(iBinder, 0)
        }
        if (view != null) {
            view.requestFocus()
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }
    }

    /**
     * 显示软键盘
     */
    fun showInput(view: View) {
        exitOrShowInput(null, view)
    }

    /**
     * 退出软键盘
     */
    fun exitInput(activity: Activity) {
        if (activity.currentFocus?.windowToken != null) {
            exitOrShowInput(activity.currentFocus?.windowToken!!, null)
        }
    }

    /**
     * 退出软键盘
     */
    fun exitInput(iBinder: IBinder) {
        exitOrShowInput(iBinder, null)
    }

    /**
     * 退出软键盘
     */
    fun exitInput(view: View) {
        exitOrShowInput(view.windowToken, null)
    }

    /**
     * 软键盘是否打开
     *
     * @param activity
     * @return
     */
    fun isOpenInput(activity: Activity): Boolean {
        return isOpenInput(activity.window.decorView)
    }

    /**
     * 软键盘是否打开
     *
     * @param view 页面跟布局
     */
    fun isOpenInput(view: View): Boolean {
        //获取当前屏幕内容的高度
        val screenHeight = view.height
        //获取View可见区域的bottom
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        return abs(screenHeight - rect.bottom) - 200 > 0
    }

    fun setOnInputChang(
        activity: Activity, onSoftPop: OnSoftPop? = null,
        onSoftClose: OnSoftClose? = null
    ) {
        val content = activity.findViewById<FrameLayout>(R.id.content)
        setOnInputChang(content.getChildAt(0), onSoftPop, onSoftClose)
    }

    fun setOnInputChang(
        window: Window,
        onSoftPop: OnSoftPop? = null,
        onSoftClose: OnSoftClose? = null
    ) {
        setOnInputChang(window.decorView, onSoftPop, onSoftClose)
    }

    fun setOnInputChang(
        view: View, onSoftPop: OnSoftPop? = null,
        onSoftClose: OnSoftClose? = null
    ) {
        //界面出现变动都会调用这个监听事件
        view.viewTreeObserver.addOnGlobalLayoutListener(
            KeyboardOnGlobalLayoutListener(
                view,
                onSoftPop, onSoftClose
            )
        )
    }

}

class KeyboardOnGlobalLayoutListener(
    var view: View,
    var onSoftPop: OnSoftPop? = null,
    var onSoftClose: OnSoftClose? = null
) :
    OnGlobalLayoutListener {
    var mNowh = 0
    var mOldh = 0
    var mScreenHeight = 0
    override fun onGlobalLayout() {
        val r = Rect()
        view.getWindowVisibleDisplayFrame(r)
        mScreenHeight = if (mScreenHeight == 0) {
            r.bottom
        } else {
            //防止第一次的时候获取的是假的
            Math.max(mScreenHeight, r.bottom)
        }
        mNowh = mScreenHeight - r.bottom
        if (mOldh != -1 && mNowh != mOldh) {
            if (mNowh > 0) {
                onSoftPop?.invoke(mNowh)
            } else {
                onSoftClose?.invoke()
            }
        }
        mOldh = mNowh
    }
}