package com.ashlikun.utils.other.coroutines

import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.other.ThreadPoolManage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
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
 * CoroutineDispatcher，用于将阻塞IO任务卸载到共享线程池。 此池中的其他线程将被创建并按需关闭。此调度程序中的任务使用的线程数受“kotlinx.coroutines.ioparallelism”
 * io_PARALLELIM_PROPERTY_NAME）系统属性值的限制。它默认限制为64个线程或内核数量（以较大者为准）。
 * 此外，最大可配置线程数由kotlin.coroutines.scheduler.max.pool.size系统属性限制。如果需要更多的并行线程，则应该使用由自己的线程池支持的自定义调度器。
 * 实施说明 此调度程序与Default调度程序共享线程，因此当已经在Default调度程序上运行时使用withContext（Dispatchers.IO）｛…｝不会导致实际切换到另一个线程——通常在同一线程中继续执行。
 * 由于线程共享，在IO调度器的操作过程中可以创建（但不使用）超过64个（默认并行度）线程
 */
val IODispatcher = Dispatchers.IO

/**
 * 将会获取默认调度器,使用共享的后台线程池
 * 默认的CoroutineDispatcher，由所有标准构建程序（如启动、异步等）使用，如果在它们的上下文中没有指定调度器或任何其他ContinuationInterceptor。
 * 它由JVM上的共享线程池支持。默认情况下，此调度器使用的最大并行级别等于CPU核的数量，但至少为两个。并行级别X保证在此调度器中并行执行的任务不超过X个。
 *
 */
val DefaultDispatcher = Dispatchers.Default

/**
 * 跟随当前线程 特点： 如果在主线程中执行调用delay以后便会切换到子线程kotlinx.coroutines.DefaultExecutor中执行
 * 一个不局限于任何特定线程的协同程序调度器。它在当前调用帧中执行协同程序的初始延续，并允许协同程序在相应的挂起函数使用的任何线程中恢复，而无需强制执行任何特定的线程策略。
 *  在此调度程序中启动的嵌套协程形成一个事件循环，以避免堆栈溢出。 事件循环 事件循环语义是一个纯粹的内部概念，除了所有排队的协程都将在最外层的无约束协程的词法范围内的当前线程上执行之外，它对执行顺序没有任何保证。
 *  例如，以下代码： withContext（调度程序.未定义）{ println（1） withContext（Dispatchers.Uncoined）
 *  ｛//嵌套的无约束 println（2） } println（3） } println（“完成”） 可以打印“1 2 3”和“1 3 2”，这是一个可以更改的实现细节。但可以保证的是，
 *  只有当两个withContext都完成时，才会打印“完成”。 请注意，如果您需要在恢复后将协程限制在特定的线程或线程池中，但仍然希望在当前调用帧中执行它，直到它第一次挂起，
 *  那么您可以在协程生成器中使用可选的CoroutineStart参数，如launch和async，将其设置为CoroutineStart。未修补。
 */
val UnconfinedDispatcher = Dispatchers.Unconfined

/**
 * 利用自定义线程池
 * 最小64个
 */
val ThreadPoolDispatcher = ThreadPoolManage.get().asCoroutineDispatcher()

//几个全局的作用域
val globalDefaultScope = DefaultScope()
val globalIoScope = IoScope()
val globalMainScope = MainScopeX()
val globalThreadPoolScope = ThreadPoolScope()


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
inline fun CException(crossinline handler: (CoroutineContext, Throwable) -> Unit) =
    CoroutineExceptionHandler(handler)

/**
 * 异常处理
 */
inline fun CException(crossinline handler: (Throwable) -> Unit) =
    CoroutineExceptionHandler { coroutineContext, throwable ->
        handler.invoke(throwable)
    }

/**
 * 异常处理
 */
inline fun CException(crossinline handler: () -> Unit) =
    CoroutineExceptionHandler { coroutineContext, throwable ->
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
    val startTime = System.currentTimeMillis()
    val result = job()
    MainHandle.printTest("Scope taskBlock", startTime)
    result
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
            val startTime = System.currentTimeMillis()
            val result = job()
            MainHandle.printTest("Scope asyncX", startTime)
            result
        }.onFailure {
            if (handleContext is CoroutineExceptionHandler) handleContext.handleException(
                context,
                it
            )
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
        val startTime = System.currentTimeMillis()
        val result = job()
        MainHandle.printTest("Scope asyncXNoCache", startTime)
        result
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
    launchXCache(context + SupervisorJob(), cache, cache2, delayTime, job)
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
) = DefaultScope().launchXCache(context, cache, cache2, delayTime, job)

/**
 * 执行，常用于最外层 [Dispatchers.Default] 线程
 * 无阻塞的
 * 这里异常会被上层捕获
 */
inline fun CoroutineScope.launchXCache(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
) = launch(CoroutineExceptionHandler(context, exception = cache, exception2 = cache2)) {
    delay(delayTime)
    val startTime = System.currentTimeMillis()
    job()
    MainHandle.printTest("Scope launchXCache", startTime)
}

/**
 * coroutineScope launch 的时候 必须 SupervisorJob() 解决异常捕获
 * SupervisorJob() 这个会导致这个协程在一个新的作用域
 * 所以自己实现了异常
 * 执行，常用于最外层 [Dispatchers.Default] 线程
 * 无阻塞的
 */
inline fun CoroutineScope.launchX(
    context: CoroutineContext = EmptyCoroutineContext,
    noinline cache: ((Throwable) -> Unit)? = null,
    noinline cache2: ((CoroutineContext, Throwable) -> Unit)? = null,
    delayTime: Long = 0,
    noinline job: suspend () -> Unit
): Job {
    val handleContext = CoroutineExceptionHandler(context, exception = cache, exception2 = cache2)
    return launch(handleContext) {
        delay(delayTime)
        //自己实现异常，防止异常会跑到外层或者无法捕获
        runCatching {
            val startTime = System.currentTimeMillis()
            job()
            MainHandle.printTest("Scope", startTime)
        }.onFailure {
            if (handleContext is CoroutineExceptionHandler) handleContext.handleException(
                context,
                it
            )
        }
    }
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
) = MainScopeX().launchXCache(context, cache, cache2, delayTime, job)


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
) = IoScope().launchXCache(context, cache, cache2, delayTime, job)

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
) = ThreadPoolScope().launchXCache(context, cache, cache2, delayTime, job)

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
    val startTime = System.currentTimeMillis()
    job()
    MainHandle.printTest("Scope taskRepeatSus", startTime)
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
suspend fun <R> currentScope(block: suspend (scope: CoroutineScope) -> R) = coroutineScope(block)

/**
 * 取消作用域,捕获异常
 * 被取消的作用域不可后续的任何任务执行，必须重新初始化
 */
fun CoroutineScope.cancelX(cause: CancellationException? = null) = runCatching { cancel(cause) }

/**
 * 判断作用域是否取消
 */
val CoroutineScope.isCancelled: Boolean
    get() {
        val job = coroutineContext[Job]
        //没有Job 就是认为取消
        return job?.isCancelled ?: true
    }