package com.ashlikun.utils.animator

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:00
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：viewPager的切换动画,缩放和透明度
 */

class DepthPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        if (position < -1) { // [-Infinity,-1)
            view.alpha = 0f
        } else if (position <= 0) { // [-1,0]
            view.alpha = 1f
            view.translationX = 0f
            view.scaleX = 1f
            view.scaleY = 1f
        } else if (position <= 1) { // (0,1]
            view.alpha = 1 - position
            view.translationX = pageWidth * -position
            val scaleFactor = (MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position)))
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        } else { // (1,+Infinity]
            view.alpha = 0f
        }
    }

    companion object {
        private const val MIN_SCALE = 0.65f
    }
}