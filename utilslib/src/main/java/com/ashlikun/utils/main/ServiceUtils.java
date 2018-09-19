package com.ashlikun.utils.main;

import android.app.ActivityManager;
import android.content.Context;

import com.ashlikun.utils.AppUtils;

import java.util.Iterator;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/19　9:31
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：服务工具
 */
public class ServiceUtils {
    /**
     * 服务是否启动了
     */
    public static boolean isServiceStart(Class serviceName) {
        Iterator localIterator = ((ActivityManager) AppUtils.getApp().getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE).iterator();
        if (localIterator == null) {
            return false;
        }
        while (localIterator.hasNext()) {
            ActivityManager.RunningServiceInfo runningService = (ActivityManager.RunningServiceInfo) localIterator.next();
            if (runningService.service.getClassName().toString()
                    .equals(serviceName.getName())) {
                return true;
            }
        }
        return true;
    }
}
