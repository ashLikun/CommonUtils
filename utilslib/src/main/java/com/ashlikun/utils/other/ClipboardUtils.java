package com.ashlikun.utils.other;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.ashlikun.utils.AppUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/19　11:14
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：剪贴板相关工具类
 */
public class ClipboardUtils {
    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    public static void copyText(CharSequence text) {
        ClipboardManager cm = (ClipboardManager) AppUtils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newPlainText(null, text));
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    public static void copyText(CharSequence label, CharSequence text) {
        ClipboardManager cm = (ClipboardManager) AppUtils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newPlainText(null, text));
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence getText() {
        ClipboardManager cm = (ClipboardManager) AppUtils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(AppUtils.getApp());
        }
        return null;
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence[] getTexts() {
        ClipboardManager cm = (ClipboardManager) AppUtils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipData clip = cm.getPrimaryClip();
        CharSequence[] result = null;
        if (clip != null && clip.getItemCount() > 0) {
            result = new CharSequence[clip.getItemCount()];
            for (int i = 0; i < clip.getItemCount(); i++) {
                result[i] = clip.getItemAt(i).coerceToText(AppUtils.getApp());
            }
        }
        return result;
    }
    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence[] getTexts() {
        ClipboardManager cm = (ClipboardManager) AppUtils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipData clip = cm.getPrimaryClip();
        CharSequence[] result = null;
        if (clip != null && clip.getItemCount() > 0) {
            result = new CharSequence[clip.getItemCount()];
            for (int i = 0; i < clip.getItemCount(); i++) {
                result[i] = clip.getItemAt(0);
            }
        }
        return result;
    }
}
