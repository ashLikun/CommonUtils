package com.ashlikun.utils.other.coroutines

import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.ThreadPoolManage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
val MainDispatcher = Dispatchers.Main.immediate

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
var defaultCoroutineExceptionHandler: CoroutineExceptionHandler =
    CoroutineExceptionHandler { _, t ->
        LogUtils.e("协程默认异常")
        t.printStackTrace()
    }

/**
 * @param isAddDefault 是否检测如果没有Handle就添加默认的
 */
inline fun CoroutineExceptionHandler(
    context: CoroutineContext,
    isAddDefault: Boolean = true,
    noinline exception: ((Throwable) -> Unit)? = null,
    noinline exception2: ((CoroutineContext, Throwable) -> Unit)? = null,
): CoroutineContext {
    var ct = context
    if (exception != null) ct += CException(exception)
    if (exception2 != null) ct += CException(exception2)
    if (isAddDefault && ct[CoroutineExceptionHandler.Key] == null) {
        if (defaultCoroutineExceptionHandler != null) {
            ct += defaultCoroutineExceptionHandler
        }
    }
    return ct
}

/**
 * 异常处理
 */
inline fun CException(crossinline handler: (CoroutineContext, Throwable) -> Unit) = CoroutineExceptionHandler(handler)

/**
 * 异常处理
 */
inline fun CException(crossinline handler: (Throwable) -> Unit) = CoroutineExceptionHandler { coroutineContext, throwable ->
    handler.invoke(throwable)
}

/**
 * 异常处理
 */
inline fun CException(crossinline handler: () -> Unit) = CoroutineExceptionHandler { coroutineContext, throwable ->
    handler.invoke()
}

/**
 * 默认作用域
 */
fun DefaultScope(): CoroutineScope =
    CoroutineScope(SupervisorJob() + DefaultDispatcher + defaultCoroutineExceptionHandler)

/**
 * IO作用域
 */
fun IoScope(): CoroutineScope =
    CoroutineScope(SupervisorJob() + IODispatcher + defaultCoroutineExceptionHandler)

/**
 * 自定义线程池作用域
 */
fun ThreadPoolScope(): CoroutineScope =
    CoroutineScope(SupervisorJob() + ThreadPoolDispatcher + defaultCoroutineExceptionHandler)

/**
 * 主线程作用域，顶级处理异常
 */
fun MainScopeX(): CoroutineScope =
    CoroutineScope(SupervisorJob() + MainDispatcher + defaultCoroutineExceptionHandler)

/**
 * 在主线程中顺序执行，属于顶级协程函数，一般用于最外层 [Dispatchers.Default] 线程
 *
 * 注意：该函数会阻塞代码继续执行
 */
inline fun <T> taskBlock(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> T
): T = runBlocking(CoroutineExceptionHandler(context, exception = cache, exception2 = cache2)) {
    delay(delayTime)
    job()
}

/**
 * 携程内部异步执行
 */
suspend inline fun <T> asyncSuspend(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> T
) = coroutineScope {
    asyncX(context, cache, cache2, delayTime, job)
}

/**
 * 携程内部异步执行
 * 不嵌套捕获Catch
 */
suspend inline fun <T> asyncSuspendNoCatch(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> T
) = coroutineScope {
    asyncXNoCache(context, cache, cache2, delayTime, job)
}

/**
 * 异步执行，常用于最外层 [Dispatchers.Default] 线程
 * 多个 async 任务是并行的
 * 特点带返回值 async 返回的是一个Deferred<T>，需要调用其await()方法获取结果。
 */
inline fun <T> taskAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> T
) = DefaultScope().asyncX(context, cache, cache2, delayTime, job)

/**
 * 异步执行，常用于最外层 [Dispatchers.Default] 线程
 * 多个 async 任务是并行的
 * 特点带返回值 async 返回的是一个Deferred<T>，需要调用其await()方法获取结果。
 */
inline fun <T> CoroutineScope.asyncX(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> T
): Deferred<T> {
    val handleContext = CoroutineExceptionHandler(context, exception = cache, exception2 = cache2)
    return async(handleContext) {
        delay(delayTime)
        //自己实现异常，防止异常会跑到外层或者无法捕获
        runCatching {
            job()
        }.onFailure {
            if (handleContext is CoroutineExceptionHandler) handleContext.handleException(context, it)
        }.getOrNull() as T
    }
}

/**
 * 异步执行，常用于最外层 [Dispatchers.Default] 线程
 * 多个 async 任务是并行的
 * 特点带返回值 async 返回的是一个Deferred<T>，需要调用其await()方法获取结果。
 */
inline fun <T> CoroutineScope.asyncXNoCache(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> T
): Deferred<T> {
    return async(CoroutineExceptionHandler(context, exception = cache, exception2 = cache2)) {
        delay(delayTime)
        job()
    }
}

/**
 * 携程内部异步执行 launchX 方式
 */
suspend inline fun launchSuspend(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = coroutineScope {
    //coroutineScope launch 的时候 必须 SupervisorJob() 解决异常捕获
    launchX(context + SupervisorJob(), cache, cache2, delayTime, job)
}

/**
 * 执行，常用于最外层 [Dispatchers.Default] 线程
 * 无阻塞的
 */
inline fun taskLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = DefaultScope().launchX(context, cache, cache2, delayTime, job)

/**
 * 执行，常用于最外层 [Dispatchers.Default] 线程
 * 无阻塞的
 */
inline fun CoroutineScope.launchX(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = launch(CoroutineExceptionHandler(context, exception = cache, exception2 = cache2)) {
    delay(delayTime)
    job()
}


/**
 * 执行，在Android UI线程中执行，可以用于最外层
 * 无阻塞的
 */
inline fun taskLaunchMain(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = MainScopeX().launchX(context, cache, cache2, delayTime, job)


/**
 * 执行，在Android IO线程中执行，可以用于最外层  [Dispatchers.IO] 线程
 * 无阻塞的
 */
inline fun taskLaunchIO(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = IoScope().launchX(context, cache, cache2, delayTime, job)

/**
 * 执行，在ThreadPoolDispatcher线程中执行，可以用于最外层
 * 无阻塞的
 */
inline fun taskLaunchThreadPoll(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = ThreadPoolScope().launchX(context, cache, cache2, delayTime, job)

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
suspend inline fun <T> withContextMain(noinline block: suspend () -> T) =
    withContext(MainDispatcher) {
        block.invoke()
    }

/**
 * 切换到IO线程
 * 多个 withContext 任务是串行的
 * 特点带返回值
 */
suspend inline fun <T> withContextIO(noinline block: suspend () -> T) =
    withContext(IODispatcher) {
        block.invoke()
    }

/**
 * 切换线程到线程池
 * 多个 withContext 任务是串行的
 * 特点带返回值
 */
suspend inline fun <T> withContextThreadPoll(noinline block: suspend () -> T) =
    withContext(ThreadPoolDispatcher) {
        block.invoke()
    }

/**
 * 切换到当前作用域
 */
suspend fun <R> currentScope(block: suspend CoroutineScope.() -> R) = coroutineScope(block)