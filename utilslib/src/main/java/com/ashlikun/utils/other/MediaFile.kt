package com.ashlikun.utils.other

import java.io.File
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:24
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：判断文件类型
 */
//静态内部类
class MediaFileType(var fileType: Int, var mimeType: String)
object MediaFile {
    // 音频文件类型
    const val FILE_TYPE_MP3 = 1
    const val FILE_TYPE_M4A = 2
    const val FILE_TYPE_WAV = 3
    const val FILE_TYPE_AMR = 4
    const val FILE_TYPE_AWB = 5
    const val FILE_TYPE_WMA = 6
    const val FILE_TYPE_OGG = 7
    private const val FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3
    private const val LAST_AUDIO_FILE_TYPE = FILE_TYPE_OGG

    // 乐器数字类型
    const val FILE_TYPE_MID = 11
    const val FILE_TYPE_SMF = 12
    const val FILE_TYPE_IMY = 13
    private const val FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID
    private const val LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY

    // 视频文件类型
    const val FILE_TYPE_MP4 = 21
    const val FILE_TYPE_M4V = 22
    const val FILE_TYPE_3GPP = 23
    const val FILE_TYPE_3GPP2 = 24
    const val FILE_TYPE_WMV = 25
    private const val FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4
    private const val LAST_VIDEO_FILE_TYPE = FILE_TYPE_WMV

    // 图片文件类型
    const val FILE_TYPE_JPEG = 31
    const val FILE_TYPE_GIF = 32
    const val FILE_TYPE_PNG = 33
    const val FILE_TYPE_BMP = 34
    const val FILE_TYPE_WBMP = 35
    private const val FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG
    private const val LAST_IMAGE_FILE_TYPE = FILE_TYPE_WBMP

    // 流媒体文件地址
    const val FILE_TYPE_M3U = 41
    const val FILE_TYPE_PLS = 42
    const val FILE_TYPE_WPL = 43
    private const val FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U
    private const val LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_WPL

    // wps 文件类型
    const val FILE_TYPE_DOC = 51
    const val FILE_TYPE_DOCX = 52
    const val FILE_TYPE_XLS = 53
    const val FILE_TYPE_XLSX = 54
    const val FILE_TYPE_PPS = 55
    const val FILE_TYPE_PPT = 56
    const val FILE_TYPE_TGZ = 57
    const val FILE_TYPE_TAR = 58
    const val FILE_TYPE_TXT = 59
    const val FILE_TYPE_WPS = 60
    const val FILE_TYPE_ZIP = 61
    const val FILE_TYPE_RAR = 62
    private const val FIRST_WPS_FILE_TYPE = FILE_TYPE_DOC
    private const val LAST_WPS_FILE_TYPE = FILE_TYPE_RAR
    private val sFileTypeMap = HashMap<String, MediaFileType>()
    private val sMimeTypeMap = HashMap<String, Int>()


    init {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg")
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4")
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav")
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr")
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb")
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma")
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg")
        addFileType("MID", FILE_TYPE_MID, "audio/midi")
        addFileType("XMF", FILE_TYPE_MID, "audio/midi")
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi")
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi")
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody")
        addFileType("MP4", FILE_TYPE_MP4, "video/mp4")
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4")
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp")
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp")
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2")
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2")
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv")
        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg")
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg")
        addFileType("GIF", FILE_TYPE_GIF, "image/gif")
        addFileType("PNG", FILE_TYPE_PNG, "image/png")
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp")
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp")
        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl")
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls")
        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl")
        addFileType("ZIP", FILE_TYPE_ZIP, "application/x-zip")
        addFileType("WPS", FILE_TYPE_WPS, "application/vnd.ms-works")
        addFileType("TXT", FILE_TYPE_TXT, "text/plain")
        addFileType("TAR", FILE_TYPE_TAR, "application/x-tar")
        addFileType("TGZ", FILE_TYPE_TGZ, "application/x-compressed")
        addFileType("PPT", FILE_TYPE_PPT, "application/vnd.ms-powerpoint")
        addFileType("PPS", FILE_TYPE_PPS, "application/vnd.ms-powerpoint")
        addFileType("DOC", FILE_TYPE_DOC, "application/msword")
        addFileType(
            "DOCX",
            FILE_TYPE_DOCX,
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        )
        addFileType("XLS", FILE_TYPE_XLS, "application/vnd.ms-excel")
        addFileType(
            "XLSX",
            FILE_TYPE_XLSX,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )
    }

    fun addFileType(extension: String, fileType: Int, mimeType: String) {
        sFileTypeMap[extension] = MediaFileType(fileType, mimeType)
        sMimeTypeMap[mimeType] = fileType
    }

    /**
     * 是否是音频文件
     */
    fun isAudioFileType(fileType: Int): Boolean {
        return fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE ||
                fileType >= FIRST_MIDI_FILE_TYPE &&
                fileType <= LAST_MIDI_FILE_TYPE
    }

    /**
     * 是否是视频文件
     */
    fun isVideoFileType(fileType: Int): Boolean {
        return fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE
    }

    /**
     * 是否是视频文件
     */
    fun isAudioFileType(path: String): Boolean {
        val type = getFileType(path)
        return if (null != type) {
            isAudioFileType(type.fileType)
        } else false
    }

    /**
     *是否是视频文件
     */
    fun isVideoFileType(path: String): Boolean {
        val type = getFileType(path)
        return if (null != type) {
            isVideoFileType(type.fileType)
        } else false
    }

    /**
     * 是否是图片文件
     */
    fun isImageFileType(fileType: Int): Boolean {
        return fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE
    }

    /**
     * 是否是图片文件
     */
    fun isImageFileType(path: String): Boolean {
        val type = getFileType(path)
        return if (null != type) {
            isImageFileType(type.fileType)
        } else false
    }

    /**
     * 是否是wps文件
     */
    fun isWpsFileType(fileType: Int): Boolean {
        return fileType in FIRST_WPS_FILE_TYPE..LAST_WPS_FILE_TYPE
    }

    /**
     * 是否是wps文件
     */
    fun isWpsFileType(path: String): Boolean {
        val type = getFileType(path)
        return if (null != type) {
            isWpsFileType(type.fileType)
        } else false
    }

    /**
     * 是否是流媒体类型
     */
    fun isPlayListFileType(fileType: Int): Boolean {
        return fileType in FIRST_PLAYLIST_FILE_TYPE..LAST_PLAYLIST_FILE_TYPE
    }

    /**
     * 获取文件类型
     */
    fun getFileType(path: String): MediaFileType? {
        val lastDot = path.lastIndexOf(".")
        return if (lastDot < 0) null else sFileTypeMap[path.substring(lastDot + 1).toUpperCase()]
    }

    /**
     * 根据 mime 类型查看文件类型
     */
    fun getFileTypeForMimeType(mimeType: String): Int {
        val value = sMimeTypeMap[mimeType]
        return value ?: 0
    }

    /**
     * 判断是不指定的格式
     *
     * @param path     文件路径
     * @param fileType 指定类型
     */
    fun isFileType(path: String, vararg fileType: String): Boolean {
        try {
            fileType?.forEach {
                val file = File(path)
                //如果文件存在
                if (file.exists()) {
                    val str = path.substring(path.lastIndexOf(".") + 1)
                    //判断后缀与传入的是否一样
                    if (str == it) return true
                    //判断大写
                    else if (str.uppercase() == it.uppercase()) return true
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}