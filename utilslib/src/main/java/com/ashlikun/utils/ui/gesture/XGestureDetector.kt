package com.ashlikun.utils.ui.gesture

import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.ashlikun.utils.other.LogUtils
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 2023/4/21　8:40
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：自己实现手势
 * 官方使用的是getX,当view触摸并且移动的时候，坐标会乱，导致抖动
 * 我这里使用getRawX
 */


open class XGestureDetector(
    val view: View,
    //是否使用MotionEvent.getX or getY ，false:使用getRawX(相对于屏幕，当移动View的时候不会抖动)
    val isUseGetRawX: Boolean = false,
    //是否处理滑动方向，0：不处理,各个方向都可以，1：水平，2：垂直，
    val scrollDirection: Int = 0,
    //是否使用惯性手势
    val isUseFling: Boolean = true,
    //手指抬起的时候是否自动计算速度回调onScroll
    val isUpToAutoFling: Boolean = true,
    val onScrollEnd: (() -> Unit)? = null,
    //当手指抬起的惯性
    val onFling: ((velocityX: Float, velocityY: Float) -> Boolean)? = null,
    val onDown: ((e: MotionEvent) -> Boolean)? = null,
    val onSingleTap: ((e: MotionEvent) -> Unit)? = null,
    //这个回调值是差值
    val onScroll: ((distanceX: Float, distanceY: Float, isFling: Boolean) -> Boolean)? = null
) {
    val touchSlopSquare: Int
    val touchSlop: Int

    var lastFocusX = 0f
        private set
    var lastFocusY = 0f
        private set
    var downFocusX = 0f
        private set
    var downFocusY = 0f
        private set

    private var isScroll = false
    private var isSingleTap = false

    //触摸的x，y焦点
    var focrsXY: Pair<Float, Float>? = null
        private set
    var flingTouchHelp: FlingTouchHelp? = null
        private set
    private var onFlingIsCallEnd = false

    init {
        ViewConfiguration.get(view.context).also {
            touchSlop = it.scaledTouchSlop
            touchSlopSquare = touchSlop * touchSlop
        }
    }

    /**
     * 是否正在惯性中
     */
    open fun isFlingStart() = flingTouchHelp?.isFling() == true

    open fun onTouchEvent(ev: MotionEvent): Boolean {
        focrsXY = ev.getFocusXY(isUseGetRawX)
        val focusX = focrsXY!!.first
        val focusY = focrsXY!!.second
        var handled = false
        var flingTouchHandled = false

        if (isUseFling) {
            if (flingTouchHelp == null) {
                flingTouchHelp = FlingTouchHelp(view, isUpToAutoFling, onFlingEnd = {
                    if (isScroll) {
                        onFlingIsCallEnd = true
                        onScrollEnd?.invoke()
                    }
                }, onFling) { distanceX, distanceY ->
                    onScroll?.invoke(distanceX, distanceY, true)
                }
            }
            flingTouchHandled = flingTouchHelp!!.onTouchEvent(ev)
        }
        when (ev.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                // 记录手指按下的位置
                downFocusX = focusX.also { lastFocusX = it }
                downFocusY = focusY.also { lastFocusY = it }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                // 记录手指按下的位置
                downFocusX = focusX.also { lastFocusX = it }
                downFocusY = focusY.also { lastFocusY = it }
            }
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置
                downFocusX = focusX.also { lastFocusX = it }
                downFocusY = focusY.also { lastFocusY = it }
                isScroll = false
                isSingleTap = false
                onFlingIsCallEnd = false
                view.parent.requestDisallowInterceptTouchEvent(true)
                handled = onDown?.invoke(ev) ?: false
            }
            MotionEvent.ACTION_MOVE -> {
                val scrollX = lastFocusX - focusX
                val scrollY = lastFocusY - focusY

                if (!isScroll) {
                    val deltaX = (focusX - downFocusX).toInt()
                    val deltaY = (focusY - downFocusY).toInt()
                    val distance = deltaX * deltaX + deltaY * deltaY
                    //是移动的记录
                    if (scrollDirection == 1) {
                        if (abs(deltaX) > touchSlop && abs(deltaX) > abs(deltaY)) {
                            //水平移动
                            view.parent.requestDisallowInterceptTouchEvent(true)
                            isScroll = true
                        } else if (abs(deltaY) > touchSlop && abs(deltaY) > abs(deltaX)) {
                            //方向不对，释放
                            view.parent.requestDisallowInterceptTouchEvent(false)
                        }

                    } else if (scrollDirection == 2) {
                        if (abs(deltaY) > touchSlop && abs(deltaY) > abs(deltaX)) {
                            //垂直移动
                            view.parent.requestDisallowInterceptTouchEvent(true)
                            isScroll = true
                        } else if (abs(deltaX) > touchSlop && abs(deltaX) > abs(deltaY)) {
                            //方向不对，释放
                            view.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    } else if (scrollDirection == 0 && distance > touchSlopSquare) {
                        //任意方向
                        view.parent.requestDisallowInterceptTouchEvent(true)
                        isScroll = true
                    } else {
                        //这里不能设置false,不然外层会把事件拿去
//                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    if (isScroll) {
                        isSingleTap = false
                        handled = onScroll?.invoke(scrollX, scrollY, false) ?: false
                        lastFocusX = focusX
                        lastFocusY = focusY
                    } else {
                        isSingleTap = distance <= touchSlopSquare
                        handled = false
                    }
                } else {
                    //后续直接就是滚动
                    isSingleTap = false
                    if (abs(scrollX) >= 1 || abs(scrollY) >= 1) {
                        handled = onScroll?.invoke(scrollX, scrollY, false) ?: false
                        lastFocusX = focusX
                        lastFocusY = focusY
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isSingleTap) {
                    onSingleTap?.invoke(ev)
                }
                if (isScroll && !isFlingStart() && !onFlingIsCallEnd) {
                    onFlingIsCallEnd = true
                    onScrollEnd?.invoke()
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                cancel()
            }
        }
        return handled || flingTouchHandled
    }

    open fun cancel() {
        isScroll = false
        isSingleTap = false
    }
}

/**
 * 获取触摸焦点
 * @param isUseGetRawX 是否使用MotionEvent.getX or getY ，false:使用getRawX(相对于屏幕，当移动View的时候不会抖动)
 * getRawX 10.0 Q 才能使用
 */
inline fun MotionEvent.getFocusXY(isUseGetRawX: Boolean = false): Pair<Float, Float> {
    if (isUseGetRawX && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        return rawX to rawY
    }
    val pointerUp = action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_POINTER_UP
    val skipIndex = if (pointerUp) actionIndex else -1
    // Determine focal point
    var sumX = 0f
    var sumY = 0f
    val count = pointerCount
    for (i in 0 until count) {
        if (skipIndex == i) continue
        if (!isUseGetRawX) {
            sumX += getX(i)
            sumY += getY(i)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                sumX += getRawX(i)
                sumY += getRawY(i)
            } else {
                sumX += rawX
                sumY += rawY
            }
        }

    }
    val div = if (pointerUp) count - 1 else count
    val focusX = sumX / div
    val focusY = sumY / div
    return focusX to focusY
}