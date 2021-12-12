package com.ashlikun.utils.assist

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:19
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：时间均值计算器,只能用于单线程计时。
 */
class TimeAverager {
    /**
     * 计时器
     */
    private val tc = TimeCounter()

    /**
     * 均值器
     */
    private val av = Averager()

    /**
     * 一个计时开始
     */
    fun start(): Long {
        return tc.start()
    }

    /**
     * 一个计时结束
     */
    fun end(): Long {
        val time = tc.duration()
        av.add(time)
        return time
    }

    /**
     * 一个计时结束,并且启动下次计时。
     */
    fun endAndRestart(): Long {
        val time = tc.durationRestart()
        av.add(time)
        return time
    }

    /**
     * 求全部计时均值
     */
    fun average(): Number {
        return av.average
    }

    /**
     * 打印全部时间值
     */
    fun print() {
        av.print()
    }

    /**
     * 清楚数据
     */
    fun clear() {
        av.clear()
    }
}