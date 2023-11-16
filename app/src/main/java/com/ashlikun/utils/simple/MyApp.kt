package com.ashlikun.utils.simple

import android.app.Application
import android.content.Context
import android.os.Looper
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.coroutines.DefaultDispatcher
import com.ashlikun.utils.other.coroutines.DefaultScope
import com.ashlikun.utils.other.coroutines.defaultCoroutineExceptionHandler
import com.ashlikun.utils.other.coroutines.globalDefaultScope

/**
 * 作者　　: 李坤
 * 创建时间: 2022/3/27　13:43
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class MyApp : Application() {
    override fun attachBaseContext(base: Context?) {
        AppUtils.attachBaseContext(base)
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.e("aaa$globalDefaultScope")
        LogUtils.e("bbb$defaultCoroutineExceptionHandler")
        DefaultScope()
        AppUtils.init(this, "utilsSimple")
        AppUtils.isDebug = true
        Looper.getMainLooper().setMessageLogging {
//            LogUtils.d(" 主线程消息执行调试,注意频繁调用   ${it}")
        }
    }
}