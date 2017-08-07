package com.ashlikun.utils.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.ashlikun.utils.Utils;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 10:47
 * <p>
 * 方法功能：activity的操作类，检测是否前台运行
 * < uses-permission android:name =“android.permission.GET_TASKS” />
 */

public class ActivityUtils {

    /**
     * 调用系统分享
     */
    public static void shareToOtherApp(Context context, String title, String content, String dialogTitle) {
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
    }

    /**
     * 获取App包 信息版本号
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:12
     * <p>
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:13
     * 方法功能：判断某个界面是否在前台
     *
     * @param classs 某个界面activity
     */
    public static boolean isForeground(Class classs) {
        if (Activity.class.isAssignableFrom(classs)) {
            String className = classs.getClass().getName();
            if (classs == null || TextUtils.isEmpty(className)) {
                return false;
            }
            ActivityManager am = (ActivityManager) Utils.getApp()
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
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:21
     * 方法功能：同上
     */
    public static boolean isForeground(Activity activity) {
        return activity != null ? isForeground(activity.getClass()) : false;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:25
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
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:26
     * 方法功能：把栈顶activity切换到前台，如果应用未启动就打开应
     * < uses-permission android:name =“android.permission.GET_TASKS” />
     *
     * @return 0：前台 1:处于后台  2：未启动或者被回收
     */
    public static int appBackgoundToForeground(Context context) {
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        if (taskList.get(0).topActivity.getPackageName().equals(context.getPackageName())) {
            return 0;//前台
        }
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                mAm.moveTaskToFront(rti.id, 0);
                return 1;//后台
            }
        }
        //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
        return 2;//未启动
    }
    /**
     * public static boolean isBackground(Context context) {

     Log.d("Nat: isBackground.packageName1", context.getPackageName());

     ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

     List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

     for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

     Log.d("Nat: isBackground.processName", appProcess.processName);

     if (appProcess.processName.equals(context.getPackageName())) {

     Log.d("Nat: isBackground.importance", String.valueOf(appProcess.importance));

     if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {

     Log.i("后台", appProcess.processName);

     return true;

     }else{

     Log.i("前台", appProcess.processName);

     return false;

     }

     }

     }

     return false;

     }
     */

    /**
     * class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {
    @Override protected Boolean doInBackground(Context... params) {
    final Context context = params[0].getApplicationContext();
    return isAppOnForeground(context);
    }
    private boolean isAppOnForeground(Context context) {
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
    if (appProcesses == null) {
    return false;
    }
    final String packageName = context.getPackageName();
    Log.d("Nat: isAppOnForeground.packageName", context.getPackageName());
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
    Log.d("Nat: isAppOnForeground.processName", appProcess.processName);
    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
    return true;
    }
    }
    return false;
    }
    }
     */
}
