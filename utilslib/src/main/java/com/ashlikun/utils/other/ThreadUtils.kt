package com.ashlikun.utils.other

import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 23:40
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：线程的一些工具
 */

object ThreadUtils : ExecutorService {
    /**
     * 是否是主线程
     */
    val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    /**
     * 在线程池中执行线程
     */
    override fun execute(command: Runnable) {
        ThreadPoolManage.get().execute(command)
    }

    override fun shutdown() {
        ThreadPoolManage.get().shutdown()
    }

    override fun shutdownNow() = ThreadPoolManage.get().shutdownNow()

    override fun isShutdown() = ThreadPoolManage.get().isShutdown()

    override fun isTerminated() = ThreadPoolManage.get().isTerminated()

    override fun awaitTermination(timeout: Long, unit: TimeUnit) =
        ThreadPoolManage.get().awaitTermination(timeout, unit)

    override fun <T : Any?> submit(task: Callable<T>) = ThreadPoolManage.get().submit(task)

    override fun <T : Any?> submit(task: Runnable, result: T) =
        ThreadPoolManage.get().submit(task, result)

    override fun submit(task: Runnable) = ThreadPoolManage.get().submit(task)

    override fun <T : Any?> invokeAll(tasks: MutableCollection<out Callable<T>>) =
        ThreadPoolManage.get().invokeAll(tasks)

    override fun <T : Any?> invokeAll(
        tasks: MutableCollection<out Callable<T>>,
        timeout: Long,
        unit: TimeUnit
    ) = ThreadPoolManage.get().invokeAll(tasks, timeout, unit)

    override fun <T : Any?> invokeAny(tasks: MutableCollection<out Callable<T>>) =
        ThreadPoolManage.get().invokeAny(tasks)

    override fun <T : Any?> invokeAny(
        tasks: MutableCollection<out Callable<T>>?,
        timeout: Long,
        unit: TimeUnit
    ) = ThreadPoolManage.get().invokeAny(tasks, timeout, unit)

    /**
     * 切换到主线程
     */
    fun toMain(command: Runnable) {
        MainHandle.post(command)
    }
}