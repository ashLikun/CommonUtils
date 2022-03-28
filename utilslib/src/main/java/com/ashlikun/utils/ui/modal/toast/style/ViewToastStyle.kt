package com.ashlikun.utils.ui.modal.toast.style

import android.content.Context
import android.view.Gravity
import android.view.View
import com.ashlikun.utils.ui.modal.toast.config.IToastStyle

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 22:00
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Toast View 包装样式实现
 */
open class ViewToastStyle(private val view: View, private val mStyle: IToastStyle<*>?) :
    IToastStyle<View> {
    override fun createView(context: Context): View {
        return view
    }

    override fun getGravity(): Int {
        return mStyle?.getGravity() ?: Gravity.CENTER
    }

    override fun getXOffset(): Int {
        return mStyle?.getXOffset() ?: 0
    }

    override fun getYOffset(): Int {
        return mStyle?.getYOffset() ?: 0
    }

    override fun getHorizontalMargin() = mStyle?.getHorizontalMargin() ?: 0

    override fun getVerticalMargin() = mStyle?.getVerticalMargin() ?: 0
}