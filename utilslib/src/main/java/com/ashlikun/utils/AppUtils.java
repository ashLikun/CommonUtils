package com.ashlikun.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.ashlikun.utils.provider.BaseContentProvider;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2016/12/30　9:59
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class AppUtils {
    private static boolean isDebug;
    private static Application myApp;
    private static int versionCode = -10088;
    private static String versionName;
    private static String packageName;

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/7 10:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：一定要在Application里面调用
     */

    public static void init(Application myApp) {
        AppUtils.myApp = myApp;
        //设置内容提供者的名字
        BaseContentProvider.AUTHORITY = myApp.getApplicationInfo().packageName + "ContentProvider";
    }

    public static void setDebug(boolean isDebug) {
        AppUtils.isDebug = isDebug;
    }


    public static Application getApp() {
        return AppUtils.myApp;
    }

    public static boolean isDebug() {
        return AppUtils.isDebug;
    }

    /**
     * 是否是主进程
     * 如果getCurProcessName为空就默认认为不是主进程
     *
     * @return
     */
    public static boolean isMainProcess() {
        String name = getCurProcessName();
        if (name == null || name.trim().length() == 0) {
            return false;
        }
        return name.equals(myApp.getApplicationInfo().packageName);
    }

    /**
     * 获得当前进程名称, >5.1的系统，有几率会关闭getRunningAppProcesses方法(只会返回我们自己的进程)
     *
     * @return
     */
    public static String getCurProcessName() {
        int pid = Process.myPid();
        //获取app进程
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = ((ActivityManager) getApp()
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningAppProcesses();
        if (appProcessInfos != null && !appProcessInfos.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo info : appProcessInfos) {
                if (info.pid == pid && !TextUtils.isEmpty(info.processName)) {
                    return info.processName;
                }
            }
        }
        //如果没有获取到运行的进程，那么久调用运行的服务来判断
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = ((ActivityManager) getApp().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE);
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
     * 兼容高版本的fileprovider
     *
     * @param file
     * @return
     */
    public static Uri getUri(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = myApp.getApplicationInfo().packageName + ".Provider";
            return FileProvider.getUriForFile(myApp, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 服务是否启动了
     */
    public static boolean isServiceStart(Class serviceName) {
        Iterator localIterator = ((ActivityManager) getApp().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE).iterator();
        if (localIterator == null) {
            return false;
        }
        while (localIterator.hasNext()) {
            ActivityManager.RunningServiceInfo runningService = (ActivityManager.RunningServiceInfo) localIterator.next();
            if (runningService.service.getClassName().toString()
                    .equals(serviceName.getName())) {
                return true;
            }
        }
        return true;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName() {
        if (TextUtils.isEmpty(versionName)) {
            PackageManager pm = AppUtils.getApp().getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(AppUtils.getApp().getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (versionName == null) {
            versionName = "";
        }
        return versionName;
    }

    /**
     * 获取版本code
     *
     * @return
     */
    public static int getVersionCode() {
        if (versionCode < 0) {
            PackageManager pm = AppUtils.getApp().getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(AppUtils.getApp().getPackageName(), 0);
                versionCode = packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionCode;
    }

    public static String getPackageName() {
        if (TextUtils.isEmpty(packageName)) {
            PackageManager pm = AppUtils.getApp().getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(AppUtils.getApp().getPackageName(), 0);
                packageName = packageInfo.packageName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (packageName == null) {
            packageName = "";
        }
        return packageName;
    }
}
