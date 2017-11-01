/*
 * @(#)DateUtils.java    Created on 2004-10-20
 * Copyright (c) 2005 ZDSoft Networks, Inc. All rights reserved.
 * $Id: DateUtils.java 34780 2013-02-17 10:43:59Z xuan $
 */
package com.ashlikun.utils.other;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 11:46
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：处理日期时间的工具类
 */
@SuppressLint("SimpleDateFormat")
public abstract class DateUtils {
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD = "yyyy-MM-dd";
    public static final String MD = "MM-dd";
    public static final String YM = "yyyy-MM";
    public static final String HMS = "HH:mm:ss";

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/27 17:28
     * 方法功能 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param calendar 时间
     * @param format   格式
     * @return 时间字符串
     */
    public static String getFormatTime(Calendar calendar, String format) {
        if (calendar == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/27 17:28
     * 方法功能 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param calendar 时间戳
     * @param format   格式
     * @return 时间字符串
     */
    public static String getFormatTime(long calendar, String format) {
        if (calendar <= 0) return "";
        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/3/27 17:28
     * 方法功能 按照指定格式把时间转换成字符串，格式的写法类似yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param calendar 时间
     * @param format   格式
     * @return 时间字符串
     */
    public static String getFormatTime(String calendar, String format) {
        if (TextUtils.isEmpty(calendar)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(calendar));
            return getFormatTime(calendar, format);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 11:08
     * <p>
     * 方法功能：获取指定格式的当前时间
     *
     * @param format 格式
     * @return 格式化后的时间
     */

    public static String getCurrentTime(String format) {
        return getFormatTime(Calendar.getInstance(), format);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:41
     * <p>
     * 方法功能：获取当前时间 并简单格式化
     */

    public static String getCurrentTimeSimple() {
        return getFormatTime(Calendar.getInstance(), YMD);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:41
     * 方法功能：获取当前时间 并完全格式化
     *
     * @return 格式化后的时间 2006-01-10 20:56:44
     */
    public static String getCurrentTimeFull() {
        return getFormatTime(Calendar.getInstance(), YMD_HMS);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:11
     * <p>
     * 方法功能：把字符串转换成日期
     *
     * @param calendar 字符串
     * @param pattern  格式化
     * @return 返回日期String pattern
     */
    public static String getTime(String calendar, String pattern) {
        return getFormatTime(getFormatTime(calendar, pattern), pattern);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:11
     * <p>
     * 方法功能：把字符串转换成日期
     *
     * @param calendar 字符串
     * @return 返回日期String YMD_HMS
     */

    public static String getTime(String calendar) {

        return getFormatTime(getFormatTime(calendar, YMD_HMS), YMD_HMS);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:41
     * <p>
     * 方法功能：根据日期对象来获取日期中的时间(YMD)
     *
     * @param calender 日期对象
     * @return 时间字符串, 格式为: YMD
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeSimple(Calendar calender) {
        return getFormatTime(calender, YMD);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:41
     * <p>
     * 方法功能：根据日期对象来获取日期中的完整时间
     *
     * @param calender 日期对象
     * @return 时间字符串, 格式为: yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeFull(Calendar calender) {
        return getFormatTime(calender, YMD_HMS);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:46
     * <p>
     * 方法功能：获取某天的结束时间,
     *
     * @param calender 要获取的时间
     * @return 结束时间 2005-10-01 23:59:59.999
     */
    public static Calendar getEndTime(Calendar calender) {
        if (calender == null) {
            calender = Calendar.getInstance();
        }
        calender.set(Calendar.HOUR_OF_DAY, 23);
        calender.set(Calendar.MINUTE, 59);
        calender.set(Calendar.SECOND, 59);
        calender.set(Calendar.MILLISECOND, 999);

        return calender;
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:46
     * <p>
     * 方法功能：获取某天的起始时间,
     *
     * @param calender 要获取的时间
     * @return 开始时间 2005-10-01 00:00:00.000
     */
    public static Calendar getStartTime(Calendar calender) {
        if (calender == null) {
            calender = Calendar.getInstance();
        }
        calender.set(Calendar.HOUR_OF_DAY, 0);
        calender.set(Calendar.MINUTE, 0);
        calender.set(Calendar.SECOND, 0);
        calender.set(Calendar.MILLISECOND, 0);
        return calender;
    }

    /**
     * 根据指定的年, 月, 日等参数获取日期对象.
     *
     * @param year  年
     * @param month 月
     * @param date  日
     * @return 对应的日期对象
     */
    public static Calendar getTime(int year, int month, int date) {
        return getTime(year, month, date, 0, 0);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:04
     * <p>
     * 方法功能：根据指定的年, 月, 日, 时, 分,秒等参数获取日期对象.
     *
     * @param year      年
     * @param month     月
     * @param date      日
     * @param hourOfDay 时(24小时制)
     * @param minute    分
     * @param second    秒
     * @return 对应的日期对象
     */
    public static Calendar getTime(int year, int month, int date, int hourOfDay,
                                   int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date, hourOfDay, minute, second);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:05
     * <p>
     * 方法功能：同上 重载
     */

    public static Calendar getTime(int year, int month, int date, int hourOfDay,
                                   int minute) {
        return getTime(year, month, date, hourOfDay, minute, 0);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:00
     * <p>
     * 方法功能：取得指定天数后的时间
     *
     * @param calendar  基准时间
     * @param dayAmount 指定天数，允许为负数
     * @return 指定天数后的时间
     */

    public static Calendar addDay(Calendar calendar, int dayAmount) {
        if (calendar == null) {
            return null;
        }
        calendar.add(Calendar.DAY_OF_YEAR, dayAmount);
        return calendar;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:00
     * <p>
     * 方法功能：取得指定小时数后的时间
     *
     * @param calendar   基准时间
     * @param hourAmount 指定小时数，允许为负数
     * @return 指定小时数后的时间
     */

    public static Calendar addHour(Calendar calendar, int hourAmount) {
        if (calendar == null) {
            return null;
        }
        calendar.add(Calendar.HOUR, hourAmount);
        return calendar;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:00
     * 方法功能：取得指定分钟数后的时间
     *
     * @param calendar     基准时间
     * @param minuteAmount 指定分钟数，允许为负数
     * @return 指定分钟数后的时间
     */

    public static Calendar addMinute(Calendar calendar, int minuteAmount) {
        if (calendar == null) {
            return null;
        }

        calendar.add(Calendar.MINUTE, minuteAmount);
        return calendar;
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:01
     * <p>
     * 方法功能：比较两日期对象中的小时和分钟部分的大小
     *
     * @param calendar        日期对象1, 如果为 null 会以当前时间的日期对象代替
     * @param anotherCalender 日期对象2, 如果为 null 会以当前时间的日期对象代替
     * @return 1>2, 则返回大于0的数;
     * 1<2 返回小于0的数;
     * 1=2, 则返回0.
     */

    public static int compareHourAndMinute(Calendar calendar, Calendar anotherCalender) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        if (anotherCalender == null) {
            anotherCalender = Calendar.getInstance();
        }

        int hourOfDay1 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute1 = calendar.get(Calendar.MINUTE);

        int hourOfDay2 = anotherCalender.get(Calendar.HOUR_OF_DAY);
        int minute2 = anotherCalender.get(Calendar.MINUTE);

        if (hourOfDay1 > hourOfDay2) {
            return 1;
        } else if (hourOfDay1 == hourOfDay2) {
            // 小时相等就比较分钟
            return minute1 > minute2 ? 1 : (minute1 == minute2 ? 0 : -1);
        } else {
            return -1;
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:03
     * <p>
     * 方法功能：比较两日期对象的大小, 忽略秒, 只精确到分钟.
     *
     * @param calendar        日期对象1, 如果为 null 会以当前时间的日期对象代替
     * @param anotherCalender 日期对象2, 如果为 null 会以当前时间的日期对象代替
     * @return 1>2, 则返回大于0的数;
     * 1<2 返回小于0的数;
     * 1=2, 则返回0.
     */
    public static int compareIgnoreSecond(Calendar calendar, Calendar anotherCalender) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        if (anotherCalender == null) {
            anotherCalender = Calendar.getInstance();
        }

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        anotherCalender.set(Calendar.SECOND, 0);
        anotherCalender.set(Calendar.MILLISECOND, 0);
        return calendar.compareTo(anotherCalender);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:07
     * <p>
     * 方法功能：根据日期对象来获取日期中的 天
     *
     * @param calender 日期
     * @return day值
     */

    public static int getTimeDay(Calendar calender) {
        return calender.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:08
     * 方法功能： 是否是今天
     *
     * @param calendar 日期
     * @return 是true，否则false
     */
    public static boolean isTotay(Calendar calendar) {
        if (calendar == null) return false;
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) || today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:08
     * 方法功能： 判断是否是闰年
     *
     * @param year 年份
     * @return 是true，否则false
     */
    public static boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        return ((GregorianCalendar) calendar).isLeapYear(year);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:09
     * <p>
     * 方法功能：取得一年中的第几周。
     *
     * @param calender 日期
     * @return 第几周
     */

    public static int getWeekOfYear(Calendar calender) {
        return calender.get(Calendar.WEEK_OF_YEAR);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:15
     * <p>
     * 方法功能：Date(1461686400000)  转化成时间
     *
     * @param calendar Date(1461686400000) 接口返回时间
     * @param pattern  格式
     * @return 格式化后的时间
     */

    public static String dateToString(String calendar, String pattern) {
        if (StringUtils.isEmpty(calendar)) return "";
        long time = dateToLong(calendar);
        if (time == 0) {
            return "";
        }
        return getFormatTime(time, pattern);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:19
     * <p>
     * 方法功能：同上  重载
     */

    public static String dateToString(String date) {
        return dateToString(date, YMD);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 11:17
     * <p>
     * 方法功能：Date(1461686400000) 转换成 时间值
     *
     * @param date Date(1461686400000) 接口返回时间
     * @return 时间值
     */

    public static Long dateToLong(String date) {
        if (StringUtils.isEmpty(date)) return 0l;
        Pattern p = Pattern.compile("\\d{10,}");
        Matcher matcher = p.matcher(date);
        long time = 0;
        while (matcher.find()) { //注意这里，是while不是if
            time = Long.valueOf(matcher.group());
        }
        if (time == 0) {
            return 0l;
        }
        return time;
    }
}
