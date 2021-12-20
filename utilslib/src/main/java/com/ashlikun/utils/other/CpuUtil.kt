package com.ashlikun.utils.other

import com.ashlikun.utils.other.file.FileIOUtils
import java.io.*
import java.util.regex.Pattern

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 13:16
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：获取cpu信息
 */
object CpuUtil {
    private const val CPU_INFO_PATH = "/proc/cpuinfo"
    private const val CPU_FREQ_NULL = "N/A"
    private const val CMD_CAT = "/system/bin/cat"
    private const val CPU_FREQ_CUR_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"
    private const val CPU_FREQ_MAX_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"
    private const val CPU_FREQ_MIN_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"
    private var CPU_NAME: String = ""
    private var CPU_CORES = 0
    private var CPU_MAX_FREQENCY: Long = 0
    private var CPU_MIN_FREQENCY: Long = 0

    /**
     * 输出cpu信息
     */
    fun printCpuInfo(): String {
        val info = FileIOUtils.read2String(CPU_INFO_PATH)
        LogUtils.i("_______  CPU :   \n$info")
        return info
    }

    /**
     * 获取cpu有效的核数
     */
    val processorsCount: Int
        //cup信息文件夹
        get() = Runtime.getRuntime().availableProcessors()

    /**
     * 获取cpu数目
     */
    val coresNumbers: Int
        get() {
            if (CPU_CORES != 0) {
                return CPU_CORES
            }
            //文件过滤
            class CpuFilter : FileFilter {
                override fun accept(pathname: File): Boolean {
                    return Pattern.matches("cpu[0-9]+", pathname.name)
                }
            }
            try {
                //cup信息文件夹
                val dir = File("/sys/devices/system/cpu/")
                //过滤名称
                val files = dir.listFiles(CpuFilter())
                CPU_CORES = files.size //缓存
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (CPU_CORES < 1) {
                CPU_CORES = Runtime.getRuntime().availableProcessors()
            }
            if (CPU_CORES < 1) {
                CPU_CORES = 1
            }
            return CPU_CORES
        }

    /**
     * 获取cpu信息
     */
    val cpuName: String
        get() {
            if (CPU_NAME.isNotEmpty()) {
                return CPU_NAME
            }
            try {
                val bufferedReader = BufferedReader(FileReader(CPU_INFO_PATH), 8192)
                val line = bufferedReader.readLine()
                bufferedReader.close()
                val array = line.split(":\\s+".toRegex(), 2).toTypedArray()
                if (array.size > 1) {
                    LogUtils.i(array[1])
                    CPU_NAME = array[1]
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return CPU_NAME
        }

    /**
     * 获取cpu当前使用频率
     */
    val currentFreqency: Long
        get() {
            try {
                return FileIOUtils.read2String(CPU_FREQ_CUR_PATH).trim().toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

    /**
     * 获取最大频率
     */
    val maxFreqency: Long
        get() {
            if (CPU_MAX_FREQENCY > 0) {
                return CPU_MAX_FREQENCY
            }
            try {
                CPU_MAX_FREQENCY = getCMDOutputString(arrayOf(CMD_CAT, CPU_FREQ_MAX_PATH))!!
                    .trim { it <= ' ' }.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return CPU_MAX_FREQENCY
        }

    /**
     * 获取最小频率
     */
    val minFreqency: Long
        get() {
            if (CPU_MIN_FREQENCY > 0) {
                return CPU_MIN_FREQENCY
            }
            try {
                CPU_MIN_FREQENCY = getCMDOutputString(arrayOf(CMD_CAT, CPU_FREQ_MIN_PATH))!!
                    .trim { it <= ' ' }.toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return CPU_MIN_FREQENCY
        }

    /**
     * 获取命令行输出
     */
    fun getCMDOutputString(args: Array<String>): String {
        try {
            val cmd = ProcessBuilder(*args)
            val process = cmd.start()
            val `in` = process.inputStream
            val sb = StringBuilder()
            val re = ByteArray(64)
            var len: Int
            while (`in`.read(re).also { len = it } != -1) {
                sb.append(String(re, 0, len))
            }
            `in`.close()
            process.destroy()
            LogUtils.i("CMD: $sb")
            return sb.toString()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return ""
    }
}