package com.ashlikun.utils.ui.modal.toast.config

/**
 * 作者　　: 李坤
 * 创建时间: 2022/3/27　21:15
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：toast显示的回调
 */
abstract class ICallToastStrategy : IToastStrategy {
    override var duration: Int? = null

    data class Wap(var status: Boolean, val isOne: Boolean, var callback: OnCallback)

    protected val callback: OnCallback = {
        if (it) {
            onShow()
        } else {
            onHidden()
        }
    }

    //只回调一次
    val callList = mutableListOf<Wap>()
    override fun addCallback(isOne: Boolean, callback: OnCallback) {
        if (callList.find { it.callback == callback } == null) {
            callList.add(Wap(false, isOne, callback))
        }
    }

    override fun removeCallback(callback: OnCallback) {
        callList.removeAll { it.callback == callback }
    }

    override fun onShow() {
        callList.forEach {
            it.callback(true)
            it.status = true
        }
    }

    override fun onHidden() {
        callList.forEach {
            if (it.status) {
                it.callback(false)
            }
        }
        //清楚一次成功的回调
        callList.removeAll { it.isOne }
    }
}