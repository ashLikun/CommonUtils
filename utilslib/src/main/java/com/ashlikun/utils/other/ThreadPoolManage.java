package com.ashlikun.utils.other;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/4/26 0026　下午 5:50
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：项目中全局的线程池管理
 */
public class ThreadPoolManage {
    private static volatile ThreadPoolManage instance = null;
    private final ExecutorService executor;

    private ThreadPoolManage() {
        int POOL_SIZE = Runtime.getRuntime().availableProcessors() / 2;
        //仿照okhttp
        executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE * 2, 0, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), threadFactory("0gow_thread_pool", false));
    }

    /**
     * 创建线程池工厂
     */
    public static ThreadFactory threadFactory(final String name, final boolean daemon) {
        /**
         * daemon线程有个特点就是"比较次要"，程序中如果所有的user线程都结束了，那这个程序本身就结束了，管daemon是否结束。
         * 而user线程就不是这样，只要还有一个user线程存在，程序就不会退出。
         */
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    public static ThreadPoolManage get() {
        //双重校验DCL单例模式
        if (instance == null) {
            //同步代码块
            synchronized (ThreadPoolManage.class) {
                if (instance == null) {
                    //创建一个新的实例
                    instance = new ThreadPoolManage();
                }
            }
        }
        //返回一个实例
        return instance;
    }


    /**
     * 在线程池中执行线程
     */
    public void execute(Runnable command) {
        executor.execute(command);
    }

}
