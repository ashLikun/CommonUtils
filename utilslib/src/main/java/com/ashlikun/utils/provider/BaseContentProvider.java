package com.ashlikun.utils.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/30 0030　下午 3:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：项目中的内容提供者，
 * 一般用于主进程提供数据给其他进程,
 * 或者其他进程写入数据给主进程
 * 在清单文件声明了 android:exported="false"，不允许其他应用访问，只能当前应用访问，Provider
 */
public class BaseContentProvider extends ContentProvider {
    /**
     * 构建url
     */
    public static final String CONTENT = "content://";
    public static  String AUTHORITY = "";
    public static final String SEPARATOR = "/";
    public static final String CONTENT_URI = CONTENT + AUTHORITY;

    /**
     * sp处理
     */
    public static final String HANDLE_SP = "handle_sp";
    /**
     * 处理这种类型的Provider
     */
    IContentProvider provider;

    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * 获取数据
     * sp: path: handle_sp/type/name/key/defaultValue---->1:handle_sp
     *
     * @param uri content://包名/path/path...
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        createProvider(uri);
        if (provider != null) {
            return provider.query(getContext(), uri);
        }
        return null;
    }

    /**
     * 该方法用于返回当前Url所代表数据的MIME类型。
     *
     * @param uri
     * @return
     */
    @Override
    public String getType(@NonNull Uri uri) {
        createProvider(uri);
        if (provider != null) {
            return provider.getType(getContext(), uri);
        }
        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        createProvider(uri);
        if (provider != null) {
            return provider.insert(getContext(), uri, values);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        createProvider(uri);
        if (provider != null) {
            return provider.delete(getContext(), uri);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return insert(uri, values) != null ? 1 : 0;
    }

    private void createProvider(Uri uri) {
        String[] path = uri.getPath().split(SEPARATOR);
        String handle = path[1];
        //根据不同类型实现不同处理器
        if (HANDLE_SP.equals(handle)) {
            provider = new ImpSpProvider();
        }
    }

}
