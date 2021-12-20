package com.ashlikun.utils.other.file

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import com.ashlikun.utils.AppUtils
import java.io.File

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:15
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：文件路径一些工具
 */

object PathUtils {

    /**
     * 获取照片选择的文件路径
     */
    fun getFileSelectPath(data: Intent): String? {
        return if (data.data == null) {
            null
        } else getPath(AppUtils.app, data.data!!)
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
        } else Environment.getExternalStorageDirectory().path

    /**
     * 获取 /storage/emulated/0/Music.
     */
    val externalMusic: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MUSIC
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).path

    /**
     * 获取 /storage/emulated/0/Podcasts.
     */
    val externalPodcasts: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PODCASTS
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).path

    /**
     * 获取 of /storage/emulated/0/Ringtones.
     */
    val externalRingtones: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_RINGTONES
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).path

    /**
     * 获取 of /storage/emulated/0/Alarms.
     */
    val externalAlarms: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_RINGTONES
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).path

    /**
     * 获取 /storage/emulated/0/Notifications.
     */
    val externalNotifications: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_NOTIFICATIONS
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).path

    /**
     * 获取 /storage/emulated/0/Pictures.
     */
    val externalPictures: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PICTURES
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path

    /**
     * 获取 /storage/emulated/0/Movies.
     */
    val externalMovies: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MOVIES
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).path

    /**
     * 获取 /storage/emulated/0/Download.
     */
    val externalDownloads: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DOWNLOADS
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path

    /**
     * 获取 /storage/emulated/0/DCIM.
     */
    val externalDcim: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DCIM
        } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path

    /**
     * 获取 /storage/emulated/0/Documents.
     */
    val externalDocuments: String
        get() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return if (isExternalStorageDisable) {
                    internalAppFiles + File.separator + "/Documents"
                } else Environment.getExternalStorageDirectory().path + "/Documents"
            }
            return if (isExternalStorageDisable) {
                internalAppFiles + File.separator + Environment.DIRECTORY_DOCUMENTS
            } else Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path
        }

    /**
     * 获取 /storage/emulated/0/Android/data/package.
     */
    val externalAppData: String
        get() = if (isExternalStorageDisable) {
            internalAppData
        } else AppUtils.app.externalCacheDir!!.parentFile.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/cache.
     */
    val externalAppCache: String
        get() = if (isExternalStorageDisable) {
            internalAppCache
        } else AppUtils.app.externalCacheDir!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files.
     */
    val externalAppFiles: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles
        } else AppUtils.app.getExternalFilesDir(null)!!.path

    /**
     * 获取 of /storage/emulated/0/Android/data/package/files/Music.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Music
     */
    val externalAppMusic: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MUSIC
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_MUSIC)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Podcasts.
     */
    val externalAppPodcasts: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PODCASTS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Ringtones.
     */
    val externalAppRingtones: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_RINGTONES
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_RINGTONES)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Alarms.
     */
    val externalAppAlarms: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_ALARMS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_ALARMS)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Notifications.
     */
    val externalAppNotifications: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_NOTIFICATIONS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Pictures.
     */
    val externalAppPictures: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_PICTURES
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Movies.
     */
    val externalAppMovies: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_MOVIES
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_MOVIES)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Download.
     */
    val externalAppDownload: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DOWNLOADS
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/DCIM.
     */
    val externalAppDcim: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + Environment.DIRECTORY_DCIM
        } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.path

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Documents.
     */
    val externalAppDocuments: String
        get() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return if (isExternalStorageDisable) {
                    internalAppFiles + File.separator + "Documents"
                } else AppUtils.app
                    .getExternalFilesDir(null)!!.path + File.separator + "Documents"
            }
            return if (isExternalStorageDisable) {
                internalAppFiles + File.separator + Environment.DIRECTORY_DOCUMENTS
            } else AppUtils.app.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.path
        }

    /**
     * 获取 /storage/emulated/0/Android/obb/package.
     */
    val externalAppObb: String
        get() = if (isExternalStorageDisable) {
            internalAppFiles + File.separator + "obb"
        } else AppUtils.app.obbDir.path
    private val isExternalStorageDisable: Boolean
        private get() = Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()

    /**
     * 获取本地url的文件路径
     *
     * @param uri
     * @return 文件路径
     */
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String {
        if (uri == null) {
            return ""
        }
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            && DocumentsContract.isDocumentUri(context, uri)
        ) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri)
            } else if (isMediaDocument(uri)) {
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
                    return ""
                }
                return getDataColumn(
                    context, contentUri, selection,
                    selectionArgs
                )
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            Log.e("ssssss", "content")
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment ?: "" else getDataColumn(
                context,
                uri
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            Log.e("ssssss", "file")
            return uri.path ?: ""
        }
        return ""
    }

    fun getDataColumn(
        context: Context, uri: Uri,
        selection: String = "", selectionArgs: Array<String> = emptyArray()
    ): String {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index) ?: ""
            }
        } finally {
            cursor?.close()
        }
        return ""
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