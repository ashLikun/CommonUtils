package com.ashlikun.utils.simple

import com.ashlikun.livedatabus.busForever
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.other.coroutines.taskLaunch
import com.ashlikun.utils.other.logge
import com.ashlikun.utils.other.loggi

/**
 * 作者　　: 李坤
 * 创建时间: 2022/4/22　14:56
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class Test2 {
    companion object {
        private val instance by lazy { Test2() }
        fun get(): Test2 = instance

    }

    var a = 10

    init {
        "${Test2::class.simpleName} init".loggi()
//        MainHandle.post {
//            LogUtils.e("aaaaaaaaaaaaaaaaaaaaa")
//        }
//        MainHandle.post(object :Runnable{
//            override fun run() {
//                LogUtils.e("aaaaaaabbbbbbbbbbbbb")
//
//                "aaa".busForever{
//                    LogUtils.e("1111111111111111")
//                }
//            }
//
//        })
        "${Test2::class.simpleName} init".loggi()
        MainHandle.post {
            LogUtils.e("aaaaaaa22222222aaaaaaaaaaaa")
            //监听连接状态
            "BLUETOOTH_CONNECT".busForever {
                LogUtils.e("aaaaaaaaaaaaaaaaaaa")
                stop()
            }
        }
    }

    @Synchronized
    private fun stop() {
        LogUtils.e("stopstopstopstop")
    }
}