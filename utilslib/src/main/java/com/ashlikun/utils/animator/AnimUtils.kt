package com.ashlikun.utils.animator

import android.animation.*
import android.view.View
import com.ashlikun.utils.animator.AnimUtils
import com.ashlikun.utils.other.DimensUtils
import kotlin.jvm.JvmOverloads
import android.widget.TextView
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.util.TypedValue
import androidx.annotation.ColorInt

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 14:28
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：动画的工具合集
 */

object AnimUtils {
    /**
     * 方法功能：上下抖动动画，用于提醒用户去点击
     * @param scaleMax:缩放的最大值 0.85
     * @param rotation:转动角度   6
     */
    fun startShakeUp(view: View, scaleMax: Float = 0.85f, rotation: Float = 6f) =
        getShakeUp(view, scaleMax, rotation).apply { start() }

    /**
     * 方法功能：上下抖动动画，用于提醒用户去点击
     *
     * @param scaleMax:缩放的最大值 0.85
     * @param rotation:转动角度   6
     */
    fun getShakeUp(view: View, scaleMax: Float = 0.85f, rotation: Float = 6f): ObjectAnimator {
        // Keyframe是一个时间/值对，用于定义在某个时刻动画的状态
        // 在不同时间段的X轴0.8-1.1的缩放
        val pvhScaleX = PropertyValuesHolder.ofKeyframe(
            "scaleX", Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.3f, scaleMax),
            Keyframe.ofFloat(0.4f, scaleMax),
            Keyframe.ofFloat(0.5f, scaleMax),
            Keyframe.ofFloat(0.6f, scaleMax),
            Keyframe.ofFloat(0.7f, scaleMax),
            Keyframe.ofFloat(0.8f, scaleMax),
            Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f)
        )
        // 在不同时间段的Y轴0.8-1.1的缩放
        val pvhScaleY = PropertyValuesHolder.ofKeyframe(
            "scaleY", Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.3f, scaleMax),
            Keyframe.ofFloat(0.4f, scaleMax),
            Keyframe.ofFloat(0.5f, scaleMax),
            Keyframe.ofFloat(0.6f, scaleMax),
            Keyframe.ofFloat(0.7f, scaleMax),
            Keyframe.ofFloat(0.8f, scaleMax),
            Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f)
        )

        // 在不同时间段的旋转 旋转角度 = 0.3*抖动系数
        val pvhRotation = PropertyValuesHolder.ofKeyframe(
            "rotation", Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(0.1f, rotation),
            Keyframe.ofFloat(0.2f, -rotation),
            Keyframe.ofFloat(0.3f, rotation),
            Keyframe.ofFloat(0.4f, -rotation),
            Keyframe.ofFloat(0.5f, rotation),
            Keyframe.ofFloat(0.6f, -rotation),
            Keyframe.ofFloat(0.7f, rotation),
            Keyframe.ofFloat(0.8f, -rotation),
            Keyframe.ofFloat(0.9f, rotation), Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(
            view, pvhScaleX, pvhScaleY, pvhRotation
        ).setDuration(1000)
    }

    /**
     * 左右抖动动画，用于表单验证失败
     *
     * @param scaleMax:缩放最大    0.85
     * @param translation:平移距离 6
     */
    fun startShakeLeft(view: View, scaleMax: Float = 0.85f, translation: Float = 6f) =
        getShakeLeft(view, scaleMax, translation).apply { start() }

    /**
     * 左右抖动动画，用于表单验证失败
     *
     * @param scaleMax:缩放最大    0.85
     * @param translation:平移距离 6
     */
    fun getShakeLeft(view: View, scaleMax: Float = 0.85f, translation: Float = 6f): ObjectAnimator {
        var translation = translation
        translation = DimensUtils.dip2px(view.context, translation).toFloat()
        // Keyframe是一个时间/值对，用于定义在某个时刻动画的状态
        // 在不同时间段的X轴0.8-1.1的缩放
        val pvhScaleX = PropertyValuesHolder.ofKeyframe(
            "scaleX", Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.3f, scaleMax),
            Keyframe.ofFloat(0.4f, scaleMax),
            Keyframe.ofFloat(0.5f, scaleMax),
            Keyframe.ofFloat(0.6f, scaleMax),
            Keyframe.ofFloat(0.7f, scaleMax),
            Keyframe.ofFloat(0.8f, scaleMax),
            Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f)
        )
        // 在不同时间段的Y轴0.8-1.1的缩放
        val pvhScaleY = PropertyValuesHolder.ofKeyframe(
            "scaleY", Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
            Keyframe.ofFloat(0.3f, scaleMax),
            Keyframe.ofFloat(0.4f, scaleMax),
            Keyframe.ofFloat(0.5f, scaleMax),
            Keyframe.ofFloat(0.6f, scaleMax),
            Keyframe.ofFloat(0.7f, scaleMax),
            Keyframe.ofFloat(0.8f, scaleMax),
            Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f)
        )

        // 在不同时间
        val pvhRotation = PropertyValuesHolder.ofKeyframe(
            "translationX", Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(0.1f, translation),
            Keyframe.ofFloat(0.2f, -translation),
            Keyframe.ofFloat(0.3f, translation),
            Keyframe.ofFloat(0.4f, -translation),
            Keyframe.ofFloat(0.5f, translation),
            Keyframe.ofFloat(0.6f, -translation),
            Keyframe.ofFloat(0.7f, translation),
            Keyframe.ofFloat(0.8f, -translation),
            Keyframe.ofFloat(0.9f, translation), Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(
            view, pvhScaleX, pvhScaleY, pvhRotation
        ).setDuration(1000)
    }

    /**
     * 缩放动画，先放大，后缩小，   用于提醒用户状态的变化
     *
     * @param scaleMax 缩放最大值
     * @param scaleMin 缩放最小值
     * @param duration 时间
     */
    fun startScaleAnim(
        view: View,
        scaleMax: Float = 1.3f,
        scaleMin: Float = 0.5f,
        duration: Int = 500
    ) = getScaleAnim(view, scaleMax, scaleMin, duration).apply { start() }

    /**
     * 缩放动画，先放大，后缩小，   用于提醒用户状态的变化
     *
     * @param scaleMax 缩放最大值
     * @param scaleMin 缩放最小值
     * @param duration 时间
     */
    fun getScaleAnim(
        view: View,
        scaleMax: Float = 1.3f,
        scaleMin: Float = 0.5f,
        duration: Int = 500
    ): ObjectAnimator {
        val scx =
            PropertyValuesHolder.ofFloat("scaleX", 1f, scaleMax, 1f, scaleMin, 1f)
        val scY =
            PropertyValuesHolder.ofFloat("scaleY", 1f, scaleMax, 1f, scaleMin, 1f)
        return ObjectAnimator.ofPropertyValuesHolder(
            view, scx, scY
        ).setDuration(duration.toLong())
    }

    /**
     * Y轴翻转
     */
    fun startRotationAnim(
        view: View,
        start: Float = 180f,
        end: Float = 360f,
        duration: Int = 500
    ) = getRotationAnim(view, start, end, duration).apply { start() }

    /**
     * Y轴翻转
     */
    fun getRotationAnim(
        view: View,
        start: Float = 180f,
        end: Float = 360f,
        duration: Int = 500
    ): ObjectAnimator {
        val scx = PropertyValuesHolder.ofFloat("rotationY", start, end)
        return ObjectAnimator.ofPropertyValuesHolder(
            view, scx
        ).setDuration(duration.toLong())
    }

    /**
     * 改变textVIew 的字体大小动画
     * 不会start
     */
    fun updateTextSize(textView: TextView, fromSize: Float, toSize: Float) =
        valueAnimator(fromSize, toSize) { anim, value ->
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }

    /**
     * 更新view的透明度
     * 不会start
     */
    fun updateAlpha(view: View, fromValue: Float, toValue: Float) =
        valueAnimator(fromValue, toValue) { anim, value ->
            view.alpha = value
        }

    /**
     * 更新textView的字体颜色
     * 不会start
     */
    fun updateTextColor(
        textView: TextView,
        @ColorInt fromColor: Int,
        @ColorInt toColor: Int
    ) = valueAnimator(fromColor, toColor, evaluator = ArgbEvaluator()) { anim, value ->
        textView.setTextColor(value)
    }


    /**
     * 更新背景色
     * 不会start
     */
    fun updateViewBackgroundColor(
        view: View,
        @ColorInt fromColor: Int,
        @ColorInt toColor: Int
    ) = valueAnimator(fromColor, toColor, evaluator = ArgbEvaluator()) { anim, value ->
        view.setBackgroundColor(value)
    }

    /**
     * 更新背景色
     * 不会start
     */
    fun <T> valueAnimator(

        fromValue: T,
        toValue: T,
        duration: Long = 500,
        evaluator: TypeEvaluator<*>? = null,
        call: (animator: ValueAnimator, value: T) -> Unit
    ) = ValueAnimator.ofObject(evaluator, fromValue, toValue).apply {
        this.duration = duration
        addUpdateListener {
            call(it, it.animatedValue as T)
        }
    }
}