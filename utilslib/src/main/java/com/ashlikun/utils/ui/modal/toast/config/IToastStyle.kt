package com.ashlikun.utils.ui.modal.toast.config

import android.content.Context
import android.view.Gravity
import android.view.View


/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 21:48
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：默认样式接口
 */
interface IToastStyle<V : View> {
    /**
     * 创建 Toast 视图
     */
    fun createView(context: Context): V

    /**
     * 获取 Toast 显示重心
     */
    fun getGravity(): Int {
        return Gravity.CENTER
    }

    /**
     * 获取 Toast 水平偏移
     */
    fun getXOffset(): Int {
        return 0
    }

    /**
     * 获取 Toast 垂直偏移
     */
    fun getYOffset(): Int {
        return 0
    }

    /**
     * 获取 Toast 水平间距
     */
    fun getHorizontalMargin(): Int {
        return 0
    }

    /**
     * 获取 Toast 垂直间距
     */
    fun getVerticalMargin(): Int {
        return 0
    }
}