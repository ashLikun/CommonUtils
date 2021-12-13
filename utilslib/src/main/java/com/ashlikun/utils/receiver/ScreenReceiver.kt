package com.ashlikun.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.ashlikun.utils.other.LogUtils.i

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 23:51
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：屏幕接收器，可收到屏幕点亮、关闭的广播，并通过回调通知给调用者
 */

typealias ScreenListener = (open: Boolean) -> Unit

class ScreenReceiver : BroadcastReceiver() {
    var screenListener: ScreenListener? = null
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_SCREEN_ON) {
            i("屏幕解锁广播...")
            screenListener?.invoke(true)
        } else if (action == Intent.ACTION_SCREEN_OFF) {
            i("屏幕加锁广播...")
            screenListener?.invoke(false)
        }
    }

    fun registerScreenReceiver(context: Context, screenListener: ScreenListener) {
        try {
            this.screenListener = screenListener
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            i("注册屏幕解锁、加锁广播接收者...")
            context.registerReceiver(this, filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unRegisterScreenReceiver(context: Context) {
        try {
            context.unregisterReceiver(this)
            i("注销屏幕解锁、加锁广播接收者...")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}