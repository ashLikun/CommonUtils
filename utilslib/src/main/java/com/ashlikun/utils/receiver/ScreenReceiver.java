package com.ashlikun.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ashlikun.utils.other.LogUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/7 10:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 方法功能：屏幕接收器，可收到屏幕点亮、关闭的广播，并通过回调通知给调用者
 */

public class ScreenReceiver extends BroadcastReceiver {
    private ScreenListener screenListener;

    public ScreenReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            LogUtils.i("屏幕解锁广播...");
            if (screenListener != null) {
                screenListener.screenOn();
            }
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            LogUtils.i("屏幕加锁广播...");
            if (screenListener != null) {
                screenListener.screenOff();
            }
        }
    }

    public void registerScreenReceiver(Context context, ScreenListener screenListener) {
        try {
            this.screenListener = screenListener;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            LogUtils.i("注册屏幕解锁、加锁广播接收者...");
            context.registerReceiver(this, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterScreenReceiver(Context context) {
        try {
            context.unregisterReceiver(this);
            LogUtils.i("注销屏幕解锁、加锁广播接收者...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface ScreenListener {
        public void screenOn();

        public void screenOff();
    }

}