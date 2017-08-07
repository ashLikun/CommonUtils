package com.ashlikun.utils.other;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 作者　　: 李坤
 * 创建时间: 13:49 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：判断文件类型
 */

public class MediaFile {

    // 音频文件类型
    public static final int FILE_TYPE_MP3 = 1;
    public static final int FILE_TYPE_M4A = 2;
    public static final int FILE_TYPE_WAV = 3;
    public static final int FILE_TYPE_AMR = 4;
    public static final int FILE_TYPE_AWB = 5;
    public static final int FILE_TYPE_WMA = 6;
    public static final int FILE_TYPE_OGG = 7;
    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_OGG;

    // 乐器数字类型
    public static final int FILE_TYPE_MID = 11;
    public static final int FILE_TYPE_SMF = 12;
    public static final int FILE_TYPE_IMY = 13;
    private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
    private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;

    // 视频文件类型
    public static final int FILE_TYPE_MP4 = 21;
    public static final int FILE_TYPE_M4V = 22;
    public static final int FILE_TYPE_3GPP = 23;
    public static final int FILE_TYPE_3GPP2 = 24;
    public static final int FILE_TYPE_WMV = 25;
    private static final int FIRST_VIDEO_FILE_TYPE = FILE_TYPE_MP4;
    private static final int LAST_VIDEO_FILE_TYPE = FILE_TYPE_WMV;

    // 图片文件类型
    public static final int FILE_TYPE_JPEG = 31;
    public static final int FILE_TYPE_GIF = 32;
    public static final int FILE_TYPE_PNG = 33;
    public static final int FILE_TYPE_BMP = 34;
    public static final int FILE_TYPE_WBMP = 35;
    private static final int FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG;
    private static final int LAST_IMAGE_FILE_TYPE = FILE_TYPE_WBMP;

    // 流媒体文件地址
    public static final int FILE_TYPE_M3U = 41;
    public static final int FILE_TYPE_PLS = 42;
    public static final int FILE_TYPE_WPL = 43;
    private static final int FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U;
    private static final int LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_WPL;


    // wps 文件类型
    public static final int FILE_TYPE_DOC = 51;
    public static final int FILE_TYPE_DOCX = 52;
    public static final int FILE_TYPE_XLS = 53;
    public static final int FILE_TYPE_XLSX = 54;
    public static final int FILE_TYPE_PPS = 55;
    public static final int FILE_TYPE_PPT = 56;
    public static final int FILE_TYPE_TGZ = 57;
    public static final int FILE_TYPE_TAR = 58;
    public static final int FILE_TYPE_TXT = 59;
    public static final int FILE_TYPE_WPS = 60;
    public static final int FILE_TYPE_ZIP = 61;
    public static final int FILE_TYPE_RAR = 62;
    private static final int FIRST_WPS_FILE_TYPE = FILE_TYPE_DOC;
    private static final int LAST_WPS_FILE_TYPE = FILE_TYPE_RAR;

    //静态内部类
    static class MediaFileType {

