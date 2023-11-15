package com.ashlikun.utils.other.svg

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.provider.CalendarContract.Colors
import androidx.core.graphics.PathParser
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.R
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.ui.extend.dp
import com.ashlikun.utils.ui.extend.resColor
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 作者　　: 李坤
 * 创建时间: 2023/11/10　18:27
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
object SvgUtils {
    const val android = "http://schemas.android.com/apk/res/android"

    fun parseSize(size: String?) = runCatching {
        if (size == null) {
            null
        } else if (size.contains("dip", true)) {
            size.replace("dip", "").toFloat().dp.toFloat()
        } else if (size.contains("px", true)) {
            size.replace("px", "").toFloat()
        } else {
            size.toFloatOrNull()
        }
    }.getOrNull()

    fun parseColor(colorStr: String?) = runCatching {
        if (colorStr.isNullOrEmpty()) {
            Color.BLACK
        } else if (colorStr == "#0") {
            Color.TRANSPARENT
        } else if (colorStr.startsWith("#")) {
            Color.parseColor(colorStr)
        } else if (colorStr.startsWith("@")) {
            colorStr.replace("@", "").toInt().resColor
        } else {
            //默认颜色黑色
            Color.RED
        }
    }.getOrNull() ?: Color.RED

    fun parseStrokeLineCap(sta: String) = runCatching {
        if (sta.equals("BUTT", true)) {
            Paint.Cap.BUTT
        } else if (sta.equals("ROUND", true)) {
            Paint.Cap.ROUND
        } else if (sta.equals("SQUARE", true)) {
            Paint.Cap.SQUARE
        } else null
    }.getOrNull()

    fun parsePath(str: String?) = runCatching { if (str.isNullOrEmpty()) null else PathParser.createPathFromPathData(str) }.getOrNull()

    /**
     * 解析vector svg xml
     */
    fun getVectorData(resId: Int): VectorData? {
        return runCatching {
            val vectorData = VectorData()
            val parser = AppUtils.app.resources.getXml(resId)
            //4.循环解析
            var type: Int = parser.eventType
            while (type != XmlPullParser.END_DOCUMENT) {
                // 判断如果遇到开始标签事件
                if (type == XmlPullParser.START_TAG) {
                    if (parser.name == "vector") {
                        vectorData.width = parseSize(parser.getAttributeValue(android, "width")) ?: 0f
                        vectorData.height = parseSize(parser.getAttributeValue(android, "height")) ?: 0f
                        vectorData.viewportWidth = parser.getAttributeValue(android, "viewportWidth").toFloat()
                        vectorData.viewportHeight = parser.getAttributeValue(android, "viewportHeight").toFloat()
                        vectorData.alpha = parseSize(parser.getAttributeValue(android, "alpha"))
                    } else if (parser.name == "path") {
                        val pathData = VectorData.PathData(
                            parseColor(parser.getAttributeValue(android, "fillColor")),
                            pathStr = parser.getAttributeValue(android, "pathData"),
                            name = parser.getAttributeValue(android, "name").orEmpty(),
                            strokeWidth = parseSize(parser.getAttributeValue(android, "strokeWidth")),
                            strokeColor = parseColor(parser.getAttributeValue(android, "strokeColor")),
                            strokeLineCap = parser.getAttributeValue(android, "strokeLineCap")?.let {
                                Paint.Cap.values().getOrNull(it.toInt())
                            },
                            strokeLineJoin = parser.getAttributeValue(android, "strokeLineJoin")?.let {
                                Paint.Join.values().getOrNull(it.toInt())
                            },
                            strokeMiterLimit = parseSize(parser.getAttributeValue(android, "strokeMiterLimit")),
                            strokeAlpha = parseSize(parser.getAttributeValue(android, "strokeAlpha")),
                            fillAlpha = parseSize(parser.getAttributeValue(android, "fillAlpha")),
                        )
                        vectorData.pathDatas.add(pathData)
                    }
                }
                type = parser.next()
            }
            vectorData
        }.onFailure { it.printStackTrace() }.getOrNull()
    }

    /**
     * 从R.array.xxx里取出点阵，
     */
    fun getPathFromStringArray(arrayId: Int, zoomSize: Float): Path {
        val path = Path()
        val points = AppUtils.app.resources.getStringArray(arrayId)
        for (i in points.indices) {
            val x = points[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var j = 0
            while (j < x.size) {
                if (j == 0) {
                    path.moveTo(x[j].toFloat() * zoomSize, x[j + 1].toFloat() * zoomSize)
                } else {
                    path.lineTo(x[j].toFloat() * zoomSize, x[j + 1].toFloat() * zoomSize)
                }
                j = j + 2
            }
        }
        return path
    }

    /**
     * 根据ArrayList<float></float>[]> path 解析
     * @return
     */
    fun getPathFromArrayFloatList(path: ArrayList<FloatArray>): Path? {
        val sPath = Path()
        for (i in path.indices) {
            val floats = path[i]
            sPath.moveTo(floats[0], floats[1])
            sPath.lineTo(floats[2], floats[3])
        }
        return sPath
    }
}