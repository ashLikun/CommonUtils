package com.ashlikun.utils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.ashlikun.utils.main.ProcessUtils;
import com.ashlikun.utils.provider.BaseContentProvider;

import java.io.File;

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
    public static ApplicationListener activityLifecycleCallbacks = new ApplicationListener();


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/7 10:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：一定要在Application里面调用
     */

    public static void init(Application myApp) {
        AppUtils.myApp = myApp;
        myApp.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
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
     * 前后台监听
     *
     * @param listener
     */
    public static void addAppRunChang(ApplicationListener.OnChangListener listener) {
        activityLifecycleCallbacks.addOnChangListener(listener);
    }

    /**
     * 前后台监听
     *
     * @param listener
     */
    public static void removeAppRunChang(ApplicationListener.OnChangListener listener) {
        activityLifecycleCallbacks.removeOnChangListener(listener);
    }

    /**
     * 是否是主进程
     * 如果getCurProcessName为空就默认认为不是主进程
     *
     * @return
     */
    public static boolean isMainProcess() {
        String name = ProcessUtils.getCurProcessName();
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return name.equals(myApp.getPackageName());
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
        return name.contains(myApp.getPackageName());
    }

    /**
     * 兼容高版本的fileprovider
     *
     * @param file
     * @return
     */
    public static Uri getUri(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = getApp().getPackageName() + ".Provider";
            return FileProvider.getUriForFile(getApp(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
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

    /**
     * 获取应用程序名称
     */
    public static String getAppName() {
        int labelRes = getPackageInfo().applicationInfo.labelRes;
        return getApp().getResources().getString(labelRes);
    }

    /**
     * 获取App包 信息版本号
     */
    public static PackageInfo getPackageInfo() {
        PackageManager packageManager = getApp().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getApp().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }


}
