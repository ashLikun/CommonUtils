package com.ashlikun.utils.ui.modal.toast.strategy

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.ui.modal.toast.config.IToast
import com.ashlikun.utils.ui.modal.toast.config.OnCallback
import kotlinx.coroutines.Runnable

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 20:30
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：系统 Toast
 */

open class SystemToast(context: Context) : IToast {
    val toast by lazy {
        Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }
    override var callback: OnCallback? = null

    /** 短吐司显示的时长  */
    open var shortDuration = 2000

    /** 长吐司显示的时长  */
    open var longDuration = 3500


    /** 吐司消息 View  */
    private var mMessageView: TextView? = null

    override fun setView(view: View) {
        toast.setView(view)
        mMessageView = findMessageView()
    }

    override fun getView() = toast.view

    override fun setDuration(duration: Int) {
        if (duration == Toast.LENGTH_LONG) {
            toast.duration = duration
        } else if (duration == Toast.LENGTH_SHORT) {
            toast.duration = duration
        } else if (duration > shortDuration) {
            toast.duration = Toast.LENGTH_LONG
        } else {
            toast.duration = Toast.LENGTH_SHORT
        }
    }

    override fun getDuration() = toast.duration

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        toast.setGravity(gravity, xOffset, yOffset)
    }

    override fun getGravity() = toast.gravity

    override fun getXOffset() = toast.xOffset

    override fun getYOffset() = toast.yOffset

    override fun getHorizontalMargin() = toast.horizontalMargin

    override fun getVerticalMargin() = toast.verticalMargin

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        toast.setMargin(horizontalMargin, verticalMargin)
    }


    override fun show() {
        if (callback != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val call = object : Toast.Callback() {
                    override fun onToastShown() {
                        super.onToastShown()
                        callback?.invoke(true)
                    }


                    override fun onToastHidden() {
                        super.onToastHidden()
                        callback?.invoke(false)
                    }
                }
                toast.removeCallback(call)
                toast.addCallback(call)
            } else {
                callback?.invoke(true)
                // 添加一个移除吐司的任务
                MainHandle.get().postDelayed(
                    runnable = { callback?.invoke(false) },
                    delayMillis = if (getDuration() == Toast.LENGTH_LONG) longDuration.toLong() else shortDuration.toLong()
                )
            }
        }
        toast.show()
    }

    override fun cancel() {
        toast.cancel()
    }

    override fun setText(id: Int) {
        toast.setText(id)
        mMessageView?.setText(id)
    }

    override fun setText(text: CharSequence) {
        toast.setText(text)
        mMessageView?.text = text
    }
}