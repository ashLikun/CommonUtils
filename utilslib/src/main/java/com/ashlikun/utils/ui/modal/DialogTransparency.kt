package com.ashlikun.utils.ui.modal

import kotlin.jvm.JvmOverloads
import android.content.Context
import com.ashlikun.utils.R
import android.app.Dialog
import android.view.View
import android.os.Bundle
import android.view.WindowManager
import com.ashlikun.utils.ui.ScreenInfoUtils
import android.view.Gravity

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/14 23:20
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：全屏的透明对话框
 */

class DialogTransparency @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.SnackBarTransparency
) : Dialog(
    context, themeResId
) {
    init {
        val view = View(context)
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val lp = window!!.attributes
        //设置宽度
        lp.width = ScreenInfoUtils.getWidth()
        //设置宽度
        lp.height = ScreenInfoUtils.getHeight()
        window!!.attributes = lp
        window!!.attributes.gravity = Gravity.CENTER
    }


}