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
package com.ashlikun.utils.other.spannable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.style.ReplacementSpan

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:31
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：提供一个整行的空白的Span，可用来用于制作段间距
 */

class BlockSpaceSpan(private val mHeight: Int) : ReplacementSpan() {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        if (fm != null) {
            //return后宽度为0，因此实际空隙和段落开始在同一行，需要加上一行的高度
            fm.top = -mHeight - paint.getFontMetricsInt(fm)
            fm.ascent = fm.top
            fm.bottom = 0
            fm.descent = fm.bottom
        }
        return 0
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        return
    }
}