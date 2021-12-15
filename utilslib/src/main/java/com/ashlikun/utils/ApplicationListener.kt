package com.ashlikun.utils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.ashlikun.utils.ui.ActivityManager
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.15 15:53
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：前后台监听，管理activityManage
 */

typealias OnChangForeground = (isForeground: Boolean) -> Unit

class ApplicationListener : ActivityLifecycleCallbacks {
    // 位于前台的 Activity 的数目
    private var foregroundCount = 0
    private val onChangListeners: MutableList<OnChangForeground> = ArrayList()

    /**
     * 判断某个界面是否在前台
     *
     * @return true:在前台，false:不在
     */
    val isForeground: Boolean
        get() = foregroundCount > 0

    /**
     * 前后台监听
     *
     * @param listener
     */
    fun addOnChangListener(listener: OnChangForeground) {
        if (!onChangListeners.contains(listener)) {
            onChangListeners.add(listener)
        }
    }

    /**
     * 前后台监听
     *
     * @param listener
     */
    fun removeOnChangListener(listener: OnChangForeground) {
        if (onChangListeners.contains(listener)) {
            onChangListeners.remove(listener)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (foregroundCount <= 0) {
            //进入前台
            for (i in onChangListeners.indices) {
                onChangListeners[i].invoke(true)
            }
        }
        foregroundCount++
    }

    override fun onActivityStopped(activity: Activity) {
        foregroundCount--
        if (foregroundCount <= 0) {
            //进入后台
            for (i in onChangListeners.indices) {
                onChangListeners[i].invoke(false)
            }
        }
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityManager.get().pushActivity(activity)
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        ActivityManager.get().removeActivity(activity)
    }


}