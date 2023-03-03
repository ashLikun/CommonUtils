/*
 * @(#)DateUtils.java    Created on 2004-10-20
 * Copyright (c) 2005 ZDSoft Networks, Inc. All rights reserved.
 * $Id: DateUtils.java 34780 2013-02-17 10:43:59Z xuan $
 */
package com.ashlikun.utils.other

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 13:24
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：处理日期时间的工具类
 */
inline fun Calendar?.formatTime(format: String = DateUtils.YMD_HMS) =
    DateUtils.getFormatTime(this, format)

inline fun Calendar?.formatYmd() =
    DateUtils.getFormatTime(this, DateUtils.YMD)

inline fun Calendar?.formatYmdHms() =
    DateUtils.getFormatTime(this, DateUtils.YMD_HMS)

inline fun Calendar?.formatHMS() =
    DateUtils.getFormatTime(this, DateUtils.HMS)

inline fun Calendar.addDay(dayAmount: Int) =
    DateUtils.addDay(this, dayAmount)

inline fun Calendar.addHour(hourAmount: Int) =
    DateUtils.addHour(this, hourAmount)

inline fun Calendar.addMinute(minuteAmount: Int) =
    DateUtils.addMinute(this, minuteAmount)

inline fun Calendar.getDay() =
    DateUtils.getTimeDay(this)

inline fun Calendar.isTotay(today: Calendar = Calendar.getInstance()) =
    DateUtils.isTotay(this, today)

inline fun Calendar.isYeserday(today: Calendar = Calendar.getInstance()) =
    DateUtils.isYeserday(this, today)

/**
 * 判断是否是闰年
 */
inline fun Calendar.isLeapYear() =
    (this as GregorianCalendar).isLeapYear(this[Calendar.YEAR])

inline fun Calendar.getWeekOfYear() =
    DateUtils.getWeekOfYear(this)

inline fun Calendar.getWeek() =
    DateUtils.getWeek(this)

inline fun Calendar.isThisWeek() =
    DateUtils.isThisWeek(this)

/**
 * @return 1>2, 则返回大于0的数;
 * 1<2 返回小于0的数;
 * 1=2, 则返回0.
 */
inline fun Calendar.compareHourAndMinute(calendar: Calendar) =
    DateUtils.compareIgnoreSecond(this, calendar)

/**
 * @return 1>2, 则返回大于0的数;
 * 1<2 返回小于0的数;
 * 1=2, 则返回0.
 */
inline fun Calendar.compareIgnoreSecond(calendar: Calendar) =
    DateUtils.compareIgnoreSecond(this, calendar)

inline fun String.formatCalendar(format: String = DateUtils.YMD_HMS) =
    DateUtils.getFormatCalendar(this, format)

/**
 * 秒变成 HH:MM:SS
 */
fun Int.formatHHmmss(format: String = "%02d:%02d:%02d") = format.format(this / 3600, ((this % 3600) / 60), ((this % 3600) % 60))
fun Int.formatHHmm(format: String = "%02d:%02d") = format.format(this / 3600, ((this % 3600) / 60))
fun Int.format02() = "%02d".format(this)

object DateUtils {
    const val YMD_HMS = "yyyy-MM-dd HH:mm:ss"
    const val YMD = "yyyy-MM-dd"
    const val MD = "MM-dd"
    const val YM = "yyyy-MM"
    const val HMS = "HH:mm:ss"
    const val REX_10D = "\\d{10,}"

    /**
     * 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param calendar 时间
     * @param format   格式
     * @return 时间字符串
     */
    fun getFormatTime(calendar: Calendar?, format: String = YMD_HMS) =
        try {
            if (calendar == null) ""
            else SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }

    /**
     * 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param calendar 时间戳
     * @param format   格式
     * @return 时间字符串
     */
    fun getFormatTime(calendar: Long, format: String = YMD_HMS) =
        if (calendar <= 0) "" else try {
            SimpleDateFormat(format, Locale.getDefault()).format(calendar)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }

