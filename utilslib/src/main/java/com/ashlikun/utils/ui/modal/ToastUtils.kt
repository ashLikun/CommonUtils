package com.ashlikun.utils.ui.modal

import android.content.res.Resources.NotFoundException
import android.view.LayoutInflater
import android.view.View
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.ui.extend.resString
import com.ashlikun.utils.ui.modal.toast.ToastLogInterceptor
import com.ashlikun.utils.ui.modal.toast.ToastStrategy
import com.ashlikun.utils.ui.modal.toast.ToastSystemStrategy
import com.ashlikun.utils.ui.modal.toast.config.IToastInterceptor
import com.ashlikun.utils.ui.modal.toast.config.IToastStrategy
import com.ashlikun.utils.ui.modal.toast.config.IToastStyle
import com.ashlikun.utils.ui.modal.toast.style.BlackToastStyle
import com.ashlikun.utils.ui.modal.toast.style.LocationToastStyle
import com.ashlikun.utils.ui.modal.toast.style.ViewToastStyle
import com.ashlikun.utils.ui.modal.toast.style.WhiteToastStyle


/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 22:15
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：Toast 框架（专治 Toast 疑难杂症）
 */

object ToastUtils {
    /**
     * Toast 默认处理策略
     */
    private val defaultStrategy by lazy {
        ToastSystemStrategy().apply {
            bindStyle(toastStyle!!)
        }
    }

    /**
     * Toast 默认样式
     */
    private val defaultToastStyle by lazy {
        BlackToastStyle()
    }

    /**
     * Toast 处理策略
     */
    var toastStrategy: IToastStrategy? = null
        get() = field ?: defaultStrategy

    /**
     * Toast 样式
     */
    var toastStyle: IToastStyle<*>? = null
        get() = field ?: defaultToastStyle

    /**
     * 设置 Toast 拦截器（可以根据显示的内容决定是否拦截这个Toast）
     * 场景：打印 Toast 内容日志、根据 Toast 内容是否包含敏感字来动态切换其他方式显示（这里可以使用我的另外一套框架 XToast）
     */
    var interceptor: IToastInterceptor = ToastLogInterceptor()

    /**
     * 显示 Toast
     */
    fun show(id: Int) {
        try {
            // 如果这是一个资源 id
            show(id.resString)
        } catch (ignored: NotFoundException) {
            // 如果这是一个 int 整数
            show(id.toString())
        }
    }

    /**
     * 获取一个toast
     */
    fun create(style: IToastStyle<*>) = ToastSystemStrategy().apply {
        bindStyle(style)
    }

    /**
     * 创建一个以window为载体的自定义toast
     */
    fun createWindow(style: IToastStyle<*>) = ToastStrategy().apply {
        bindStyle(style)
    }

    fun show(text: CharSequence) {
        // 如果是空对象或者空文本就不显示
        if (text.isEmpty()) return
        toastStrategy?.show(text)
    }

    /**
     * 取消吐司的显示
     */
    fun cancel() {
        toastStrategy?.cancel()
    }

    /**
     * 设置吐司的位置
     *
     * @param gravity 重心
     */
    fun setGravity(gravity: Int) {
        setGravity(gravity, 0, 0)
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        setGravity(gravity, xOffset, yOffset, 0f, 0f)
    }

    fun setGravity(
        gravity: Int, xOffset: Int, yOffset: Int, horizontalMargin: Float, verticalMargin: Float
    ) {
        toastStrategy?.bindStyle(
            LocationToastStyle(
                toastStyle!!, gravity, xOffset, yOffset, horizontalMargin.toInt(),
                verticalMargin.toInt()
            )
        )
    }

    /**
     * 给当前 Toast 设置新的布局
     */
    fun setView(id: Int) {
        if (id == View.NO_ID) return
        setStyle(ViewToastStyle(LayoutInflater.from(AppUtils.app).inflate(id, null), toastStyle))
    }

    /**
     * 初始化全局的 Toast 样式
     *
     * @param style         样式实现类，框架已经实现两种不同的样式
     * 黑色样式：[BlackToastStyle]
     * 白色样式：[WhiteToastStyle]
     */
    fun setStyle(style: IToastStyle<*>) {
        toastStyle = style
        toastStrategy?.bindStyle(style)
    }
}