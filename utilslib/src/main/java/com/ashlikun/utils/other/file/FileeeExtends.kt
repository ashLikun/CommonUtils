package com.ashlikun.utils.other.file

import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * 作者　　: 李坤
 * 创建时间: 2021/12/12　16:01
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：文件扩展方法相关
 */
/**
 * is 转 bytes
 */
inline fun InputStream.toByte() = FileIOUtils.is2Bytes(this)
inline fun String.toFileByte() = FileIOUtils.is2Bytes(FileInputStream(this))

/**
 * 删除文件或者目录
 */
inline fun File.deletes() = FileUtils.deleteFile(this)
inline fun String.deletes() = FileUtils.deleteFile(this)

/**
 * 获取指定文件夹内所有文件大小的和
 */
inline fun File.fileSizes() = FileUtils.getFileSizes(this)
inline fun String.fileSizes() = FileUtils.getFileSizes(File(this))

/**
 * 单文件大小
 */
inline fun File.fileSize() = FileUtils.getFileSize(this)
inline fun String.fileSize() = FileUtils.getFileSize(File(this))

/**
 * 复制
 */
inline fun File.copyFile(destPath: File, deleteSrc: Boolean = false) = FileIOUtils.copyFile(
    this, destPath, deleteSrc
)

/**
 * 复制
 */
inline fun String.copyFile(destPath: String, deleteSrc: Boolean = false) = FileIOUtils.copyFile(
    this, destPath, deleteSrc
)

/**
 * 从输入流中写入文件
 */
inline fun InputStream.writeFile(filePath: String, append: Boolean = false) =
    FileIOUtils.writeFileFromIS(
        filePath, this, append
    )

inline fun InputStream.writeFile(file: File, append: Boolean = false) =
    FileIOUtils.writeFileFromIS(
        file, this, append
    )

inline fun File.writeFile(ins: InputStream, append: Boolean = false) =
    FileIOUtils.writeFileFromIS(
        this, ins, append
    )

/**
 * 从输入流中写入文件
 */
inline fun ByteArray.writeFile(filePath: String, append: Boolean = false) =
    FileIOUtils.writeFileFromBytesByStream(
        File(filePath), this, append
    )

inline fun ByteArray.writeFile(file: File, append: Boolean = false) =
    FileIOUtils.writeFileFromBytesByStream(
        file, this, append
    )

inline fun File.writeFile(bytes: ByteArray, append: Boolean = false) =
    FileIOUtils.writeFileFromBytesByStream(
        this, bytes, append
    )

/**
 * 从字符串中写入文件
 */
inline fun String.writeFile(filePath: String, append: Boolean = false) =
    FileIOUtils.writeFileFromString(
        filePath, this, append
    )

inline fun String.writeFile(file: File, append: Boolean = false) =
    FileIOUtils.writeFileFromString(
        file, this, append
    )

inline fun File.writeFile(content: String, append: Boolean = false) =
    FileIOUtils.writeFileFromString(
        this, content, append
    )

/**
 * 返回文件中的行
 */
inline fun File.read2List(charsetName: String = "", st: Int = 0, end: Int = 0x7FFFFFFF) =
    FileIOUtils.readFile2List(
        this, charsetName, st, end
    )

inline fun File.read2String(charsetName: String = "") =
    FileIOUtils.readFile2String(
        this, charsetName
    )