package com.ashlikun.utils.bug

import java.lang.Class
import java.lang.reflect.Constructor
import java.lang.Exception
import java.lang.reflect.Method
import com.ashlikun.utils.other.ThreadPoolManage
import java.lang.Runnable
import com.ashlikun.utils.bug.BugUtils
import android.app.Activity
import android.os.Build
import com.ashlikun.utils.other.LogUtils
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import com.ashlikun.utils.other.coroutines.taskLaunch

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:22
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：一些bug的处理
 */

class BugUtils {


    companion object {
        fun init() {
            taskLaunch {
                finalizeTimedOut()
            }
        }

        /**
         * 部分OPPO机型 AssetManager.finalize() timed out的修复
         * https://www.jianshu.com/p/89e2719be9c7
         * 仔细核查后发现多发在OPPO系列的机型中，包括R9 A33 A59等等。猜测是OPPO的定制ROM在底层做了什么修改。为了减少这样的报错，只有对OPPO进行特殊处理。
         * 查找了一些资料后发现这类错误是由于回收对象时间过长，由FinalizerWatchdogDaemon负责计时，超时后抛出异常关闭VM的。
         * 那么有两种解决办法：1关掉这个负责计时的，2延长计时时间
         */
        fun finalizeTimedOut() {
            try {
                val clazz = Class.forName("java.lang.Daemons\$FinalizerWatchdogDaemon")
                val method = clazz.superclass!!.getDeclaredMethod("stop")
                method.isAccessible = true
                val field = clazz.getDeclaredField("INSTANCE")
                field.isAccessible = true
                method.invoke(field[null])
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        /**
         * 当targetSDKVersion为26或者27时，在 Android 8.0 的设备上，一些设置了windowIsTranslucent标志，将背景设为透明，同事将屏幕方向锁定的Activity会崩溃    8.1之后已经修复
         *
         * @param activity
         * @return
         */
        fun orientationBug8_0(activity: Activity) {
            /**
             *
             * if (getApplicationInfo().targetSdkVersion > O && mActivityInfo.isFixedOrientation()) {
             * final TypedArray ta = obtainStyledAttributes(com.android.internal.R.styleable.Window);
             * final boolean isTranslucentOrFloating = ActivityInfo.isTranslucentOrFloating(ta);
             * ta.recycle();
             *
             * if (isTranslucentOrFloating) {
             * throw new IllegalStateException(
             * "Only fullscreen opaque activities can request orientation");
             * }
             * }
             */
            //适配8.0  并且只在8.0 不能同时固定屏幕和透明背景
            if (activity.applicationInfo.targetSdkVersion > Build.VERSION_CODES.O && Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating(
                    activity
                )
            ) {
                val result = fixOrientation(activity)
                LogUtils.e("onCreate fixOrientation when Oreo, result = $result")
            }
        }

        fun fixOrientation(activity: Activity?): Boolean {
            try {
                val field = Activity::class.java.getDeclaredField("mActivityInfo")
                field.isAccessible = true
                val o = field[activity] as ActivityInfo
                o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                field.isAccessible = false
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        /**
         * 是否是透明
         *
         * @param activity
         * @return
         */
        fun isTranslucentOrFloating(activity: Activity): Boolean {
            var isTranslucentOrFloating = false
            try {
                val styleableRes = Class.forName("com.android.internal.R\$styleable")
                    .getField("Window")[null] as IntArray
                val ta = activity.obtainStyledAttributes(styleableRes)
                val m = ActivityInfo::class.java.getMethod(
                    "isTranslucentOrFloating",
                    TypedArray::class.java
                )
                m.isAccessible = true
                isTranslucentOrFloating = m.invoke(null, ta) as Boolean
                m.isAccessible = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return isTranslucentOrFloating
        }

        /**
         * Android P（Android 9）出现Detected problems with API compatibility问题解决
         * https://blog.csdn.net/wjw_java_android/article/details/108381123
         */
        fun closeAndroidPDialog() {
            try {
                val aClass = Class.forName("android.content.pm.PackageParser\$Package")
                val declaredConstructor = aClass.getDeclaredConstructor(String::class.java)
                declaredConstructor.setAccessible(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val cls = Class.forName("android.app.ActivityThread")
                val declaredMethod = cls.getDeclaredMethod("currentActivityThread")
                declaredMethod.isAccessible = true
                val activityThread = declaredMethod.invoke(null)
                val mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown")
                mHiddenApiWarningShown.isAccessible = true
                mHiddenApiWarningShown.setBoolean(activityThread, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}