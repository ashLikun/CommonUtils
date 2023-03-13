package com.ashlikun.utils.ui.modal.toast

import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.widget.Toast
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.ui.modal.toast.config.OnCallback
import com.ashlikun.utils.ui.modal.toast.strategy.CustomToast
import java.lang.ref.WeakReference

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:41
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：自定义 Toast 实现类
 */
internal class ToastImpl( //当前的吐司对象
    private val mToast: CustomToast
) {
    /**
     * 回调
     */
    var callback: OnCallback? = null

    /**
     * WindowManager 辅助类
     * 懒加载是为了显示toast的时候在新的activity显示
     * 如 A跳转B，然后显示toast，或者先显示toast，然后跳转
     * 如 B返回A，然后显示toast，或者先显示toast，然后跳转
     */
    private val mWindowLifecycle by lazy {
        WindowLifecycle(WeakReference(AppUtils.fContext))
    }


    /**
     * 当前是否已经显示
     */
    var isShow = false

    /**
     * 当前是否全局显示 Application
     * 前提是有悬浮框权限 android.permission.SYSTEM_ALERT_WINDOW
     */
    var mGlobalShow = true


    /***
     * 显示吐司弹窗
     * 这里尽力不要直接调用，防止window消失情况
     */
    fun show() {
        if (isShow) {
            return
        }
        MainHandle.get().removeCallbacks(mShowRunnable)
        MainHandle.get().post(runnable = mShowRunnable)
    }

    /**
     * 取消吐司弹窗
     * 这里尽力不要直接调用，防止window消失情况
     */
    fun cancel() {
        if (!isShow) {
            return
        }
        MainHandle.get().removeCallbacks(mShowRunnable)
        MainHandle.get().removeCallbacks(mCancelRunnable)
        MainHandle.get().post(runnable = mCancelRunnable)
    }

    private val mShowRunnable = Runnable {
        val view = mToast.getView() ?: return@Runnable
        val windowManager = mWindowLifecycle.windowManager ?: return@Runnable
        val params = WindowManager.LayoutParams()
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.format = PixelFormat.TRANSLUCENT
        params.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //通过根布局判断是否点击
        if (!(view.isClickable || view.isLongClickable)) {
            params.flags =
                params.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        }
        params.packageName = AppUtils.packageName
        params.gravity = mToast.getGravity()
        params.x = mToast.getXOffset()
        params.y = mToast.getYOffset()
        params.verticalMargin = mToast.getVerticalMargin()
        params.horizontalMargin = mToast.getHorizontalMargin()
        params.windowAnimations = mToast.animations

        // 如果是全局显示
        if (mGlobalShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Settings.canDrawOverlays(AppUtils.app)) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                }
            } else {
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
        }
        try {
            windowManager.addView(mToast.getView(), params)
            // 添加一个移除吐司的任务
            MainHandle.get().postDelayed(
                runnable = { cancel() },
                delayMillis = mToast.getCalculateDuration()
            )
            // 注册生命周期管控
            mWindowLifecycle.register(this@ToastImpl)
            // 当前已经显示
            isShow = true
            callback?.invoke(true)
        } catch (e: IllegalStateException) {
            // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
            // java.lang.IllegalStateException: View android.widget.TextView has already been added to the window manager.
            // 如果 WindowManager 绑定的 Activity 已经销毁，则会抛出异常
            // android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@ef1ccb6 is not valid; is your activity running?
            e.printStackTrace()
        } catch (e: BadTokenException) {
            e.printStackTrace()
        }
    }
    private val mCancelRunnable = Runnable {
        try {
            val windowManager = mWindowLifecycle.windowManager ?: return@Runnable
            windowManager.removeViewImmediate(mToast.getView())
            callback?.invoke(false)
        } catch (e: IllegalArgumentException) {
            // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
            // java.lang.IllegalArgumentException: View=android.widget.TextView not attached to window manager
            e.printStackTrace()
        } finally {
            // 反注册生命周期管控
            mWindowLifecycle.unregister()
            // 当前没有显示
            isShow = false
        }
    }


}