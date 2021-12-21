package com.ashlikun.utils.other

import android.util.Log
import com.ashlikun.utils.AppUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:08
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：项目的Log工具
 */
inline fun log(content: Any, tr: Throwable? = null) = LogUtils.d(content, tr)
inline fun loge(content: Any, tr: Throwable? = null) = LogUtils.e(content, tr)
inline fun logw(content: Any, tr: Throwable? = null) = LogUtils.w(content, tr)
inline fun logi(content: Any, tr: Throwable? = null) = LogUtils.i(content, tr)
inline fun logd(content: Any, tr: Throwable? = null) = LogUtils.d(content, tr)
inline fun logv(content: Any, tr: Throwable? = null) = LogUtils.v(content, tr)
inline fun logwtf(content: Any, tr: Throwable? = null) = LogUtils.wtf(content, tr)

inline fun Any.logg(tr: Throwable? = null) = LogUtils.d(this, tr)
inline fun Any.logge(tr: Throwable? = null) = LogUtils.e(this, tr)
inline fun Any.loggw(tr: Throwable? = null) = LogUtils.w(this, tr)
inline fun Any.loggi(tr: Throwable? = null) = LogUtils.i(this, tr)
inline fun Any.loggd(tr: Throwable? = null) = LogUtils.d(this, tr)
inline fun Any.loggv(tr: Throwable? = null) = LogUtils.v(this, tr)
inline fun Any.loggwtf(tr: Throwable? = null) = LogUtils.wtf(this, tr)

object LogUtils {


    /**
     * 设置log日志的前缀
     *
     * @param customTagPrefix
     */
    fun setCustomTagPrefix(customTagPrefix: String?) {
        var customTagPrefix = customTagPrefix
        customTagPrefix = customTagPrefix
    }

    /**
     * 得到标签,log标签+类名+方法名+第几行
     *
     * @return
     */
    private fun generateTag(): String {
        val caller = Throwable().stackTrace[2]
        var tag = "%s.%s(L:%d)"
        var callerClazzName = caller.className
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1)
        tag = String.format(tag, callerClazzName, caller.methodName, caller.lineNumber)
        return tag
    }

    /**
     * Log.d的输出颜色是蓝色的，仅输出debug调试的意思，但他会输出上层的信息，过滤起来可以通过DDMS的Logcat标签来选择.
     *
     * @param content
     */
    fun d(content: Any?, tr: Throwable? = null) {
        if (!AppUtils.isDebug) return
        if (content == null) return
        val tag = generateTag()
        if (tr == null) Log.d(tag, content.toString()) else Log.d(tag, content.toString(), tr)
    }

    /**
     * Log.e为红色，可以想到error错误，这里仅显示红色的错误信息，这些错误就需要我们认真的分析，查看栈的信息了。
     *
     * @param content
     */

    fun e(content: Any?, tr: Throwable? = null) {
        if (!AppUtils.isDebug) return
        if (content == null) return
        val tag = generateTag()
        if (tr == null) Log.e(tag, content.toString()) else Log.e(tag, content.toString(), tr)
    }


    /**
     * Log.i的输出为绿色，一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息
     *
     * @param content
     */

    fun i(content: Any?, tr: Throwable? = null) {
        if (!AppUtils.isDebug) return
        if (content == null) return
        val tag = generateTag()
        if (tr == null) Log.i(tag, content.toString()) else Log.i(tag, content.toString(), tr)
    }

    /**
     * Log.v 的调试颜色为黑色的，任何消息都会输出，这里的v代表verbose啰嗦的意思，平时使用就是Log.v("","");
     *
     * @param content
     */
    fun v(content: Any?, tr: Throwable? = null) {
        if (!AppUtils.isDebug) return
        if (content == null) return
        val tag = generateTag()
        if (tr == null) Log.v(tag, content.toString()) else Log.v(tag, content.toString(), tr)
    }

    /**
     * Log.w的意思为橙色，可以看作为warning警告，一般需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息。
     *
     * @param content
     */

    fun w(content: Any?, tr: Throwable? = null) {
        if (!AppUtils.isDebug) return
        if (content == null) return
        val tag = generateTag()
        if (tr == null) Log.w(tag, content.toString()) else Log.w(tag, content.toString(), tr)
    }

    fun w(tr: Throwable) = w("", tr)

    fun wtf(content: Any?, tr: Throwable? = null) {
        if (!AppUtils.isDebug) return
        if (content == null) return
        val tag = generateTag()
        if (tr == null) Log.wtf(tag, content.toString()) else Log.wtf(tag, content.toString(), tr)
    }

    fun wtf(tr: Throwable) = LogUtils.wtf("", tr)
}