/* 
 * @(#)Validators.java    Created on 2004-10-10
 * Copyright (c) 2005 ZDSoft.net, Inc. All rights reserved.
 * $Id: Validators.java 30896 2012-09-27 04:37:48Z xuan $
 */
package com.ashlikun.utils.other;

import java.util.regex.Pattern;

/**
 * 对字符串按照常用规则进行验证的工具类
 *
 * @author xuan
 * @version $Revision: 30896 $, $Date: 2012-09-27 12:37:48 +0800 (周四, 27 九月 2012) $
 */
public abstract class Validators {

    /**
     * 简体中文的正则表达式。
     */
    public static final String REGEX_SIMPLE_CHINESE = "^[\u4E00-\u9FA5]+$";

    /**
     * 字母数字的正则表达式。
     */
    public static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]+";

    /**
     * 移动手机号码的正则表达式。
     * 2015-12-27
     */
    public static final String REGEX_CHINA_MOBILE = "1(3[4-9]|4[7]|5[012789]|8[2378])\\d{8}";

    /**
     * 联通手机号码的正则表达式。
     * 中国联通拥有号码段为：130、131、132、155、156（3G）、186（3G）、185（3G）、176；
     * 2015-12-27
     */
    public static final String REGEX_CHINA_UNICOM = "1(3[0-2]|5[56]|7[6]|8[56])\\d{8}";

    /**
     * 电信手机号码的正则表达式。
     * 2015-12-27
     */
    public static final String REGEX_CHINA_TELECOM = "1(33|53|77|8(019))\\d{8}";
    /**
     * 3大运行商的号码
     */
    public static final String REGEX_PHONE_NUMBER = "1(3[0-9]|4[7]|5[0-35-9]|7[367]|8[0-35-9])\\d{8}";

    /**
     * 整数或浮点数的正则表达式。signed
     */
    public static final String REGEX_NUMERIC = "(\\+|-){0,1}(\\d+)([.]?)(\\d*)";
    /**
     * 整数的正则表达式。signed
     */
    public static final String REGEX_NUMBER = "[0-9]*";
    /**
     * 整数或浮点数的正则表达式。unsigned
     */
    public static final String REGEX_NUMERIC_UNSIGNED = "(\\d+)([.]?)(\\d*)";

    /**
     * 身份证号码的正则表达式。
     */
    public static final String REGEX_ID_CARD = "(\\d{14}|\\d{17})(\\d|x|X)";

    /**
     * 电子邮箱的正则表达式。
     */
    public static final String REGEX_EMAIL = ".+@.+\\.[a-z]+";

    /**
     * 固话号码的正则表达式。
     */
    private static final String REGEX_GUHUA_NUMBER = "(([\\(（]\\d+[\\)）])?|(\\d+[-－]?)*)\\d+";

    /**
     * 判断字符串是否只包含字母和数字.
     *
     * @param str 字符串
     * @return 如果字符串只包含字母和数字, 则返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isAlphanumeric(String str) {
        return isRegexMatch(str, REGEX_ALPHANUMERIC);
    }


    /**
     * 是否为中国移动手机号码。
     *
     * @param str 字符串
     * @return 如果是移动号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaMobile(String str) {
        return isRegexMatch(str, REGEX_CHINA_MOBILE);
    }

    /**
     * 是否为中国联通手机号码。
     *
     * @param str 字符串
     * @return 如果是联通号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaUnicom(String str) {
        return isRegexMatch(str, REGEX_CHINA_UNICOM);
    }

    /**
     * 判断是否为电信手机。
     *
     * @param str 字符串
     * @return 如果是电信号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaTelecom(String str) {
        return isRegexMatch(str, REGEX_CHINA_TELECOM);
    }

    /**
     * 判断是否为3大运行商。
     *
     * @param str 字符串
     * @return 如果是电信号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isAllTelecom(String str) {
        return isRegexMatch(str, REGEX_PHONE_NUMBER);
    }



    /**
     * 判断字符串是否是合法的电子邮箱地址.
     *
     * @param str 字符串
     * @return 是true，否则false
     */
    public static boolean isEmail(String str) {
        return isRegexMatch(str, REGEX_EMAIL);
    }


    /**
     * 字符串是否为Empty，null和空格都算是Empty
     *
     * @param str 字符串
     * @return true/false
     */
    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    /**
     * <p>
     * Validating for ID card number.
     * </p>
     *
     * @param str string to be validated
     * @return If the str is valid ID card number return <code>true</code>, otherwise return <code>false</code>.
     */
    public static boolean isIdCardNumber(String str) {
        // 15位或18数字, 14数字加x(X)字符或17数字加x(X)字符才是合法的
        return isRegexMatch(str, REGEX_ID_CARD);
    }

    /**
     * 是否为手机号码, 包括移动, 联通, 电信手机号码.
     *
     * @param str 字符串
     * @return 若是合法的手机号码返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isMobile(String str) {
        return isAllTelecom(str);
    }

    /**
     * 是否为数字的字符串。
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isNumber(String str) {
        return isRegexMatch(str, REGEX_NUMBER);
    }

    /**
     * 是否是固定范围内的数字的字符串
     *
     * @param str
     * @param min
     * @param max
     * @return true/false
     */
    public static boolean isNumber(String str, int min, int max) {
        if (!isNumber(str)) {
            return false;
        }

        int number = Integer.parseInt(str);
        return number >= min && number <= max;
    }

    /**
     * 判断字符是否为整数或浮点数. <br>
     *
     * @param str 字符串
     * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
     */
    public static boolean isNumeric(String str) {
        return isRegexMatch(str, REGEX_NUMERIC);
    }

    /**
     * 判断字符是否为符合精度要求的整数或浮点数。
     *
     * @param str         字符串
     * @param fractionNum 小数部分的最多允许的位数
     * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
     */
    public static boolean isNumeric(String str, int fractionNum) {
        if (isEmpty(str)) {
            return false;
        }

        // 整数或浮点数
        String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d{0," + fractionNum + "})";
        return Pattern.matches(regex, str);
    }

    /**
     * 是否是手机号码
     */
    public static boolean isPhoneNumber(String str) {
        // Regex for checking phone number
        return isRegexMatch(str, REGEX_PHONE_NUMBER);
    }

    /**
     * 判断是否是合法的邮编
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isPostcode(String str) {
        if (isEmpty(str)) {
            return false;
        }

        if (str.length() != 6 || !Validators.isNumber(str)) {
            return false;
        }

        return true;
    }

    /**
     * 判断是否是固定长度范围内的字符串
     *
     * @param str
     * @param minLength
     * @param maxLength
     * @return true/false
     */
    public static boolean isString(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }

        if (minLength < 0) {
            return str.length() <= maxLength;
        } else if (maxLength < 0) {
            return str.length() >= minLength;
        } else {
            return str.length() >= minLength && str.length() <= maxLength;
        }
    }

    /**
     * 判断是否是合法的时间字符串。
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isTime(String str) {
        if (isEmpty(str) || str.length() > 8) {
            return false;
        }

        String[] items = str.split(":");

        if (items.length != 2 && items.length != 3) {
            return false;
        }

        for (int i = 0; i < items.length; i++) {
            if (items[i].length() != 2 && items[i].length() != 1) {
                return false;
            }
        }

        return !(!isNumber(items[0], 0, 23) || !isNumber(items[1], 0, 59) || (items.length == 3 && !isNumber(items[2],
                0, 59)));
    }

    /**
     * 是否是简体中文字符串。
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isSimpleChinese(String str) {
        return isRegexMatch(str, REGEX_SIMPLE_CHINESE);
    }

    /**
     * 判断字符串是否匹配了正则表达式。
     *
     * @param str   字符串
     * @param regex 正则表达式
     * @return true/false
     */
    public static boolean isRegexMatch(String str, String regex) {
        return str != null && str.length() > 0 && str.matches(regex);
    }


    /**
     * 获取指定字符的固定长度
     *
     * @param max
     * @return
     */
    public static String getFixLength(int max) {
        return String.format("[\\S\\s]{" + max + ",%d}", max);
    }


    /**
     * 获取指定判断字符最大长度的正则表达式
     *
     * @param max
     * @return
     */
    public static String getLengthMaxRegex(int max) {
        return String.format("[\\S\\s]{1,%d}", max);
    }

    public static String getLenghMinRegex(int min) {
        return String.format("[\\S\\s]{%d,}", min);
    }

    /**
     * 获取指定判断字符长度的正则表达式
     *
     * @param max
     * @return
     */
    public static String getLengthSRegex(int min, int max) {
        return String.format("[\\S\\s]{%d,%d}", min, max);
    }
}
