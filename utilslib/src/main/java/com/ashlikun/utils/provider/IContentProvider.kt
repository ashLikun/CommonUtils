package com.ashlikun.utils.provider

import android.content.Context
import android.net.Uri
import android.database.Cursor
import android.content.ContentValues

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/1 0001　上午 10:36
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：处理ContentProvider的接口，不同的处理只需实现当前接口即可
 */
interface IContentProvider {
    /**
     * 查找数据
     *
     * @param context
     * @param uri
     * @return
     */
    fun query(uri: Uri): Cursor?

    /**
     * 插入数据
     *
     * @param context
     * @param uri
     * @param values
     * @return
     */
    fun insert(uri: Uri, values: ContentValues?): Uri?

    /**
     * 删除数据
     *
     * @param context
     * @param uri
     * @return
     */
    fun delete(uri: Uri): Int
    fun getType(uri: Uri): String?
}