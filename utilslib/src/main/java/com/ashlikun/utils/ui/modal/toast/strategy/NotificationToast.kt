package com.ashlikun.utils.ui.modal.toast.strategy

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import com.ashlikun.utils.ui.modal.toast.config.OnCallback
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 20:36
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：处理 Toast 关闭通知栏权限之后无法弹出的问题
 */
open class NotificationToast(context: Context) :
    SystemToast(context) {
    override fun show() {
        hookNotificationService()
        super.show()
    }

    companion object {
        /**
         * 是否已经 Hook 了一次通知服务
         */
        private var sHookService = false

        @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
        private fun hookNotificationService() {
            if (sHookService) {
                return
            }
            sHookService = true
            try {
                // 获取到 Toast 中的 getService 静态方法
                val getService = Toast::class.java.getDeclaredMethod("getService")
                getService.isAccessible = true
                // 执行方法，会返回一个 INotificationManager$Stub$Proxy 类型的对象
                val iNotificationManager = getService.invoke(null) ?: return
                // 如果这个对象已经被动态代理过了，并且已经 Hook 过了，则不需要重复 Hook
                if (Proxy.isProxyClass(iNotificationManager.javaClass) &&
                    Proxy.getInvocationHandler(iNotificationManager) is NotificationServiceProxy
                ) {
                    return
                }
                val iNotificationManagerProxy = Proxy.newProxyInstance(
                    Thread.currentThread().contextClassLoader,
                    arrayOf(Class.forName("android.app.INotificationManager")),
                    NotificationServiceProxy(iNotificationManager)
                )
                // 将原来的 INotificationManager$Stub$Proxy 替换掉
                val sService = Toast::class.java.getDeclaredField("sService")
                sService.isAccessible = true
                sService[null] = iNotificationManagerProxy
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 通知服务代理代理对象
     */
    internal class NotificationServiceProxy(
        /** 被代理的对象  */
        private val mSource: Any
    ) : InvocationHandler {
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
            when (method.name) {
                "enqueueToast", "enqueueToastEx", "cancelToast" ->                 // 将包名修改成系统包名，这样就可以绕过系统的拦截
                    // 部分华为机将 enqueueToast 修改成了 enqueueToastEx
                    args[0] = "android"
                else -> {}
            }
            // 使用动态代理
            return method.invoke(mSource, *args)
        }
    }
}