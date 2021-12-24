package com.ashlikun.utils.ui

import android.app.Activity
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 18:18
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：存放Activity的栈
 */

class ActivityManager private constructor() {
    companion object {
        private var activityStack: Stack<Activity> = Stack()
        private val instance by lazy { ActivityManager() }
        fun get(): ActivityManager = instance

        /**
         * 获取前台 Activity
         */
        val foregroundActivity: Activity?
            get() = get().currentActivity()
    }

    /**
     * 获取指定的运行中的activity,只取最新的
     */
    fun <T : Activity> getTagActivity(activity: Class<out Activity>?): T? {
        if (activity == null) return null
        var returnAct = activityStack.findLast { it.javaClass == activity }
        if (returnAct != null && returnAct.isFinishing) {
            activityStack.remove(returnAct)
            return getTagActivity<T>(activity)
        }
        return returnAct as T?
    }

    /**
     * 获取指定的运行中的activity，取全部的
     */
    fun <T : Activity> getTagActivitys(activity: Class<out Activity>?): List<T> {
        if (activity == null) return listOf()
        val returnAct = activityStack.filter {
            it.javaClass == activity
        }.toMutableList().filter {
            if (it.isFinishing) {
                activityStack.remove(it)
            }
            !it.isFinishing
        }
        return returnAct as List<T>
    }

    /**
     * 退出Activity
     */
    fun exitActivity(activity: Activity?) {
        if (activity == null) return
        if (activityStack.contains(activity)) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activityStack.remove(activity)
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 退出这个Activity
     * @param isOne 如果多个 是否只是退出最新的一个
     * @param activity
     */
    fun exitActivity(activity: Class<out Activity>?, isOne: Boolean = true) {
        if (activity == null) return
        // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
        activityStack.reversed().forEach {
            if (it.javaClass == activity) {
                activityStack.remove(it)
                it.finish()
                if (isOne) return@forEach
            }
        }
    }

    /**
     * 是否已经有这个activity
     *
     * @param activity
     * @return
     */
    fun contains(activity: Class<out Activity>?) =
        if (activity == null) false else activityStack.find {
            !it.isFinishing && it.javaClass == activity
        }

    /**
     * 回退到指定的activity
     * @param isFinishThis 是否销毁这个Activity
     */
    fun goBack(activity: Class<out Activity>?, isFinishThis: Boolean = false): Boolean {
        if (activity == null) return false
        if (contains(activity) != null) {
            while (activityStack.size > 1) {
                if (!currentActivity(activity)) exitTopActivity()
                else break
            }
            //退出当前activity
            if (isFinishThis) exitTopActivity()
            return true
        }
        return false
    }

    /**
     * 获得当前栈顶Activity
     */
    fun currentActivity(): Activity? {
        var activity = activityStack.lastElement()
        if (activity?.isFinishing == true) {
            activityStack.remove(activity)
            return currentActivity()
        }
        return activity
    }

    /**
     * 当前栈顶是不是这个activity
     *
     * @return
     */
    fun currentActivity(activityClas: Class<out Activity>?) = if (activityClas == null) false else
        currentActivity()?.javaClass == activityClas

    /**
     * 将当前Activity推入栈中
     */
    fun pushActivity(activity: Activity?) {
        if (activity == null) return
        activityStack.addElement(activity)
    }

    /**
     * 将当前Activity从栈中退出
     */
    fun removeActivity(activity: Activity?) {
        if (activity == null) return
        if (activityStack.contains(activity)) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activityStack.remove(activity)
        }
    }

    /**
     * 退出栈中所有Activity
     */
    fun exitAllActivity() {
        while (true) {
            val activity = currentActivity() ?: break
            exitActivity(activity)
        }
    }

    /**
     * 退出栈中所有Activity
     */
    fun exitAllActivity(vararg activity: Class<out Activity>?) {
        if (activity == null) return
        for (c in activity) {
            exitActivity(c)
        }
    }

    /**
     * 退出栈顶activity
     */
    fun exitTopActivity() {
        activityStack.pop()?.finish()
    }

    /**
     * 获取 指定activity 个数
     */
    fun getActivitySize(activityClas: Class<out Activity>?): Int {
        if (activityClas == null) return 0
        return activityStack.reversed().filter {
            if (it.isFinishing) activityStack.remove(it)
            it.javaClass == activityClas
        }.size
    }

    /**
     * 获取activity 个数
     *
     * @return
     */
    val activitySize: Int
        get() = activityStack.size


}