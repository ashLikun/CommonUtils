package com.ashlikun.utils.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import com.ashlikun.utils.AppUtils;

import java.util.ArrayList;

/**
 * @author MaTianyu
 * @date 2014-11-19
 */

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/7 15:07
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：通知栏工具
 */

public class NotificationUtil {
    private static int LedID = 0;

    /**
     * @param activityClass 点击通知后进入的Activity
     * @param bundle        intent参数
     * @param id            通知id
     * @param icon          图标
     * @param title         标题
     * @param msg           主题消息
     */
    public static void notification(String activityClass,
                                    Bundle bundle,
                                    int id,
                                    int icon,
                                    String title,
                                    String msg) {
        // 设置通知的事件消息
        Intent intent = new Intent();
        intent.setPackage(AppUtils.getApp().getPackageName());
        intent.putExtras(bundle);
        intent.setComponent(new ComponentName(AppUtils.getApp().getPackageName(), activityClass));
        notification(intent, id, icon, title, msg);
    }

    /**
     * @param intent 点击通知后进入的Activity
     * @param id     通知id
     * @param icon   图标
     * @param title  标题
     * @param msg    主题消息
     */
    public static void notification(Intent intent,
                                    int id,
                                    int icon,
                                    String title,
                                    String msg) {
        notification(intent, id, icon, title, msg, true);
    }

    /**
     * @param intent     点击通知后进入的Activity
     * @param id         通知id
     * @param icon       图标
     * @param title      标题
     * @param msg        主题消息
     * @param autoCancel 是否自动取消，点击的时候
     */
    public static void notification(Intent intent,
                                    int id,
                                    int icon,
                                    String title,
                                    String msg,
                                    boolean autoCancel) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppUtils.getApp(), AppUtils.getApp().getPackageName())
                //左部图标
                .setSmallIcon(icon)
                //上部标题
                .setContentTitle(title)
                //中部通知内容
                .setContentText(msg)
                //点击通知后自动消失
                .setAutoCancel(autoCancel);
        //通知的声音震动等都随系统
        builder.setDefaults(Notification.DEFAULT_ALL);
        //也可以选择使用声音文件，setSound(uri)
        PendingIntent resultPendingIntent = PendingIntent.getActivity(AppUtils.getApp(), 0, intent,
                //允许更新
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        //用消息的id对应的hashCode作为通知id
        //如果没有就创建，如果有就更新，
        //第一个参数是设置创建通知的id或者需要更新通知的id
        //发出状态栏通知
        NotificationManager nm = (NotificationManager) AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        // 此处必须兼容android O设备，否则系统版本在O以上可能不展示通知栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    AppUtils.getApp().getPackageName(),
                    AppUtils.getAppName(),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            nm.createNotificationChannel(channel);
        }
        nm.notify(AppUtils.getAppName(), id, builder.build());
    }

    /**
     * 取消通知
     *
     * @param notificationId，通知的id
     */
    public static void cancel(int notificationId) {
        NotificationManager nm = (NotificationManager) AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        //撤销指定id通知
        nm.cancel(notificationId);
    }

    /**
     * 取消通知
     *
     * @param tag，通知的tag
     * @param notificationId，通知的id
     */
    public static void cancel(String tag, int notificationId) {
        NotificationManager nm = (NotificationManager) AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        //撤销指定id通知
        nm.cancel(tag, notificationId);
    }

    /**
     * 取消全部通知
     */
    public static void cancel() {
        NotificationManager nm = (NotificationManager) AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        //撤销本程序发出的全部通知
        nm.cancelAll();
    }


    /********************************************************************************************
     *                                           下面是手机顶部小灯的通知
     ********************************************************************************************/

    public static void lightLed(Context context, int colorOx, int durationMS) {
        lightLed(context, colorOx, 0, durationMS);
    }

    public static void lightLed(Context context, int colorOx, int startOffMS, int durationMS) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.ledARGB = colorOx;
        notification.ledOffMS = startOffMS;
        notification.ledOnMS = durationMS;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        LedID++;
        nm.notify(LedID, notification);
        nm.cancel(LedID);
    }

    public static void lightLed(final Context context, final int colorOx, final int startOffMS, final int durationMS,
                                int repeat) {
        if (repeat < 1) {
            repeat = 1;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        for (int i = 0; i < repeat; i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lightLed(context, colorOx, startOffMS, durationMS);
                }
            }, (startOffMS + durationMS) * i);
        }
    }

    public static void lightLed(Context context, ArrayList<LightPattern> patterns) {
        if (patterns == null) {
            return;
        }
        for (LightPattern lp : patterns) {
            lightLed(context, lp.argb, lp.startOffMS, lp.durationMS);
        }
    }

    public static class LightPattern {
        public int argb = 0;
        public int startOffMS = 0;
        public int durationMS = 0;

        public LightPattern(int argb, int startOffMS, int durationMS) {
            this.argb = argb;
            this.startOffMS = startOffMS;
            this.durationMS = durationMS;
        }
    }

}
