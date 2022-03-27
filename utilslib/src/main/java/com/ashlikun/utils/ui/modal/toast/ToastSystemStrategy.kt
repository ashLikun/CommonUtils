package com.ashlikun.utils.ui.modal.toast

import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.ui.ActivityManager
import com.ashlikun.utils.ui.NotificationUtil
import com.ashlikun.utils.ui.modal.toast.config.IToast
import com.ashlikun.utils.ui.modal.toast.config.IToastStrategy
import com.ashlikun.utils.ui.modal.toast.config.IToastStyle
import com.ashlikun.utils.ui.modal.toast.strategy.*
import java.lang.ref.WeakReference


/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:06
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：系统Toast 默认处理器
 * 优先级是：[SafeToast]->[NotificationToast]->[SystemToast]
 */
open class ToastSystemStrategy : ToastStrategy() {

    override fun create(): IToast {
        val toast = when {
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


}