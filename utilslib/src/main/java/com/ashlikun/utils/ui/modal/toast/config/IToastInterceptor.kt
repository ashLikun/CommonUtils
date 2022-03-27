package com.ashlikun.utils.ui.modal.toast.config

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/ToastUtils
 * time   : 2019/05/19
 * desc   : Toast 拦截器接口
 */
interface IToastInterceptor {
    /**
     * 根据显示的文本决定是否拦截该 Toast
     */
    fun intercept(text: CharSequence): Boolean
}