package com.ashlikun.utils.ui.modal.toast.config

import android.view.View
import android.widget.TextView

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:12
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：Toast 接口
 */
typealias OnCallback = (isShow: Boolean) -> Unit

interface IToast {
    var callback: OnCallback?

    /**
     * 显示
     */
    fun show()

    /**
     * 取消
     */
    fun cancel()

    /**
     * 设置文本
     */
    fun setText(id: Int)
    fun setText(text: CharSequence)

    /**
     * 设置布局
     */
    fun setView(view: View)
    fun getView(): View?


    /**
     * 设置显示时长
     */
    fun setDuration(duration: Int)

    /**
     * 获取显示时长
     */
    fun getDuration(): Int

    /**
     * 设置重心偏移
     */
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int)

    /**
     * 获取显示重心
     */
    fun getGravity(): Int

    /**
     * 获取水平偏移
     */
    fun getXOffset(): Int

    /**
     * 获取垂直偏移
     */
    fun getYOffset(): Int

    /**
     * 设置水平间距
     */
    fun getHorizontalMargin(): Float

    /**
     * 设置垂直间距
     */
    fun getVerticalMargin(): Float

    /**
     * 设置屏幕间距
     */
    fun setMargin(horizontalMargin: Float, verticalMargin: Float)

    /**
     * 智能获取用于显示消息的 TextView
     */
    fun findMessageView(): TextView? {
        val view = getView() ?: return null
        if (view is TextView) {
            if (view.getId() == View.NO_ID) {
                view.setId(android.R.id.message)
            } else require(view.getId() == android.R.id.message) {
                // 必须将 TextView 的 id 值设置成 android.R.id.message
                "You must set the ID value of TextView to android.R.id.message"
            }
            return view
        }
        val messageView = view.findViewById<View>(android.R.id.message)
        if (messageView is TextView) {
            return messageView
        }
        return null
//        throw IllegalArgumentException("You must include a TextView with an ID value of android.R.id.message")
    }


}