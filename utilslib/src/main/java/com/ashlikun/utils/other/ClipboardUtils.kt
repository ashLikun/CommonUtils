package com.ashlikun.utils.other

import android.content.ClipData
import android.content.ClipData.Item
import android.content.ClipboardManager
import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.content.Context
import com.ashlikun.utils.AppUtils.app

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 10:21
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：剪贴板相关工具类
 */

object ClipboardUtils {
    /**
     * 复制文本到剪贴板
     */
    fun copyText(text: CharSequence) =
        (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
            ClipData.newPlainText(null, text)
        )

    /**
     * 复制文本到剪贴板
     */
    fun copyText(text: CharSequence, label: CharSequence? = null) =
        (app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
            ClipData.newPlainText(label, text)
        )

    /**
     * 获取剪贴板 最新的文本
     */
    val text: CharSequence
        get() {
            val cm = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = cm.primaryClip
            return if (clip != null && clip.itemCount > 0) {
                clip.getItemAt(0).coerceToText(app) ?: ""
            } else ""
        }

    /**
     * 获取剪贴板 最新的文本
     */
    val texts: List<CharSequence>
        get() {
            val cm = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = cm.primaryClip
            val result = mutableListOf<CharSequence>()
            if (clip != null && clip.itemCount > 0) {
                for (i in 0 until clip.itemCount) {
                    result[i] = clip.getItemAt(i).coerceToText(app) ?: ""
                }
            }
            return result
        }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本 原始对象
     */
    val clipItems: List<Item>
        get() {
            val cm = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = cm.primaryClip
            val result = mutableListOf<Item>()
            if (clip != null && clip.itemCount > 0) {
                for (i in 0 until clip.itemCount) {
                    result[i] = clip.getItemAt(0)
                }
            }
            return result
        }

    /**
     * 添加剪贴板数据改变监听器
     */
    fun addChangedListener(changedListener: OnPrimaryClipChangedListener) {
        // 获取系统剪贴板
        val cm = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 添加剪贴板数据改变监听器
        cm.addPrimaryClipChangedListener(changedListener)
    }

    /**
     * 移除剪贴板数据改变监听器
     */
    fun removeChangedListener(changedListener: OnPrimaryClipChangedListener) {
        // 获取系统剪贴板
        val cm = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 添加剪贴板数据改变监听器
        cm.removePrimaryClipChangedListener(changedListener)
    }
}