package com.ashlikun.utils.ui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/3/27 17:54
 * <p>
 * 方法功能：存放Activity的栈
 */

public class ActivityManager {
    private static Stack<Activity> activityStack = new Stack<>();
    private static ActivityManager instance;

    /**
     * 获取指定的运行中的activity,只取最新的
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
            return getTagActivity(activity);
        }
        return (T) returnAct;
    }

    /**
     * 获取指定的运行中的activity，取全部的
     */
    public <T extends Activity> List<T> getTagActivitys(Class<? extends Activity> activity) {
        List<T> returnAct = new ArrayList<T>();
        if (activity != null) {
            for (Activity a : activityStack) {
                if (a.getClass() == activity) {
                    returnAct.add((T) a);
                }
            }
        }
        Iterator iterator = returnAct.iterator();
        while (iterator.hasNext()){

        }
        for (int i = 0; i < ; i++) {

        }
        for (T a : returnAct) {
            if (a != null && a.isFinishing()) {
                activityStack.remove(a);
            }
        }

        return returnAct;
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
     * 是否已经有这个activity
     *
     * @param activity
     * @return
     */
    public Activity contains(Class<? extends Activity> activity) {
        if (!activityStack.isEmpty()) {
            for (int i = 0; i < activityStack.size(); i++) {
                if (!activityStack.get(i).isFinishing() &&
                        activityStack.get(i).getClass() == activity) {
                    return activityStack.get(i);
                }
            }
        }
        return null;
    }

    /**
     * 回退到指定的activity
     */
    public boolean goBack(Class<? extends Activity> activity) {
        if (contains(activity) != null) {
            while (activityStack.size() > 1) {
                if (!currentActivity(activity)) {
                    exitTopActivity();
                } else {
                    break;
                }
            }
            return true;

        }
        return false;
    }

    /**
     * 回退到指定的activity，并且销毁指定的activity
     */
    public boolean goBackFinish(Class<? extends Activity> activity) {
        Activity tagActivity = contains(activity);
        if (tagActivity != null) {
            while (!activityStack.isEmpty()) {
                if (!currentActivity(activity)) {
                    exitTopActivity();
                } else {
                    break;
                }
            }
            //退出当前activity
            exitTopActivity();
            return true;
        }
        return false;
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
                return currentActivity();
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

    /**
     * 退出栈顶activity
     */
    public void exitTopActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            Activity activity = activityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 获取 指定activity 个数
     *
     * @return
     */
    public int getActivitySize(Class<? extends Activity> activityClas) {
        int size = 0;
        if (activityStack != null && !activityStack.isEmpty()) {
            ArrayList<Activity> finifshA = new ArrayList<>();
            for (int i = 0; i < activityStack.size(); i++) {
                Activity activity = activityStack.get(i);
                if (activity.isFinishing()) {
                    finifshA.add(activity);
                } else if (activity.getClass() == activityClas) {
                    size++;
                }
            }
            for (Activity a : finifshA) {
                a.finish();
            }
        }
        return size;
    }

    /**
     * 获取activity 个数
     *
     * @return
     */
    public int getActivitySize() {
        if (activityStack != null) {
            return activityStack.size();
        }
        return 0;
    }
}
