package com.ashlikun.utils.ui;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

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

    @SuppressLint("SetJavaScriptEnabled")
    public static void configWebview(WebView webView) {
        // 设置文本编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        // 可以缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        // webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);// 默认缩放模式
        webView.getSettings().setUseWideViewPort(false);
    }


    /*
   * html 获取纯文本
   */
    public static String Html2Text(String inputString) {
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
