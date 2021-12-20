package com.ashlikun.utils.assist

import android.content.Context
import android.os.PowerManager
import com.ashlikun.utils.other.LogUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:19
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：屏幕管理， 点亮、关闭屏幕，判断屏幕是否点亮
 * <uses-permission android:name="android.permission.WAKE_LOCK"></uses-permission>
 */
class WakeLock(context: Context, tag: String = "ashlikun.utils:WakeLock") {
    var powerManager: PowerManager
    var wakeLock: PowerManager.WakeLock

    init {
        ////获取电源的服务 声明电源管理器
        powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.FULL_WAKE_LOCK,
            tag
        )
    }

    //点亮屏幕
    fun on() {
        //点亮亮屏
        LogUtils.i("PowerManager.WakeLock : wakeLock.isHeld: " + wakeLock.isHeld)
        if (!wakeLock.isHeld) {
            LogUtils.i("PowerManager.WakeLock : 点亮屏幕")
            wakeLock.acquire()
        }
    }

    //灭掉屏幕
    fun off() {
        //释放亮屏
        LogUtils.i("PowerManager.WakeLock : wakeLock.isHeld: " + wakeLock.isHeld)
        if (wakeLock.isHeld) {
            LogUtils.i("PowerManager.WakeLock : 灭掉屏幕")
            try {
                wakeLock.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //释放
    fun release() {
        if (wakeLock.isHeld) {
            try {
                wakeLock.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}