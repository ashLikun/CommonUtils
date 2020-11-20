package com.ashlikun.utils.bug;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;

import com.ashlikun.utils.other.LogUtils;
import com.ashlikun.utils.other.ThreadPoolManage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者　　: 李坤
 * 创建时间: 2019/9/26　10:49
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：一些bug的处理
 */
public class BugUtils {
    public static void init() {
        ThreadPoolManage.get().execute(new Runnable() {
            @Override
            public void run() {
                finalizeTimedOut();
            }
        });
    }

    /**
     * 部分OPPO机型 AssetManager.finalize() timed out的修复
     * https://www.jianshu.com/p/89e2719be9c7
     * 仔细核查后发现多发在OPPO系列的机型中，包括R9 A33 A59等等。猜测是OPPO的定制ROM在底层做了什么修改。为了减少这样的报错，只有对OPPO进行特殊处理。
     * 查找了一些资料后发现这类错误是由于回收对象时间过长，由FinalizerWatchdogDaemon负责计时，超时后抛出异常关闭VM的。
     * 那么有两种解决办法：1关掉这个负责计时的，2延长计时时间
     */
    public static void finalizeTimedOut() {

        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 当targetSDKVersion为26或者27时，在 Android 8.0 的设备上，一些设置了windowIsTranslucent标志，将背景设为透明，同事将屏幕方向锁定的Activity会崩溃    8.1之后已经修复
     *
     * @param activity
     * @return
     */
    public static void orientationBug8_0(Activity activity) {
        /**
         *
         if (getApplicationInfo().targetSdkVersion > O && mActivityInfo.isFixedOrientation()) {
                    final TypedArray ta = obtainStyledAttributes(com.android.internal.R.styleable.Window);
                    final boolean isTranslucentOrFloating = ActivityInfo.isTranslucentOrFloating(ta);
                    ta.recycle();

                    if (isTranslucentOrFloating) {
                        throw new IllegalStateException(
                            "Only fullscreen opaque activities can request orientation");
                    }
         }
         */
        //适配8.0  并且只在8.0 不能同时固定屏幕和透明背景
        if (activity.getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.O && Build.VERSION.SDK_INT == Build.VERSION_CODES.O
                && isTranslucentOrFloating(activity)) {
            boolean result = fixOrientation(activity);
            LogUtils.e("onCreate fixOrientation when Oreo, result = " + result);
        }
    }

    public static boolean fixOrientation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(activity);
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 是否是透明
     *
     * @param activity
     * @return
     */
    public static boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = activity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }
}
