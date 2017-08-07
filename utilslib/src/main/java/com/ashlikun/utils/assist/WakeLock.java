package com.ashlikun.utils.assist;

import android.content.Context;
import android.os.PowerManager;

import com.ashlikun.utils.other.LogUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/6 0006 15:11
 * <p>
 * 方法功能：屏幕管理， 点亮、关闭屏幕，判断屏幕是否点亮
 * <uses-permission android:name="android.permission.WAKE_LOCK"/>
 */
public class WakeLock {
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    public WakeLock(Context context, String tag) {
        ////获取电源的服务 声明电源管理器
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, tag);
    }

    //点亮屏幕
    public void on() {
        //点亮亮屏
        LogUtils.i("PowerManager.WakeLock : wakeLock.isHeld: " + wakeLock.isHeld());
        if (!wakeLock.isHeld()) {
            LogUtils.i("PowerManager.WakeLock : 点亮屏幕");
            wakeLock.acquire();
        }
    }

    //灭掉屏幕
    public void off() {
        //释放亮屏
        LogUtils.i("PowerManager.WakeLock : wakeLock.isHeld: " + wakeLock.isHeld());
        if (wakeLock.isHeld()) {
            LogUtils.i("PowerManager.WakeLock : 灭掉屏幕");
            try {
                wakeLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //释放
    public void release() {
        if (wakeLock != null && wakeLock.isHeld()) {
            try {
                wakeLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public PowerManager.WakeLock getWakeLock() {
        return wakeLock;
    }

    public void setWakeLock(PowerManager.WakeLock wakeLock) {
        this.wakeLock = wakeLock;
    }

    public PowerManager getPowerManager() {
        return powerManager;
    }

    public void setPowerManager(PowerManager powerManager) {
        this.powerManager = powerManager;
    }
}
