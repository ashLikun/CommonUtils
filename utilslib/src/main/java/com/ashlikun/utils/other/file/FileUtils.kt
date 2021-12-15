package com.ashlikun.utils.other.file

import java.io.File
import android.content.Context
import android.os.Bundle
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import java.io.InputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.io.FileInputStream
import java.io.FileNotFoundException
import android.util.Log
import com.ashlikun.utils.AppUtils
import java.text.DecimalFormat
import kotlin.Throws
import kotlin.jvm.JvmOverloads

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:00
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：对文件操作的类
 */

object FileUtils {

    const val SIZETYPE_B = 1 // 获取文件大小单位为B的double值
    const val SIZETYPE_KB = 2 // 获取文件大小单位为KB的double值
    const val SIZETYPE_MB = 3 // 获取文件大小单位为MB的double值
    const val SIZETYPE_GB = 4 // 获取文件大小单位为GB的double值

    /**
     * 创建目录
     */
    fun createMkdirs(path: String): String {
        val file = File(path)
        if (file.exists() || file.mkdirs()) {
        }
        return path
    }

    /**
     * 获取清单文件的mate值
     */
    fun getMetaValue(metaKey: String): String {
        var metaData: Bundle? = null
        var apiKey: String? = null
        try {
            val ai = AppUtils.app.packageManager
                .getApplicationInfo(
                    AppUtils.app.packageName,
                    PackageManager.GET_META_DATA
                )
            if (null != ai) {
                metaData = ai.metaData
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey)
            }
        } catch (e: NameNotFoundException) {
        }
        return apiKey ?: ""
    }

    /**
     * 读取输入流
     */
    fun readByte(ins: InputStream) = FileIOUtils.is2Bytes(ins)

    /**
     * 判断文件夹是否为空
     */
    fun isEmptyFolder(path: String): Boolean {
        val file = File(path)
        if (file.exists() && file.isDirectory) {
            if (file.list().isNotEmpty()) {
                return false
            }
        }
        return true
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    fun getFileOrFilesSize(filePath: String, sizeType: Int = SIZETYPE_B): Double {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = if (file.isDirectory) {
                getFileSizes(file)
            } else {
                getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("获取文件大小", "获取失败!")
        }
        return formetFileSize(blockSize, sizeType)
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    fun getAutoFileOrFilesSize(filePath: String): String {
        val file = File(filePath)
        var blockSize: Long = 0
        try {
            blockSize = if (file.isDirectory) {
                getFileSizes(file)
            } else {
                getFileSize(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("获取文件大小", "获取失败!")
        }
        return autoFormetFileSize(blockSize.toDouble())
    }

    /**
     * 获取指定文件大小
     */
    fun getFileSize(file: File): Long {
        var size: Long = 0
        try {
            if (file.exists()) {
                var fis = FileInputStream(file)
                size = fis.available().toLong()
                fis.close()
            } else {
                Log.e("获取文件大小", "文件不存在!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     */
    fun getFileSizes(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList) {
                size = if (aFileList.isDirectory) {
                    size + getFileSizes(aFileList)
                } else {
                    size + aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 转换文件大小,指定转换的类型
     */
    fun formetFileSize(fileS: Long, sizeType: Int = SIZETYPE_B): Double {
        val df = DecimalFormat("0.00")
        var fileSizeLong = 0.0
        when (sizeType) {
            SIZETYPE_B -> fileSizeLong = java.lang.Double.valueOf(
                df.format(
                    fileS.toDouble()
                )
            )
            SIZETYPE_KB -> fileSizeLong = java.lang.Double.valueOf(
                df.format(
                    fileS.toDouble() / 1024
                )
            )
            SIZETYPE_MB -> fileSizeLong = java.lang.Double.valueOf(
                df.format(
                    fileS.toDouble() / 1048576
                )
            )
            SIZETYPE_GB -> fileSizeLong = java.lang.Double.valueOf(
                df
                    .format(fileS.toDouble() / 1073741824)
            )
            else -> {}
        }
        return fileSizeLong
    }

    /**
     * 自动格式化单位
     *
     * @param size size
     * @return size
     */
    fun autoFormetFileSize(size: Double): String {
        //# 一个数字，不包括 0 , 0 一个数字
        val df = DecimalFormat("0.#")
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return df.format(size) + "Byte"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            return df.format(kiloByte) + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            return df.format(megaByte) + "MB"
        }
        val teraBytes = gigaByte / 1024
        return if (teraBytes < 1) {
            df.format(gigaByte) + "GB"
        } else df.format(teraBytes) + "TB"
    }

    /**
     * 删除文件或者目录
     */
    fun deleteFile(file: File) {
        if (!file.exists()) {
            false
        } else {
            if (file.isFile) {
                file.delete()
            }
            if (file.isDirectory) {
                val childFile = file.listFiles()
                if (childFile.isNullOrEmpty()) {
                    file.delete()
                }
                for (f in childFile) {
                    deleteFile(f)
                }
                file.delete()
            }
        }
    }

    /**
     * 删除文件或者目录
     */
    fun deleteFile(strFile: String) {
        deleteFile(File(strFile))
    }
}