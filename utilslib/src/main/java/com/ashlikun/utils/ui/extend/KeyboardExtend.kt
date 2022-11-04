package com.ashlikun.utils.ui.extend

import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2021/1/22　16:30
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：软键盘和状态栏的一些扩展
 */
inline fun Window.insetsControllerX(view: View = decorView): WindowInsetsControllerCompat? {
    return WindowCompat.getInsetsController(this, view)
}


/**
 * 显示软键盘
 */
inline fun Window.showIme(view: View = decorView) {
    insetsControllerX(view)?.show(WindowInsetsCompat.Type.ime())
}

/**
 * 隐藏软键盘
 */
inline fun Window.hineIme(view: View = decorView) {
    insetsControllerX(view)?.hide(WindowInsetsCompat.Type.ime())
}

/**
 * 显示状态栏
 */
inline fun Window.showStatusBar(view: View = decorView) {
    insetsControllerX(view)?.show(WindowInsetsCompat.Type.statusBars())
}


/**
 * 隐藏状态栏
 */
inline fun Window.hideStatusBar(view: View = decorView) {
    insetsControllerX(view)?.hide(WindowInsetsCompat.Type.statusBars())
}

/**
 * 设置状态栏字体颜色
 */
inline fun Window.setLightStatusBars(isLight: Boolean, view: View = decorView) {
    insetsControllerX(view)?.isAppearanceLightStatusBars = isLight
}

/**
 * 设置状态栏字体颜色
 */
inline fun Window.isAppearanceLightNavigationBars(view: View = decorView) = insetsControllerX(view)?.isAppearanceLightStatusBars ?: false

/**
 * 显示导航栏
 */
inline fun Window.showNavigationBar(view: View = decorView) {
    insetsControllerX(view)?.show(WindowInsetsCompat.Type.navigationBars())
}

/**
 * 隐藏导航栏
 */
inline fun Window.hindNavigationBar(view: View = decorView) {
    insetsControllerX(view)?.hide(WindowInsetsCompat.Type.navigationBars())
}

/**
 * 启动Window时不自动弹出软键盘
 */
inline fun Window.setSoftInputModeHidden() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

/**
 * 启动Window时自动弹出软键盘
 */
inline fun Window.setSoftInputModeShow() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
}

