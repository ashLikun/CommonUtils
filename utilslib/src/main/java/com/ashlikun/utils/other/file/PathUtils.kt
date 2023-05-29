package com.ashlikun.utils.other.file

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.core.net.toFile
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.MediaFile
import com.ashlikun.utils.other.getMediaFile
import java.io.File

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:15
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：文件路径一些工具,uri一些工具
 */
inline val File.toUri
    get() = AppUtils.getUri(this)
inline val Uri.toFileData
    get() = PathUtils.getFileData(this)

inline val File.toFileData
    get() = FileData(name, length(), path, MediaFile.getFileType(path)?.mimeType.orEmpty(), uri = toUri)

object PathUtils {
    /**
     * 获取照片选择的文件路径
     */
    fun getFileSelectPath(data: Intent): String? {
        return if (data.data == null) {
            null
        } else getPath(data.data!!)
    }

    /**
     * 获取根目录
     */
    val rootPath: String
        get() = Environment.getRootDirectory().path

    /**
     * 获取data目录
     */
    val dataPath: String
        get() = Environment.getDataDirectory().path

    /**
     * 获取下载缓存目录
     */
    val downloadCachePath: String
        get() = Environment.getDownloadCacheDirectory().path

    /**
     * 获取 /data/data/package.
     */
    val internalAppData: String
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            AppUtils.app.applicationInfo.dataDir
        } else AppUtils.app.dataDir.path

    /**
     * 获取 /data/data/package/code_cache.
     */
    val internalAppCodeCacheDir: String
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppUtils.app.applicationInfo.dataDir + File.separator + "code_cache"
        } else AppUtils.app.codeCacheDir.path

    /**
     * 获取 /data/data/package/cache.
     */
    val internalAppCache: String
        get() = AppUtils.app.cacheDir.path

    /**
     * 获取 /data/data/package/databases.
     */
    val internalAppDbs: String
        get() = AppUtils.app.applicationInfo.dataDir + File.separator + "databases"

    /**
     * 获取 /data/data/package/databases/name.
     *
     * @param name 数据库名称
     */
    fun getInternalAppDb(name: String?): String {
        return AppUtils.app.getDatabasePath(name).path
    }

    /**
     * 获取 /data/data/package/files.
     */
    val internalAppFiles: String
        get() = AppUtils.app.filesDir.path

    /**
     * 获取 /data/data/package/shared_prefs.
     */
    val internalAppSp: String
        get() = AppUtils.app.applicationInfo.dataDir + File.separator + "shared_prefs"

    /**
     * 获取 /storage/emulated/0.
     */
    val externalStorage: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles
        } else runCatching {
            Environment.getExternalStorageDirectory().path
        }.getOrNull() ?: internalAppFiles

    /**
     * 获取 /storage/emulated/0/Music.
     */
    val externalMusic: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MUSIC
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_MUSIC)

    /**
     * 获取 /storage/emulated/0/Podcasts.
     */
    val externalPodcasts: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PODCASTS
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_PODCASTS)

    /**
     * 获取 of /storage/emulated/0/Ringtones.
     */
    val externalRingtones: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_RINGTONES
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_RINGTONES)

    /**
     * 获取 of /storage/emulated/0/Alarms.
     */
    val externalAlarms: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_ALARMS
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_ALARMS)

    /**
     * 获取 /storage/emulated/0/Notifications.
     */
    val externalNotifications: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_NOTIFICATIONS
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_NOTIFICATIONS)

    /**
     * 获取 /storage/emulated/0/Pictures.
     */
    val externalPictures: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PICTURES
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_PICTURES)

    /**
     * 获取 /storage/emulated/0/Movies.
     */
    val externalMovies: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MOVIES
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_MOVIES)

    /**
     * 获取 /storage/emulated/0/Download.
     */
    val externalDownloads: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DOWNLOADS
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_DOWNLOADS)

    /**
     * 获取 /storage/emulated/0/DCIM.
     */
    val externalDcim: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DCIM
        } else runCatching {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path
        }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_DCIM)

    /**
     * 获取 /storage/emulated/0/Documents.
     */
    val externalDocuments: String
        get() {
            return if (isExternalStorageDisable) {
                internalAppFiles + File.separator + Environment.DIRECTORY_DOCUMENTS
            } else runCatching {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
            }.getOrNull() ?: (internalAppFiles + File.separator + Environment.DIRECTORY_DOCUMENTS)
        }

    /**
     * 获取 /storage/emulated/0/Android/data/package.
     */
    val externalAppData: String
        get() = if (isExternalStorageDisable) {
            internalAppData
        } else AppUtils.app.externalCacheDir?.parentFile?.path ?: internalAppData

    /**
     * 获取 /storage/emulated/0/Android/data/package/cache.
     */
    val externalAppCache: String
        get() = if (isExternalStorageDisable) {
            internalAppCache
        } else AppUtils.app.externalCacheDir?.path ?: internalAppCache

    /**
     * 获取 /storage/emulated/0/Android/data/package/files.
     */
    val externalAppFiles: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles
        } else AppUtils.app.getExternalFilesDir(null)?.path ?: internalAppFiles

    /**
     * 获取 of /storage/emulated/0/Android/data/package/files/Music.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Music
     */
    val externalAppMusic: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MUSIC
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_MUSIC)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Podcasts.
     */
    val externalAppPodcasts: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PODCASTS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_PODCASTS)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Ringtones.
     */
    val externalAppRingtones: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_RINGTONES
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_RINGTONES)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_RINGTONES)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Alarms.
     */
    val externalAppAlarms: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_ALARMS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_ALARMS)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_ALARMS)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Notifications.
     */
    val externalAppNotifications: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_NOTIFICATIONS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_NOTIFICATIONS)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Pictures.
     */
    val externalAppPictures: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PICTURES
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_PICTURES)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Movies.
     */
    val externalAppMovies: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MOVIES
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_MOVIES)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Download.
     */
    val externalAppDownload: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DOWNLOADS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_DOWNLOADS)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/DCIM.
     */
    val externalAppDcim: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DCIM
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_DCIM)?.path
            ?: (internalAppFiles + File.separator + Environment.DIRECTORY_DCIM)

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Documents.
     */
    val externalAppDocuments: String
        get() {
            return if (isExternalStorageDisable) {
                internalAppFiles + File.separator + Environment.DIRECTORY_DOCUMENTS
            } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path
                ?: (internalAppFiles + File.separator + Environment.DIRECTORY_DOCUMENTS)
        }

    /**
     * 获取 /storage/emulated/0/Android/obb/package.
     */
    val externalAppObb: String
        get() = AppUtils.app.obbDir.path
    private val isExternalStorageDisable: Boolean
        private get() = Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()

    /**
     * Uri 转成FileData
     */
    fun getFileData(uri: Uri): FileData? {
        val result = runCatching { uri.toFile().let { it.toFileData } }.getOrNull()
        if (result != null) return result
        runCatching {
            val data = AppUtils.app.contentResolver.query(uri, null, null, null, null)
            if (data != null && data.moveToNext()) {
                val displayNameIndex = data.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                val fileSizeIndex = data.getColumnIndex(MediaStore.MediaColumns.SIZE)
                val dataIndex = data.getColumnIndex(MediaStore.MediaColumns.DATA)
                val mimeIndex = data.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)

                var fileName = data.getStringOrNull(displayNameIndex)
                var fileSize = data.getIntOrNull(fileSizeIndex)
                var mimeType = data.getStringOrNull(mimeIndex)
                var filePath = data.getStringOrNull(dataIndex)
                data.close()
                if (filePath.isNullOrEmpty()) {
                    filePath = getPath(uri)
                }
                if (!filePath.isNullOrEmpty()) {
                    runCatching {
                        val file = File(filePath)
                        if (fileName.isNullOrEmpty()) {
                            fileName = file.name
                        }
                        if (fileSize == null) {
                            fileSize = file.length()?.toInt()
                        }
                        if (mimeType.isNullOrEmpty()) {
                            mimeType = file.getMediaFile?.mimeType
                        }
                    }
                }

                return FileData(fileName.orEmpty(), fileSize?.toLong() ?: 0L, filePath, mimeType = mimeType.orEmpty(), uri = uri)
            } else {
                null
            }
        }
        return null
    }

    /**
     * 获取本地url的文件路径
     * @return 文件路径
     */
    fun getPath(uri: Uri): String? {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(AppUtils.app, uri)) {
            // ExternalStorageProvider
            runCatching {
                //来之扩展存储卡（应用）选择的
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return (Environment.getExternalStorageDirectory().toString() + "/" + split[1])
                    }
                }
                //来之下载（应用）选择的
                else if (isDownloadsDocument(uri)) {
                    runCatching {
                        val path = getDataColumnString(AppUtils.app, uri)
                        if (!path.isNullOrEmpty()) {
                            return path
                        }
                    }
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), split[1].toLong())
                    return getDataColumnString(AppUtils.app, contentUri)
                }
                //来之媒体应用选择的
                else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    var contentUri = when (split[0]) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    if (contentUri == null) {
                        return null
                    }
                    return getDataColumnString(AppUtils.app, contentUri, selection, selectionArgs)
                }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumnString(AppUtils.app, uri)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }


    /**
     * 获取 图片 ContentValue
     */
    fun getImageContentValues(
        file: File,
        mimeType: String = MediaFile.getFileType(file.absolutePath)?.mimeType ?: "image/jpeg",
        desc: String = "App Save Image",
        currentTime: Long = System.currentTimeMillis()
    ) =
        ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, file.name)
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.DATE_ADDED, currentTime)
            put(MediaStore.Images.Media.DATE_MODIFIED, currentTime)
            put(MediaStore.Images.Media.DATE_TAKEN, currentTime)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.DESCRIPTION, desc)
            put(MediaStore.Images.Media.ORIENTATION, 0)
            put(MediaStore.Images.Media.DATA, file.absolutePath)
            put(MediaStore.Images.Media.SIZE, file.length())
        }

    /**
     * 获取 视频 ContentValue
     */
    fun getVideoContentValues(
        file: File,
        mimeType: String = MediaFile.getFileType(file.absolutePath)?.mimeType ?: "image/jpeg",
        desc: String = "App Save Video",
        currentTime: Long = System.currentTimeMillis()
    ) =
        ContentValues().apply {
            put(MediaStore.Video.Media.TITLE, file.name)
            put(MediaStore.Video.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Video.Media.DATE_ADDED, currentTime)
            put(MediaStore.Video.Media.DATE_MODIFIED, currentTime)
            put(MediaStore.Video.Media.DATE_TAKEN, currentTime)
            put(MediaStore.Video.Media.MIME_TYPE, mimeType)
            put(MediaStore.Video.Media.DESCRIPTION, desc)
            put(MediaStore.Video.Media.ORIENTATION, 0)
            put(MediaStore.Video.Media.DATA, file.absolutePath)
            put(MediaStore.Video.Media.SIZE, file.length())
        }

    /**
     * 获取 音频 ContentValue
     */
    fun getAudioContentValues(
        file: File,
        mimeType: String = MediaFile.getFileType(file.absolutePath)?.mimeType ?: "audio/*",
        currentTime: Long = System.currentTimeMillis()
    ) =
        ContentValues().apply {
            put(MediaStore.Audio.Media.TITLE, file.name)
            put(MediaStore.Audio.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Audio.Media.DATE_ADDED, currentTime)
            put(MediaStore.Audio.Media.DATE_MODIFIED, currentTime)
            put(MediaStore.Audio.Media.DATE_TAKEN, currentTime)
            put(MediaStore.Audio.Media.MIME_TYPE, mimeType)
            put(MediaStore.Audio.Media.ORIENTATION, 0)
            put(MediaStore.Audio.Media.DATA, file.absolutePath)
            put(MediaStore.Audio.Media.SIZE, file.length())
        }

    /**
     * 文件-->uri
     */
    fun getFileContentUri(file: File): Uri? {
        // 低版本直接用 Uri.fromFile
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        }
        val volumeName = "external"
        val filePath = file.absolutePath
        var uri: Uri? = null
        val cursor = AppUtils.app.contentResolver.query(
            MediaStore.Files.getContentUri(volumeName),
            arrayOf(MediaStore.Files.FileColumns._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath),
            null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID)
                val id: Int = cursor.getInt(idIndex)
                uri = MediaStore.Files.getContentUri(volumeName, id.toLong())
            }
            cursor.close()
        }
        return uri
    }

    /**
     * File 自动转换成URI
     */
    fun getUri(file: File): Uri? {
        if (!file.exists()) return null
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        }
        val filePath = file.absolutePath
        if (MediaFile.isImageFileType(filePath)) {
            return getImageContentUri(file)
        } else if (MediaFile.isVideoFileType(filePath)) {
            return getVideoContentUri(file)
        } else if (MediaFile.isAudioFileType(filePath)) {
            return getAudioContentUri(file)
        }
        return getFileContentUri(file)
    }

    /**
     * 图片文件-->Url
     */
    fun getImageContentUri(file: File): Uri? {
        if (!file.exists()) return null
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        }
        val filePath = file.absolutePath
        val cursor = AppUtils.app.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=? ", arrayOf(filePath), null
        )
        var uri: Uri? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID)
                val id: Int = cursor.getInt(idIndex)
                val baseUri = Uri.parse("content://media/external/images/media")
                uri = Uri.withAppendedPath(baseUri, "" + id)
            }
            cursor.close()
        }
        return uri ?: AppUtils.app.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getImageContentValues(file))
    }

    /**
     * 视频文件---》Url
     */
    fun getVideoContentUri(file: File): Uri? {
        if (!file.exists()) return null
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        }
        var uri: Uri? = null
        val filePath = file.absolutePath
        val cursor = AppUtils.app.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Video.Media._ID),
            MediaStore.Video.Media.DATA + "=? ",
            arrayOf(filePath),
            null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID)
                val id: Int = cursor.getInt(idIndex)
                val baseUri = Uri.parse("content://media/external/video/media")
                uri = Uri.withAppendedPath(baseUri, "" + id)
            }
            cursor.close()
        }
        return uri ?: AppUtils.app.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, getVideoContentValues(file))
    }

    /**
     * 音频文件到URL
     */
    fun getAudioContentUri(file: File): Uri? {
        if (!file.exists()) return null
        var uri: Uri? = null
        val filePath = file.absolutePath
        val cursor = AppUtils.app.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Media._ID), MediaStore.Audio.Media.DATA + "=? ", arrayOf(filePath), null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID)
                val id: Int = cursor.getInt(idIndex)
                val baseUri = Uri.parse("content://media/external/audio/media")
                uri = Uri.withAppendedPath(baseUri, "" + id)
            }
            cursor.close()
        }
        return uri ?: AppUtils.app.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getAudioContentValues(file))
    }

    fun getDataColumnString(context: Context, uri: Uri, selection: String = "", selectionArgs: Array<String> = emptyArray()): String? {
        var cursor: Cursor? = null
        val column = MediaStore.MediaColumns.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getStringOrNull(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri) = "com.android.externalstorage.documents" == uri
        .authority

    fun isDownloadsDocument(uri: Uri) = "com.android.providers.downloads.documents" == uri
        .authority

    fun isMediaDocument(uri: Uri) = "com.android.providers.media.documents" == uri
        .authority

    fun isGooglePhotosUri(uri: Uri) = "com.google.android.apps.photos.content" == uri
        .authority
}