package com.ashlikun.utils.ui.modal.toast.style

import android.content.Context
import android.view.View
import com.ashlikun.utils.ui.modal.toast.config.IToastStyle

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 21:58
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍： Toast 位置包装样式实现
 */

class LocationToastStyle @JvmOverloads constructor(
    private val mStyle: IToastStyle<*>,
    private val mGravity: Int,
    private val mXOffset: Int = 0,
    private val mYOffset: Int = 0,
    private val mHorizontalMargin: Int = 0,
    private val mVerticalMargin: Int = 0
) : IToastStyle<View> {
    override fun createView(context: Context): View {
        return mStyle.createView(context)
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

    override fun getHorizontalMargin() = mHorizontalMargin

    override fun getVerticalMargin() = mVerticalMargin
}