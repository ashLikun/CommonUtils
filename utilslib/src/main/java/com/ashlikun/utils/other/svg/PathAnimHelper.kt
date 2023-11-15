package com.ashlikun.pathanim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Path
import android.graphics.PathMeasure
import android.view.animation.LinearInterpolator

/**
 * 作者　　: 李坤
 * 创建时间:2017/1/19　15:15
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：一个操作Path的工具类
 */
class PathAnimHelper(
    val sourcePath: Path, //源Path
    val animPath: Path = Path(), //用于绘制动画的Path
    val animTime: Long = 1500, //动画一共的时间
    val isInfinite: Boolean = true,//是否无限循环
    //是否处理默认
    val isDefaultHandler: Boolean = true,
    var callback: ((pathMeasure: PathMeasure?, animPath: Path?, progress: Float) -> Unit)? = null //动画监听者
) {
    private var mAnimator: ValueAnimator? = null //动画对象

    var valueOld = 0f

    /**
     * 停止动画
     */
    fun stop() {
        if (null != mAnimator && mAnimator!!.isRunning) {
            mAnimator!!.end()
        }
    }

    /**
     * 一个SourcePath 内含多段Path，循环取出每段Path，并做一个动画
     * 自定义动画的总时间
     * 和是否循环
     */
    fun start() {
        val pathMeasure = PathMeasure()
        animPath.reset()
        animPath.lineTo(0f, 0f)
        valueOld = 0f
        pathMeasure.setPath(sourcePath, true)
        loopAnim(pathMeasure)
    }

    /**
     * 循环取出每一段path ，并执行动画
     */
    protected fun loopAnim(pathMeasure: PathMeasure) {
        //动画正在运行的话，先停止动画
        stop()
        mAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
            interpolator = LinearInterpolator()
            duration = animTime
            //无限循环
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener(AnimatorUpdateListener { animation ->
                val value = animation.animatedValue as Float
                if (value < valueOld) {
                    return@AnimatorUpdateListener
                }
                valueOld = value
                if (callback != null) {
                    if (isDefaultHandler) {
                        //获取一个段落
                        pathMeasure.getSegment(0f, pathMeasure.length / 100.0f * value, animPath, true)
                    }
                    callback!!.invoke(pathMeasure, animPath, value)
                } else {
                    //获取一个段落
                    pathMeasure.getSegment(0f, pathMeasure.length / 100.0f * value, animPath, true)
                }
            })
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator) {
                    //每段path走完后，要补一下 某些情况会出现 animPath不满的情况
                    pathMeasure.getSegment(0f, pathMeasure.length, animPath, true)
                    //绘制完一条Path之后，再绘制下一条
                    pathMeasure.nextContour()
                    //长度为0 说明一次循环结束
                    if (pathMeasure.length == 0f) {
                        if (isInfinite) { //如果需要循环动画
                            animPath!!.reset()
                            animPath!!.lineTo(0f, 0f)
                            valueOld = 0f
                            pathMeasure.setPath(sourcePath, false)
                        } else { //不需要就停止（因为repeat是无限 需要手动停止）
                            stop()
                        }
                    } else {
                        valueOld = 0f
                    }
                }
            })
            start()
        }
    }


}