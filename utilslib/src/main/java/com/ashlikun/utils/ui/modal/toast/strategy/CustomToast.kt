package com.ashlikun.utils.ui.modal.toast.strategy

import android.R
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ashlikun.utils.ui.modal.toast.config.IToast

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:13
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：自定义 Toast 基类
 */

abstract class CustomToast : IToast {
    companion object {
        /**
         * 延迟时间
         */
        const val DELAY_TIMEOUT = 200L
    }

    /** Toast 布局  */
    private var mView: View? = null

    /** Toast 消息 View  */
    private var mMessageView: TextView? = null

    /** Toast 显示重心  */
    private var mGravity = 0

    /** Toast 显示时长  */
    private var mDuration = 0

    /** 水平偏移  */
    private var mXOffset = 0

    /** 垂直偏移  */
    private var mYOffset = 0

    /** 水平间距  */
    private var mHorizontalMargin = 0f

    /** 垂直间距  */
    private var mVerticalMargin = 0f

    /** Toast 动画  */
    open var animations = R.style.Animation_Toast

    /** 短吐司显示的时长  */
    open var shortDuration = 2000

    /** 长吐司显示的时长  */
    open var longDuration = 3500

    override fun setText(id: Int) {
        if (mView == null) {
            return
        }
        setText(mView!!.resources.getString(id))
    }

    override fun setText(text: CharSequence) {
        mMessageView?.text = text
    }

    override fun setView(view: View) {
        mView = view
        mMessageView = findMessageView()
    }

    override fun getView(): View? {
        return mView
    }

    override fun setDuration(duration: Int) {
        mDuration = duration
    }

    override fun getDuration(): Int {
        return mDuration
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
    }

    override fun getGravity(): Int {
        return mGravity
    }

    override fun getXOffset(): Int {
        return mXOffset
    }

    override fun getYOffset(): Int {
        return mYOffset
    }

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun getHorizontalMargin(): Float {
        return mHorizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return mVerticalMargin
    }

    fun getCalculateDuration(): Long {
        if (mDuration == Toast.LENGTH_LONG) {
            return longDuration.toLong()
        } else if (mDuration == Toast.LENGTH_SHORT) {
            return shortDuration.toLong()
        }
        return mDuration.toLong()
    }

}