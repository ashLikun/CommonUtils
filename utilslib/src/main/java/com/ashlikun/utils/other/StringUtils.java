/*
 * @(#)StringHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.ashlikun.utils.other;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 13:53
 * <p>
 * 方法功能：字符串一些工具类
 */

public class StringUtils {

    /**
     * 用于判断指定字符是否为空白字符，空白符包含：空格、tab键、换行符。
     *
     * @param s
     * @return
     */
    public static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/10/12 14:08
     * <p>
     * 方法功能：去除字符串空字符
     */
    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 10:54
     * <p>
     * 方法功能：是否正常的字符串
     */
    public static boolean isEmpty(String str) {
        return (TextUtils.isEmpty(str) || str.equals("null") || str.equals("NULL"));
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:00
     * <p>
     * 方法功能：比较两个字符串
     */

    public static boolean isEquals(String actual, String expected) {
        return actual == expected
                || (actual == null ? expected == null : actual.equals(expected));
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:01
     * <p>
     * 方法功能：字节转换成合适的单位
     */
    public static String prettyBytes(long value) {
        String args[] = {"B", "KB", "MB", "GB", "TB"};
        StringBuilder sb = new StringBuilder();
        int i;
        if (value < 1024L) {
            sb.append(String.valueOf(value));
            i = 0;
        } else if (value < 1048576L) {
            sb.append(String.format("%.1f", value / 1024.0));
            i = 1;
        } else if (value < 1073741824L) {
            sb.append(String.format("%.2f", value / 1048576.0));
            i = 2;
        } else if (value < 1099511627776L) {
            sb.append(String.format("%.3f", value / 1073741824.0));
            i = 3;
        } else {
            sb.append(String.format("%.4f", value / 1099511627776.0));
            i = 4;
        }
        sb.append(' ');
        sb.append(args[i]);
        return sb.toString();
    }

    /**
     * 二进位组转十六进制字符串
     *
     * @param buf 二进位组
     * @return 十六进制字符串
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 十六进制字符串转二进位组
     *
     * @param hexStr 十六进制字符串
     * @return 二进位组
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];

        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/4/29 11:05
     * 方法功能：非空判断处理和转换为String类型
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
     *               1如传的是String类型就会认为String为空时要转换的内容，不传为空时默认转换为未知，
     *               2如果传入的是intent类型，会认为double类型要保留的小数位数，
     *               3如是传入的是0会认为double要取整
     * @return 把内容转换为String返回
     */

    public static String dataFilter(Object source, Object filter) {
        try {
            if (source != null && !isEmpty(source.toString())) {//数据源没有异常
                if (source instanceof String) {
                    return source.toString().trim();//String 处理
                } else if (source instanceof Double || source instanceof Float) {
                    //小数处理，
                    BigDecimal bd = new BigDecimal(Double.parseDouble(source.toString()));
                    if (filter != null && filter instanceof Integer) {
                        if ((int) filter == 0) {
                            return String.valueOf((int) (bd.setScale(0, BigDecimal.ROUND_HALF_EVEN).doubleValue()));
                        } else {
                            return String.valueOf(bd.setScale(Math.abs((int) filter), BigDecimal.ROUND_HALF_EVEN).doubleValue());
                        }
                    }
                    return String.valueOf(bd.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
                } else if (source instanceof Integer || source instanceof Boolean) {
                    return source.toString();
                } else {
                    return "未知";
                }
            } else if (filter != null) {
                //数据源异常 并且filter不为空
                return filter.toString();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    public static String dataFilter(Object source) {
        return dataFilter(source, null);
    }

    /**
     * 方法功能：返回指定长度的字符串
     */
    public static String isNullToConvert(String str, int maxLenght) {
        return isEmpty(str) ? "未知" : str.substring(0, str.length() < maxLenght ? str.length() : maxLenght);
    }

    /**
     * 方法功能：小数 四舍五入 19.0->19.0    返回Double
     */
    public static Double roundDouble(double val, int precision) {
        //小数处理，
        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(precision, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * double转String,保留小数点后两位
     */
    public static String roundDoubleToFormat(double val, int precision) {
        //使用0.00不足位补0，#.##仅保留有效位
        return numberFormat(roundDouble(val, precision), precision);
    }

    /**
     * double转String,保留小数点后两位,三位三位的隔开
     *
     * @param precision 保留几位小数不足位补0
     */
    public static String roundDoubleToFormat3(double val, int precision) {
        //使用0.00不足位补0，#.##仅保留有效位
        return numberFormat3(roundDouble(val, precision), precision);
    }

    /**
     * double转String
     *
     * @param precision 保留几位小数不足位补0
     */
    public static String numberFormat(double val, int precision) {
        if (precision == 0) {
            return String.valueOf((int) val);
        }
        //使用0.00不足位补0，#.##仅保留有效位 ,
        StringBuilder pattern = new StringBuilder("#.");
        for (int i = 0; i < precision; i++) {
            pattern.append("0");
        }
        return new DecimalFormat(pattern.toString()).format(val);
    }

    /**
     * double转String,三位三位的隔开
     *
     * @param precision 保留几位小数不足位补0
     */
    public static String numberFormat3(double val, int precision) {
        if (precision == 0) {
            return new DecimalFormat("#,###").format(val);
        }
        // #,###.0000:金钱数字保留4位小数且三位三位的隔开
        StringBuilder pattern = new StringBuilder("#,###.");
        for (int i = 0; i < precision; i++) {
            pattern.append("0");
        }
        return new DecimalFormat(pattern.toString()).format(val);
    }


    /**
     * 方法功能：小数后两位
     */

    public static Double roundDouble(double val) {
        return roundDouble(val, 2);
    }

    /**
     * 方法功能：小数 四舍五入 19.0->19.0   返回字符串
     */
    public static String roundString(double val, int precision) {
        return String.valueOf(roundDouble(val, precision));
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:08
     * <p>
     * 方法功能：从url里面获取最后一个，就是文件名
     */
    public static String getUrlToFileName(String url) {
        if (url != null) {
            String[] splitS = url.split("/");
            if (splitS.length > 0) {
                return splitS[splitS.length - 1];
            }
        }
        return null;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:09
     * <p>
     * 方法功能：获取钱过滤成字符串
     */
    public static String getMoney(int money) {
        return money <= 0 ? "- -" : String.valueOf(money);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/11/9 0009 22:39
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：比较2个数字大小（字符串格式）
     *
     * @return 1:第一个 > 第二个
     * 0:第一个 = 第二个
     * -1:第一个 < 第二个
     * -2:异常
     */
    public static int compareNumber(String number1, String number2) {
        try {
            int numberInt1 = Integer.valueOf(number1);
            int numberInt2 = Integer.valueOf(number2);
            if (numberInt1 > numberInt2) {
                return 1;
            } else if (numberInt1 == numberInt2) {
                return 0;
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 按照指定的行数截取字符串,末尾。。。
     *
     * @param paint
     * @param textSize
     * @param layoutWidth
     * @param content
     * @param maxLine
     * @return
     */
    public static CharSequence ellipsize(TextPaint paint, float textSize, int layoutWidth, CharSequence content, int maxLine) {
        if (layoutWidth != 0 && !TextUtils.isEmpty((CharSequence) content)) {
            float oldTextSize = paint.getTextSize();
            paint.setTextSize(textSize);
            StaticLayout layout = new StaticLayout((CharSequence) content, paint, layoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            int count = layout.getLineCount();
            if (count > maxLine) {
                int start = layout.getLineStart(maxLine - 1);
                content = content.subSequence(0, start) + (String) TextUtils.ellipsize(((CharSequence) content)
                        .subSequence(start, ((CharSequence) content).length()), paint, (float) layoutWidth, TextUtils.TruncateAt.END);
            }
            paint.setTextSize(oldTextSize);
            return content;
        } else {
            return content;
        }
    }
}
