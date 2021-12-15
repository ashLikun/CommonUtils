package com.ashlikun.utils.ui

import com.ashlikun.utils.AppUtils.app
import com.ashlikun.utils.AppUtils.versionName
import com.ashlikun.utils.other.StringUtils.isEmpty
import java.lang.StringBuilder
import android.webkit.WebView
import android.webkit.WebSettings
import android.os.Build
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.ui.WebViewUtils
import android.annotation.TargetApi
import java.util.regex.Matcher
import java.lang.Exception
import com.ashlikun.utils.other.StringUtils
import java.util.regex.Pattern

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.15 15:49
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：WebView的一些工具
 * view.loadUrl("javascript:window.local_obj.showSource('<body>'+" +
 * "document.getElementById('content').innerHTML+'</body>');");
 */

object WebViewUtils {
    /**
     * 字体
     */
    val font: String
        get() {
            val builder = StringBuilder()
            // <body class="MsoNormal"> </body>
            builder.append(".Likun { font-family: fzlt;}")
            builder.append("@font-face {font-family:fzlt;")
            builder.append("src:url(file:///android_asset/fzlt.ttf);}")
            return builder.toString()
        }

    /**
     * 配置webview
     */
    fun configWebview(webView: WebView) {
        val webSettings = webView.settings
        // 设置文本编码
        webSettings.defaultTextEncodingName = "utf-8"
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        //允许js代码
        webSettings.javaScriptEnabled = true
        //允许SessionStorage/LocalStorage存储
        webSettings.domStorageEnabled = true
        //禁用放缩
        webSettings.displayZoomControls = false
        webSettings.builtInZoomControls = false
        //禁用文字缩放
        webSettings.textZoom = 100
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize((10 * 1024 * 1024).toLong())
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true)
        webSettings.setAppCachePath(app.getDir("appcache", 0).path)
        //允许WebView使用File协议
        webSettings.allowFileAccess = true
        //不保存密码
        webSettings.savePassword = false
        //设置UA
        webSettings.userAgentString = webSettings.userAgentString + " androidApp/" + versionName
        //移除部分系统JavaScript接口
        removeJavascriptInterfaces(webView)
        //自动加载图片
        webSettings.loadsImagesAutomatically = true
    }

    /**
     * 安全措施
     */
    private fun removeJavascriptInterfaces(webView: WebView) {
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_")
                webView.removeJavascriptInterface("accessibility")
                webView.removeJavascriptInterface("accessibilityTraversal")
            }
        } catch (tr: Throwable) {
            tr.printStackTrace()
        }
    }

    /**
     * html 获取纯文本
     */
    fun html2Text(inputString: String): String {
        if (inputString.trim { it <= ' ' } == "") {
            return ""
        }
        var htmlStr: String = inputString // 含html标签的字符串
        var textStr = ""
        val p_script: Pattern
        val m_script: Matcher
        val p_style: Pattern
        val m_style: Matcher
        val p_html: Pattern
        val m_html: Matcher
        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            val regEx_script =
                "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            val regEx_style =
                "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"
            val regEx_html = "<[^>]+>" // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE)
            m_script = p_script.matcher(htmlStr)
            htmlStr = m_script.replaceAll("") // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE)
            m_style = p_style.matcher(htmlStr)
            htmlStr = m_style.replaceAll("") // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE)
            m_html = p_html.matcher(htmlStr)
            htmlStr = m_html.replaceAll("") // 过滤html标签
            textStr = htmlStr
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
        return textStr // 返回文本字符串
    }

    /**
     * 获得超链接
     */
    fun getHrefInnerHtml(href: String): String {
        if (isEmpty(href)) {
            return ""
        }
        val hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*"
        val hrefPattern = Pattern
            .compile(hrefReg, Pattern.CASE_INSENSITIVE)
        val hrefMatcher = hrefPattern.matcher(href)
        return if (hrefMatcher.matches()) hrefMatcher.group(1) else href
    }
}