package com.ashlikun.utils.simple

import android.app.Application
import android.content.Context
import com.ashlikun.utils.AppUtils

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
        AppUtils.init(this, "utilsSimple")
        AppUtils.isDebug = true
    }
}