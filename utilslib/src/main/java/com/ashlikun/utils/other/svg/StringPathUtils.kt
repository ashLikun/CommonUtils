package com.ashlikun.utils.other.svg

import android.graphics.Path
import android.util.SparseArray

/**
 * 获取StoreHouse风格Path
 * Created by srain on 11/7/14.
 *
 *
 * 增加一个getPath()方法，
 * update by zhangxutong on 2016 11 04
 */
object StringPathUtils {
    private val sPointList: SparseArray<FloatArray> = SparseArray()
    val LETTERS = arrayOf(
        floatArrayOf( // A
            24f, 0f, 1f, 22f,
            1f, 22f, 1f, 72f,
            24f, 0f, 47f, 22f,
            47f, 22f, 47f, 72f,
            1f, 48f, 47f, 48f
        ), floatArrayOf( // B
            0f, 0f, 0f, 72f,
            0f, 0f, 37f, 0f,
            37f, 0f, 47f, 11f,
            47f, 11f, 47f, 26f,
            47f, 26f, 38f, 36f,
            38f, 36f, 0f, 36f,
            38f, 36f, 47f, 46f,
            47f, 46f, 47f, 61f,
            47f, 61f, 38f, 71f,
            37f, 72f, 0f, 72f
        ), floatArrayOf( // C
            47f, 0f, 0f, 0f,
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f
        ), floatArrayOf( // D
            0f, 0f, 0f, 72f,
            0f, 0f, 24f, 0f,
            24f, 0f, 47f, 22f,
            47f, 22f, 47f, 48f,
            47f, 48f, 23f, 72f,
            23f, 72f, 0f, 72f
        ), floatArrayOf( // E
            0f, 0f, 0f, 72f,
            0f, 0f, 47f, 0f,
            0f, 36f, 37f, 36f,
            0f, 72f, 47f, 72f
        ), floatArrayOf( // F
            0f, 0f, 0f, 72f,
            0f, 0f, 47f, 0f,
            0f, 36f, 37f, 36f
        ), floatArrayOf( // G
            47f, 23f, 47f, 0f,
            47f, 0f, 0f, 0f,
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f,
            47f, 72f, 47f, 48f,
            47f, 48f, 24f, 48f
        ), floatArrayOf( // H
            0f, 0f, 0f, 72f,
            0f, 36f, 47f, 36f,
            47f, 0f, 47f, 72f
        ), floatArrayOf( // I
            0f, 0f, 47f, 0f,
            24f, 0f, 24f, 72f,
            0f, 72f, 47f, 72f
        ), floatArrayOf( // J
            47f, 0f, 47f, 72f,
            47f, 72f, 24f, 72f,
            24f, 72f, 0f, 48f
        ), floatArrayOf( // K
            0f, 0f, 0f, 72f,
            47f, 0f, 3f, 33f,
            3f, 38f, 47f, 72f
        ), floatArrayOf( // L
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f
        ), floatArrayOf( // M
            0f, 0f, 0f, 72f,
            0f, 0f, 24f, 23f,
            24f, 23f, 47f, 0f,
            47f, 0f, 47f, 72f
        ), floatArrayOf( // N
            0f, 0f, 0f, 72f,
            0f, 0f, 47f, 72f,
            47f, 72f, 47f, 0f
        ), floatArrayOf( // O
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f,
            47f, 72f, 47f, 0f,
            47f, 0f, 0f, 0f
        ), floatArrayOf( // P
            0f, 0f, 0f, 72f,
            0f, 0f, 47f, 0f,
            47f, 0f, 47f, 36f,
            47f, 36f, 0f, 36f
        ), floatArrayOf( // Q
            0f, 0f, 0f, 72f,
            0f, 72f, 23f, 72f,
            23f, 72f, 47f, 48f,
            47f, 48f, 47f, 0f,
            47f, 0f, 0f, 0f,
            24f, 28f, 47f, 71f
        ), floatArrayOf( // R
            0f, 0f, 0f, 72f,
            0f, 0f, 47f, 0f,
            47f, 0f, 47f, 36f,
            47f, 36f, 0f, 36f,
            0f, 37f, 47f, 72f
        ), floatArrayOf( // S
            47f, 0f, 0f, 0f,
            0f, 0f, 0f, 36f,
            0f, 36f, 47f, 36f,
            47f, 36f, 47f, 72f,
            47f, 72f, 0f, 72f
        ), floatArrayOf( // T
            0f, 0f, 47f, 0f,
            24f, 0f, 24f, 72f
        ), floatArrayOf( // U
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f,
            47f, 72f, 47f, 0f
        ), floatArrayOf( // V
            0f, 0f, 24f, 72f,
            24f, 72f, 47f, 0f
        ), floatArrayOf( // W
            0f, 0f, 0f, 72f,
            0f, 72f, 24f, 49f,
            24f, 49f, 47f, 72f,
            47f, 72f, 47f, 0f
        ), floatArrayOf( // X
            0f, 0f, 47f, 72f,
            47f, 0f, 0f, 72f
        ), floatArrayOf( // Y
            0f, 0f, 24f, 23f,
            47f, 0f, 24f, 23f,
            24f, 23f, 24f, 72f
        ), floatArrayOf( // Z
            0f, 0f, 47f, 0f,
            47f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f
        )
    )
    val NUMBERS = arrayOf(
        floatArrayOf( // 0
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f,
            47f, 72f, 47f, 0f,
            47f, 0f, 0f, 0f
        ), floatArrayOf( // 1
            24f, 0f, 24f, 72f
        ), floatArrayOf( // 2
            0f, 0f, 47f, 0f,
            47f, 0f, 47f, 36f,
            47f, 36f, 0f, 36f,
            0f, 36f, 0f, 72f,
            0f, 72f, 47f, 72f
        ), floatArrayOf( // 3
            0f, 0f, 47f, 0f,
            47f, 0f, 47f, 36f,
            47f, 36f, 0f, 36f,
            47f, 36f, 47f, 72f,
            47f, 72f, 0f, 72f
        ), floatArrayOf( // 4
            0f, 0f, 0f, 36f,
            0f, 36f, 47f, 36f,
            47f, 0f, 47f, 72f
        ), floatArrayOf( // 5
            0f, 0f, 0f, 36f,
            0f, 36f, 47f, 36f,
            47f, 36f, 47f, 72f,
            47f, 72f, 0f, 72f,
            0f, 0f, 47f, 0f
        ), floatArrayOf( // 6
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f,
            47f, 72f, 47f, 36f,
            47f, 36f, 0f, 36f
        ), floatArrayOf( // 7
            0f, 0f, 47f, 0f,
            47f, 0f, 47f, 72f
        ), floatArrayOf( // 8
            0f, 0f, 0f, 72f,
            0f, 72f, 47f, 72f,
            47f, 72f, 47f, 0f,
            47f, 0f, 0f, 0f,
            0f, 36f, 47f, 36f
        ), floatArrayOf( // 9
            47f, 0f, 0f, 0f,
            0f, 0f, 0f, 36f,
            0f, 36f, 47f, 36f,
            47f, 0f, 47f, 72f
        )
    )

