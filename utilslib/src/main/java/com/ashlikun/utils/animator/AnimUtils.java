package com.ashlikun.utils.animator;

import android.animation.ArgbEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.ashlikun.utils.other.DimensUtils;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/2　13:38
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class AnimUtils {
    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/27 17:25
     * <p>
     * 方法功能：上下抖动动画，用于提醒用户去点击
     *
     * @param scaleMax:缩放的最大值 0.85
     * @param rotation:转动角度   6
     */

    public static void shakeUp(View view, float scaleMax, float rotation) {
        getShakeUp(view, scaleMax, rotation).start();

    }

    public static ObjectAnimator getShakeUp(View view, float scaleMax, float rotation) {
        // Keyframe是一个时间/值对，用于定义在某个时刻动画的状态
        // 在不同时间段的X轴0.8-1.1的缩放
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
                "scaleX", Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.3f, scaleMax),
                Keyframe.ofFloat(0.4f, scaleMax),
                Keyframe.ofFloat(0.5f, scaleMax),
                Keyframe.ofFloat(0.6f, scaleMax),
                Keyframe.ofFloat(0.7f, scaleMax),
                Keyframe.ofFloat(0.8f, scaleMax),
                Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f));
        // 在不同时间段的Y轴0.8-1.1的缩放
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
                "scaleY", Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.3f, scaleMax),
                Keyframe.ofFloat(0.4f, scaleMax),
                Keyframe.ofFloat(0.5f, scaleMax),
                Keyframe.ofFloat(0.6f, scaleMax),
                Keyframe.ofFloat(0.7f, scaleMax),
                Keyframe.ofFloat(0.8f, scaleMax),
                Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f));

        // 在不同时间段的旋转 旋转角度 = 0.3*抖动系数
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
                "rotation", Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, rotation),
                Keyframe.ofFloat(0.2f, -rotation),
                Keyframe.ofFloat(0.3f, rotation),
                Keyframe.ofFloat(0.4f, -rotation),
                Keyframe.ofFloat(0.5f, rotation),
                Keyframe.ofFloat(0.6f, -rotation),
                Keyframe.ofFloat(0.7f, rotation),
                Keyframe.ofFloat(0.8f, -rotation),
                Keyframe.ofFloat(0.9f, rotation), Keyframe.ofFloat(1f, 0f));
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                view, pvhScaleX, pvhScaleY, pvhRotation).setDuration(1000);
        return objectAnimator;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/27 17:26
     * <p>
     * 方法功能：左右抖动动画，用于表单验证失败
     *
     * @param scaleMax:缩放最大    0.85
     * @param translation:平移距离 6
     */

    public static void shakeLeft(View view, float scaleMax, float translation) {
        translation = DimensUtils.dip2px(view.getContext(), translation);
        // Keyframe是一个时间/值对，用于定义在某个时刻动画的状态
        // 在不同时间段的X轴0.8-1.1的缩放
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
                "scaleX", Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.3f, scaleMax),
                Keyframe.ofFloat(0.4f, scaleMax),
                Keyframe.ofFloat(0.5f, scaleMax),
                Keyframe.ofFloat(0.6f, scaleMax),
                Keyframe.ofFloat(0.7f, scaleMax),
                Keyframe.ofFloat(0.8f, scaleMax),
                Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f));
        // 在不同时间段的Y轴0.8-1.1的缩放
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
                "scaleY", Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.3f, scaleMax),
                Keyframe.ofFloat(0.4f, scaleMax),
                Keyframe.ofFloat(0.5f, scaleMax),
                Keyframe.ofFloat(0.6f, scaleMax),
                Keyframe.ofFloat(0.7f, scaleMax),
                Keyframe.ofFloat(0.8f, scaleMax),
                Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f));

        // 在不同时间
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
                "translationX", Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, translation),
                Keyframe.ofFloat(0.2f, -translation),
                Keyframe.ofFloat(0.3f, translation),
                Keyframe.ofFloat(0.4f, -translation),
                Keyframe.ofFloat(0.5f, translation),
                Keyframe.ofFloat(0.6f, -translation),
                Keyframe.ofFloat(0.7f, translation),
                Keyframe.ofFloat(0.8f, -translation),
                Keyframe.ofFloat(0.9f, translation), Keyframe.ofFloat(1f, 0f));
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                view, pvhScaleX, pvhScaleY, pvhRotation).setDuration(1000);
        objectAnimator.start();

    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/27 17:27
     * <p>
     * 方法功能： 缩放动画，先放大，后缩小，   用于提醒用户状态的变化
     *
     * @param scaleMax 缩放最大值
     * @param scaleMin 缩放最小值
     * @param duration 时间
     */

    public static void scaleAnim(View view, float scaleMax, float scaleMin, int duration) {

        PropertyValuesHolder scx = PropertyValuesHolder.ofFloat("scaleX", 1, scaleMax, 1, scaleMin, 1);
        PropertyValuesHolder scY = PropertyValuesHolder.ofFloat("scaleY", 1, scaleMax, 1, scaleMin, 1);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                view, scx, scY).setDuration(duration);
        objectAnimator.start();


    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/27 17:27
     * <p>
     * 方法功能： 缩放动画，先放大，后缩小，   用于提醒用户状态的变化
     */
    public static void scaleAnim(View view) {
        scaleAnim(view, 1.3f, 0.5f, 800);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/2 16:33
     * <p>
     * 方法功能：Y轴翻转
     */

    public static void rotationAnim(View view, float start, float end, int duration) {

        PropertyValuesHolder scx = PropertyValuesHolder.ofFloat("rotationY", start, end);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                view, scx).setDuration(duration);
        objectAnimator.start();

    }

    public static void rotationAnim(View view) {
        rotationAnim(view, 180, 360, 800);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 10:01
     * <p>
     * 方法功能：改变textVIew 的字体大小动画
     */
    public static void updateTextSize(final TextView textView, float fromSize, float toSize) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromSize, toSize);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue);
            }
        });
        animator.start();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 10:02
     * <p>
     * 方法功能：更新view的透明度
     */

    public static void updateAlpha(final View view, float fromValue, float toValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromValue, toValue);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                view.setAlpha(animatedValue);
            }
        });
        animator.start();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 10:02
     * <p>
     * 方法功能：更新textView的字体颜色
     */

    public static void updateTextColor(final TextView textView, @ColorInt int fromColor,
                                       @ColorInt int toColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 10:03
     * <p>
     * 方法功能：更新背景色
     */

    public static void updateViewBackgroundColor(final View view, @ColorInt int fromColor,
                                                 @ColorInt int toColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }


}
