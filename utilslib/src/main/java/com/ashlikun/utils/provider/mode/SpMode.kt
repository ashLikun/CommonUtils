package com.ashlikun.utils.provider.mode

import android.net.Uri
import com.ashlikun.utils.provider.BaseContentProvider
import com.ashlikun.utils.provider.ImpSpProvider
import kotlin.reflect.KClass

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 18:24
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：封装的Sp  Url模型
 */

class SpMode {
    lateinit var uri: Uri
    lateinit var handle: String
    lateinit var type: String
    lateinit var name: String
    lateinit var key: String
    var isException = false
    private fun createUrl(name: String, key: String, type: String) {
        // path: sp/type/name/key/defaultValue---->1:sp
        handle = BaseContentProvider.HANDLE_SP
        this.type = type
        this.name = name
        this.key = key
        val sb = StringBuilder(BaseContentProvider.CONTENT_URI)
        //sp
        sb.append(BaseContentProvider.SEPARATOR)
        sb.append(BaseContentProvider.HANDLE_SP)
        //添加type
        sb.append(BaseContentProvider.SEPARATOR)
        sb.append(type)
        //添加name
        sb.append(BaseContentProvider.SEPARATOR)
        sb.append(name)
        //添加key
        sb.append(BaseContentProvider.SEPARATOR)
        sb.append(key)
        uri = Uri.parse(sb.toString())
    }

    /**
     * 初始化一个Mode
     */
    constructor(uri: Uri) {
        this.uri = uri
        //分解url,按照'/'
        val path = uri.path?.split(BaseContentProvider.SEPARATOR)
        try {
            handle = path?.getOrNull(1) ?: ""
            type = path?.getOrNull(2) ?: ""
            name = path?.getOrNull(3) ?: ""
            key = path?.getOrNull(4) ?: ""
        } catch (e: Exception) {
            isException = true
        }
    }

    /**
     * 初始化一个可以解析的Mode
     *
     * @param name sp名称
     * @param key  sp 键
     * @param type 值得类型
     */
    constructor(name: String, key: String, type: KClass<*>) {
        var typeStr = "Object"
        when (type) {
            String::class -> typeStr = ImpSpProvider.TYPE_STRING
            Int::class -> typeStr = ImpSpProvider.TYPE_INT
            Boolean::class -> typeStr = ImpSpProvider.TYPE_BOOLEAN
            Float::class -> typeStr = ImpSpProvider.TYPE_FLOAT
            Long::class -> typeStr = ImpSpProvider.TYPE_LONG
            MutableSet::class -> typeStr = ImpSpProvider.TYPE_STRING_SET
        }
        createUrl(name, key, typeStr)
    }

    /**
     * 初始化一个可以解析的Mode
     *
     * @param name sp名称
     * @param key  sp 键
     * @param type 值得类型
     */
    constructor(name: String, key: String, type: String) {
        createUrl(name, key, type)
    }


}