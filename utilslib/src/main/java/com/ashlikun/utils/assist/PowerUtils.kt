package com.ashlikun.utils.assist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.ashlikun.utils.AppUtils
import java.util.concurrent.atomic.AtomicInteger

/**
 * 作者　　: 李坤
 * 创建时间: 2022/9/9　10:01
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
/**
 * 是否忽略电池优化白名单
 */
fun Context.isIgnoringBatteryOptimizations() = runCatching {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PowerUtils.pwm.isIgnoringBatteryOptimizations(packageName)
    } else false
}.getOrNull() ?: false

private fun <I, O> ComponentActivity.registerForActivityResultX(
    contract: ActivityResultContract<I, O>,
    isUnregister: Boolean = true,
    key: String = "KeepLiveForActivityResult",
    callback: (O) -> Unit
): ActivityResultLauncher<I> {
    var launcher: ActivityResultLauncher<I>? = null
    //这种注册需要自己unregister
    launcher = activityResultRegistry.register(key + AtomicInteger().getAndIncrement(), contract) {
        callback.invoke(it)
        //这里主动释放
        if (isUnregister)
            launcher!!.unregister()
    }
    return launcher
}

/**
 * 忽略电池优化白名单
 * @param isToList true:跳转到设置里面的电池优化管理列表,false:弹窗形式申请电池优化白名单
 * @param hook 在启动弹窗或者跳转页面前，允许插入你的代码
 * @param result 结果
 */
fun ComponentActivity.ignoreBattery(isToList: Boolean = false, hook: ((run: () -> Unit) -> Unit)? = null, result: ((Boolean) -> Unit)? = null) {
    runCatching {
        if (isIgnoringBatteryOptimizations()) result?.invoke(true)
        else {
            fun start() = registerForActivityResultX(ActivityResultContracts.StartActivityForResult()) {
                //请求结果
                result?.invoke(isIgnoringBatteryOptimizations())
            }.launch(if (isToList) Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS) else Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
            })
            hook?.invoke(::start).apply {
                if (hook == null) start()
            }
        }
    }.getOrElse {
        it.printStackTrace()
        result?.invoke(false)
    }
}

object PowerUtils {
    val pwm by lazy {
        AppUtils.app.getSystemService(Context.POWER_SERVICE) as PowerManager
    }

    /**
     * 是否省电模式
     */
    val isPowerSaveMode
        get() = pwm.isPowerSaveMode

    /**
     * 是否开启屏幕
     */
    val isInteractive
        get() = pwm.isInteractive
}

