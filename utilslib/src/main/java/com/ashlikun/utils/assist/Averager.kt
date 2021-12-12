package com.ashlikun.utils.assist

import java.util.ArrayList
import kotlin.jvm.Synchronized
import com.ashlikun.utils.other.LogUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:00
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：用以统计平均数
 */

class Averager {
    private val numList = ArrayList<Number>()

    /**
     * 添加一个数字
     */
    @Synchronized
    fun add(num: Number) {
        numList.add(num)
    }

    /**
     * 清除全部
     */
    fun clear() {
        numList.clear()
    }

    /**
     * 返回参与均值计算的数字个数
     */
    fun size(): Number {
        return numList.size
    }

    /**
     * 获取平均数
     */
    val average: Number
        get() = if (numList.size == 0) {
            0
        } else {
            var sum = 0f
            var i = 0
            val size = numList.size
            while (i < size) {
                sum += numList[i].toFloat()
                i++
            }
            sum / numList.size
        }

    /**
     * 打印数字列
     */
    fun print(): String {
        val str = "PrintList(" + size() + "): " + numList
        LogUtils.i(str)
        return str
    }
}