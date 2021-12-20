package com.ashlikun.utils.ui.modal

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ashlikun.utils.R
import com.ashlikun.utils.ui.UiUtils
import com.ashlikun.utils.ui.keyboard.KeyboardUtils.exitInput
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.google.android.material.snackbar.SnackbarContentLayout

/**
 * 作者　　: 李坤
 * 创建时间: 16:35 Administrator
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：底部弹出消息Snackbar的工具类
 */
object SnackbarUtil {
    const val Info = 1
    const val Confirm = 2
    const val Warning = 3 //警告 orange
    const val Error = 4 //错误 red
    var red = -0xbbcca
    var green = -0xb350b0
    var blue = -0xde6a0d
    var orange = -0x3ef9

    /**
     * 自定义时常显示Snackbar，自定义颜色
     */
    fun showIndefinite(
        view: View,
        message: String,
        duration: Int,
        messageColor: Int,
        backgroundColor: Int
    ): Snackbar {
        exitInput(view)
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_INDEFINITE)
            .setDuration(duration)
        setSnackbarColor(snackbar, messageColor, backgroundColor)
        return snackbar
    }

    fun showShort(activity: Activity, message: String, type: Int): Snackbar {
        exitInput(activity)
        val snackbar = Snackbar.make(UiUtils.getRootView(activity), message, Snackbar.LENGTH_SHORT)
        switchType(snackbar, type)
        return snackbar
    }

    fun showLong(activity: Activity, message: String, type: Int): Snackbar {
        exitInput(activity)
        val snackbar = Snackbar.make(UiUtils.getRootView(activity), message, Snackbar.LENGTH_LONG)
        switchType(snackbar, type)
        return snackbar
    }

    fun showIndefinite(activity: Activity, message: String, type: Int): Snackbar {
        exitInput(activity)
        val snackbar =
            Snackbar.make(UiUtils.getRootView(activity), message, Snackbar.LENGTH_INDEFINITE)
        switchType(snackbar, type)
        return snackbar
    }

    /**
     * 短显示Snackbar，可选预设类型
     */
    fun showShort(view: View, message: String, type: Int): Snackbar {
        exitInput(view)
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        switchType(snackbar, type)
        return snackbar
    }

    /**
     * 长显示Snackbar，可选预设类型
     */
    fun showLong(view: View, message: String, type: Int): Snackbar {
        exitInput(view)
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        switchType(snackbar, type)
        return snackbar
    }

    /**
     * 自定义时常显示Snackbar，可选预设类型
     *
     * @param view
     * @param message
     * @param type
     * @return
     */
    fun showIndefinite(view: View, message: String, duration: Int, type: Int): Snackbar {
        exitInput(view)
        val snackbar =
            Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setDuration(duration)
        switchType(snackbar, type)
        return snackbar
    }

    /**
     * 选择预设类型
     */
    private fun switchType(snackbar: Snackbar, type: Int) {
        when (type) {
            Info -> setSnackbarColor(snackbar, Color.WHITE, blue)
            Confirm -> setSnackbarColor(snackbar, Color.WHITE, green)
            Warning -> setSnackbarColor(snackbar, Color.WHITE, orange)
            Error -> setSnackbarColor(snackbar, Color.YELLOW, red)
        }
    }

    /**
     * 设置Snackbar背景颜色
     */
    fun setSnackbarColor(snackbar: Snackbar, backgroundColor: Int) {
        val view = snackbar.view
        view.fitsSystemWindows = false
        if (view != null) {
            view.setBackgroundColor(backgroundColor)
        }
    }

    /**
     * 设置Snackbar文字和背景颜色
     */
    fun setSnackbarColor(snackbar: Snackbar, messageColor: Int, backgroundColor: Int) {
        val view = snackbar.view
        view.fitsSystemWindows = false
        if (view != null) {
            view.setBackgroundColor(backgroundColor)
            val viewGroup = view as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                if (viewGroup.getChildAt(i) is SnackbarContentLayout) {
                    ((viewGroup.getChildAt(i) as SnackbarContentLayout).getChildAt(0) as TextView).setTextColor(
                        messageColor
                    )
                }
            }
        }
    }

    /**
     * 向Snackbar中添加view
     * @param index    新加布局在Snackbar中的位置
     */
    fun SnackbarAddView(snackbar: Snackbar, layoutId: Int, index: Int) {
        val snackbarview = snackbar.view
        val snackbarLayout = snackbarview as SnackbarLayout
        val add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId, null)
        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        p.gravity = Gravity.CENTER_VERTICAL
        snackbarLayout.addView(add_view, index, p)
    }

    /**
     * 显示信息
     */
    fun showSnackbar(
        activity: Activity,
        view: View,
        result: String,
        type: Int,
        isFinish: Boolean,
        callback: Snackbar.Callback? = null
    ) {
        val snackbar = showLong(view, result, type)
        if (isFinish) {
            val dialog = DialogTransparency(activity)
            dialog.show()
            snackbar.addCallback(if (isFinish) object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    super.onDismissed(snackbar, event)
                    dialog.dismiss()
                    if (callback != null) {
                        callback.onDismissed(snackbar, event)
                    } else {
                        activity.finish()
                    }
                }
            } else null)
        } else {
            snackbar.setAction(R.string.snackbar_action) { }
        }
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.show()
    }
}