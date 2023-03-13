package com.ashlikun.utils.ui.modal.toast

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.ashlikun.utils.main.ActivityUtils.getActivity
import java.lang.ref.WeakReference

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:53
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：WindowManager 生命周期管控
 */
internal class WindowLifecycle(
    /**
     * 当前 Activity
     * 不能是Application addView 时候会报错
     */
    private var context: WeakReference<Context>
) : ActivityLifecycleCallbacks {
    /**
     * 自定义 Toast 实现类
     */
    private var mToastImpl: ToastImpl? = null

    /**
     * 获取 WindowManager 对象
     */
    val windowManager: WindowManager?
        get() {
            if (context.get() != null) {
                if (context.get() is Application) {
                    return context.get()!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                } else {
                    val activity = getActivity(context.get())
                    if (activity != null) {
                        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
                            null
                        } else activity.windowManager
                    }
                }
            }
            return null
        }

    /**
     * [Application.ActivityLifecycleCallbacks]
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}

    // A 跳转 B 页面的生命周期方法执行顺序：
    // onPause(A) ---> onCreate(B) ---> onStart(B) ---> onResume(B) ---> onStop(A) ---> onDestroyed(A)
    override fun onActivityPaused(activity: Activity) {
        if (getActivity(context.get()) !== activity) {
            return
        }
        if (mToastImpl == null) {
            return
        }

        // 不能放在 onStop 或者 onDestroyed 方法中，因为此时新的 Activity 已经创建完成，必须在这个新的 Activity 未创建之前关闭这个 WindowManager
        // 调用取消显示会直接导致新的 Activity 的 onCreate 调用显示吐司可能显示不出来的问题，又或者有时候会立马显示然后立马消失的那种效果
        mToastImpl!!.cancel()
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (getActivity(context.get()) !== activity) {
            return
        }
        mToastImpl?.cancel()
        unregister()
        context.clear()
    }

    /**
     * 注册
     */
    fun register(impl: ToastImpl) {
        mToastImpl = impl
        val activity = getActivity(context.get()) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.registerActivityLifecycleCallbacks(this)
        } else {
            activity.application.registerActivityLifecycleCallbacks(this)
        }
    }

    /**
     * 反注册
     */
    fun unregister() {
        mToastImpl = null
        val activity = getActivity(context.get()) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.unregisterActivityLifecycleCallbacks(this)
        } else {
            activity.application.unregisterActivityLifecycleCallbacks(this)
        }
    }
}