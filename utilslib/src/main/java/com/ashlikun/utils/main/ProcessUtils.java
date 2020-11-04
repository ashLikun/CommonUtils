package com.ashlikun.utils.main;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import androidx.annotation.NonNull;

import android.text.TextUtils;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/19　9:25
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：进程相关工具
 */
public class ProcessUtils {
    //缓存
    private static String currentProcessName;

    /**
     * 是否主进程正在运行
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainProcess() {
        String name = ProcessUtils.getCurProcessName();
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return name.equals(AppUtils.getApp().getPackageName());
    }

    /**
     * 是否APP进程
     * com.ogow.activity
     * com.ogow.activity:webview
     *
     * @return 获取失败也是APP进程
     */
    public static boolean isAppProcess() {
        String name = ProcessUtils.getCurProcessName();
        if (TextUtils.isEmpty(name)) {
            return true;
        }
        return name.contains(AppUtils.getApp().getPackageName());
    }

    /**
     * 获得当前进程名称, >5.1的系统，有几率会关闭getRunningAppProcesses方法(只会返回我们自己的进程)
     *
     * @return
     */
    public static String getCurProcessName() {

        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //1)通过Application的API获取当前进程名
        currentProcessName = getCurrentProcessNameByApplication();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //2)通过反射ActivityThread获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityThread();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //3)通过ActivityManager获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityManager();

        return currentProcessName;
    }

    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    public static String getCurrentProcessNameByApplication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }
        return null;
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    public static String getCurrentProcessNameByActivityThread() {

        try {
            Class cla = Class.forName("android.app.ActivityThread", false, Application.class.getClassLoader());
            Object currentProcessName = ClassUtils.getMethod(cla, "currentProcessName");
            if (currentProcessName != null) {
                return currentProcessName.toString();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 通过ActivityManager 获取进程名，需要IPC通信
     */
    public static String getCurrentProcessNameByActivityManager() {
        int pid = Process.myPid();
        //获取app进程
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = ((android.app.ActivityManager) AppUtils.getApp()
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningAppProcesses();
        if (appProcessInfos != null && !appProcessInfos.isEmpty()) {
            for (android.app.ActivityManager.RunningAppProcessInfo info : appProcessInfos) {
                if (info.pid == pid && !TextUtils.isEmpty(info.processName)) {
                    return info.processName;
                }
            }
        }
        //如果没有获取到运行的进程，那么久调用运行的服务来判断
        List<android.app.ActivityManager.RunningServiceInfo> runningServiceInfos = ((android.app.ActivityManager) AppUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE);
        if (runningServiceInfos != null && !runningServiceInfos.isEmpty()) {
            for (ActivityManager.RunningServiceInfo info : runningServiceInfos) {
                if (info.pid == pid && !TextUtils.isEmpty(info.process)) {
                    return info.process;
                }
            }
        }
        //返回默认值
        return "";
    }

    /**
     * 获取全部后台进程
     * {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @return
     */
    public static Set<String> getAllBackgroundProcesses() {
        ActivityManager am =
                (ActivityManager) AppUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        Set<String> set = new HashSet<>();
        if (info != null) {
            for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                Collections.addAll(set, aInfo.pkgList);
            }
        }
        return set;
    }

    /**
     * 杀掉全部后台进程
     * {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static Set<String> killAllBackgroundProcesses() {
        ActivityManager am =
                (ActivityManager) AppUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        Set<String> set = new HashSet<>();
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            for (String pkg : aInfo.pkgList) {
                am.killBackgroundProcesses(pkg);
                set.add(pkg);
            }
        }
        info = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            for (String pkg : aInfo.pkgList) {
                set.remove(pkg);
            }
        }
        return set;
    }

    /**
     * 杀掉指定的进程
     * {@code <uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES" />}</p>
     *
     * @param packageName The name of the package.
     * @return {@code true}: success<br>{@code false}: fail
     */
    @SuppressLint("MissingPermission")
    public static boolean killBackgroundProcesses(@NonNull final String packageName) {
        ActivityManager am =
                (ActivityManager) AppUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (Arrays.asList(aInfo.pkgList).contains(packageName)) {
                am.killBackgroundProcesses(packageName);
            }
        }
        info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (Arrays.asList(aInfo.pkgList).contains(packageName)) {
                return false;
            }
        }
        return true;
    }
}
