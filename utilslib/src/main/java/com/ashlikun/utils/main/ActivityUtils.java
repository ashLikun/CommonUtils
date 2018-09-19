package com.ashlikun.utils.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ashlikun.utils.AppUtils;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 10:47
 * <p>
 * 方法功能：activity的操作类，检测是否前台运行,获取进程名称
 * < uses-permission android:name =“android.permission.GET_TASKS” />
 */

public class ActivityUtils {


    /**
     * 是否存在这个activity
     *
     * @param pkg 包名
     * @param cls class名
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isActivityExists(@NonNull final String pkg,
                                           @NonNull final String cls) {
        Intent intent = new Intent();
        intent.setClassName(pkg, cls);
        return !(AppUtils.getApp().getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(AppUtils.getApp().getPackageManager()) == null ||
                AppUtils.getApp().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }



    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 方法功能：判断某个界面是否在前台
     *
     * @param classs 某个界面activity
     * @return true:在前台，false:不在
     */
    public static boolean isForeground(Class classs) {
        if (Activity.class.isAssignableFrom(classs)) {
            String className = classs.getName();
            if (classs == null || TextUtils.isEmpty(className)) {
                return false;
            }
            ActivityManager am = (ActivityManager) AppUtils.getApp()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
            if (list != null && list.size() > 0) {
                ComponentName cpn = list.get(0).topActivity;
                if (className.equals(cpn.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 方法功能：同上
     */
    public static boolean isForeground(Activity activity) {
        return activity != null ? isForeground(activity.getClass()) : false;
    }

    /**
     * 方法功能：判断应用是否处于前台
     * < uses-permission android:name =“android.permission.GET_TASKS” />
     *
     * @param context 上下文对象
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        if (runningTasks != null) {
            ActivityManager.RunningTaskInfo foregroundTaskInfo = runningTasks.get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
            if (context.getPackageName().equals(foregroundTaskPackageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法功能：把栈顶activity切换到前台，如果应用未启动就打开应
     * < uses-permission android:name =“android.permission.GET_TASKS” />
     *
     * @return 0：前台 1:处于后台  2：未启动或者被回收
     */
    public static int appBackgoundToForeground(Context context) {
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        if (taskList.get(0).topActivity.getPackageName().equals(context.getPackageName())) {
            return 0;//前台
        }
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                mAm.moveTaskToFront(rti.id, 0);
                //后台
                return 1;
            }
        }
        //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
        //未启动
        return 2;
    }


}
