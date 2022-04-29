package com.ashlikun.utils.other

import android.util.Log
import com.ashlikun.utils.AppUtils
import kotlin.NullPointerException

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:08
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：项目的Log工具
 */
/**
 * inline 方法打印的行数会有问题
 */
fun log(content: Any, tr: Throwable? = null) = LogUtils.d(content, tr)
fun loge(content: Any, tr: Throwable? = null) = LogUtils.e(content, tr)
fun logw(content: Any, tr: Throwable? = null) = LogUtils.w(content, tr)
fun logi(content: Any, tr: Throwable? = null) = LogUtils.i(content, tr)
fun logd(content: Any, tr: Throwable? = null) = LogUtils.d(content, tr)
fun logv(content: Any, tr: Throwable? = null) = LogUtils.v(content, tr)
fun logwtf(content: Any, tr: Throwable? = null) = LogUtils.wtf(content, tr)

fun Any.logg(tr: Throwable? = null) = LogUtils.d(this, tr)

fun Any.logge(tr: Throwable? = null) = LogUtils.e(this, tr)

fun Any.loggw(tr: Throwable? = null) = LogUtils.w(this, tr)
fun Any.loggi(tr: Throwable? = null) = LogUtils.i(this, tr)
fun Any.loggd(tr: Throwable? = null) = LogUtils.d(this, tr)
fun Any.loggv(tr: Throwable? = null) = LogUtils.v(this, tr)
fun Any.loggwtf(tr: Throwable? = null) = LogUtils.wtf(this, tr)

object LogUtils {
    const val I = "I"
    const val E = "E"
    const val W = "W"
    const val D = "D"
    const val V = "V"

    /**
     * 调用日志的回调
     * @param type 日志类型
     * @param content 日志内容
     * @param 是否允许打印日志，true：打印，false：不打印
     */
    var call: ((type: String, tag: String, content: String) -> Boolean)? = null

    /**
     * 设置log日志的前缀
     */
    fun setCustomTagPrefix(customTagPrefix: String?) {
        var customTagPrefix = customTagPrefix
        customTagPrefix = customTagPrefix
    }

    /**
     * 得到标签,log标签+类名+方法名+第几行
     */
    private fun generateTag(): String {
        val callers = Throwable().stackTrace
        var tag = "%s.%s(L:%d)"
        //去除应用包名
        val caller = callers.find { !it.className.contains(LogUtils::class.java.simpleName) }
        var callerClazzName = caller?.className?.replace(AppUtils.packageName + ".", "") ?: "LogUtils"
        if (callerClazzName.contains(LogUtils::class.java.simpleName)) {
            //这种情况说明代码是异步执行，找不到调用的地方
            tag = "LogUtils"
        } else {
            tag = String.format(tag, callerClazzName, caller?.methodName.orEmpty(), caller?.lineNumber ?: 0)
        }
        return tag
    }

    /**
     * Log.d的输出颜色是蓝色的，仅输出debug调试的意思，但他会输出上层的信息，过滤起来可以通过DDMS的Logcat标签来选择.
     */
    fun d(content: Any?, tr: Throwable? = null) {
        if (content == null) return
        if (call?.invoke(D, generateTag(), "$content,${tr?.message.orEmpty()}") == false) return

        if (!AppUtils.isDebug) return
        val tag = generateTag()
        if (tr == null) Log.d(tag, content.toString()) else Log.d(tag, content.toString(), tr)
    }

    /**
     * Log.e为红色，可以想到error错误，这里仅显示红色的错误信息，这些错误就需要我们认真的分析，查看栈的信息了。
     */
    fun e(content: Any?, tr: Throwable? = null) {
        if (content == null) return
        if (call?.invoke(E, generateTag(), "$content,${tr?.message.orEmpty()}") == false) return
        if (!AppUtils.isDebug) return
        val tag = generateTag()
        if (tr == null) Log.e(tag, content.toString()) else Log.e(tag, content.toString(), tr)
    }


    /**
     * Log.i的输出为绿色，一般提示性的消息information，它不会输出Log.v和Log.d的信息，但会显示i、w和e的信息
     */
    fun i(content: Any?, tr: Throwable? = null) {
        if (content == null) return
        if (call?.invoke(I, generateTag(), "$content,${tr?.message.orEmpty()}") == false) return
        if (!AppUtils.isDebug) return
        val tag = generateTag()
        if (tr == null) Log.i(tag, content.toString()) else Log.i(tag, content.toString(), tr)
    }

    /**
     * Log.v 的调试颜色为黑色的，任何消息都会输出，这里的v代表verbose啰嗦的意思，平时使用就是Log.v("","");
     */
    fun v(content: Any?, tr: Throwable? = null) {
        if (content == null) return
        if (call?.invoke(V, generateTag(), "$content,${tr?.message.orEmpty()}") == false) return
        if (!AppUtils.isDebug) return
        val tag = generateTag()
        if (tr == null) Log.v(tag, content.toString()) else Log.v(tag, content.toString(), tr)
    }

    /**
     * Log.w的意思为橙色，可以看作为warning警告，一般需要我们注意优化Android代码，同时选择它后还会输出Log.e的信息。
     */
    fun w(content: Any?, tr: Throwable? = null) {
        if (content == null) return
        if (call?.invoke(W, generateTag(), "$content,${tr?.message.orEmpty()}") == false) return
        if (!AppUtils.isDebug) return
        val tag = generateTag()
        if (tr == null) Log.w(tag, content.toString()) else Log.w(tag, content.toString(), tr)
    }

    fun w(tr: Throwable) = w("", tr)
    fun wtf(content: Any?, tr: Throwable? = null) {
        if (content == null) return
        if (call?.invoke(W, generateTag(), "$content,${tr?.message.orEmpty()}") == false) return
        if (!AppUtils.isDebug) return
        val tag = generateTag()
        if (tr == null) Log.wtf(tag, content.toString()) else Log.wtf(tag, content.toString(), tr)
    }

    fun wtf(tr: Throwable) = wtf("", tr)
}