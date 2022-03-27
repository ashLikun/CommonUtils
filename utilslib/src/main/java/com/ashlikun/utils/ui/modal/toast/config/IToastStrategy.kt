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

    /**
     * 显示 Toast
     */
    fun show(text: CharSequence, delayMillis: Long = 0)
    fun show()

    /**
     * 取消 Toast
     */
    fun cancel()
}