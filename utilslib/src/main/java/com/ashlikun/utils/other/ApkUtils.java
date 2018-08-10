package com.ashlikun.utils.other;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/2/24　15:04
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：操作apk的工具
 */

public class ApkUtils {
    public String getSelfApk(Context context) {
        ApplicationInfo p = null;
        try {
            p = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return p.sourceDir;
    }

}
