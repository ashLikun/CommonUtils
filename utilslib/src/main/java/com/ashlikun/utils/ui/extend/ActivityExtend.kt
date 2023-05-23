package com.ashlikun.utils.ui.extend

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.ashlikun.utils.main.ActivityUtils
import com.ashlikun.utils.other.LogUtils
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
fun View.toLifecycle() = findViewTreeLifecycleOwner()!!
fun View.toLifecycleOrNull() = findViewTreeLifecycleOwner()

fun View.lifecycle(attached: ((LifecycleOwner) -> Unit)) {
    toLifecycleOrNull().also {
        if (it != null) attached(toLifecycle())
        else if (!isInEditMode) {
            addOnAttach { attached(toLifecycle()) }
        }
    }
}

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
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    } else {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
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

/**
 * 取出最大的那一个刷新率Fps，直接设置给window
 */
fun Window.setMaxFps() {
    runCatching {
        //地图模式默认关闭了高刷
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val display = this.windowManager.defaultDisplay
            // 获取系统window支持的模式
            val modes = display.supportedModes
            // 对获取的模式，大小与当前的窗口大小一致，基于刷新率的大小进行排序，从小到大排序
            val modesList = modes.filter { it.physicalWidth == display.width && it.physicalHeight == display.height }.sortedBy { it.refreshRate }
            modesList.forEach {
                LogUtils.e("当前刷新模式有：$it")
            }
            LogUtils.e("设置的刷新率是：${modesList.last()}")
            modesList.lastOrNull()?.also { mode ->
                this.let {
                    val lp = it.attributes
                    // 取出最大的那一个刷新率Fps，直接设置给window
                    lp.preferredDisplayModeId = mode.modeId
                    it.attributes = lp
                }
            }

        }
    }
}

fun Window.getMaxFps(): Float? {
    runCatching {
        //地图模式默认关闭了高刷
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val display = this.windowManager.defaultDisplay
            // 获取系统window支持的模式
            val modes = display.supportedModes
            // 对获取的模式，大小与当前的窗口大小一致，基于刷新率的大小进行排序，从小到大排序
            val modesList = modes.filter { it.physicalWidth == display.width && it.physicalHeight == display.height }.sortedBy { it.refreshRate }
            return modesList.lastOrNull()?.refreshRate
        }
    }
    return null
}

fun Window.getFps(): Float? {
    runCatching {
        //地图模式默认关闭了高刷
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return this.windowManager.defaultDisplay.refreshRate
        }
    }
    return null
}
