package com.ashlikun.utils.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.ashlikun.utils.AppUtils
import java.util.*

/**
 * @author MaTianyu
 * @date 2014-11-19
 */

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/7 15:07
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：通知栏工具
 */

object NotificationUtil {
    private var LedID = 0

    /**
     * @param activityClass 点击通知后进入的Activity
     * @param bundle        intent参数
     * @param id            通知id
     * @param icon          图标
     * @param title         标题
     * @param msg           主题消息
     * @param autoCancel    是否自动取消，点击的时候
     */
    @JvmStatic
    fun notification(activityClass: String,
                     bundle: Bundle,
                     id: Int,
                     icon: Int,
                     title: String,
                     msg: String,
                     autoCancel: Boolean = true,
                     defaults: Int = Notification.DEFAULT_ALL): NotificationCompat.Builder {
        // 设置通知的事件消息
        val intent = Intent()
        intent.setPackage(AppUtils.getApp().packageName)
        intent.putExtras(bundle)
        intent.component = ComponentName(AppUtils.getApp().packageName, activityClass)
        return notification(id, icon, title, msg, autoCancel, intent, defaults)
    }

    /**
     * @param intent         点击通知后进入的Activity,可以为null
     * @param notificationId 通知id
     * @param icon           图标
     * @param title          标题
     * @param msg            主题消息
     * @param autoCancel     是否自动取消，点击的时候
     */
    @JvmStatic
    fun notification(
            notificationId: Int,
            icon: Int,
            title: String,
            msg: String,
            autoCancel: Boolean = true,
            intent: Intent? = null,
            defaults: Int = Notification.DEFAULT_ALL): NotificationCompat.Builder {

        return show(notificationId,
                createBuilder(icon, title, msg, autoCancel, intent, defaults))
    }

    /**
     * 创建一个 NotificationCompat.Builder
     *
     * @param intent     点击通知后进入的Activity,可以为null
     * @param icon       图标
     * @param title      标题
     * @param msg        主题消息
     * @param autoCancel 是否自动取消，点击的时候
     * @param defaults 提示类型  声音：[NotificationCompat.DEFAULT_SOUND], 震动：[NotificationCompat.DEFAULT_VIBRATE], 顶部灯光：[NotificationCompat.DEFAULT_LIGHTS],
     */
    @JvmStatic
    fun createBuilder(icon: Int,
                      title: String,
                      msg: String,
                      autoCancel: Boolean = true,
                      intent: Intent? = null,
                      defaults: Int = Notification.DEFAULT_ALL): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(AppUtils.getApp(), AppUtils.getApp().packageName)
                //左部图标
                .setSmallIcon(icon)
                //设置通知的优先级：最大
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //上部标题
                .setContentTitle(title)
                //中部通知内容
                .setContentText(msg)
                .setAutoCancel(autoCancel)
        //通知的声音震动等都随系统,也可以选择使用声音文件，setSound(uri)
        builder.setDefaults(defaults)
        if (defaults == NotificationCompat.DEFAULT_ALL || (defaults and NotificationCompat.DEFAULT_LIGHTS) != 0) {
            builder.setLights(0xffFF0000.toInt(), 3000, 3000)
        }
        builder.setOnlyAlertOnce(true)
        if (intent != null) {
            val resultPendingIntent = PendingIntent.getActivity(AppUtils.getApp(), 0, intent,
                    //允许更新
                    PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(resultPendingIntent)
        }
        // 此处必须兼容android O设备，否则系统版本在O以上可能不展示通知栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    AppUtils.getApp().packageName,
                    AppUtils.getAppName(),
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(defaults == NotificationCompat.DEFAULT_ALL || (defaults and NotificationCompat.DEFAULT_VIBRATE) != 0)
            if (defaults == NotificationCompat.DEFAULT_ALL || (defaults and NotificationCompat.DEFAULT_LIGHTS) != 0) {
                channel.lightColor = 0xffFF0000.toInt()
                channel.enableLights(true)
            } else {
                channel.enableLights(false)
            }
            if (defaults != NotificationCompat.DEFAULT_ALL && (defaults and NotificationCompat.DEFAULT_SOUND) == 0) {
                channel.setSound(null, null)
            }
            (AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
        return builder
    }

    /**
     * 显示或者更新通知
     */
    @JvmStatic
    fun show(notificationId: Int, builder: NotificationCompat.Builder, tag: String = AppUtils.getAppName()): NotificationCompat.Builder {
        val nm = AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //用消息的id对应的hashCode作为通知id
        //如果没有就创建，如果有就更新，
        //第一个参数是设置创建通知的id或者需要更新通知的id
        //发出状态栏通知
        var n = builder.build()
        nm.notify(tag, notificationId, n)
        return builder
    }

    /**
     * 取消通知
     *
     * @param notificationId，通知的id
     */
    @JvmStatic
    fun cancel(notificationId: Int) {
        val nm = AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //撤销指定id通知
        nm.cancel(notificationId)
    }

    /**
     * 取消通知
     *
     * @param tag，通知的tag
     * @param notificationId，通知的id
     */
    @JvmStatic
    fun cancel(tag: String, notificationId: Int) {
        val nm = AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //撤销指定id通知
        nm.cancel(tag, notificationId)
    }

    /**
     * 取消全部通知
     */
    @JvmStatic
    fun cancel() {
        val nm = AppUtils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //撤销本程序发出的全部通知
        nm.cancelAll()
    }


    /********************************************************************************************
     * 下面是手机顶部小灯的通知
     */
    @JvmStatic
    fun lightLed(context: Context, colorOx: Int, durationMS: Int) {
        lightLed(context, colorOx, 0, durationMS)
    }

    @JvmStatic
    fun lightLed(context: Context, colorOx: Int, startOffMS: Int, durationMS: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification()
        notification.ledARGB = colorOx
        notification.ledOffMS = startOffMS
        notification.ledOnMS = durationMS
        notification.flags = Notification.FLAG_SHOW_LIGHTS
        LedID++
        nm.notify(LedID, notification)
        nm.cancel(LedID)
    }

    @JvmStatic
    fun lightLed(context: Context, colorOx: Int, startOffMS: Int, durationMS: Int,
                 repeat: Int) {
        var repeat = repeat
        if (repeat < 1) {
            repeat = 1
        }
        val handler = Handler(Looper.getMainLooper())
        for (i in 0 until repeat) {
            handler.postDelayed({ lightLed(context, colorOx, startOffMS, durationMS) }, ((startOffMS + durationMS) * i).toLong())
        }
    }

    @JvmStatic
    fun lightLed(context: Context, patterns: ArrayList<LightPattern>?) {
        if (patterns == null) {
            return
        }
        for (lp in patterns) {
            lightLed(context, lp.argb, lp.startOffMS, lp.durationMS)
        }
    }

    class LightPattern(argb: Int, startOffMS: Int, durationMS: Int) {
        var argb = 0
        var startOffMS = 0
        var durationMS = 0

        init {
            this.argb = argb
            this.startOffMS = startOffMS
            this.durationMS = durationMS
        }
    }

}