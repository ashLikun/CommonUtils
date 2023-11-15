package com.ashlikun.utils.other

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:24
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：单例形式的hander,主线程
 */
inline fun LifecycleOwner.postMain(runnable: Runnable) {
    MainHandle.post(this, runnable)
}

inline fun LifecycleOwner.postDelayed(delayMillis: Long, runnable: Runnable) {
    MainHandle.postDelayed(this, delayMillis, runnable)
}

inline fun LifecycleOwner.postDelayed(token: Any, delayMillis: Long, runnable: Runnable) {
    MainHandle.postDelayed(this, token, delayMillis, runnable)
}

class MainHandle private constructor(looper: Looper) {
    var mainHandle = Handler(looper)
    val observer = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                //销毁

            }
        }
    }

    /**
     * 感知生命周期
     */
    private inner class MyLifecycleEventObserver(var lifecycleOwner: LifecycleOwner, runnable: Runnable) : LifecycleEventObserver {

        val runnableX = Runnable {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                runnable.run()
            }
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                lifecycleOwner.lifecycle.removeObserver(this)
                mainHandle.removeCallbacks(runnableX)
            }
        }

    }

    /**
     * 这个方法会造成Activity内存泄露
     * 如果不是在activity相关使用就可以
     * 可以使用带LifecycleOwner的方法
     */
    fun post(lifecycleOwner: LifecycleOwner? = null, runnable: Runnable) {
        if (isMain) {
            runnable.run()
        } else {
            if (lifecycleOwner != null) {
                //监听生命周期
                MyLifecycleEventObserver(lifecycleOwner, runnable).apply {
                    lifecycleOwner.lifecycle.addObserver(this)
                    mainHandle.post(runnableX)
                }
            } else {
                mainHandle.post(runnable)
            }
        }
    }

    /**
     * 这个方法会造成Activity内存泄露
     * 如果不是在activity相关使用就可以
     * 可以使用带LifecycleOwner的方法
     */
    fun postDelayed(lifecycleOwner: LifecycleOwner? = null, delayMillis: Long, runnable: Runnable) {
        if (lifecycleOwner != null) {
            //监听生命周期
            MyLifecycleEventObserver(lifecycleOwner, runnable).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                mainHandle.postDelayed(runnableX, delayMillis)
            }
        } else {
            mainHandle.postDelayed(runnable, delayMillis)
        }
    }


    /**
     * 这个方法会造成Activity内存泄露
     * 如果不是在activity相关使用就可以
     * 可以使用带LifecycleOwner的方法
     */
    fun postDelayed(lifecycleOwner: LifecycleOwner? = null, token: Any, delayMillis: Long, runnable: Runnable) {
        if (lifecycleOwner != null) {
            //监听生命周期
            MyLifecycleEventObserver(lifecycleOwner, runnable).apply {
                lifecycleOwner.lifecycle.addObserver(this)
                val message = Message.obtain(get()?.mainHandle, runnableX)
                message.obj = token
                get().mainHandle.sendMessageDelayed(message, delayMillis)
            }
        } else {
            val message = Message.obtain(get()?.mainHandle, runnable)
            message.obj = token
            get().mainHandle.sendMessageDelayed(message, delayMillis)
        }

    }


    fun sendMessageDelayed(msg: Message, delayMillis: Long): Boolean {
        return mainHandle.sendMessageAtTime(msg, delayMillis)
    }

    fun removeCallbacks(runable: Runnable) {
        mainHandle.removeCallbacks(runable)
    }

    companion object {
        private val instance by lazy { MainHandle(Looper.getMainLooper()) }
        fun get(): MainHandle = instance
        var printTestCall: ((type: String, startTime: Long) -> Unit)? = null
        fun printTest(type: String, startTime: Long) {
            printTestCall?.invoke(type, startTime)
        }

        /**
         * 这个方法会造成Activity内存泄露
         * 如果不是在activity相关使用就可以
         * 可以使用带LifecycleOwner的方法
         */
        fun post(lifecycleOwner: LifecycleOwner? = null, runnable: Runnable) {
            get().post(lifecycleOwner, runnable)
        }

        /**
         * 同步主线程返回
         */
        fun <T> postSync(lifecycleOwner: LifecycleOwner? = null, runnable: () -> T): T {
            val startTime = System.currentTimeMillis()
            if (isMain) {
                val result = runnable()
                printTest("postSync a", startTime)
                return result
            } else {
                //主线程处理数据，然后同步返回
                val lock = Object()
                var hasVal = false
                var res: T? = null
                post(lifecycleOwner) {
                    res = runnable()
                    hasVal = true
                    synchronized(lock) {
                        lock.notifyAll()
                    }
                }
                synchronized(lock) { if (!hasVal) lock.wait() }
                printTest("postSync b", startTime)
                return res as T
            }


        }

        /**
         * 这个方法会造成Activity内存泄露
         * 如果不是在activity相关使用就可以
         * 可以使用带LifecycleOwner的方法
         */
        fun postDelayed(lifecycleOwner: LifecycleOwner? = null, delayMillis: Long, runnable: Runnable) {
            get().postDelayed(lifecycleOwner, delayMillis, runnable)
        }


        /**
         * 同步主线程返回
         */
        fun <T> postDelayedSync(lifecycleOwner: LifecycleOwner? = null, delayMillis: Long, runnable: () -> T): T {
            if (isMain) {
                return runnable()
            } else {
                //主线程处理数据，然后同步返回
                val lock = Object()
                var hasVal = false
                var res: T? = null
                postDelayed(lifecycleOwner, delayMillis) {
                    res = runnable()
                    hasVal = true
                    synchronized(lock) {
                        lock.notifyAll()
                    }
                }
                synchronized(lock) { if (!hasVal) lock.wait() }
                return res as T
            }
        }

        /**
         * 这个方法会造成Activity内存泄露
         * 如果不是在activity相关使用就可以
         * 可以使用带LifecycleOwner的方法
         */
        fun postDelayed(lifecycleOwner: LifecycleOwner? = null, token: Any, delayMillis: Long, runnable: Runnable) {
            get().postDelayed(lifecycleOwner, token, delayMillis, runnable)
        }


        fun sendMessageDelayed(runnable: Runnable, token: Any, delayMillis: Long) {
            val message = Message.obtain(get().mainHandle, runnable)
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