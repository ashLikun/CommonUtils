package com.ashlikun.utils.main

import android.app.ActivityManager
import android.content.Context
import com.ashlikun.utils.AppUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 15:13
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：服务工具
 */

object ServiceUtils {
    /**
     * 服务是否启动了
     */
    fun isServiceStart(serviceName: Class<*>): Boolean {
        val localIterator = (AppUtils.app
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
            Int.MAX_VALUE
        ).iterator()
        while (localIterator.hasNext()) {
            val runningService = localIterator.next() as ActivityManager.RunningServiceInfo
            if (runningService.service.className
                == serviceName.name
            ) {
                return true
            }
        }
        return true
    }
}