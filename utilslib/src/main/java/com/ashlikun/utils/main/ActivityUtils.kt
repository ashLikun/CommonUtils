package com.ashlikun.utils.main

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.view.ContextThemeWrapper
import com.ashlikun.utils.AppUtils

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
     * 是否存在这个activity
     *
     * @param pkg 包名
     * @param cls class名
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isActivityExists(
        pkg: String,
        cls: String
    ): Boolean {
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
    fun appRunStatus(ismoveTaskToFront: Boolean = true): Int {
        return if (!isForeground) {
            //处于后台
            var isHoutTai = false
            //获取ActivityManager
            val am =
                AppUtils.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            //获得当前运行的task,反转是为了对应app的任务栈顺序
            am.getRunningTasks(100)?.reversed()?.forEach {
                //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
                if (it.topActivity!!.packageName == AppUtils.app.packageName) {
                    //这里移动全部的任务栈
                    if (ismoveTaskToFront) {
                        am.moveTaskToFront(it.id, 0)
                    }
                    //后台
                    isHoutTai = true
                }
            }
            //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
            //未启动
            if (isHoutTai) 1 else 2
        } else 0
    }
}