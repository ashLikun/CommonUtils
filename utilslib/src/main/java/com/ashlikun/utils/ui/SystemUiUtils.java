package com.ashlikun.utils.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ashlikun.utils.other.StringUtils;

import java.io.File;
import java.io.IOException;

import static com.ashlikun.utils.AppUtils.getApp;


/**
 * 作者　　: 李坤
 * 创建时间:2017/8/6 0006　15:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：启动系统自带的ui
 */

public class SystemUiUtils {
    /**
     * 根据手机好拨打电话
     *
     * @param context
     * @param phone
     */
    public static void callByPhone(final Context context, final String phone) {
        if (StringUtils.isEmpty(phone)) {
            return;
        }
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .content("确定拨打 " + phone)
                .title("拨打电话")
                .positiveText("拨打")
                .negativeColor(Color.GRAY)
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                        context.startActivity(intent);
                    }
                })
                .build();
        dialog.show();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:28
     * <p>
     * 方法功能：开始拍照
     *
     * @param context
     * @param appSDCachePath 文件存放路径
     * @param requestCode    请求的Code
     * @return 文件，异步的
     */

    public static String startPicture(Activity context, String appSDCachePath, int requestCode) {
        File fDir = new File(appSDCachePath);
        String cachePath = System.currentTimeMillis() + ".jpg";
        if (fDir.exists() || fDir.mkdirs()) {
            File cameraFile = new File(fDir, cachePath);
            if (!cameraFile.exists()) {
                try {
                    cameraFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            context.startActivityForResult(intent, requestCode);
        }
        return cachePath;
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:29
     * <p>
     * 方法功能：开启文件选择
     *
     * @param context
     * @param type    媒体类型 audio/*  选择音频
     *                video/*  选择视频
     *                video/*;image/*  同时选择视频和图片
     *                * /*  全部  没有空格
     */

    public static void startFileSelect(Activity context, String type, int requestCode) {
        Intent intent = new Intent();
        intent.setType(type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:32
     * <p>
     * 方法功能：同上
     */

    public static void startFileSelect(Fragment context, String Type, int requestCode) {
        Intent intent = new Intent();
        intent.setType(Type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取照片选择的文件路径
     */
    public static String getFileSelectPath(Intent data) {
        if (data == null || data.getData() == null) return null;
        return getPath(getApp(), data.getData());
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:41
     * 方法功能： 获取本地url的文件路径
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
            if (cursor != null)
                cursor.close();
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