    init {

        // A - Z
        for (i in LETTERS.indices) {
            sPointList.append(i + 65, LETTERS[i])
        }
        // a - z
        for (i in LETTERS.indices) {
            sPointList.append(i + 65 + 32, LETTERS[i])
        }
        // 0 - 9
        for (i in NUMBERS.indices) {
            sPointList.append(i + 48, NUMBERS[i])
        }
        // blank
        addChar(' ', floatArrayOf())
        // -
        addChar('-', floatArrayOf(0f, 36f, 47f, 36f))
        // .
        addChar('.', floatArrayOf(24f, 60f, 24f, 72f))
    }

    fun addChar(c: Char, points: FloatArray) {
        sPointList.append(c.code, points)
    }

    fun getPathList(str: String): ArrayList<FloatArray> {
        return getPathList(str, 1f, 14)
    }

    fun getPath(str: String, scale: Float, gapBetweenLetter: Int): Path {
        val list = getPathList(str, scale, gapBetweenLetter)
        val path = Path()
        if (list == null) {
            return path
        }
        path.moveTo(list[0][0], list[0][1])
        for (item in list) {
            path.moveTo(item[0], item[1])
            path.lineTo(item[2], item[3])
        }
        return path
    }

    fun getPath(str: String): Path {
        return getPath(str, 1f, 14)
    }

    /**
     */
    fun getPathList(str: String, scale: Float, gapBetweenLetter: Int): ArrayList<FloatArray> {
        val list = ArrayList<FloatArray>()
        var offsetForWidth = 0f
        for (element in str) {
            val pos = element.code
            val key = sPointList.indexOfKey(pos)
            if (key == -1) {
                continue
            }
            val points = sPointList[pos]
            val pointCount = points.size / 4
            for (j in 0 until pointCount) {
                val line = FloatArray(4)
                for (k in 0..3) {
                    val l = points[j * 4 + k]
                    // x
                    if (k % 2 == 0) {
                        line[k] = (l + offsetForWidth) * scale
                    } else {
                        line[k] = l * scale
                    }
                }
                list.add(line)
            }
            offsetForWidth += (57 + gapBetweenLetter).toFloat()
        }
        return list
    }
}