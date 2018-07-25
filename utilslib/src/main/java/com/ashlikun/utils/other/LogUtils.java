package com.ashlikun.utils.other;

import android.util.Log;

import static com.ashlikun.utils.AppUtils.isDebug;


/**
 * 作者　　: 李坤
 * 创建时间: 13:47 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：项目的Log工具
 */

public class LogUtils {


    private LogUtils() {
    }

    /**
     * 设置是否开启debug模式
     *
     * @param isDebug
     */
    public static void setIsDebug(boolean isDebug) {
        isDebug = isDebug;
    }

    /**
     * 设置log日志的前缀
     *
     * @param customTagPrefix
     */
    public static void setCustomTagPrefix(String customTagPrefix) {
        customTagPrefix = customTagPrefix;
    }

    /**
     * 得到标签,log标签+类名+方法名+第几行
     *
     * @return
     */
    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        return tag;
    }


    /**
     * Log.d的输出颜色是蓝色的，仅输出debug调试的意思，但他会输出上层的信息，过滤起来可以通过DDMS的Logcat标签来选择.
     *
     * @param content
     */
    public static void d(String content) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.d(tag, content, tr);
    }

    /**
     * Log.e为红色，可以想到error错误，这里仅显示红色的错误信息，这些错误就需要我们认真的分析，查看栈的信息了。
     *
     * @param content
     */
    public static void e(String content) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.e(tag, content, tr);
    }


    public static void e(Number content) {
        if (!isDebug()) return;
        String tag = generateTag();
        Log.e(tag, String.valueOf(content));
    }

    public static void e(boolean content) {
        if (!isDebug()) return;
        String tag = generateTag();
        Log.e(tag, String.valueOf(content));
    }

    /**
     * Log.i的输出为绿色，一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息
     *
     * @param content
     */
    public static void i(String content) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.i(tag, content, tr);
    }

    /**
     * Log.v 的调试颜色为黑色的，任何消息都会输出，这里的v代表verbose啰嗦的意思，平时使用就是Log.v("","");
     *
     * @param content
     */
    public static void v(String content) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.v(tag, content, tr);
    }

    /**
     * Log.w的意思为橙色，可以看作为warning警告，一般需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息。
     *
     * @param content
     */
    public static void w(String content) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.w(tag, tr);
    }


    public static void wtf(String content) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.wtf(tag, content);
    }

    public static void wtf(String content, Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.wtf(tag, content, tr);
    }

    public static void wtf(Throwable tr) {
        if (!isDebug()) return;
        String tag = generateTag();

        Log.wtf(tag, tr);
    }
}
