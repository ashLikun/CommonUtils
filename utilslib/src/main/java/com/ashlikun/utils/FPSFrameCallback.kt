package com.ashlikun.utils

import android.view.Choreographer
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.ui.extend.getFps
import com.ashlikun.utils.ui.extend.getMaxFps
import com.ashlikun.utils.ui.fActivity

/**
 * 作者　　: 李坤
 * 创建时间: 2023/2/2　13:29
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：用于监听FPS
 */
class FPSFrameCallback : Choreographer.FrameCallback {
    companion object {
        //丢帧多少打印日志
        var LOG_MAX = 10

        //回调出去
        var call: ((skippedFrames: Int) -> Unit)? = null

        fun init() {
            Choreographer.getInstance().postFrameCallback(FPSFrameCallback())
        }
    }

    private val TAG = "FPS_TEST"
    private var mLastFrameTimeNanos = 0L
    private var mFrameIntervalNanos = 1000000000 / 60.0
    private var isInitFps = false

    init {
        initFps()
    }

    private fun initFps() {
        if (isInitFps) return
        fActivity?.also {
            isInitFps = true
            mFrameIntervalNanos = 1000000000 / (it.window.getFps()?.toDouble() ?: 60.0)
        }
    }

    override fun doFrame(frameTimeNanos: Long) {
        initFps()
        //初始化时间
        if (mLastFrameTimeNanos == 0L) {
            mLastFrameTimeNanos = frameTimeNanos
        }
        val jitterNanos = frameTimeNanos - mLastFrameTimeNanos
        if (jitterNanos >= mFrameIntervalNanos) {
            val skippedFrames = (jitterNanos / mFrameIntervalNanos).toInt()
            if (skippedFrames > LOG_MAX) {
                call?.invoke(skippedFrames)
                //丢帧10以上打印日志
                LogUtils.e("${TAG}:当前页面${fActivity?.javaClass?.simpleName}丢帧 skippedFrames = ${skippedFrames}")
            }
        }
        mLastFrameTimeNanos = frameTimeNanos
        //注册下一帧回调
        Choreographer.getInstance().postFrameCallback(this)
    }
}