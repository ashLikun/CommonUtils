package com.ashlikun.utils.ui

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.IntentUtils
import java.util.*
import com.ashlikun.utils.ui.notify.NotifyActivity

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.15 15:39
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：通知栏工具
 */
object NotificationUtil {
    private var LedID = 0

    /**
     * 是否开启通知
     */
    fun isNotificationEnabled() = try {
        NotificationManagerCompat.from(AppUtils.app).areNotificationsEnabled()
    } catch (e: Exception) {
        false
    }

    /**
     * 跳转到推送设置
     */
    fun gotoSet(): Unit {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", AppUtils.packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", AppUtils.packageName)
                intent.putExtra("app_uid", AppUtils.app.applicationInfo.uid)
            }
            else -> {
                // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", AppUtils.app.packageName, null)
            }
        }
        IntentUtils.jump(intent)

    }

    /**
     * @param activityClass 点击通知后进入的Activity 请使用[NotifyActivity]
     * @param bundle        intent参数
     * @param id            通知id
     * @param icon          图标
     * @param title         标题
     * @param msg           主题消息
     * @param autoCancel    是否自动取消，点击的时候
     */
    fun notification(
        activityClass: String,
        bundle: Bundle,
        id: Int,
        icon: Int,
        largeIcon: Bitmap? = null,
        title: String,
        msg: String,
        autoCancel: Boolean = true,
        defaults: Int = Notification.DEFAULT_ALL
    ): NotificationCompat.Builder {
        // 设置通知的事件消息
        val intent = Intent()
        intent.setPackage(AppUtils.app.packageName)
        intent.putExtras(bundle)
        intent.component = ComponentName(AppUtils.app.packageName, activityClass)
        return notification(id, icon, title, msg, largeIcon, autoCancel, intent, defaults)
    }

    /**
     * @param intent         点击通知后进入的Activity,可以为null 请使用[NotifyActivity]
     * @param notificationId 通知id
     * @param icon           图标
     * @param title          标题
     * @param msg            主题消息
     * @param autoCancel     是否自动取消，点击的时候
     */
    fun notification(
        notificationId: Int,
        icon: Int,
        title: String,
        msg: String,
        largeIcon: Bitmap? = null,
        autoCancel: Boolean = true,
        intent: Intent? = null,
        defaults: Int = Notification.DEFAULT_ALL
    ): NotificationCompat.Builder {

        return show(
            notificationId,
            createBuilder(icon, title, msg, largeIcon, autoCancel, intent, defaults = defaults)
        )
    }

    /**
     * 创建一个 NotificationCompat.Builder
     *
     * @param intent     点击通知后进入的Activity,可以为null  请使用[NotifyActivity]
     * @param pendingIntent     点击通知后进入的Activity,可以为null（PendingIntent）
     * @param icon       图标
     * @param title      标题
     * @param msg        主题消息
     * @param autoCancel 是否自动取消，点击的时候
     * @param channelName 渠道名称，也是id
     * @param channelGroupName 渠道组名称，也是id
     * @param lockscreenVisibility 设置锁屏可见  null:默认，true：可见，false：不可见
     * @param defaults 提示类型  声音：[NotificationCompat.DEFAULT_SOUND], 震动：[NotificationCompat.DEFAULT_VIBRATE], 顶部灯光：[NotificationCompat.DEFAULT_LIGHTS],
     * @param importance 渠道优先级  高（有声音和提示）：[NotificationManager.IMPORTANCE_HIGH]
     */
    fun createBuilder(
        icon: Int,
        title: String,
        msg: String,
        largeIcon: Bitmap? = null,
        autoCancel: Boolean = true,
        intent: Intent? = null,
        pendingIntent: PendingIntent? = null,
        channelName: String = "默认通知",
        channelGroupName: String = AppUtils.appName,
        lockscreenVisibility: Boolean? = null,
        importance: Int = NotificationManager.IMPORTANCE_HIGH,
        defaults: Int = Notification.DEFAULT_ALL
    ): NotificationCompat.Builder {
        createChannel(
            channelName = channelName,
            channelGroupName = channelGroupName,
            lockscreenVisibility = lockscreenVisibility,
            importance = importance,
            defaults = defaults
        )
        val builder = NotificationCompat.Builder(AppUtils.app, channelName)
            .setWhen(System.currentTimeMillis())//设置事件发生的时间。面板中的通知是按这个时间排序。
            //左部图标
            .setSmallIcon(icon)
            //大图标
            .setLargeIcon(largeIcon)
            //设置通知的优先级
            .setPriority(importance - 3)
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
//        builder.setOnlyAlertOnce(true)
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        } else if (intent != null) {
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.getActivity(AppUtils.app, 0, intent, PendingIntent.FLAG_MUTABLE)
            else PendingIntent.getActivity(AppUtils.app, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)
        }
        return builder
    }

    /**
     * 创建渠道
     */
    fun createChannel(
        channelName: String = "默认通知",
        channelGroupName: String = AppUtils.appName,
        lockscreenVisibility: Boolean? = null,
        importance: Int = NotificationManager.IMPORTANCE_HIGH,
        defaults: Int = Notification.DEFAULT_ALL
    ) {
        // 此处必须兼容android O设备，否则系统版本在O以上可能不展示通知栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelName,
                channelName,
                importance
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
            //设置锁屏可见
            if (lockscreenVisibility == true) {
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            } else if (lockscreenVisibility == false) {
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_SECRET
            }
            //渠道分组
            val grouping = NotificationChannelGroup(channelGroupName, channelGroupName)
            channel.group = channelGroupName
            val nm = NotificationManagerCompat.from(AppUtils.app)
            nm.createNotificationChannelGroup(grouping)
            nm.createNotificationChannel(channel)
        }
    }

    /**
     * 显示或者更新通知
     */
    fun show(
        notificationId: Int,
        builder: NotificationCompat.Builder,
        tag: String = AppUtils.appName
    ): NotificationCompat.Builder {
        val nm = NotificationManagerCompat.from(AppUtils.app)
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
     * @param tag，通知的tag
     * @param notificationId，通知的id
     */
    fun cancel(notificationId: Int, tag: String = AppUtils.appName) {
        val nm = NotificationManagerCompat.from(AppUtils.app)
        //撤销指定id通知
        nm.cancel(tag, notificationId)
    }

    /**
     * 取消全部通知
     */
    fun cancel() {
        val nm = NotificationManagerCompat.from(AppUtils.app)
        //撤销本程序发出的全部通知
        nm.cancelAll()
    }


    /********************************************************************************************
     * 下面是手机顶部小灯的通知
     */
    fun lightLed(context: Context, colorOx: Int, durationMS: Int) {
        lightLed(context, colorOx, 0, durationMS)
    }

    fun lightLed(context: Context, colorOx: Int, startOffMS: Int, durationMS: Int) {
        val nm = NotificationManagerCompat.from(AppUtils.app)
        val notification = Notification()
        notification.ledARGB = colorOx
        notification.ledOffMS = startOffMS
        notification.ledOnMS = durationMS
        notification.flags = Notification.FLAG_SHOW_LIGHTS
        LedID++
        nm.notify(LedID, notification)
        nm.cancel(LedID)
    }

    fun lightLed(
        context: Context, colorOx: Int, startOffMS: Int, durationMS: Int,
        repeat: Int
    ) {
        var repeat = repeat
        if (repeat < 1) {
            repeat = 1
        }
        val handler = Handler(Looper.getMainLooper())
        for (i in 0 until repeat) {
            handler.postDelayed(
                { lightLed(context, colorOx, startOffMS, durationMS) },
                ((startOffMS + durationMS) * i).toLong()
            )
        }
    }

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
