/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ashlikun.utils.ui.resources

import android.graphics.Color

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/14 23:34
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：颜色工具类
 */

object ColorUtils {
    fun setColorAlpha(color: Int, alpha: Float): Int {
        return setColorAlpha(color, alpha, true)
    }

    /**
     * 设置颜色的alpha值
     *
     * @param color    需要被设置的颜色值
     * @param alpha    取值为[0,1]，0表示全透明，1表示不透明
     * @param override 覆盖原本的 alpha
     * @return 返回改变了 alpha 值的颜色值
     */
    fun setColorAlpha(color: Int, alpha: Float, override: Boolean): Int {
        val origin = if (override) 0xff else color shr 24 and 0xff
        return color and 0x00ffffff or (alpha * origin).toInt() shl 24
    }

    /**
     * 根据比例，在两个color值之间计算出一个color值
     * **注意该方法是ARGB通道分开计算比例的**
     *
     * @param fromColor 开始的color值
     * @param toColor   最终的color值
     * @param fraction  比例，取值为[0,1]，为0时返回 fromColor， 为1时返回 toColor
     * @return 计算出的color值
     */
    fun computeColor(fromColor: Int, toColor: Int, fraction: Float): Int {
        var fraction = fraction
        fraction = Math.max(Math.min(fraction, 1f), 0f)
        val minColorA = Color.alpha(fromColor)
        val maxColorA = Color.alpha(toColor)
        val resultA = ((maxColorA - minColorA) * fraction).toInt() + minColorA
        val minColorR = Color.red(fromColor)
        val maxColorR = Color.red(toColor)
        val resultR = ((maxColorR - minColorR) * fraction).toInt() + minColorR
        val minColorG = Color.green(fromColor)
        val maxColorG = Color.green(toColor)
        val resultG = ((maxColorG - minColorG) * fraction).toInt() + minColorG
        val minColorB = Color.blue(fromColor)
        val maxColorB = Color.blue(toColor)
        val resultB = ((maxColorB - minColorB) * fraction).toInt() + minColorB
        return Color.argb(resultA, resultR, resultG, resultB)
    }

    /**
     * 将 color 颜色值转换为十六进制字符串
     *
     * @param color 颜色值
     * @return 转换后的字符串
     */
    fun colorToString(color: Int): String {
        return String.format("#%08X", color)
    }

    /**
     * 2个颜色混合
     *
     * @param fg 前景
     * @param bg 背景
     * @return
     */
    fun blendColor(fg: Int, bg: Int): Int {
        val sca = Color.alpha(fg)
        val scr = Color.red(fg)
        val scg = Color.green(fg)
        val scb = Color.blue(fg)
        val dca = Color.alpha(bg)
        val dcr = Color.red(bg)
        val dcg = Color.green(bg)
        val dcb = Color.blue(bg)
        val color_r = dcr * (0xff - sca) / 0xff + scr * sca / 0xff
        val color_g = dcg * (0xff - sca) / 0xff + scg * sca / 0xff
        val color_b = dcb * (0xff - sca) / 0xff + scb * sca / 0xff
        return (color_r shl 16) + (color_g shl 8) + color_b or -0x1000000
    }

    /**
     * 这个颜色是不是深色的
     *
     * @param color
     * @return
     */
    fun isColorDrak(color: Int): Boolean {
        //int t = (color >> 24) & 0xFF;
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return r * 0.299 + g * 0.578 + b * 0.114 <= 192
    }
}