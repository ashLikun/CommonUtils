package com.ashlikun.utils.other.coroutines

import com.ashlikun.utils.other.ThreadPoolManage
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 作者　　: 李坤
 * 创建时间: 2020/5/9　16:29
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：协程的一些工具
 * 协程特点：
 * 1. 同一线程可以有多个协程
 * 2. 同一协程可以运行在不同上下文中 通过runBlocking 包子协程 run 实现 ： runBlocking(ctx1){ run(ctx2){ }  }
 */

/**
 * 运行在主线程中
 */
val MainDispatcher = Dispatchers.Main

/**
 * 运行在IO线程中
 */
val IODispatcher = Dispatchers.IO

/**
 * 将会获取默认调度器,使用共享的后台线程池
 */
val DefaultDispatcher = Dispatchers.Default

/**
 * 跟随当前线程 特点： 如果在主线程中执行调用delay以后便会切换到子线程kotlinx.coroutines.DefaultExecutor中执行
 */
val UnconfinedDispatcher = Dispatchers.Unconfined

/**
 * 利用自定义线程池
 */
val ThreadPoolDispatcher = ThreadPoolManage.get().asCoroutineDispatcher()


/**
 * 本框架协成默认错误的处理,如果调用者处理了，那么这里不会调用
 */
val defaultCoroutineExceptionHandler: CoroutineExceptionHandler =
    CoroutineExceptionHandler { _, t ->
        t.printStackTrace()
    }

inline fun CoroutineExceptionHandler(context: CoroutineContext): CoroutineContext {
    var ct = context
    if (context[CoroutineExceptionHandler.Key] == null) {
        if (defaultCoroutineExceptionHandler != null) {
            ct = context + defaultCoroutineExceptionHandler
        }
    }
    return ct
}


/**
 * 在主线程中顺序执行，属于顶级协程函数，一般用于最外层
 *
 * 注意：该函数会阻塞代码继续执行
 */
inline fun <T> taskBlock(
    context: CoroutineContext = EmptyCoroutineContext,
    delayTime: Long = 0,
    noinline job: suspend () -> T
): T = runBlocking(CoroutineExceptionHandler(context)) {
    delay(delayTime)
    job()
}

/**
 * 异步执行，常用于最外层
 * 多个 async 任务是并行的
 * 特点带返回值 async 返回的是一个Deferred<T>，需要调用其await()方法获取结果。
 */
inline fun <T> taskAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    delayTime: Long = 0,
    noinline job: suspend () -> T
): Deferred<T> = GlobalScope.async(CoroutineExceptionHandler(context)) {
    delay(delayTime)
    job()
}

/**
 * 执行，常用于最外层
 * 无阻塞的
 */
inline fun taskLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = GlobalScope.launch(CoroutineExceptionHandler(context)) {
    delay(delayTime)
    job()
}

/**
 * 执行，在Android UI线程中执行，可以用于最外层
 * 无阻塞的
 */
inline fun taskLaunchMain(delayTime: Long = 0, noinline job: suspend () -> Unit) =
    taskLaunch(MainDispatcher, delayTime, job)

/**
 * 执行，在ThreadPoolDispatcher线程中执行，可以用于最外层
 * 无阻塞的
 */
inline fun taskLaunchThreadPoll(delayTime: Long = 0, noinline job: suspend () -> Unit) =
    taskLaunch(ThreadPoolDispatcher, delayTime, job)

/**
 * 心跳执行 默认重复次数1次，可用于最外层
 */
inline fun taskRepeat(
    context: CoroutineContext = EmptyCoroutineContext,
    repeat: Int = 1,
    delayTime: Long = 0,
    noinline job: () -> Unit
) = taskLaunch(context) {
    taskRepeatSus(repeat, delayTime, job)
}


/**
 * 心跳执行 默认重复次数1次，不能用于最外层
 */
suspend inline fun taskRepeatSus(
    repeat: Int = 1,
    delayTime: Long = 0,
    crossinline job: () -> Unit
) = repeat(repeat) {
    delay(delayTime)
    job()
}

/**
 * 切换到main线程
 * 多个 withContext 任务是串行的
 * 特点带返回值
 */
suspend inline fun <T> withContextMain(noinline block: suspend CoroutineScope.() -> T) =
    withContext(MainDispatcher, block)

/**
 * 切换线程到线程池
 * 多个 withContext 任务是串行的
 * 特点带返回值
 */
suspend inline fun <T> withContextThreadPoll(noinline block: suspend CoroutineScope.() -> T) =
    withContext(ThreadPoolDispatcher, block)