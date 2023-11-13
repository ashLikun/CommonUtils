package com.ashlikun.utils.other.svg

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

/**
 * 作者　　: 李坤
 * 创建时间: 2023/11/10　18:27
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
data class VectorData(
    var width: Float = 0f,
    var height: Float = 0f,
    var viewportWidth: Float = 0f,
    var viewportHeight: Float = 0f,
    //0-1
    var alpha: Float? = null,
    var pathDatas: MutableList<PathData> = mutableListOf(),
) {
    data class PathData(
        val fillColor: Int,
        val pathStr: String,
        val strokeWidth: Float? = null,
        val strokeColor: Int? = null,
        val strokeLineCap: Paint.Cap? = null,
        val strokeLineJoin: Paint.Join? = null,
        val strokeMiterLimit: Float? = null,
        val strokeAlpha: Float? = null,
        val fillAlpha: Float? = null,
    ) {
        val path: Path? by lazy {
            SvgUtils.parsePath(pathStr)
        }
    }

}