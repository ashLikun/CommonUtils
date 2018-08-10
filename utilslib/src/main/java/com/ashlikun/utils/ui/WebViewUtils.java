package com.ashlikun.utils.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2016/1/20.
 * <p>
 * view.loadUrl("javascript:window.local_obj.showSource('<body>'+" +
 * "document.getElementById('content').innerHTML+'</body>');");
 */
public class WebViewUtils {

    public static String getFont() {
        StringBuilder builder = new StringBuilder();
        // <body class="MsoNormal"> </body>
        builder.append(".Likun { font-family: fzlt;}");
        builder.append("@font-face {font-family:fzlt;");
        builder.append("src:url(file:///android_asset/fzlt.ttf);}");
        return builder.toString();
    }

    /**
     * 配置webview
     *
     * @param webView
     */
    public static void configWebview(WebView webView) {

        WebSettings webSettings = webView.getSettings();
        // 设置文本编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(AppUtils.getApp().getDir("appcache", 0).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //设置UA
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " androidApp/" + AppUtils.getVersionName());
        //移除部分系统JavaScript接口
        removeJavascriptInterfaces(webView);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
    }

    /**
     * 安全措施
     *
     * @param webView
     */
    @TargetApi(11)
    private static final void removeJavascriptInterfaces(WebView webView) {
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.removeJavascriptInterface("accessibility");
                webView.removeJavascriptInterface("accessibilityTraversal");
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

    /**
     * html 获取纯文本
     */
    public static String html2Text(String inputString) {
        if (inputString == null || inputString.trim().equals("")) {
            return "";
        }

        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return textStr;// 返回文本字符串
    }

    /*
     * 获得超链接
     */
    public static String getHrefInnerHtml(String href) {
        if (StringUtils.isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern
                .compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }
}
