package com.ashlikun.utils.ui.gesture

import android.view.*
import android.widget.OverScroller
import com.ashlikun.utils.other.LogUtils
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 2023/4/20　17:27
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：惯性手势处理类
 */
open class FlingTouchHelp(
    val view: View,
    //手指抬起的时候是否自动计算速度回调onScroll
    val isUpToAutoFling: Boolean = true,
    val onFlingEnd: (() -> Unit)? = null,
    //当手指抬起的惯性
    val onFling: ((velocityX: Float, velocityY: Float) -> Boolean)? = null,
    val onScroll: ((distanceX: Float, distanceY: Float) -> Unit)? = null
) :
    Runnable {
    private val scroller = OverScroller(view.context)
    private var currentX = 0
    private var currentY = 0

    //手势速度追踪
    var velocityTracker: VelocityTracker? = null
        private set
    var maximumFlingVelocity = 0
    var minimumFlingVelocity = 0

    init {
        ViewConfiguration.get(view.context).also {
            maximumFlingVelocity = it.scaledMaximumFlingVelocity
            minimumFlingVelocity = it.scaledMinimumFlingVelocity
        }
    }

    /**
     * 追踪触摸事件
     */
    open fun onTouchEvent(ev: MotionEvent): Boolean {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        var handled = false
        velocityTracker!!.addMovement(ev)
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 取消之前的Fling
                cancelFling()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                velocityTracker!!.computeCurrentVelocity(1000, maximumFlingVelocity.toFloat())
                val upIndex: Int = ev.actionIndex
                val id1: Int = ev.getPointerId(upIndex)
                val x1 = velocityTracker!!.getXVelocity(id1)
                val y1 = velocityTracker!!.getYVelocity(id1)
                for (i in 0 until ev.pointerCount) {
                    if (i == upIndex) continue
                    val id2 = ev.getPointerId(i)
                    val x = x1 * velocityTracker!!.getXVelocity(id2)
                    val y = y1 * velocityTracker!!.getYVelocity(id2)
                    val dot = x + y
                    if (dot < 0) {
                        velocityTracker!!.clear()
                        break
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                val pointerId: Int = ev.getPointerId(0)
                velocityTracker!!.computeCurrentVelocity(1000, maximumFlingVelocity.toFloat())
                val velocityY = velocityTracker!!.getYVelocity(pointerId)
                val velocityX = velocityTracker!!.getXVelocity(pointerId)
                if (abs(velocityY) > minimumFlingVelocity || abs(velocityX) > minimumFlingVelocity) {
                    handled = onFling?.invoke(velocityX, velocityY) ?: false
                    if (isUpToAutoFling) {
                        fling((-velocityX).toInt(), (-velocityY).toInt())
                    }
                }
                // 当我们调用上面的应用程序时，这可能已经被清除了。
                velocityTracker?.recycle()
                velocityTracker = null
            }
        }
        return handled
    }

    /**
     * 取消事件
     */
    open fun cancel() {
        velocityTracker?.recycle()
        velocityTracker = null
        cancelFling()
    }

    open fun cancelFling() {
        scroller.forceFinished(true)
        view.removeCallbacks(this)
    }

    open fun isFling() = !scroller.isFinished

    /**
     * 根据速度计算值
     * 应该在计算速度后使用
     */
    open fun fling(velocityX: Int, velocityY: Int) {
        currentX = 0
        currentY = 0
        scroller.fling(0, 0, velocityX, velocityY, Int.MIN_VALUE, Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, 0, 0)
        view.post(this)
    }

    override fun run() {
        if (scroller.isFinished) {
            onFlingEnd?.invoke()
            return
        }
        if (scroller.computeScrollOffset()) {
            val newX = scroller.currX
            val newY = scroller.currY
            onScroll?.invoke((newX - currentX).toFloat(), (newY - currentY).toFloat())
            currentX = newX
            currentY = newY
            view.postDelayed(this, (1000 / 60).toLong())
        } else {
            onFlingEnd?.invoke()
        }
    }


}