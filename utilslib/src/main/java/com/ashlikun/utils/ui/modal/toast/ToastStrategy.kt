package com.ashlikun.utils.ui.modal.toast

import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.ui.ActivityManager
import com.ashlikun.utils.ui.NotificationUtil
import com.ashlikun.utils.ui.modal.ToastUtils
import com.ashlikun.utils.ui.modal.toast.config.*
import com.ashlikun.utils.ui.modal.toast.strategy.*
import java.lang.ref.WeakReference


/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:06
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：Toast 默认处理器
 * 优先级是：[ContextToast]->[SafeToast]->[NotificationToast]->[SystemToast]
 */

open class ToastStrategy : ICallToastStrategy() {
    /**
     * Toast 对象
     */
    protected var mToastReference: WeakReference<IToast>? = null

    /**
     * Toast 样式
     */
    protected var mToastStyle: IToastStyle<*>? = null

    /** 最新的文本  */
    @Volatile
    private var mLatestText: CharSequence = ""


    override fun bindStyle(style: IToastStyle<*>) {
        mToastStyle = style
    }

    override fun getToast() = mToastReference?.get()

    override fun create(): IToast {
        val toast = when {
            //附属于Activity,,,,如果有悬浮窗权限，就开启全局的 Toast
            ActivityManager.foregroundActivity != null || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    Settings.canDrawOverlays(AppUtils.app)) -> ContextToast()
            //自定义时间也是使用自定义Toast
            duration != null && duration != Toast.LENGTH_LONG && duration != Toast.LENGTH_SHORT -> ContextToast()
            // 处理 Android 7.1 上 Toast 在主线程被阻塞后会导致报错的问题
            Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1 -> SafeToast(AppUtils.app)
            // 处理 Toast 关闭通知栏权限之后无法弹出的问题
            // 通过查看和对比 NotificationManagerService 的源码
            // 发现这个问题已经在 Android 10 版本上面修复了
            // 但是 Toast 只能在前台显示，没有通知栏权限后台 Toast 仍然无法显示
            // 并且 Android 10 刚好禁止了 Hook 通知服务
            // 已经有通知栏权限，不需要 Hook 系统通知服务也能正常显示系统 Toast
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !NotificationUtil.isNotificationEnabled() ->
                NotificationToast(AppUtils.app)
            //系统默认
            else -> SystemToast(AppUtils.app)
        }
        toast.callback = callback
        // targetSdkVersion >= 30 的情况下在后台显示自定义样式的 Toast 会被系统屏蔽，并且日志会输出以下警告：
        // Blocking custom toast from package com.xxx.xxx due to package not in the foreground
        // targetSdkVersion < 30 的情况下 new Toast，并且不设置视图显示，系统会抛出以下异常：
        // java.lang.RuntimeException: This Toast was not created with Toast.makeText()
        if (toast is CustomToast || Build.VERSION.SDK_INT < Build.VERSION_CODES.R || AppUtils.app.applicationInfo.targetSdkVersion < Build.VERSION_CODES.R) {
            mToastStyle?.also {
                toast.setView(it.createView(AppUtils.app))
                toast.setGravity(it.getGravity(), it.getXOffset(), it.getYOffset())
                toast.setMargin(
                    it.getHorizontalMargin().toFloat(),
                    it.getVerticalMargin().toFloat()
                )
            }

        }
        return toast
    }

    override fun show(text: CharSequence, delayMillis: Long) {
        mLatestText = text
        //主线程执行
        MainHandle.get().removeCallbacks(mShowRunnable)
        if (delayMillis > 0) {
            MainHandle.get().postDelayed(runnable = mShowRunnable, delayMillis = delayMillis)
        } else {
            MainHandle.get().post(runnable = mShowRunnable)
        }

    }

    override fun show() {
        show("", 0)
    }

    override fun cancel() {
        MainHandle.get().removeCallbacks(mShowRunnable)
        MainHandle.get().post(runnable = mCancelRunnable)

    }


    /**
     * 获取 Toast 显示时长
     */
    protected fun getToastDuration(): Int {
        if (duration != null) return duration!!
        //自动判断
        return if (mLatestText.length > 20) Toast.LENGTH_LONG else if (mLatestText.length > 100) 5000 else Toast.LENGTH_SHORT
    }

    /**
     * 显示任务
     */
    protected val mShowRunnable = Runnable {
        if (ToastUtils.interceptor.intercept(mLatestText)) return@Runnable
        //取消上一个显示的toast
        mCancelRunnable.run()
        val toast = create()
        // 为什么用 WeakReference，而不用 SoftReference ？
        // https://github.com/getActivity/ToastUtils/issues/79
        mToastReference = WeakReference(toast)
        toast.setDuration(getToastDuration())
        toast.setText(mLatestText)
        toast.show()
    }

    /**
     * 取消任务
     */
    protected val mCancelRunnable = Runnable {
        mToastReference?.get()?.cancel()
    }

}