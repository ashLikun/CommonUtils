package com.ashlikun.utils.other

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.ResolveInfo
import android.util.Log
import com.ashlikun.utils.AppUtils


/**
 * 作者　　: 李坤
 * 创建时间: 2022/5/11　21:06
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：系统权限设置的页面
 */
object PermisstionSettingPage {
    fun start() {
        if (!IntentUtils.startMyAppSetting()) {
            when {
                RomUtils.isHuawei -> toHuawei()
                RomUtils.isXiaomi -> toXiaomi()
                RomUtils.isMeizu -> toMeizu()
                RomUtils.isOppo -> toOppo()
            }
        }
    }

    fun toHuawei() = Intent(AppUtils.packageName)
        .apply {
            component = ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
//            component = ComponentName("com.android.packageinstaller", "com.android.packageinstaller.permission.ui.ManagePermissionsActivity")
        }
        .jump()

    fun toMeizu() = Intent("com.meizu.safe.security.SHOW_APPSEC").apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        putExtra("packageName", AppUtils.packageName)
    }.jump()

    fun toXiaomi() = Intent("miui.intent.action.APP_PERM_EDITOR").apply {
        putExtra("extra_pkgname", AppUtils.packageName)
        component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
    }

    fun toSony() = Intent().apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra("packageName", AppUtils.packageName)
        component = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
    }.jump()

    fun toOppo() = Intent().apply {
        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra("packageName", AppUtils.packageName)
        //        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        component = ComponentName("com.coloros.securitypermission",
            "com.coloros.securitypermission.permission.PermissionAppAllPermissionActivity") //R11t 7.1.1 os-v3.2
    }.jump()

    fun doStartApplicationWithPackageName(packagename: String) = runCatching {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo = AppUtils.app.packageManager.getPackageInfo(packagename, 0)
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)
        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveinfoList: List<ResolveInfo> = AppUtils.app.packageManager
            .queryIntentActivities(resolveIntent, 0)
        LogUtils.e("PermissionPageManager    resolveinfoList" + resolveinfoList.size)
        for (i in resolveinfoList.indices) {
            LogUtils.e("PermissionPageManager" + resolveinfoList[i].activityInfo.packageName + resolveinfoList[i].activityInfo.name)
        }
        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            val packageName = resolveinfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            val className = resolveinfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            intent.component = ComponentName(packageName, className)
            if (!intent.jump()) {
                throw RuntimeException(" jump false")
            }
        }
    }.isSuccess
}