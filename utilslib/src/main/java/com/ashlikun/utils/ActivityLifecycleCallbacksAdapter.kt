package com.ashlikun.utils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.15 15:53
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：对[Application.ActivityLifecycleCallbacks] 的简化
 */

abstract class ActivityLifecycleCallbacksAdapter : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}