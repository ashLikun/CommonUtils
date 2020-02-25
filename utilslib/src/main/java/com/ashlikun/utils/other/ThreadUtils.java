package com.ashlikun.utils.other;

import android.os.Looper;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/19　11:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：线程的一些工具
 */
public class ThreadUtils {
    /**
     * 是否是主线程
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 在线程池中执行线程
     */
    public static void execute(Runnable command) {
        ThreadPoolManage.get().execute(command);
    }

    /**
     * 切换到主线程
     */
    public static void toMain(Runnable command) {
        MainHandle.get().post(command);
    }
}
