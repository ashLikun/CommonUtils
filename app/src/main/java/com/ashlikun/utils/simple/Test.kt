package com.ashlikun.utils.simple

import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.other.coroutines.taskLaunch

/**
 * 作者　　: 李坤
 * 创建时间: 2022/4/22　14:56
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class Test {
    companion object {
        private val instance by lazy { Test() }
        fun get(): Test = instance
        fun appRun() {
            taskLaunch {
                get().init()
            }
        }
    }

    private fun init() {
        Test2.get()
    }


}