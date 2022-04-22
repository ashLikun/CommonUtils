package com.ashlikun.utils.other

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
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
            LogUtils.e("aaaa ${runnable::class.java.isAnonymousClass}")
            mainHandle.post(WeakRunnable(runnable))
        }
    }

    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        mainHandle.postDelayed(WeakRunnable(runnable), delayMillis)
    }

    fun postDelayed(runnable: Runnable, token: Any, delayMillis: Long) {
        val message = Message.obtain(get()?.mainHandle, WeakRunnable(runnable))
        message.obj = token
        get().mainHandle.sendMessageDelayed(message, delayMillis)
    }

    fun sendMessageDelayed(msg: Message, delayMillis: Long): Boolean {
        return mainHandle.sendMessageAtTime(msg, delayMillis)
    }

    fun removeCallbacks(runable: Runnable) {
        mainHandle.removeCallbacks(runable)
    }

    /**
     * 解决回调内存泄露
     */
    class WeakRunnable(var runnable: Runnable) : Runnable {
        //Kotlin 的回调会有莫名其妙问题
//        var runnable: WeakReference<Runnable?>? = WeakReference(runnable)
        override fun run() {
            runnable.run()
        }
    }

    companion object {
        private val instance by lazy { MainHandle(Looper.getMainLooper()) }
        fun get(): MainHandle = instance

        fun post(runnable: Runnable) {
            get().post(runnable)
        }

        fun postDelayed(runnable: Runnable, delayMillis: Long) {
            get().postDelayed(runnable, delayMillis)
        }

        fun postDelayed(runnable: Runnable, token: Any, delayMillis: Long) {
            val message = Message.obtain(get().mainHandle, WeakRunnable(runnable))
            message.obj = token
            get().sendMessageDelayed(message, delayMillis)
        }

        /**
         * 是否是主线程
         */
        val isMain: Boolean
            get() = Looper.myLooper() == Looper.getMainLooper()
    }

}