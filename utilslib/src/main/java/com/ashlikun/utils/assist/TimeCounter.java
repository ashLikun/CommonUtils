package com.ashlikun.utils.assist;


import com.ashlikun.utils.other.LogUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/6 0006  15:06
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：统计计时
 */

public class TimeCounter {
    private long t;

    public TimeCounter() {
        start();
    }

    /**
     * Count start.
     */
    public long start() {
        t = System.currentTimeMillis();
        return t;
    }

    /**
     * Get duration and restart.
     */
    public long durationRestart() {
        long now = System.currentTimeMillis();
        long d = now - t;
        t = now;
        return d;
    }

    /**
     * Get duration.
     */
    public long duration() {
        return System.currentTimeMillis() - t;
    }

    /**
     * Print duration.
     */
    public void printDuration(String tag) {
        LogUtils.i(tag + " :  " + duration());
    }

    /**
     * Print duration.
     */
    public void printDurationRestart(String tag) {
        LogUtils.i(tag + " :  " + durationRestart());
    }
}