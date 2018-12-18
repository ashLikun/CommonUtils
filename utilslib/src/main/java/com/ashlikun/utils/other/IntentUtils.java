package com.ashlikun.utils.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.ashlikun.utils.AppUtils;

import java.io.File;
import java.io.IOException;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/19　9:42
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：系统一些意图的工具类
 */
public class IntentUtils {
    /**
     * 根据手机号发送短信
     *
     * @param context
     * @param phone
     */
    public static void sendSmsByPhone(Context context, String phone) {
        if (StringUtils.isEmpty(phone)) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        // intent.putExtra("sms_body", "");
        context.startActivity(intent);
    }

    /**
     * 调用系统分享
     */
    public static void shareToOtherApp(Context context, String title, String content, String dialogTitle) {
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
    }

    /**
     * 回到桌面
     */
    public static void startHomeActivity() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        AppUtils.getApp().startActivity(homeIntent);
    }


    /**
     * 根据手机好拨打电话
     */
    public static void callPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return;
        }
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
        AppUtils.getApp().startActivity(intent);
    }

    /**
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
     * 方法功能：同上
     */

    public static void startFileSelect(Fragment context, String Type, int requestCode) {
        Intent intent = new Intent();
        intent.setType(Type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, requestCode);
    }
}
