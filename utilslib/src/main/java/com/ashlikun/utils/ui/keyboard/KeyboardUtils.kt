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
import android.widget.EditText
import android.widget.FrameLayout
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.DimensUtils.dip2px
import com.ashlikun.utils.other.store.StoreUtils
import com.ashlikun.utils.ui.extend.addOnGlobalLayoutListener
import kotlin.math.abs

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 16:07
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：软键盘工具
 */

inline fun View.exitInput() = KeyboardUtils.exitInput(this)
inline fun Activity.exitInput() = KeyboardUtils.exitInput(this)
inline fun View.showInput() = KeyboardUtils.showInput(this)
inline fun View.setOnKeyboardChang(noinline call: OnSoftPopOrClose): KeyboardOnGlobalLayoutListener? =
    KeyboardUtils.setOnInputChang(this, call = call)

inline fun Activity.setOnKeyboardChang(noinline call: OnSoftPopOrClose): KeyboardOnGlobalLayoutListener? =
    KeyboardUtils.setOnInputChang(this, call = call)

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
        if (view == null) return
        exitOrShowInput(null, view)
    }

    /**
     * 退出软键盘
     */
    fun exitInput(activity: Activity) {
        if (activity == null) return
        if (activity.currentFocus?.windowToken != null) {
            exitOrShowInput(activity.currentFocus?.windowToken!!, null)
        }
    }

    /**
     * 退出软键盘
     */
    fun exitInput(iBinder: IBinder) {
        if (iBinder == null) return
        exitOrShowInput(iBinder, null)
    }

    /**
     * 退出软键盘
     */
    fun exitInput(view: View?) {
        if (view == null) return
        if (view is EditText) {
            view.clearFocus()
        }
        exitOrShowInput(view.windowToken, null)
    }

    /**
     * 软键盘是否打开
     *
     * @param activity
     * @return
     */
    fun isOpenInput(activity: Activity?): Boolean {
        if (activity == null) return false
        return isOpenInput(activity.window.decorView)
    }

    /**
     * 软键盘是否打开
     *
     * @param view 页面跟布局
     */
    fun isOpenInput(view: View?): Boolean {
        if (view == null) return false
        //获取当前屏幕内容的高度
        val screenHeight = view.height
        //获取View可见区域的bottom
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        return abs(screenHeight - rect.bottom) - 200 > 0
    }

    fun setOnInputChang(
        activity: Activity?, onSoftPop: OnSoftPop? = null,
        onSoftClose: OnSoftClose? = null, call: OnSoftPopOrClose? = null
    ): KeyboardOnGlobalLayoutListener? {
        if (activity == null) return null
        val content = activity.findViewById<FrameLayout>(R.id.content)
        return setOnInputChang(content.getChildAt(0), onSoftPop, onSoftClose, call)
    }

    fun setOnInputChang(
        window: Window?, onSoftPop: OnSoftPop? = null,
        onSoftClose: OnSoftClose? = null, call: OnSoftPopOrClose? = null
    ): KeyboardOnGlobalLayoutListener? {
        if (window == null) return null
        return setOnInputChang(window.decorView, onSoftPop, onSoftClose, call)
    }

    fun setOnInputChang(
        view: View?, onSoftPop: OnSoftPop? = null,
        onSoftClose: OnSoftClose? = null, call: OnSoftPopOrClose? = null
    ): KeyboardOnGlobalLayoutListener? {
        if (view == null) return null
        val lis = KeyboardOnGlobalLayoutListener(view, onSoftPop, onSoftClose, call)
        //界面出现变动都会调用这个监听事件
        view.viewTreeObserver.addOnGlobalLayoutListener(lis)
        view.setTag(0x99886633.toInt(), lis)
        return lis
    }
}

class KeyboardOnGlobalLayoutListener(
    var view: View,
    var onSoftPop: OnSoftPop? = null,
    var onSoftClose: OnSoftClose? = null,
    var onSoftChange: OnSoftPopOrClose? = null
) : OnGlobalLayoutListener {
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
                onSoftChange?.invoke(true, mNowh)
            } else {
                onSoftClose?.invoke()
                onSoftChange?.invoke(false, mNowh)
            }
        }
        mOldh = mNowh
    }
}