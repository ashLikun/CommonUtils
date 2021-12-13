package com.ashlikun.utils.other

import android.os.Looper
import java.lang.Runnable
import com.ashlikun.utils.other.ThreadPoolManage
import com.ashlikun.utils.other.MainHandle

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 23:40
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：线程的一些工具
 */

object ThreadUtils {
    /**
     * 是否是主线程
     */
    val isMainThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    /**
     * 在线程池中执行线程
     */
    fun execute(command: Runnable) {
        ThreadPoolManage.get().execute(command)
    }

    /**
     * 切换到主线程
     */
    fun toMain(command: Runnable) {
        MainHandle.post(command)
    }
}