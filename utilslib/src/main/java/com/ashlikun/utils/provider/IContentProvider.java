package com.ashlikun.utils.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/1 0001　上午 10:36
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：处理ContentProvider的接口，不同的处理只需实现当前接口即可
 */
public interface IContentProvider {
    /**
     * 查找数据
     *
     * @param context
     * @param uri
     * @return
     */
    Cursor query(Context context, Uri uri);

    /**
     * 插入数据
     *
     * @param context
     * @param uri
     * @param values
     * @return
     */
    Uri insert(Context context, Uri uri, ContentValues values);

    /**
     * 删除数据
     *
     * @param context
     * @param uri
     * @return
     */
    int delete(Context context, Uri uri);

    String getType(Context context, Uri uri);
}
