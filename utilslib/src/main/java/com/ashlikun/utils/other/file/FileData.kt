package com.ashlikun.utils.other.file

import android.net.Uri
import androidx.core.net.toFile
import java.io.File
import java.io.Serializable

/**
 * 作者　　: 李坤
 * 创建时间: 2022/11/17　20:07
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */


data class FileData(val name: String, val size: Long, val path: String?, val mimeType: String, val uri: Uri? = null) : Serializable {
    val file: File? by lazy {
        if (!path.isNullOrEmpty()) File(path) else runCatching { uri?.toFile() }.getOrNull()
    }
}