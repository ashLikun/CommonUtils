package com.ashlikun.utils.main

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.view.ContextThemeWrapper
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.ui.fActivity
import com.ashlikun.utils.ui.notify.NotifyActivity

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 22:53
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：activity的操作类，检测是否前台运行,获取进程名称
 * < uses-permission android:name =“android.permission.GET_TASKS” />
 */

object ActivityUtils {
    /**
     * 获取全部ActivityInfo
     */
    fun activitys() = AppUtils.app.packageManager.getPackageInfo(AppUtils.app.packageName, PackageManager.GET_ACTIVITIES)?.activities ?: emptyArray()

    /**
     * 获取指定的ActivityInfo
     */
    fun activity(className: String) =
        AppUtils.app.packageManager.getPackageInfo(AppUtils.app.packageName, PackageManager.GET_ACTIVITIES).activities?.find { it.name == className }

    /**
     * 是否存在这个activity
     *
     * @param pkg 包名
     * @param cls class名
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isActivityExists(pkg: String, cls: String): Boolean {
        val intent = Intent()
        intent.setClassName(pkg, cls)
        return !(AppUtils.app.packageManager.resolveActivity(
            intent,
            0
        ) == null || intent.resolveActivity(AppUtils.app.packageManager) == null || AppUtils.app.packageManager.queryIntentActivities(
            intent,
            0
        ).size == 0)
    }

    /**
     * 从context中获取activity，如果context不是activity那么久返回null
     */
    fun getActivity(context: Context?): Activity? {
        if (context == null) {
            return null
        }
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return getActivity(context.baseContext)
        } else if (context is ContextThemeWrapper) {
            return getActivity(context.baseContext)
        }
        return null
    }

    /**
     * 判断某个界面是否在前台
     *
     * @return true:在前台，false:不在
     */
    val isForeground: Boolean
        get() = AppUtils.activityLifecycleCallbacks.isForeground


    /**
     * 获取app运行状态
     * 把栈顶activity切换到前台，如果应用未启动就打开应
     * <uses-permission android:name="android.permission.GET_TASKS"></uses-permission>
     * //后台移动到前台需要
     * <uses-permission android:name="android.permission.REORDER_TASKS"></uses-permission>
     * @param ismoveTaskToFront 是否把任务栈移动到顶部
     * @return 0：前台 1:处于后台  2：未启动或者被回收
     */
    fun appRunStatus(ismoveTaskToFront: Boolean = true, ignoreTaskAffinitys: Array<String> = emptyArray(), ignoreActivitys: Array<Class<out Activity>> = emptyArray()): Int {
        return if (!isForeground) {
            if (com.ashlikun.utils.ui.ActivityManager.get().currentActivity(ignoreActivitys + arrayOf(NotifyActivity::class.java)) == null) {
                //一个页面不存在就未启动
                return 2
            }
            //处于后台
            var isHoutTai = false
            //获取ActivityManager
            val am =
                AppUtils.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val activities = AppUtils.app.packageManager.getPackageInfo(AppUtils.app.packageName, PackageManager.GET_ACTIVITIES).activities
            //获得当前运行的task,反转是为了对应app的任务栈顺序
            am.getRunningTasks(100)?.reversed()?.forEach {

                //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
                it.describeContents()
                if (it.topActivity?.packageName == AppUtils.app.packageName) {
                    //这里移动全部的任务栈
                    if (ismoveTaskToFront) {
                        am.moveTaskToFront(it.id, 0)
                    }
                    //后台
                    val actInfo = activities.find { itt -> itt.name == it.topActivity?.className }
                    if (actInfo?.taskAffinity != "jpush.custom" && ignoreTaskAffinitys.find { actInfo?.taskAffinity == it } == null) {
                        isHoutTai = true
                    }
                }
            }
            //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
            //未启动
            if (isHoutTai) 1 else 2
        } else 0
    }

    /**
     * 返回启动Activity的名称。
     */
    fun getLauncherActivity(pkg: String): String {
        if (pkg.isEmpty()) return ""
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(pkg)
        val pm: PackageManager = AppUtils.app.getPackageManager()
        val info = pm.queryIntentActivities(intent, 0)
        return info.getOrNull(0)?.activityInfo?.name.orEmpty()
    }
}