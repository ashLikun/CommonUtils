package com.ashlikun.utils.provider.mode;

import android.net.Uri;

import com.ashlikun.utils.provider.BaseContentProvider;
import com.ashlikun.utils.provider.ImpSpProvider;

import java.util.Set;


/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/1 0001　上午 10:04
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：封装的Sp  Url模型
 */
public class SpMode {
    public Uri uri;
    public String handle;
    public String type;
    public String name;
    public String key;
    public boolean isException = false;

    /**
     * 初始化一个Mode
     */
    public SpMode(Uri uri) {
        this.uri = uri;
        //分解url,按照'/'
        String[] path = uri.getPath().split(BaseContentProvider.SEPARATOR);
        try {
            handle = path[1];
            type = path[2];
            name = path[3];
            key = path[4];
        } catch (Exception e) {
            isException = true;
        }

    }

    /**
     * 初始化一个可以解析的Mode
     *
     * @param name sp名称
     * @param key  sp 键
     * @param type 值得类型
     */
    public SpMode(String name, String key, Class type) {
        String typeStr = "Object";
        if (type.isAssignableFrom(String.class)) {
            typeStr = ImpSpProvider.TYPE_STRING;
        } else if (type.isAssignableFrom(Integer.class)) {
            typeStr = ImpSpProvider.TYPE_INT;
        } else if (type.isAssignableFrom(Boolean.class)) {
            typeStr = ImpSpProvider.TYPE_BOOLEAN;
        } else if (type.isAssignableFrom(Float.class)) {
            typeStr = ImpSpProvider.TYPE_FLOAT;
        } else if (type.isAssignableFrom(Long.class)) {
            typeStr = ImpSpProvider.TYPE_LONG;
        } else if (Set.class.isAssignableFrom(type)) {
            typeStr = ImpSpProvider.TYPE_STRING_SET;
        }
        createUrl(name, key, typeStr);
    }

    /**
     * 初始化一个可以解析的Mode
     *
     * @param name sp名称
     * @param key  sp 键
     * @param type 值得类型
     */
    public SpMode(String name, String key, String type) {
        createUrl(name, key, type);
    }

    private void createUrl(String name, String key, String type) {
        // path: sp/type/name/key/defaultValue---->1:sp
        this.handle = BaseContentProvider.HANDLE_SP;
        this.type = type;
        this.name = name;
        this.key = key;
        StringBuilder sb = new StringBuilder(BaseContentProvider.CONTENT_URI);
        //sp
        sb.append(BaseContentProvider.SEPARATOR);
        sb.append(BaseContentProvider.HANDLE_SP);
        //添加type
        sb.append(BaseContentProvider.SEPARATOR);
        sb.append(type);
        //添加name
        sb.append(BaseContentProvider.SEPARATOR);
        sb.append(name);
        //添加key
        sb.append(BaseContentProvider.SEPARATOR);
        sb.append(key);
        uri = Uri.parse(sb.toString());
    }
}
