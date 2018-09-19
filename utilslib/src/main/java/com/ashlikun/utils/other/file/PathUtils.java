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
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取data目录
     */
    public static String getDataPath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 获取下载缓存目录
     */
    public static String getDownloadCachePath() {
        return Environment.getDownloadCacheDirectory().getAbsolutePath();
    }

    /**
     * 获取 /data/data/package.
     */
    public static String getInternalAppDataPath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return AppUtils.getApp().getApplicationInfo().dataDir;
        }
        return AppUtils.getApp().getDataDir().getAbsolutePath();
    }

    /**
     * 获取 /data/data/package/code_cache.
     */
    public static String getInternalAppCodeCacheDir() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return AppUtils.getApp().getApplicationInfo().dataDir + "/code_cache";
        }
        return AppUtils.getApp().getCodeCacheDir().getAbsolutePath();
    }

    /**
     * 获取 /data/data/package/cache.
     */
    public static String getInternalAppCachePath() {
        return AppUtils.getApp().getCacheDir().getAbsolutePath();
    }

    /**
     * 获取 /data/data/package/databases.
     */
    public static String getInternalAppDbsPath() {
        return AppUtils.getApp().getApplicationInfo().dataDir + "/databases";
    }

    /**
     * 获取 /data/data/package/databases/name.
     *
     * @param name 数据库名称
     */
    public static String getInternalAppDbPath(String name) {
        return AppUtils.getApp().getDatabasePath(name).getAbsolutePath();
    }

    /**
     * 获取 /data/data/package/files.
     */
    public static String getInternalAppFilesPath() {
        return AppUtils.getApp().getFilesDir().getAbsolutePath();
    }

    /**
     * 获取 /data/data/package/shared_prefs.
     */
    public static String getInternalAppSpPath() {
        return AppUtils.getApp().getApplicationInfo().dataDir + "shared_prefs";
    }

    /**
     * 获取 /storage/emulated/0.
     */
    public static String getExternalStoragePath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Music.
     */
    public static String getExternalMusicPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Podcasts.
     */
    public static String getExternalPodcastsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
    }

    /**
     * 获取 of /storage/emulated/0/Ringtones.
     */
    public static String getExternalRingtonesPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getAbsolutePath();
    }

    /**
     * 获取 of /storage/emulated/0/Alarms.
     */
    public static String getExternalAlarmsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Notifications.
     */
    public static String getExternalNotificationsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Pictures.
     */
    public static String getExternalPicturesPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Movies.
     */
    public static String getExternalMoviesPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Download.
     */
    public static String getExternalDownloadsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/DCIM.
     */
    public static String getExternalDcimPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Documents.
     */
    public static String getExternalDocumentsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents";
        }
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package.
     */
    public static String getExternalAppDataPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalCacheDir().getParentFile().getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/cache.
     */
    public static String getExternalAppCachePath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        //noinspection ConstantConditions
        return AppUtils.getApp().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files.
     */
    public static String getExternalAppFilesPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(null).getAbsolutePath();
    }

    /**
     * 获取 of /storage/emulated/0/Android/data/package/files/Music.
     *
     * @return the path of /storage/emulated/0/Android/data/package/files/Music
     */
    public static String getExternalAppMusicPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        //noinspection ConstantConditions
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Podcasts.
     */
    public static String getExternalAppPodcastsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Ringtones.
     */
    public static String getExternalAppRingtonesPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_RINGTONES).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Alarms.
     */
    public static String getExternalAppAlarmsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Notifications.
     */
    public static String getExternalAppNotificationsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Pictures.
     */
    public static String getExternalAppPicturesPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Movies.
     */
    public static String getExternalAppMoviesPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Download.
     */
    public static String getExternalAppDownloadPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/DCIM.
     */
    public static String getExternalAppDcimPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/data/package/files/Documents.
     */
    public static String getExternalAppDocumentsPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return AppUtils.getApp().getExternalFilesDir(null).getAbsolutePath() + "/Documents";
        }
        return AppUtils.getApp().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    }

    /**
     * 获取 /storage/emulated/0/Android/obb/package.
     */
    public static String getExternalAppObbPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApp().getObbDir().getAbsolutePath();
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
