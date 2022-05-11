package com.ashlikun.utils.other

import android.content.ComponentName
import android.content.Intent
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
}