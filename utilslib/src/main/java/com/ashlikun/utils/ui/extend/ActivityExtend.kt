package com.ashlikun.utils.ui.extend

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import com.ashlikun.utils.main.ActivityUtils
import com.ashlikun.utils.ui.status.StatusBarCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2019/10/18　16:13
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Activity的一些扩展方法
 */
fun Activity?.getRootView() = this?.findViewById<View>(android.R.id.content)
fun Context.toLifecycle() = getActivity() as LifecycleOwner
fun Context.toLifecycleOrNull() = getActivity() as? LifecycleOwner
fun Context.toCActivity() = getActivity() as ComponentActivity
fun Context.toCActivityOrNull() = getActivity() as? ComponentActivity

fun Activity?.getDecorView() = this?.window?.decorView

fun Context?.toActivity() = ActivityUtils.getActivity(this)

var Activity.windowBrightness
    get() = window.attributes.screenBrightness
    set(brightness) {
        //小于0或大于1.0默认为系统亮度
        window.attributes = window.attributes.apply {
            screenBrightness = if (brightness >= 1.0 || brightness < 0) -1.0F else brightness
        }
    }


fun Activity.setStatusBarVisible(show: Boolean, statusBar: StatusBarCompat? = null) {
    if (show) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    } else {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
    statusBar?.setStatusDarkColor()
}

/**
 * 从context中获取activity，如果context不是activity那么久返回null
 */
fun Context?.getActivity(): Activity? {
    fun getActivity(context: Context?): Activity? {
        if (context == null) {
            return null
        }
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return getActivity(context.baseContext)
        }
        return null
    }
    return getActivity(this)
}

/**
 * 设置页面饱和度
 */
fun Activity?.setViewSaturation(sat: Float = 0f) {
    getDecorView()?.setViewSaturation(sat)
}