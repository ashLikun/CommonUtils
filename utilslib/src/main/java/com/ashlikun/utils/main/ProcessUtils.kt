package com.ashlikun.utils.main

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.text.TextUtils
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.ClassUtils
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 23:06
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：进程相关工具
 */

object ProcessUtils {
    //缓存
    private var currentProcessName: String? = null

    /**
     * 是否主进程正在运行
     *
     * @return `true`: yes<br></br>`false`: no
     */
    val isMainProcess: Boolean
        get() {
            val name = curProcessName
            return if (TextUtils.isEmpty(name)) {
                false
            } else name == AppUtils.app.packageName
        }

    /**
     * 是否APP进程
     * com.ogow.activity
     * com.ogow.activity:webview
     *
     * @return 获取失败也是APP进程
     */
    val isAppProcess: Boolean
        get() {
            val name = curProcessName
            return if (TextUtils.isEmpty(name)) {
                true
            } else name!!.contains(AppUtils.app.packageName)
        }
    //1)通过Application的API获取当前进程名

    //2)通过反射ActivityThread获取当前进程名

    //3)通过ActivityManager获取当前进程名
    /**
     * 获得当前进程名称, >5.1的系统，有几率会关闭getRunningAppProcesses方法(只会返回我们自己的进程)
     *
     * @return
     */
    val curProcessName: String
        get() {
            if (!currentProcessName.isNullOrEmpty()) {
                return currentProcessName!!
            }

            //1)通过Application的API获取当前进程名
            currentProcessName = currentProcessNameByApplication
            if (!currentProcessName.isNullOrEmpty()) {
                return currentProcessName!!
            }

            //2)通过反射ActivityThread获取当前进程名
            currentProcessName = currentProcessNameByActivityThread
            if (!currentProcessName.isNullOrEmpty()) {
                return currentProcessName!!
            }

            //3)通过ActivityManager获取当前进程名
            currentProcessName = currentProcessNameByActivityManager
            return currentProcessName ?: ""
        }

    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    val currentProcessNameByApplication: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) Application.getProcessName() else ""

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    val currentProcessNameByActivityThread: String
        get() {
            try {
                val cla = Class.forName(
                    "android.app.ActivityThread",
                    false,
                    Application::class.java.classLoader
                )
                val currentProcessName = ClassUtils.getMethod(cla, "currentProcessName")
                if (currentProcessName != null) {
                    return currentProcessName.toString()
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return ""
        }


    /**
     * 获取app进程
     * 如果没有获取到运行的进程，那么久调用运行的服务来判断
     * 返回默认值
     * 通过ActivityManager 获取进程名，需要IPC通信
     */
    val currentProcessNameByActivityManager: String
        get() {
            val pid = Process.myPid()
            //获取app进程
            val appProcessInfos = (AppUtils.app
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .runningAppProcesses
            if (appProcessInfos != null && !appProcessInfos.isEmpty()) {
                for (info in appProcessInfos) {
                    if (info.pid == pid && !TextUtils.isEmpty(info.processName)) {
                        return info.processName
                    }
                }
            }
            //如果没有获取到运行的进程，那么久调用运行的服务来判断
            val runningServiceInfos = (AppUtils.app
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
                Int.MAX_VALUE
            )
            if (runningServiceInfos != null && !runningServiceInfos.isEmpty()) {
                for (info in runningServiceInfos) {
                    if (info.pid == pid && !TextUtils.isEmpty(info.process)) {
                        return info.process
                    }
                }
            }
            //返回默认值
            return ""
        }

    /**
     * 获取全部后台进程
     * `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />`
     *
     * @return
     */
    val allBackgroundProcesses: Set<String>
        get() {
            val am = AppUtils.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val info = am.runningAppProcesses
            val set: HashSet<String> = HashSet()
            if (info != null) {
                for (aInfo in info) {
                    set.addAll(aInfo.pkgList)
                }
            }
            return set
        }

    /**
     * 杀掉全部后台进程
     * `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />`
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    fun killAllBackgroundProcesses(): Set<String> {
        val am = AppUtils.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var info = am.runningAppProcesses
        val set: MutableSet<String> = HashSet()
        for (aInfo in info) {
            for (pkg in aInfo.pkgList) {
                am.killBackgroundProcesses(pkg)
                set.add(pkg)
            }
        }
        info = am.runningAppProcesses
        for (aInfo in info) {
            for (pkg in aInfo.pkgList) {
                set.remove(pkg)
            }
        }
        return set
    }

    /**
     * 杀掉指定的进程
     * `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />`
     *
     * @param packageName The name of the package.
     * @return `true`: success<br></br>`false`: fail
     */
    @SuppressLint("MissingPermission")
    fun killBackgroundProcesses(packageName: String): Boolean {
        val am = AppUtils.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var info = am.runningAppProcesses
        if (info == null || info.size == 0) {
            return true
        }
        for (aInfo in info) {
            if (Arrays.asList(*aInfo.pkgList).contains(packageName)) {
                am.killBackgroundProcesses(packageName)
            }
        }
        info = am.runningAppProcesses
        if (info == null || info.size == 0) {
            return true
        }
        for (aInfo in info) {
            if (Arrays.asList(*aInfo.pkgList).contains(packageName)) {
                return false
            }
        }
        return true
    }
}