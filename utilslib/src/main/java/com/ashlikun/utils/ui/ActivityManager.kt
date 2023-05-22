package com.ashlikun.utils.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import com.ashlikun.utils.ui.extend.lastElementOrNull
import com.ashlikun.utils.ui.extend.popOrNull
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 18:18
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：存放Activity的栈
 */
/**
 * 获取前台 Activity
 */
val fActivity: Activity?
    get() = ActivityManager.get().currentActivity()

/**
 * 获取前台 Activity
 */
val fCActivity: ComponentActivity?
    get() = ActivityManager.get().getTagActivity(ComponentActivity::class.java)

val fFActivity: FragmentActivity?
    get() = ActivityManager.get().getTagActivity(FragmentActivity::class.java)

inline fun <reified T : Activity> fTagActivity(): T? {
    return ActivityManager.get().getTagActivity(T::class.java)
}

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

        /**
         * 获取前台 Activity
         */
        val fActivity: Activity?
            get() = get().currentActivity()

        /**
         * 获取前台 Activity
         */
        val fCActivity: ComponentActivity?
            get() = get().getTagActivity(ComponentActivity::class.java)

        val fFActivity: FragmentActivity?
            get() = get().getTagActivity(FragmentActivity::class.java)

    }

    /**
     * 获取当前全部Activity
     */
    fun getActivitys() = activityStack.toList()

    /**
     * 获取指定的运行中的activity,只取最新的
     */
    fun <T : Activity> getTagActivity(activity: Class<out Activity>?): T? {
        if (activity == null) return null
        var returnAct = activityStack.findLast { activity.isAssignableFrom(it.javaClass) }
        if (returnAct != null && returnAct.isFinishing) {
            activityStack.remove(returnAct)
            return getTagActivity(activity)
        }
        return returnAct as T?
    }

    /**
     * 获取指定的运行中的activity，取全部的
     */
    fun <T : Activity> getTagActivitys(activity: Class<out Activity>?): List<T> {
        if (activity == null) return listOf()
        val returnAct = activityStack.filter {
            activity.isAssignableFrom(it.javaClass)
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
        // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
        if (activityStack.remove(activity)) {
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
     * @param ignoreActivitys 忽略的Activity
     */
    fun currentActivity(ignoreActivitys: Array<Class<Activity>> = emptyArray()): Activity? {
        var activity = if (ignoreActivitys.isEmpty()) activityStack.lastElementOrNull()
        else activityStack.lastOrNull { !ignoreActivitys.contains(it.javaClass) }
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
    fun removeActivity(activity: Activity?): Boolean {
        if (activity == null) return false
        // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
        return activityStack.remove(activity)
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
        activityStack.popOrNull()?.finish()
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