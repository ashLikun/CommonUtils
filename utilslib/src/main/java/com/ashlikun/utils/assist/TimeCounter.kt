package com.ashlikun.utils.assist

import com.ashlikun.utils.other.LogUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:19
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：统计计时
 */
class TimeCounter {
    private var t: Long = 0

    /**
     * Count start.
     */
    fun start(): Long {
        t = System.currentTimeMillis()
        return t
    }

    /**
     * Get duration and restart.
     */
    fun durationRestart(): Long {
        val now = System.currentTimeMillis()
        val d = now - t
        t = now
        return d
    }

    /**
     * Get duration.
     */
    fun duration(): Long {
        return System.currentTimeMillis() - t
    }

    /**
     * Print duration.
     */
    fun printDuration(tag: String) {
        LogUtils.i(tag + " :  " + duration())
    }

    /**
     * Print duration.
     */
    fun printDurationRestart(tag: String) {
        LogUtils.i(tag + " :  " + durationRestart())
    }

    init {
        start()
    }
}