        int fileType;
        String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    private static HashMap<String, MediaFileType> sFileTypeMap = new HashMap<String, MediaFileType>();
    private static HashMap<String, Integer> sMimeTypeMap = new HashMap<String, Integer>();

    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, new Integer(fileType));
    }

    static {
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma");
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg");

        addFileType("MID", FILE_TYPE_MID, "audio/midi");
        addFileType("XMF", FILE_TYPE_MID, "audio/midi");
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi");
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi");
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody");

        addFileType("MP4", FILE_TYPE_MP4, "video/mp4");
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4");
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv");

        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("GIF", FILE_TYPE_GIF, "image/gif");
        addFileType("PNG", FILE_TYPE_PNG, "image/png");
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp");
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp");

        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl");
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls");
        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl");


        addFileType("ZIP", FILE_TYPE_ZIP, "application/x-zip");
        addFileType("WPS", FILE_TYPE_WPS, "application/vnd.ms-works");
        addFileType("TXT", FILE_TYPE_TXT, "text/plain");
        addFileType("TAR", FILE_TYPE_TAR, "application/x-tar");
        addFileType("TGZ", FILE_TYPE_TGZ, "application/x-compressed");
        addFileType("PPT", FILE_TYPE_PPT, "application/vnd.ms-powerpoint");
        addFileType("PPS", FILE_TYPE_PPS, "application/vnd.ms-powerpoint");
        addFileType("DOC", FILE_TYPE_DOC, "application/msword");
        addFileType("DOCX", FILE_TYPE_DOCX, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        addFileType("XLS", FILE_TYPE_XLS, "application/vnd.ms-excel");
        addFileType("XLSX", FILE_TYPE_XLSX, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");


        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = sFileTypeMap.keySet().iterator();

        while (iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(iterator.next());
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:47
     * <p>
     * 方法功能：是否是音频文件
     */

    public static boolean isAudioFileType(int fileType) {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                        fileType <= LAST_MIDI_FILE_TYPE));
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:48
     * <p>
     * 方法功能：是否是视频文件
     */

    public static boolean isVideoFileType(int fileType) {
        return (fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:48
     * <p>
     * 方法功能：是否是视频文件
     */
    public static boolean isAudioFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isAudioFileType(type.fileType);
        }
        return false;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:49
     * <p>
     * 方法功能：是否是视频文件
     */

    public static boolean isVideoFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isVideoFileType(type.fileType);
        }
        return false;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:48
     * <p>
     * 方法功能：是否是图片文件
     */
    public static boolean isImageFileType(int fileType) {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:48
     * <p>
     * 方法功能：是否是图片文件
     */
    public static boolean isImageFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isImageFileType(type.fileType);
        }
        return false;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:48
     * <p>
     * 方法功能：是否是wps文件
     */
    public static boolean isWpsFileType(int fileType) {
        return (fileType >= FIRST_WPS_FILE_TYPE &&
                fileType <= LAST_WPS_FILE_TYPE);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:48
     * <p>
     * 方法功能：是否是wps文件
     */
    public static boolean isWpsFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isWpsFileType(type.fileType);
        }
        return false;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:49
     * <p>
     * 方法功能：是否是流媒体类型
     */
    public static boolean isPlayListFileType(int fileType) {
        return (fileType >= FIRST_PLAYLIST_FILE_TYPE &&
                fileType <= LAST_PLAYLIST_FILE_TYPE);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:49
     * <p>
     * 方法功能：获取文件类型
     */
    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return null;
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase());
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:51
     * <p>
     * 方法功能：根据 mime 类型查看文件类型
     */

    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        return (value == null ? 0 : value.intValue());
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:51
     * 方法功能：判断是不指定的格式
     *
     * @param path     文件路径
     * @param fileType 指定类型
     */
    public static boolean isFileType(String path, String... fileType) {
        String[] type = fileType;
        try {
            for (int i = 0; i < type.length; i++) {
                File file = new File(path);
                //如果文件存在
                if (file.exists()) {
                    String str = path.substring(path.lastIndexOf(".") + 1);
                    //判断后缀与传入的是否一样
                    if (str.equals(type[i])) {
                        return true;

                        //判断大写
                    } else if (str.toUpperCase().equals(type[i].toUpperCase())) {
                        return true;
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     *     {".3gp",    "video/3gpp"},
     {".apk",    "application/vnd.android.package-archive"},
     {".asf",    "video/x-ms-asf"},
     {".avi",    "video/x-msvideo"},
     {".bin",    "application/octet-stream"},
     {".bmp",    "image/bmp"},
     {".c",  "text/plain"},
     {".class",  "application/octet-stream"},
     {".conf",   "text/plain"},
     {".cpp",    "text/plain"},
     {".doc",    "application/msword"},
     {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
     {".xls",    "application/vnd.ms-excel"},
     {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
     {".exe",    "application/octet-stream"},
     {".gif",    "image/gif"},
     {".gtar",   "application/x-gtar"},
     {".gz", "application/x-gzip"},
     {".h",  "text/plain"},
     {".htm",    "text/html"},
     {".html",   "text/html"},
     {".jar",    "application/java-archive"},
     {".java",   "text/plain"},
     {".jpeg",   "image/jpeg"},
     {".jpg",    "image/jpeg"},
     {".js", "application/x-javascript"},
     {".log",    "text/plain"},
     {".m3u",    "audio/x-mpegurl"},
     {".m4a",    "audio/mp4a-latm"},
     {".m4b",    "audio/mp4a-latm"},
     {".m4p",    "audio/mp4a-latm"},
     {".m4u",    "video/vnd.mpegurl"},
     {".m4v",    "video/x-m4v"},
     {".mov",    "video/quicktime"},
     {".mp2",    "audio/x-mpeg"},
     {".mp3",    "audio/x-mpeg"},
     {".mp4",    "video/mp4"},
     {".mpc",    "application/vnd.mpohun.certificate"},
     {".mpe",    "video/mpeg"},
     {".mpeg",   "video/mpeg"},
     {".mpg",    "video/mpeg"},
     {".mpg4",   "video/mp4"},
     {".mpga",   "audio/mpeg"},
     {".msg",    "application/vnd.ms-outlook"},
     {".ogg",    "audio/ogg"},
     {".pdf",    "application/pdf"},
     {".png",    "image/png"},
     {".pps",    "application/vnd.ms-powerpoint"},
     {".ppt",    "application/vnd.ms-powerpoint"},
     {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
     {".prop",   "text/plain"},
     {".rc", "text/plain"},
     {".rmvb",   "audio/x-pn-realaudio"},
     {".rtf",    "application/rtf"},
     {".sh", "text/plain"},
     {".tar",    "application/x-tar"},
     {".tgz",    "application/x-compressed"},
     {".txt",    "text/plain"},
     {".wav",    "audio/x-wav"},
     {".wma",    "audio/x-ms-wma"},
     {".wmv",    "audio/x-ms-wmv"},
     {".wps",    "application/vnd.ms-works"},
     {".xml",    "text/plain"},
     {".z",  "application/x-compress"},
     {".zip",    "application/x-zip"},
     {"",        "
     */
}
