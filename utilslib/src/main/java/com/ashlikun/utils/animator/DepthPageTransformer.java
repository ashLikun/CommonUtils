package com.ashlikun.utils.animator;

import android.view.View;

import androidx.viewpager.widget.ViewPager;


/**
 * 作者　　: 李坤
 * 创建时间: 2017/6/27 17:35
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：viewPager的切换动画
 */


public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.65f;


    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) { // (0,1]
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            view.setAlpha(0);
        }
    }
}
