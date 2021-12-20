package com.ashlikun.utils.other

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.ashlikun.utils.AppUtils.app
import com.ashlikun.utils.ui.ActivityManager
import java.io.File
import java.io.IOException

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:06
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：系统一些意图的工具类
 */
object IntentUtils {
    fun jump(intent: Intent) {
        val activity = ActivityManager.foregroundActivity
        if (activity != null) {
            activity.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            app.startActivity(intent)
        }
    }

    /**
     * 根据手机号发送短信
     *
     * @param phone
     */
    fun sendSmsByPhone(phone: String) {
        if (phone.isEmpty()) {
            return
        }
        val uri = Uri.parse("smsto:$phone")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        // intent.putExtra("sms_body", "");
        jump(intent)
    }

    /**
     * 调用系统分享
     */
    fun shareToOtherApp(title: String, content: String, dialogTitle: String) {
        val intentItem = Intent(Intent.ACTION_SEND)
        intentItem.type = "text/plain"
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title)
        intentItem.putExtra(Intent.EXTRA_TEXT, content)
        jump(Intent.createChooser(intentItem, dialogTitle))
    }

    /**
     * 回到桌面
     */
    fun startHomeActivity() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        jump(intent)
    }

    /**
     * 根据手机好拨打电话
     */
    fun callPhone(phone: String) {
        if (phone.isEmpty()) {
            return
        }
        val intent = Intent("android.intent.action.CALL", Uri.parse("tel:$phone"))
        jump(intent)
    }

    /**
     * 开始拍照
     *
     * @param context
     * @param appSDCachePath 文件存放路径
     * @param requestCode    请求的Code
     * @return 文件，异步的
     */
    fun startPicture(context: Activity, appSDCachePath: String, requestCode: Int): String {
        val fDir = File(appSDCachePath)
        val cachePath = System.currentTimeMillis().toString() + ".jpg"
        if (fDir.exists() || fDir.mkdirs()) {
            val cameraFile = File(fDir, cachePath)
            if (!cameraFile.exists()) {
                try {
                    cameraFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile))
            context.startActivityForResult(intent, requestCode)
        }
        return cachePath
    }

    /**
     * 开启文件选择
     *
     * @param context
     * @param type    媒体类型 audio/ *  选择音频
     * video/ *  选择视频
     * video/ *;image/ *  同时选择视频和图片
     * * / *  全部  没有空格
     */
    fun startFileSelect(context: Activity, type: String, requestCode: Int) {
        val intent = Intent()
        intent.type = type
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        context.startActivityForResult(intent, requestCode)
    }

    /**
     * 同上
     */
    fun startFileSelect(context: Fragment, type: String, requestCode: Int) {
        val intent = Intent()
        intent.type = type
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        context.startActivityForResult(intent, requestCode)
    }
}