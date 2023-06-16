/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ashlikun.utils.ui.gesture


import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.ashlikun.utils.ui.gesture.FlingTouchHelp
import com.ashlikun.utils.ui.gesture.getFocusXY
import kotlin.math.abs

/**
 * @author　　: 李坤
 * 创建时间: 2023/4/21 8:39
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：封装手势处理
 */

open class CustomGestureDetector(
    private val view: View,
    //是否处理滑动方向，0：不处理,各个方向都可以，1：水平，2：垂直，
    val scrollDirection: Int = 0,
    //是否处理Fling 手势，会调用onScroll方法
    val isFling: Boolean = true,
    var listener: OnGestureListener
) {
    val context = view.context
    var flingTouchHelp: FlingTouchHelp? = null
        private set
    val gestureDetector: GestureDetector
    private var isScroll = false
    private var startX = 0f
    private var startY = 0f
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop / 2


    init {
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                if (!isScroll) return false
                return listener.onScroll(distanceX, distanceY, false)
            }

            override fun onDown(e: MotionEvent): Boolean {
                return listener.onDown(e)
            }

            override fun onLongPress(e: MotionEvent) {
                listener.onLongPress(e)
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (!isScroll) return false
                if (isFling) {
                    flingTouchHelp = FlingTouchHelp(view, onFlingEnd = {
                        flingTouchHelp = null
                        listener.onFlingEnd()
                    }) { dx, dy ->
                        listener.onScroll(dx, dy, true)
                    }
                    flingTouchHelp?.fling((-velocityX).toInt(), (-velocityY).toInt())
                }
                return listener.onFling(e1, e2, -velocityX, -velocityY)
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                listener.onSingleClick(e)
                return false
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                return listener.onDoubleTap(e)
            }

            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                return false
            }
        })
    }

    open fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            if (scrollDirection == 1 || scrollDirection == 2) {
                val focrsXY = ev.getFocusXY()
                val focusX = focrsXY.first
                val focusY = focrsXY.second
                when (ev.actionMasked) {
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        // 记录手指按下的位置
                        startX = focusX
                        startY = focusY
                    }
                    MotionEvent.ACTION_POINTER_UP -> {
                        // 记录手指按下的位置
                        startX = focusX
                        startY = focusY
                    }
                    MotionEvent.ACTION_DOWN -> {
                        // 记录手指按下的位置
                        startX = focusX
                        startY = focusY
                        isScroll = false
                        view.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        // 获取当前手指位置
                        val endY: Float = ev.y
                        val endX: Float = ev.x
                        val distanceX: Float = abs(endX - startX)
                        val distanceY: Float = abs(endY - startY)
                        if (!isScroll) {
                            if (scrollDirection == 1) {
                                if (distanceX > touchSlop && distanceX > distanceY) {
                                    //水平事件
                                    view.parent.requestDisallowInterceptTouchEvent(true)
                                    isScroll = true
                                } else if (distanceY > touchSlop && distanceY > distanceX) {
                                    view.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            } else if (scrollDirection == 2) {
                                if (distanceY > touchSlop && distanceY > distanceX) {
                                    //垂直事件
                                    view.parent.requestDisallowInterceptTouchEvent(true)
                                    isScroll = true
                                } else if (distanceX > touchSlop && distanceX > distanceY) {
                                    view.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                        }
                    }
                }
            } else {
                isScroll = true
            }
            //再处理普通的手势
            val result = gestureDetector.onTouchEvent(ev)
            if (scrollDirection == 0) {
                //请求父不要拦截事件
                view.parent.requestDisallowInterceptTouchEvent(true)
            }
            return result
        } catch (e: IllegalArgumentException) {
            false
        }
    }


    /**
     * 是否正在惯性中
     */
    open fun isFlingStart() = flingTouchHelp?.isFling() == true

    interface OnGestureListener {
        /**
         * 移动事件,自带惯性处理
         */
        fun onScroll(distanceX: Float, distanceY: Float, isFling: Boolean): Boolean

        /**
         * 按下事件
         */
        fun onDown(e: MotionEvent): Boolean

        /**
         * 长按事件
         */
        fun onLongPress(e: MotionEvent)

        /**
         * 惯性事件结束，前提开启惯性处理
         */
        fun onFlingEnd()

        /**
         * 惯性事件
         */
        fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean

        /**
         * 双击事件
         */
        fun onDoubleTap(e: MotionEvent): Boolean

        /**
         * 单击事件
         */
        fun onSingleClick(e: MotionEvent)
    }

    open abstract class AdapterOnGestureListener : OnGestureListener {
        override fun onScroll(distanceX: Float, distanceY: Float, isFling: Boolean) = false
        override fun onDown(e: MotionEvent) = false
        override fun onLongPress(e: MotionEvent) {
        }

        override fun onFlingEnd() {

        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float) = false

        override fun onDoubleTap(e: MotionEvent) = false

        override fun onSingleClick(e: MotionEvent) {
        }

    }
}