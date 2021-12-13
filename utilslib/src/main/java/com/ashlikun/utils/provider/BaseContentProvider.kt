package com.ashlikun.utils.provider

import android.content.ContentProvider
import com.ashlikun.utils.provider.IContentProvider
import android.net.Uri
import android.database.Cursor
import android.content.ContentValues
import com.ashlikun.utils.provider.BaseContentProvider
import com.ashlikun.utils.provider.ImpSpProvider

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 22:47
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：项目中的内容提供者，
 * 一般用于主进程提供数据给其他进程,
 * 或者其他进程写入数据给主进程
 * 在清单文件声明了 android:exported="false"，不允许其他应用访问，只能当前应用访问，Provider
 */

class BaseContentProvider : ContentProvider() {

    companion object {
        /**
         * 构建url
         */
        const val CONTENT = "content://"
        var AUTHORITY = ""
        const val SEPARATOR = "/"
        val CONTENT_URI = CONTENT + AUTHORITY

        /**
         * sp处理
         */
        const val HANDLE_SP = "handle_sp"
    }

    /**
     * 处理这种类型的Provider
     */
    var provider: IContentProvider? = null
    override fun onCreate(): Boolean {
        return true
    }

    /**
     * 获取数据
     * sp: path: handle_sp/type/name/key/defaultValue---->1:handle_sp
     *
     * @param uri content://包名/path/path...
     */
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        createProvider(uri)
        return provider?.query(uri)
    }

    /**
     * 该方法用于返回当前Url所代表数据的MIME类型。
     *
     * @param uri
     * @return
     */
    override fun getType(uri: Uri): String? {
        createProvider(uri)
        return provider?.getType(uri)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        createProvider(uri)
        return provider?.insert(uri, values)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        createProvider(uri)
        return provider?.delete(uri) ?: 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return if (insert(uri, values) != null) 1 else 0
    }

    private fun createProvider(uri: Uri) {
        val path = uri.path?.split(SEPARATOR)?.toTypedArray()
        if (path != null) {
            val handle = path[1]
            //根据不同类型实现不同处理器
            if (HANDLE_SP == handle) {
                provider = ImpSpProvider()
            }
        }
    }

}