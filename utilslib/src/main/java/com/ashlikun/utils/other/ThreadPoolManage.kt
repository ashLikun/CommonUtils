package com.ashlikun.utils.other

import java.util.concurrent.*
import kotlin.math.max

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 23:38
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：项目中全局的线程池管理
 *
 *          推荐使用kotlin协成
 */

class ThreadPoolManage private constructor() :
//仿照okhttp
    ThreadPoolExecutor(
        AVAILABLE_PROCESSORS,
        max(AVAILABLE_PROCESSORS * 32, 64),
        0,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        threadFactory("thread_pool_manage", false)

    ) {
    companion object {
        private val instance by lazy { ThreadPoolManage() }
        val AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors()
        fun get(): ThreadPoolManage = instance

        /**
         * 创建线程池工厂
         */
        fun threadFactory(name: String, daemon: Boolean): ThreadFactory {
            /**
             * daemon线程有个特点就是"比较次要"，程序中如果所有的user线程都结束了，那这个程序本身就结束了，管daemon是否结束。
             * 而user线程就不是这样，只要还有一个user线程存在，程序就不会退出。
             */
            return ThreadFactory { runnable ->
                val result = Thread(runnable, name)
                result.isDaemon = daemon
                result
            }
        }
    }

}