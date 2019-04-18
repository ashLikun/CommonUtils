package com.ashlikun.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * 作者　　: 李坤
 * 创建时间: 2019/4/18　14:38
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：对{@link Application.ActivityLifecycleCallbacks} 的简化
 */
public abstract class ActivityLifecycleCallbacksAdapter implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
