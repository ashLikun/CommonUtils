/*
 * @(#)StringHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.ashlikun.utils.other

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.experimental.and
import kotlin.math.min

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 23:16
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：字符串一些工具类
 */
/**
 * 去除字符串空字符
 */
inline fun String.isSpace() = StringUtils.isSpace(this)
inline fun String.trimx() = StringUtils.trim(this)

/**
 * 小数 四舍五入 19.0->19.0    返回Double
 */
inline fun String.roundDouble(precision: Int = 2) =
    StringUtils.roundDouble(this.toDoubleOrNull() ?: 0.0, precision)

inline fun Number.roundDouble(precision: Int = 2) =
    StringUtils.roundDouble(this.toDouble(), precision)

/**
 * double转String
 *
 * @param precision 保留几位小数不足位补0
 */
inline fun String.numberFormat(precision: Int = 2) =
    StringUtils.numberFormat(this.toDoubleOrNull() ?: 0.0, precision)

inline fun Number.numberFormat(precision: Int = 2) =
    StringUtils.numberFormat(this.toDouble(), precision)

/**
 * double转String,三位三位的隔开
 *
 * @param precision 保留几位小数不足位补0
 */
inline fun String.numberFormat3(precision: Int = 2) =
    StringUtils.numberFormat3(this.toDoubleOrNull() ?: 0.0, precision)

inline fun Number.numberFormat3(precision: Int = 2) =
    StringUtils.numberFormat3(this.toDouble(), precision)

/**
 * 最大值裁剪字符串
 */
inline fun String.substring(maxLenght: Int) = this.substring(0, min(this.length - 1, maxLenght))

