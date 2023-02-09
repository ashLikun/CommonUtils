package com.ashlikun.utils.ui.modal.toast.config


/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:05
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：Toast 处理策略
 */


interface IToastStrategy {
    /**
     * 绑定样式
     */
    fun bindStyle(style: IToastStyle<*>)

    /**
     * 创建 Toast
     */
    fun create(): IToast
    fun getToast(): IToast?

    /**
     * 显示和隐藏的回调
     * @param isOne 是否只回调一次，防止常驻
     */
    fun addCallback(isOne: Boolean = true, callback: OnCallback)
    fun removeCallback(callback: OnCallback)

    /**
     * 显示 Toast
     */
    fun show(text: CharSequence, delayMillis: Long = 0)
    fun show()

    /**
     * 取消 Toast
     */
    fun cancel()

    /**
     * toast消失时候的回调
     */
    fun onHidden()

    /**
     * Toast显示时候的回调
     */
    fun onShow()

    /**
     * 显示时间
     */
    var duration: Int?
}