    /**
     * 字符串转Calendar
     * @param time   时间字符串
     * @param format 格式
     */
    fun getFormatCalendar(time: String, format: String = YMD_HMS): Calendar? {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        try {
            val calendar = Calendar.getInstance()
            calendar.time = sdf.parse(time)
            return calendar
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
     * @param calendar     时间
     * @param originFormat 原来数据的格式
     * @param format       输出的格式
     * @return 时间字符串
     */
    fun getFormatTime(
        calendar: String,
        originFormat: String = YMD_HMS,
        format: String = YMD_HMS
    ): String {
        return if (calendar.isEmpty()) ""
        else try {
            SimpleDateFormat(format, Locale.getDefault()).format(
                SimpleDateFormat(
                    originFormat,
                    Locale.getDefault()
                ).parse(calendar)
            )
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取指定格式的当前时间
     *
     * @param format 格式
     * @return 格式化后的时间
     */
    fun getCurrentTime(format: String = YMD) = getFormatTime(Calendar.getInstance(), format)


    /**
     * 获取当前时间 并完全格式化
     *
     * @return 格式化后的时间 2006-01-10 20:56:44
     */
    val currentTimeYmdHms: String
        get() = getFormatTime(Calendar.getInstance(), YMD_HMS)

    /**
     * 根据日期对象来获取日期中的时间(YMD)
     * @return 时间字符串, 格式为: YMD
     */
    fun getYmd(calender: Calendar?) = getFormatTime(calender, YMD)

    /**
     *：根据日期对象来获取日期中的完整时间
     * @return 时间字符串, 格式为: yyyy-MM-dd HH:mm:ss
     */
    fun getYmdHms(calender: Calendar?) = getFormatTime(calender, YMD_HMS)

    /**
     * 获取某天的结束时间,
     *
     * @param calender 要获取的时间
     * @return 结束时间 2005-10-01 23:59:59.999
     */
    fun getEndTime(calender: Calendar = Calendar.getInstance()): Calendar {
        calender[Calendar.HOUR_OF_DAY] = 23
        calender[Calendar.MINUTE] = 59
        calender[Calendar.SECOND] = 59
        calender[Calendar.MILLISECOND] = 999
        return calender
    }

    /**
     * 获取某天的起始时间,
     * @param calender 要获取的时间
     * @return 开始时间 2005-10-01 00:00:00.000
     */
    fun getStartTime(calender: Calendar = Calendar.getInstance()): Calendar {
        calender[Calendar.HOUR_OF_DAY] = 0
        calender[Calendar.MINUTE] = 0
        calender[Calendar.SECOND] = 0
        calender[Calendar.MILLISECOND] = 0
        return calender
    }

    /**
     * 根据指定的年, 月, 日, 时, 分,秒等参数获取日期对象.
     *
     * @param year      年
     * @param month     月
     * @param date      日
     * @param hourOfDay 时(24小时制)
     * @param minute    分
     * @param second    秒
     * @return 对应的日期对象
     */
    fun getTime(
        year: Int, month: Int, date: Int, hourOfDay: Int = 0,
        minute: Int = 0, second: Int = 0
    ): Calendar {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, date, hourOfDay, minute, second)
        cal[Calendar.MILLISECOND] = 0
        return cal
    }

    /**
     * 取得指定天数后的时间
     * @param calendar  基准时间
     * @param dayAmount 指定天数，允许为负数
     * @return 指定天数后的时间
     */
    fun addDay(calendar: Calendar, dayAmount: Int) = calendar.apply {
        add(Calendar.DAY_OF_YEAR, dayAmount)
    }

    /**
     * 取得指定小时数后的时间
     * @param calendar   基准时间
     * @param hourAmount 指定小时数，允许为负数
     * @return 指定小时数后的时间
     */
    fun addHour(calendar: Calendar, hourAmount: Int) = calendar.apply {
        add(Calendar.HOUR, hourAmount)
    }

    /**
     * 取得指定分钟数后的时间
     *
     * @param calendar     基准时间
     * @param minuteAmount 指定分钟数，允许为负数
     * @return 指定分钟数后的时间
     */
    fun addMinute(calendar: Calendar, minuteAmount: Int) = calendar.apply {
        add(Calendar.MINUTE, minuteAmount)
    }

    /**
     * 比较两日期对象中的小时和分钟部分的大小
     *
     * @param calendar        日期对象1, 如果为 null 会以当前时间的日期对象代替
     * @param anotherCalender 日期对象2, 如果为 null 会以当前时间的日期对象代替
     * @return 1>2, 则返回大于0的数;
     * 1<2 返回小于0的数;
     * 1=2, 则返回0.
     */
    fun compareHourAndMinute(calendar: Calendar, calender2: Calendar): Int {
        val hourOfDay1 = calendar[Calendar.HOUR_OF_DAY]
        val minute1 = calendar[Calendar.MINUTE]
        val hourOfDay2 = calender2[Calendar.HOUR_OF_DAY]
        val minute2 = calender2[Calendar.MINUTE]
        return if (hourOfDay1 > hourOfDay2) 1
        else if (hourOfDay1 == hourOfDay2)
        // 小时相等就比较分钟
            if (minute1 > minute2) 1 else if (minute1 == minute2) 0 else -1
        else -1
    }

    /**
     * 比较两日期对象的大小, 忽略秒, 只精确到分钟.
     *
     * @param calendar        日期对象1, 如果为 null 会以当前时间的日期对象代替
     * @param anotherCalender 日期对象2, 如果为 null 会以当前时间的日期对象代替
     * @return 1>2, 则返回大于0的数;
     * 1<2 返回小于0的数;
     * 1=2, 则返回0.
     */
    fun compareIgnoreSecond(calendar: Calendar, calender2: Calendar = Calendar.getInstance()): Int {
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calender2[Calendar.SECOND] = 0
        calender2[Calendar.MILLISECOND] = 0
        return calendar.compareTo(calender2)
    }

    /**
     * 根据日期对象来获取日期中的 天
     *
     * @param calender 日期
     * @return day值
     */
    fun getTimeDay(calender: Calendar) = calender[Calendar.DAY_OF_MONTH]

    /**
     * 是否是今天
     *
     * @param calendar 日期
     * @param today    今天的日期
     * @return 是true，否则false
     */
    fun isTotay(calendar: Calendar, today: Calendar = Calendar.getInstance()): Boolean {
        return today[Calendar.YEAR] == calendar[Calendar.YEAR] && today[Calendar.DAY_OF_YEAR] == calendar[Calendar.DAY_OF_YEAR]
    }

    /**
     * 是否是昨天
     *
     * @param calendar 日期
     * @param today    今天的日期
     * @return 是true，否则false
     */
    fun isYeserday(calendar: Calendar, today: Calendar = Calendar.getInstance()) =
        isTotay(addDay(calendar, 1), today)

    /**
     * 判断是否是闰年
     *
     * @param year 年份
     * @return 是true，否则false
     */
    fun isLeapYear(year: Int): Boolean {
        return (Calendar.getInstance() as GregorianCalendar).isLeapYear(year)
    }

    /**
     * 取得一年中的第几周。
     *
     * @param calender 日期
     * @return 第几周
     */
    fun getWeekOfYear(calender: Calendar): Int {
        //如果是周日，就要-1
        return if (getWeek(calender) == 7) {
            calender[Calendar.WEEK_OF_YEAR] - 1
        } else calender[Calendar.WEEK_OF_YEAR]
    }

    /**
     * 是否本周
     *
     * @param calender 日期
     * @param today    今天
     * @return
     */
    fun isThisWeek(calender: Calendar, today: Calendar = Calendar.getInstance()) =
        getWeekOfYear(today) == getWeekOfYear(calender)

    /**
     * 获取周几
     */
    fun getWeek(calender: Calendar): Int {
        val week = calender[Calendar.DAY_OF_WEEK] - 1
        return if (week == 0) 7 else week
    }

    /**
     * Date(1461686400000)  转化成时间
     *
     * @param calendar Date(1461686400000) 接口返回时间
     * @param pattern  格式
     * @return 格式化后的时间
     */
    fun dateToString(calendar: String, pattern: String = YMD): String {
        if (calendar.isEmpty()) return ""
        val time = dateToLong(calendar)
        return if (time == 0L) "" else getFormatTime(time, pattern)
    }

    /**
     * Date(1461686400000) 转换成 时间值
     *
     * @param date Date(1461686400000) 接口返回时间
     * @return 时间值
     */
    fun dateToLong(date: String): Long {
        if (date.isEmpty()) return 0L
        val p = Pattern.compile(REX_10D)
        val matcher = p.matcher(date)
        var time: Long = 0
        while (matcher.find()) { //注意这里，是while不是if
            time = java.lang.Long.valueOf(matcher.group())
        }
        return time
    }
}