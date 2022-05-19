package com.ashlikun.utils.other.file

import android.graphics.Bitmap
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.StringUtils
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 15:18
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：文件读写
 */

object FileIOUtils {
    /**
     * 默认读取的缓存大小
     */
    var bufferSize = 8192

    /**
     * 读取assets文件目录下的文件，以字符串返回
     */
    fun readAssets(name: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream = AppUtils.resources.assets.open(name)
            return is2Bytes(inputStream).toString()
        } catch (e: Exception) {
        } finally {
            close(inputStream)
        }
        return ""
    }

    /**
     * 把Bitmap写入文件
     *
     * @param bm       数据流
     * @param file     文件
     * @param recreate 如果文件存在，是否需要删除重建
     * @return 是否写入成功
     */
    fun writeImage(
        bm: Bitmap?,
        file: File,
        quality: Int = 100,
        recreate: Boolean = true
    ): Boolean {
        if (bm == null) return false
        var res = false
        try {
            if (recreate && file.exists()) {
                file.delete()
            }
            if (createOrExistsFile(file)) {
                val bos = BufferedOutputStream(FileOutputStream(file))
                //写入缓存
                if (file != null && (file.path.endsWith(".png") || file.path.endsWith(".PNG"))) {
                    bm.compress(Bitmap.CompressFormat.PNG, quality, bos)
                } else {
                    bm.compress(Bitmap.CompressFormat.JPEG, quality, bos)
                }
                bos.flush()
                bos.close()
                res = true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return res
    }

    /**
     * 把键值对写入文件
     *
     * @param filePath 文件路径
     * @param key      键
     * @param value    值
     * @param comment  该键值对的注释
     */
    fun writeProperties(
        filePath: String, key: String,
        value: String, comment: String = "no_comment"
    ) {
        if (key.isEmpty() || filePath.isEmpty()) {
            return
        }
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            // 先读取文件，再把键值对追加到后面
            p.load(fis)
            p.setProperty(key, value)
            fos = FileOutputStream(f)
            p.store(fos, comment)
        } catch (e: Exception) {
        } finally {
            close(fis)
            close(fos)
        }
    }

    /**
     * 根据值读取
     */
    fun readProperties(
        filePath: String, key: String,
        defaultValue: String = ""
    ): String {
        if (key.isNullOrEmpty() || filePath.isNullOrEmpty()) return ""
        var value: String? = null
        var fis: FileInputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            p.load(fis)
            value = p.getProperty(key, defaultValue)
        } catch (e: IOException) {
        } finally {
            close(fis)
        }
        return value ?: ""
    }


    /**
     * 复制文件，可以选择是否删除源文件
     */
    fun copyFile(
        srcPath: String, destPath: String,
        deleteSrc: Boolean = false
    ) = copyFile(File(srcPath), File(destPath), deleteSrc)

    /**
     * 复制文件，可以选择是否删除源文件
     */
    fun copyFile(
        srcFile: File, destFile: File,
        deleteSrc: Boolean = false
    ): Boolean {
        if (!srcFile.exists() || !srcFile.isFile) return false
        var ins: InputStream? = null
        var out: OutputStream? = null
        try {
            ins = FileInputStream(srcFile)
            out = FileOutputStream(destFile)
            val buffer = ByteArray(1024)
            var i = -1
            while (ins.read(buffer).also { i = it } > 0) {
                out.write(buffer, 0, i)
                out.flush()
            }
            if (deleteSrc) {
                srcFile.delete()
            }
        } catch (e: Exception) {
            return false
        } finally {
            close(out)
            close(ins)
        }
        return true
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    fun copyFile(
        srcFile: File, outputStream: OutputStream?,
        deleteSrc: Boolean = false
    ): Boolean {
        if (!srcFile.exists() || !srcFile.isFile) return false
        var ins: InputStream? = null
        var out: OutputStream = outputStream ?: return false
        try {
            ins = FileInputStream(srcFile)
            val buffer = ByteArray(1024)
            var i = -1
            while (ins.read(buffer).also { i = it } > 0) {
                out.write(buffer, 0, i)
                out.flush()
            }
            if (deleteSrc) {
                srcFile.delete()
            }
        } catch (e: Exception) {
            return false
        } finally {
            close(out)
            close(ins)
        }
        return true
    }

    /**
     * 把字符串键值对的map写入文件
     */
    fun writeMap(
        filePath: String, map: Map<String, String>,
        append: Boolean, comment: String = "no_comment"
    ) {
        if (map.isNullOrEmpty() || filePath.isNullOrEmpty()) return
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            val p = Properties()
            if (append) {
                fis = FileInputStream(f)
                // 先读取文件，再把键值对追加到后面
                p.load(fis)
            }
            p.putAll(map)
            fos = FileOutputStream(f)
            p.store(fos, comment)
        } catch (e: Exception) {
        } finally {
            close(fis)
            close(fos)
        }
    }

    /**
     * 从输入流中写入文件。
     */
    fun writeIs(
        filePath: String, ins: InputStream, append: Boolean = false
    ) = writeIs(File(filePath), ins, append)

    /**
     * 从输入流中写入文件
     */
    fun writeIs(
        file: File, ins: InputStream, append: Boolean = false
    ): Boolean {
        if (!createOrExistsFile(file)) {
            return false
        }
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(FileOutputStream(file, append))
            val data = ByteArray(bufferSize)
            var len: Int
            while (ins.read(data, 0, bufferSize).also { len = it } != -1) {
                os.write(data, 0, len)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            close(ins)
            close(os)
        }
    }

    /**
     * 从字节流写入文件。
     */
    fun writeByte(
        file: File, bytes: ByteArray, append: Boolean = false
    ): Boolean {
        if (bytes == null || !createOrExistsFile(file)) {
            return false
        }
        var bos: BufferedOutputStream? = null
        return try {
            bos = BufferedOutputStream(FileOutputStream(file, append))
            bos.write(bytes)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            close(bos)
        }
    }

    /**
     * 按通道从字节写入文件。
     */
    fun writeByteByChannel(
        filePath: String, bytes: ByteArray, append: Boolean = false, isForce: Boolean = true
    ) = writeByteByChannel(File(filePath), bytes, append, isForce)

    /**
     * 按通道从字节写入文件
     */
    fun writeByteByChannel(
        file: File, bytes: ByteArray, append: Boolean = false, isForce: Boolean = true
    ): Boolean {
        var fc: FileChannel? = null
        return try {
            fc = FileOutputStream(file, append).channel
            fc.position(fc.size())
            fc.write(ByteBuffer.wrap(bytes))
            if (isForce) {
                fc.force(true)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            close(fc)
        }
    }

    /**
     * 通过映射从字节写入文件。
     */
    fun writeByteByMap(
        filePath: String, bytes: ByteArray, append: Boolean = false, isForce: Boolean = true
    ) = writeByteByMap(File(filePath), bytes, append, isForce)

    /**
     * 通过映射从字节写入文件
     */
    fun writeByteByMap(
        file: File, bytes: ByteArray, append: Boolean = true, isForce: Boolean = false
    ): Boolean {
        if (!createOrExistsFile(file)) {
            return false
        }
        var fc: FileChannel? = null
        return try {
            fc = FileOutputStream(file, append).channel
            val mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.size.toLong())
            mbb.put(bytes)
            if (isForce) {
                mbb.force()
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            close(fc)
        }
    }

    /**
     * 从字符串中写入文件。
     */
    fun writeString(
        filePath: String, content: String, append: Boolean = false
    ) = writeString(File(filePath), content, append)

    /**
     * 从字符串中写入文件。
     */
    fun writeString(
        file: File, content: String, append: Boolean = false
    ): Boolean {
        if (!createOrExistsFile(file)) {
            return false
        }
        var bw: BufferedWriter? = null
        return try {
            bw = BufferedWriter(FileWriter(file, append))
            bw.write(content)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            close(bw)
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    // 写和读的分界线
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 把字符串键值对的文件读入map
     */
    fun readMap(
        filePath: String
    ): Map<String, String> {
        if (filePath.isNullOrEmpty()) {
            return mapOf()
        }
        var map: Map<String, String>? = null
        var fis: FileInputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            p.load(fis)
            // 因为properties继承了map，所以直接通过p来构造一个map
            map = p.toMap() as Map<String, String>
        } catch (e: Exception) {
        } finally {
            close(fis)
        }
        return map ?: mapOf()
    }

    /**
     * 返回文件中的行
     */
    fun read2List(
        filePath: String, charsetName: String = "", st: Int = 0, end: Int = 0x7FFFFFFF,
    ) = read2List(File(filePath), charsetName, st, end)

    /**
     * 返回文件中的行
     */
    fun read2List(
        file: File, charsetName: String = "", st: Int = 0, end: Int = 0x7FFFFFFF
    ): MutableList<String> {
        if (st > end) {
            return mutableListOf()
        }
        var reader: BufferedReader? = null
        return try {
            var line: String
            var curLine = 1
            val list = mutableListOf<String>()
            reader = if (StringUtils.isSpace(charsetName)) {
                BufferedReader(InputStreamReader(FileInputStream(file)))
            } else {
                BufferedReader(
                    InputStreamReader(FileInputStream(file), charsetName)
                )
            }
            while (reader.readLine().also { line = it } != null) {
                if (curLine > end) {
                    break
                }
                if (curLine in st..end) {
                    list.add(line)
                }
                ++curLine
            }
            list
        } catch (e: IOException) {
            e.printStackTrace()
            mutableListOf()
        } finally {
            close(reader)
        }
    }

    /**
     * 在文件中返回字符串。
     */
    fun read2String(filePath: String, charsetName: String = "") =
        read2String(File(filePath), charsetName)

    /**
     * 在文件中返回字符串。
     */
    fun read2String(file: File, charsetName: String = ""): String {
        val bytes = read2Bytes(file)
        return if (StringUtils.isSpace(charsetName)) bytes.toString() else bytes.toString(
            Charset.forName(
                charsetName
            )
        )
    }

    /**
     * 按流返回文件中的字节。
     */
    fun read2Bytes(filePath: String) = read2Bytes(File(filePath))

    /**
     * 按流返回文件中的字节。
     */
    fun read2Bytes(file: File): ByteArray {
        return if (!file.exists()) {
            ByteArray(0)
        } else try {
            is2Bytes(FileInputStream(file))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            ByteArray(0)
        }
    }

    /**
     * 按通道返回文件中的字节。
     */
    fun read2BytesByChannel(filePath: String) = read2BytesByChannel(File(filePath))

    /**
     * 按通道返回文件中的字节。
     */
    fun read2BytesByChannel(file: File): ByteArray {
        if (!file.exists()) {
            return ByteArray(0)
        }
        var fc: FileChannel? = null
        return try {
            fc = RandomAccessFile(file, "r").channel
            val byteBuffer = ByteBuffer.allocate(fc.size().toInt())
            while (true) {
                if (fc.read(byteBuffer) <= 0) {
                    break
                }
            }
            byteBuffer.array()
        } catch (e: IOException) {
            e.printStackTrace()
            ByteArray(0)
        } finally {
            close(fc)
        }
    }

    /**
     * 按map返回文件中的字节。
     */
    fun readFile2BytesByMap(filePath: String) = readFile2BytesByMap(File(filePath))

    /**
     * 按map返回文件中的字节。
     */
    fun readFile2BytesByMap(file: File): ByteArray {
        if (!file.exists()) {
            return ByteArray(0)
        }
        var fc: FileChannel? = null
        return try {
            fc = RandomAccessFile(file, "r").channel
            val size = fc.size().toInt()
            val mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size.toLong()).load()
            val result = ByteArray(size)
            mbb[result, 0, size]
            result
        } catch (e: IOException) {
            e.printStackTrace()
            ByteArray(0)
        } finally {
            close(fc)
        }
    }

    /**
     * InputStream 转换成Byte
     */
    fun is2Bytes(ins: InputStream): ByteArray {
        var os: ByteArrayOutputStream? = null
        return try {
            os = ByteArrayOutputStream()
            val b = ByteArray(bufferSize)
            var len: Int
            while (ins.read(b, 0, bufferSize).also { len = it } != -1) {
                os.write(b, 0, len)
            }
            os.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            ByteArray(0)
        } finally {
            close(ins)
            close(os)
        }
    }

    /**
     * 关闭流
     */
    fun close(io: Closeable?): Boolean {
        try {
            io?.close()
        } catch (e: IOException) {
        }
        return true
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    fun createOrExistsFile(filePath: String): Boolean {
        return createOrExistsFile(File(filePath))
    }

    fun createOrExistsFile(file: File): Boolean {
        if (file.exists()) {
            return file.isFile
        }
        return if (!createOrExistsDir(file.parentFile)) false
        else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun createOrExistsDir(file: File): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }
}