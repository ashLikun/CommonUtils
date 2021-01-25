package com.ashlikun.utils.other.file;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.StringUtils;

import java.io.File;

import static com.ashlikun.utils.AppUtils.getApp;


/**
 * 作者　　: 李坤
 * 创建时间:2017/8/6 0006　15:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：文件路径一些工具
 */
public class PathUtils {

    /**
     * 获取文件正确路径
     *
     * @param filePath
     * @return
     */
    public static File getFileByPath(final String filePath) {
        return StringUtils.isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * 获取照片选择的文件路径
     */
    public static String getFileSelectPath(Intent data) {
        if (data == null || data.getData() == null) {
            return null;
        }
        return getPath(getApp(), data.getData());
    }

    /**
     * 获取根目录
     */
    public static String getRootPath() {
        return Environment.getRootDirectory().getPath();
    }

    /**
     * 获取data目录
     */
    public static String getDataPath() {
        return Environment.getDataDirectory().getPath();
    }

    /**
     * 获取下载缓存目录
     */
    public static String getDownloadCachePath() {
        return Environment.getDownloadCacheDirectory().getPath();
    }

    /**
     * 获取 /data/data/package.
     */
    public static String getInternalAppData() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return AppUtils.getApp().getApplicationInfo().dataDir;
        }
        return AppUtils.getApp().getDataDir().getPath();
    }

    /**
     * 获取 /data/data/package/code_cache.
     */
    public static String getInternalAppCodeCacheDir() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return AppUtils.getApp().getApplicationInfo().dataDir + File.separator + "code_cache";
        }
        return AppUtils.getApp().getCodeCacheDir().getPath();
    }

    /**
     * 获取 /data/data/package/cache.
     */
    public static String getInternalAppCache() {
        return AppUtils.getApp().getCacheDir().getPath();
    }

    /**
     * 获取 /data/data/package/databases.
     */
    public static String getInternalAppDbs() {
        return AppUtils.getApp().getApplicationInfo().dataDir + File.separator + "databases";
    }

    /**
     * 获取 /data/data/package/databases/name.
     *
     * @param name 数据库名称
     */
    public static String getInternalAppDb(String name) {
        return AppUtils.getApp().getDatabasePath(name).getPath();
    }

    /**
     * 获取 /data/data/package/files.
     */
    public static String getInternalAppFiles() {
        return AppUtils.getApp().getFilesDir().getPath();
    }

    /**
     * 获取 /data/data/package/shared_prefs.
     */
    public static String getInternalAppSp() {
        return AppUtils.getApp().getApplicationInfo().dataDir + File.separator + "shared_prefs";
    }

    /**
     * 获取 /storage/emulated/0.
     */
    public static String getExternalStorage() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles();
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取 /storage/emulated/0/Music.
     */
    public static String getExternalMusic() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_MUSIC;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Podcasts.
     */
    public static String getExternalPodcasts() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_PODCASTS;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getPath();
    }

    /**
     * 获取 of /storage/emulated/0/Ringtones.
     */
    public static String getExternalRingtones() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_RINGTONES;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getPath();
    }

    /**
     * 获取 of /storage/emulated/0/Alarms.
     */
    public static String getExternalAlarms() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_RINGTONES;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Notifications.
     */
    public static String getExternalNotifications() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_NOTIFICATIONS;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Pictures.
     */
    public static String getExternalPictures() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_PICTURES;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Movies.
     */
    public static String getExternalMovies() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_MOVIES;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Download.
     */
    public static String getExternalDownloads() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_DOWNLOADS;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/DCIM.
     */
    public static String getExternalDcim() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_DCIM;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Documents.
     */
    public static String getExternalDocuments() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (isExternalStorageDisable()) {
                return getInternalAppFiles() + File.separator + "/Documents";
            }
            return Environment.getExternalStorageDirectory().getPath() + "/Documents";
        }
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_DOCUMENTS;
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package.
     */
    public static String getExternalAppData() {
        if (isExternalStorageDisable()) {
            return getInternalAppData();
        }
        return AppUtils.getApp().getExternalCacheDir().getParentFile().getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/cache.
     */
    public static String getExternalAppCache() {
        if (isExternalStorageDisable()) {
            return getInternalAppCache();
        }
        //noinspection ConstantConditions
        return AppUtils.getApp().getExternalCacheDir().getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files.
     */
    public static String getExternalAppFiles() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles();
        }
        return AppUtils.getApp().getExternalFilesDir(null).getPath();
    }

    /**
     * 获取 of /storage/emulated/0/Android/data/package/files/Music.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Music
     */
    public static String getExternalAppMusic() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_MUSIC;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Podcasts.
     */
    public static String getExternalAppPodcasts() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_PODCASTS;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_PODCASTS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Ringtones.
     */
    public static String getExternalAppRingtones() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_RINGTONES;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_RINGTONES).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Alarms.
     */
    public static String getExternalAppAlarms() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_ALARMS;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_ALARMS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Notifications.
     */
    public static String getExternalAppNotifications() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_NOTIFICATIONS;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Pictures.
     */
    public static String getExternalAppPictures() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_PICTURES;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Movies.
     */
    public static String getExternalAppMovies() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_MOVIES;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Download.
     */
    public static String getExternalAppDownload() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_DOWNLOADS;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/DCIM.
     */
    public static String getExternalAppDcim() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_DCIM;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Documents.
     */
    public static String getExternalAppDocuments() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (isExternalStorageDisable()) {
                return getInternalAppFiles() + File.separator + "Documents";
            }
            return AppUtils.getApp().getExternalFilesDir(null).getPath() + File.separator + "Documents";
        }
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + Environment.DIRECTORY_DOCUMENTS;
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath();
    }

    /**
     * 获取 /storage/emulated/0/Android/obb/package.
     */
    public static String getExternalAppObb() {
        if (isExternalStorageDisable()) {
            return getInternalAppFiles() + File.separator + "obb";
        }
        return AppUtils.getApp().getObbDir().getPath();
    }

    private static boolean isExternalStorageDisable() {
        return !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取本地url的文件路径
     *
     * @param uri
     * @return 文件路径
     */
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) {
        if (uri == null) {
            return "";
        }
        // DocumentProvider
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e("ssssss", "content");
            // Return the remote address
            if (isGooglePhotosUri(uri)) {

                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e("ssssss", "file");
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

}
