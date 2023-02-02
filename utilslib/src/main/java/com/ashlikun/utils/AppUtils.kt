package com.ashlikun.utils

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.content.FileProvider
import com.ashlikun.utils.bug.BugUtils
import com.ashlikun.utils.provider.BaseContentProvider
import com.ashlikun.utils.ui.ActivityManager
import com.ashlikun.utils.ui.fActivity
import com.ashlikun.utils.ui.fCActivity
import com.ashlikun.utils.ui.resources.ResUtils
import java.io.File

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.15 15:54
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：App 工具类
 */
/**
 * 作者　　: 李坤
 * 创建时间: 2022/10/13　10:35
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：自定义view预览的问题
 */


object AppUtils {
    var isDebug = false
    lateinit var app: Application
        private set
    lateinit var base: Context
        private set

    /**
     * 编辑模式的Context
     */
    var editModeContext: Context? = null
    fun View.initAppEditMode() {
        if (isInEditMode) {
            AppUtils.base = context
            AppUtils.editModeContext = context
            AppUtils.defaultContextCall = {
                context
            }
        }
    }

    /**
     * 默认的Context
     * 为了适配使用Activity的,不是一股脑调用Application的
     * 当后台调用的时候可能不是调用者的Activity
     * 注意内存泄露
     */
    var defaultContextCall: () -> Context = {
//        (ActivityManager.foregroundActivity ?: app)
        app
    }

    fun attachBaseContext(context: Context?) {
        if (context != null) base = context
    }

    /**
     * 一定要在Application里面调用
     * 在全部库的最前面调用
     */
    fun init(myApp: Application, isDebug: Boolean = false) {
        this.isDebug = isDebug
        app = myApp
        base = myApp
        myApp.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        //解决一些疑难杂症
        BugUtils.init()
        //监听FPS
        if (isDebug) {
            FPSFrameCallback.init()
        }
    }

    /**
     * 取版本code
     */
    val versionCode by lazy {
        packageInfo.versionCode
    }

    /**
     * 版本名称
     */
    val versionName by lazy {
        packageInfo.versionName.orEmpty()
    }

    /**
     * 包名
     */
    val packageName by lazy {
        app.packageName.orEmpty()
    }

    var activityLifecycleCallbacks = ApplicationListener()

    /**
     * 前后台监听
     */
    fun addAppRunChang(listener: OnChangForeground) {
        activityLifecycleCallbacks.addOnChangListener(listener)
    }

    /**
     * 前后台监听
     */
    fun removeAppRunChang(listener: OnChangForeground) {
        activityLifecycleCallbacks.removeOnChangListener(listener)
    }

    /**
     * 兼容高版本的fileprovider
     */
    fun getUri(file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val authority = app.packageName + ".Provider"
            FileProvider.getUriForFile(app, authority, file)
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * 获取应用程序名称
     */
    val appName: String
        get() {
            val labelRes = packageInfo?.applicationInfo?.labelRes ?: 0
            return ResUtils.getString(labelRes)
        }

    /**
     * 获取App包 信息版本号
     */
    val packageInfo: PackageInfo
        get() = app.packageManager.getPackageInfo(app.packageName, 0)

    /**
     * 获取其他App包 信息版本号
     */
    fun getPackageInfo(packageName: String = app.packageName, flag: Int = 0) = try {
        app.packageManager.getPackageInfo(packageName, flag)
    } catch (e: Exception) {
        null
    }


    /**
     * Appication 的 resources
     */
    val appResources: Resources
        get() = runCatching {
            app.resources
        }.getOrNull() ?: editModeContext?.resources!!

    val defaultContext: Context
        get() = defaultContextCall()
    val defaultResources: Resources
        get() = defaultContext.resources

    /**
     * 栈顶的Activity
     */
    val fContext: Context
        get() = runCatching {
            fActivity ?: app
        }.getOrNull() ?: editModeContext!!

}