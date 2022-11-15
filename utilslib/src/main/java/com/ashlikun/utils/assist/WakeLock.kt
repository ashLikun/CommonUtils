package com.ashlikun.utils.assist

import android.content.Context
import android.os.PowerManager
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.LogUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:19
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：屏幕管理， 点亮、关闭屏幕，判断屏幕是否点亮
 * <uses-permission android:name="android.permission.WAKE_LOCK"></uses-permission>
 *
 * PARTIAL_WAKE_LOCK : CPU 运转，屏幕和键盘灯关闭
 * SCREEN_DIM_WAKE_LOCK ： 已过期（API 17） CPU 运转，屏幕亮但是比较暗，键盘灯关闭
 * SCREEN_BRIGHT_WAKE_LOCK ： 已过期（API 13）CPU 运转，屏幕高亮，键盘灯关闭
 * FULL_WAKE_LOCK ：已过期（API 17） 完全唤醒，CPU 运转，屏幕高亮，键盘灯亮
 */
class WakeLock(
    tag: String = "ashlikun.utils:WakeLock",
    levelAndFlags: Int = PowerManager.FULL_WAKE_LOCK
) {
    val powerManager: PowerManager by lazy {
        AppUtils.app.getSystemService(Context.POWER_SERVICE) as PowerManager
    }
    var wakeLock: PowerManager.WakeLock? = null

    init {
        ////获取电源的服务 声明电源管理器
        runCatching {
            wakeLock = powerManager.newWakeLock(levelAndFlags, tag)
        }
    }

    //点亮屏幕，保存Cpu唤醒
    fun on() {
        //点亮亮屏
        val wakeLock = wakeLock ?: return
        try {
            if (!wakeLock.isHeld) {
                LogUtils.i("PowerManager.WakeLock : 点亮屏幕")
                wakeLock.acquire()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //灭掉屏幕
    fun off() {
        release()
    }

    //释放
    fun release() {
        val wakeLock = wakeLock ?: return
        //释放亮屏
        LogUtils.i("PowerManager.WakeLock : wakeLock.isHeld: " + wakeLock.isHeld)
        try {
            if (wakeLock.isHeld) {
                LogUtils.i("PowerManager.WakeLock : 灭掉屏幕")
                wakeLock.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}