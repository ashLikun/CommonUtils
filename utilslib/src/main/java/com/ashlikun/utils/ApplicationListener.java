package com.ashlikun.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.ashlikun.utils.ui.ActivityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author　　: 李坤
 * 创建时间: 2019/2/1 16:55
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：前后台监听，管理activityManage
 */
public class ApplicationListener implements Application.ActivityLifecycleCallbacks {

    // 位于前台的 Activity 的数目
    private int foregroundCount = 0;

    private List<OnChangListener> onChangListeners = new ArrayList<>();

    /**
     * 方法功能：判断某个界面是否在前台
     *
     * @return true:在前台，false:不在
     */
    public boolean isForeground() {
        return foregroundCount > 0;
    }

    /**
     * 前后台监听
     *
     * @param listener
     */
    public void addOnChangListener(OnChangListener listener) {
        if (!onChangListeners.contains(listener)) {
            onChangListeners.add(listener);
        }
    }

    /**
     * 前后台监听
     *
     * @param listener
     */
    public void removeOnChangListener(OnChangListener listener) {
        if (onChangListeners.contains(listener)) {
            onChangListeners.remove(listener);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (foregroundCount <= 0) {
            //进入前台
            for (int i = 0; i < onChangListeners.size(); i++) {
                onChangListeners.get(i).onChang(true);
            }
        }
        foregroundCount++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        foregroundCount--;
        if (foregroundCount <= 0) {
            //进入后台
            for (int i = 0; i < onChangListeners.size(); i++) {
                onChangListeners.get(i).onChang(false);
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityManager.getInstance().pushActivity(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityManager.getInstance().removeActivity(activity);
    }

    public interface OnChangListener {
        void onChang(boolean isForeground);
    }

}
