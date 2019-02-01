package com.ashlikun.utils.ui;

import android.app.Activity;

import java.util.Stack;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/3/27 17:54
 * <p>
 * 方法功能：存放Activity的栈
 */

public class ActivityManager {
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    /**
     * 获取指定的运行中的activity
     *
     * @param activity
     * @param <T>
     * @return
     */
    public <T> T getTagActivity(Class<? extends Activity> activity) {
        Activity returnAct = null;
        if (activity != null) {
            for (Activity a : activityStack) {
                if (a.getClass() == activity) {
                    returnAct = a;
                    break;
                }
            }
        }
        if (returnAct != null && returnAct.isFinishing()) {
            activityStack.remove(returnAct);
            returnAct = null;
        }
        return (T) returnAct;
    }

    private ActivityManager() {
    }

    /**
     * 获取前台 Activity
     */
    public static Activity getForegroundActivity() {
        return getInstance().currentActivity();
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 退出Activity
     *
     * @param activity
     */
    public void exitActivity(Activity activity) {
        if (activity != null) {
            if (activityStack.contains(activity)) {
                // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 退出Activity
     *
     * @param activity
     */
    public void exitActivity(Class activity) {
        if (activity != null) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            for (Activity a : activityStack) {
                if (a.getClass() == activity) {
                    activityStack.remove(a);
                    a.finish();
                    a = null;
                    break;
                }
            }

        }
    }

    /**
     * 获得当前栈顶Activity
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.empty()) {
            activity = activityStack.lastElement();
            if (activity != null && activity.isFinishing()) {
                activityStack.remove(activity);
                activity = null;
            }
        }
        return activity;
    }

    /**
     * 当前栈顶是不是这个activity
     *
     * @return
     */
    public boolean currentActivity(Class<? extends Activity> activityClas) {
        Activity activity = currentActivity();
        if (activity != null && activity.getClass() == activityClas) {
            return true;
        }
        return false;
    }

    /**
     * 将当前Activity推入栈中
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.addElement(activity);
    }

    /**
     * 退出栈中所有Activity
     */
    public void exitAllActivity() {

        try {
            while (true) {
                Activity activity = currentActivity();
                if (activity == null) {
                    break;
                }
                exitActivity(activity);
            }
        } catch (Exception e) {

        }

    }

    /**
     * 退出栈中所有Activity
     *
     * @param activity
     */
    public void exitAllActivity(Class... activity) {
        try {
            for (Activity a : activityStack) {
                if (a == null) {
                    continue;
                }
                boolean isfinish = true;
                for (Class c : activity) {
                    if (a.getClass() == c) {
                        isfinish = false;
                        break;
                    }
                }
                if (isfinish) {
                    exitActivity(a);
                }
            }
        } catch (Exception e) {

        }

    }

}
