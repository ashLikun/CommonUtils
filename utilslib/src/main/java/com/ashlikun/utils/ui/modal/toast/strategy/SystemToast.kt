package com.ashlikun.utils.ui.modal.toast.strategy

import android.app.Application
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ashlikun.utils.ui.modal.toast.config.IToast

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 20:30
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：系统 Toast
 */

open class SystemToast(application: Application) : IToast {
    val toast by lazy {
        Toast.makeText(application, "", Toast.LENGTH_SHORT)
    }

    /** 吐司消息 View  */
    private var mMessageView: TextView? = null

    override fun setView(view: View) {
        toast.setView(view)
        mMessageView = findMessageView()
    }

    override fun getView() = toast.view

    override fun setDuration(duration: Int) {
        toast.duration = duration
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