object StringUtils {
    /**
     * 用于判断指定字符是否为空白字符，空白符包含：空格、tab键、换行符。
     */
    fun isSpace(s: String): Boolean {
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    /**
     * 去除字符串空字符
     */
    fun trim(str: String) = str?.trim { it <= ' ' }

    /**
     * 是否正常的字符串
     */
    fun isEmpty(str: String) = str.isEmpty() || str == "null" || str == "NULL"

    /**
     *  比较两个字符串
     */
    fun isEquals(actual: String, expected: String): Boolean {
        return (actual === expected
                || if (actual == null) expected == null else actual == expected)
    }

    /**
     * 字节转换成合适的单位
     */
    fun prettyBytes(value: Long): String {
        val args = arrayOf("B", "KB", "MB", "GB", "TB")
        val sb = StringBuilder()
        val i: Int = when {
            value < 1024L -> {
                sb.append(value.toString())
                0
            }
            value < 1048576L -> {
                sb.append(String.format("%.1f", value / 1024.0))
                1
            }
            value < 1073741824L -> {
                sb.append(String.format("%.2f", value / 1048576.0))
                2
            }
            value < 1099511627776L -> {
                sb.append(String.format("%.3f", value / 1073741824.0))
                3
            }
            else -> {
                sb.append(String.format("%.4f", value / 1099511627776.0))
                4
            }
        }
        sb.append(' ')
        sb.append(args[i])
        return sb.toString()
    }


    /**
     * 非空判断处理和转换为String类型
     * dataFilter("aaa")  -> aaa
     * dataFilter(null)    ->"未知"
     * dataFilter("aaa","未知")  -> aaa
     * dataFilter(123.456  ,  2) -> 123.46
     * dataFilter(123.456  ,  0) -> 123
     * dataFilter(123.456  )    -> 123.46
     * dataFilter(56  )    -> "56"
     * dataFilter(true)        ->true
     *
     * @param source 主要对String,Integer,Double这三种类型进行处理
     * @param filter 要改的内容，这个要转换的内容可以不传，
     * 1如传的是String类型就会认为String为空时要转换的内容，不传为空时默认转换为未知，
     * 2如果传入的是intent类型，会认为double类型要保留的小数位数，
     * 3如是传入的是0会认为double要取整
     * @return 把内容转换为String返回
     */
    fun dataFilter(source: Any, filter: Any? = null): String {
        try {
            if (source != null && !isEmpty(source.toString())) { //数据源没有异常
                return (source as? String)?.toString()?.trim { it <= ' ' } //String 处理
                    ?: if (source is Double || source is Float) {
                        //小数处理，
                        val bd = BigDecimal(source.toString().toDouble())
                        if (filter != null && filter is Int) {
                            return if (filter == 0) {
                                return bd.setScale(0, BigDecimal.ROUND_HALF_EVEN)
                                    .toInt().toString()
                            } else {
                                bd.setScale(Math.abs(filter), BigDecimal.ROUND_HALF_EVEN).toDouble()
                                    .toString()
                            }
                        }
                        bd.setScale(2, BigDecimal.ROUND_HALF_EVEN).toDouble().toString()
                    } else if (source is Int || source is Boolean) {
                        source.toString()
                    } else {
                        ""
                    }
            } else if (filter != null) {
                //数据源异常 并且filter不为空
                return filter.toString()
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }


    /**
     * 小数 四舍五入 19.0->19.0    返回Double
     */
    fun roundDouble(value: Double, precision: Int = 2) =
        BigDecimal(value).setScale(precision, BigDecimal.ROUND_HALF_EVEN).toDouble()

    /**
     * double转String,保留小数点后两位
     * 使用0.00不足位补0，#.##     # 一个数字，不包括 0 , 0 一个数字
     */
    fun roundDoubleToFormat(value: Double, precision: Int) =
        numberFormat(roundDouble(value, precision), precision)

    /**
     * double转String,保留小数点后两位,三位三位的隔开
     * 使用0.00不足位补0，#.##     # 一个数字，不包括 0 , 0 一个数字
     * @param precision 保留几位小数不足位补0
     */
    fun roundDoubleToFormat3(value: Double, precision: Int) =
        numberFormat3(roundDouble(value, precision), precision)

    /**
     * double转String
     *
     * @param precision 保留几位小数不足位补0
     */
    fun numberFormat(value: Double, precision: Int): String {
        if (precision == 0) {
            return value.toInt().toString()
        }
        //使用0.00不足位补0，#.##     # 一个数字，不包括 0 , 0 一个数字
        val pattern = StringBuilder("0.")
        for (i in 0 until precision) {
            pattern.append("0")
        }
        return DecimalFormat(pattern.toString()).format(value)
    }

    /**
     * double转String,三位三位的隔开
     *
     * @param precision 保留几位小数不足位补0
     */
    fun numberFormat3(value: Double, precision: Int): String {
        if (precision == 0) {
            return DecimalFormat("0,###").format(value)
        }
        // #,##0.0000:金钱数字保留4位(不足补一位0)小数且三位三位的隔开
        val pattern = StringBuilder("#,##0.")
        for (i in 0 until precision) {
            pattern.append("0")
        }
        return DecimalFormat(pattern.toString()).format(value)
    }

    /**
     * 小数 四舍五入 19.0->19.0   返回字符串
     */
    fun roundString(value: Double, precision: Int) = roundDouble(value, precision).toString()

    /**
     * 从url里面获取最后一个，就是文件名
     */
    fun getUrlToFileName(url: String): String {
        val split = url.split("/").toTypedArray()
        return split.getOrNull(split.size - 1) ?: ""
    }

    /**
     * 获取钱过滤成字符串
     */
    fun getMoney(money: Int): String {
        return if (money <= 0) "- -" else money.toString()
    }

    /**
     * 比较2个数字大小（字符串格式）
     *
     * @return 1:第一个 > 第二个
     * 0:第一个 = 第二个
     * -1:第一个 < 第二个
     * -2:异常
     */
    fun compareNumber(number1: String, number2: String): Int {
        return try {
            val numberInt1 = number1.toInt()
            val numberInt2 = number2.toInt()
            when {
                numberInt1 > numberInt2 -> 1
                numberInt1 == numberInt2 -> 0
                else -> -1
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            -2
        }
    }

    /**
     * 按照指定的行数截取字符串,末尾。。。
     */
    fun ellipsize(
        paint: TextPaint,
        textSize: Float,
        layoutWidth: Int,
        content: CharSequence,
        maxLine: Int
    ): CharSequence {
        var content = content
        return if (layoutWidth != 0 && !TextUtils.isEmpty(content)) {
            val oldTextSize = paint.textSize
            paint.textSize = textSize
            val layout = StaticLayout(
                content,
                paint,
                layoutWidth,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                true
            )
            val count = layout.lineCount
            if (count > maxLine) {
                val start = layout.getLineStart(maxLine - 1)
                content = content.subSequence(0, start).toString() + TextUtils.ellipsize(
                    content
                        .subSequence(start, content.length),
                    paint,
                    layoutWidth.toFloat(),
                    TextUtils.TruncateAt.END
                ) as String
            }
            paint.textSize = oldTextSize
            content
        } else {
            content
        }
    }
}