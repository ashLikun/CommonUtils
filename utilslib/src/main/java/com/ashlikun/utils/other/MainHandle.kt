package com.ashlikun.utils.other

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:24
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：单例形式的hander,主线程
 */

class MainHandle private constructor(looper: Looper) {
    var mainHandle = Handler(looper)

    fun post(runnable: Runnable) {
        if (isMain) {
            runnable.run()
        } else {
            mainHandle.post(SoftRunnable(runnable))
        }
    }

    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        mainHandle.postDelayed(SoftRunnable(runnable), delayMillis)
    }

    fun postDelayed(runnable: Runnable, token: Any, delayMillis: Long) {
        val message = Message.obtain(get()?.mainHandle, SoftRunnable(runnable))
        message.obj = token
        get().mainHandle.sendMessageDelayed(message, delayMillis)
    }

    fun removeCallbacks(runable: Runnable) {
        mainHandle.removeCallbacks(runable)
    }

    /**
     * 解决回调内存泄露
     */
    class SoftRunnable(runnable: Runnable) : Runnable {
        var runnable: WeakReference<Runnable?>? = WeakReference(runnable)
        override fun run() {
            runnable?.get()?.run()
        }
    }

    companion object {
        private val instance by lazy { MainHandle(Looper.getMainLooper()) }
        fun get(): MainHandle = instance

        fun post(runnable: Runnable) {
            get().mainHandle.post(SoftRunnable(runnable))
        }

        fun postDelayed(runnable: Runnable, delayMillis: Long) {
            get().mainHandle.postDelayed(SoftRunnable(runnable), delayMillis)
        }

        fun postDelayed(runnable: Runnable, token: Any, delayMillis: Long) {
            val message = Message.obtain(get().mainHandle, SoftRunnable(runnable))
            message.obj = token
            get().mainHandle.sendMessageDelayed(message, delayMillis)
        }

        /**
         * 是否是主线程
         */
        val isMain: Boolean
            get() = Looper.myLooper() == Looper.getMainLooper()
    }

}