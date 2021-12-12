package com.ashlikun.utils.other

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.ashlikun.utils.AppUtils
import java.io.File

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:46
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：操作apk的工具
 */

object ApkUtils {
    /**
     * 当前应用的apk
     */
    fun getSelfApk(): String {
        var p: ApplicationInfo = try {
            AppUtils.app.packageManager.getApplicationInfo(AppUtils.app.packageName, 0)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return ""
        }
        return p.sourceDir
    }

    /**
     * 安装apk
     */
    fun installApp(
        activity: Activity, filePath: String, requestCode: Int
    ) {
        installApp(
            activity, File(filePath), requestCode
        )
    }

    /**
     * 安装apk
     *
     * 目标sdk大于25 必须加权限
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param file The file.
     */
    fun installApp(
        activity: Activity,
        file: File,
        requestCode: Int
    ) {
        if (!file.exists()) {
            return
        }
        activity.startActivityForResult(getInstallAppIntent(file), requestCode)
    }

    /**
     * 安装apk
     *
     * 目标sdk大于25 必须加权限
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param file The file.
     */
    fun installApp(file: File) {
        if (!file.exists()) {
            return
        }
        AppUtils.app.startActivity(getInstallAppIntent(file, true))
    }

    fun getInstallAppIntent(file: File, isNewTask: Boolean = false): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        intent.setDataAndType(AppUtils.getUri(file), type)
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }

    /**
     * 是否可以安装未知app权限
     * 兼容8.0
     */
    fun canRequestPackageInstalls() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) AppUtils.app.packageManager.canRequestPackageInstalls() else true

    /**
     * 跳转到设置-允许安装未知来源-页面
     * 注意这个是8.0新API
     */
    fun unknownAppInstall() = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 这个apk是否已经安装了
     *
     * @param packagename
     * @return `true`: yes<br></br>`false`: no
     */
    fun isAppInstalled(packagename: String) = try {
        AppUtils.app.packageManager.getPackageInfo(packagename, 0) != null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } != null

    /**
     * 静默安装
     * 必须权限
     * `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param filePath 文件
     * @param params
     * @return `true`: success<br></br>`false`: fail
     */
    fun installAppSilent(filePath: String, params: String = "") =
        installAppSilent(File(filePath), params)
    /**
     * 静默安装
     * 必须权限
     * `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param file     文件
     * @param params
     * @param isRooted 是否是rooot
     * @return `true`: success<br></br>`false`: fail
     * install [-lrtsfd] [PATH]：安装命令；
     * -l ：锁定应用程序；
     * -r：重新安装应用，且保留应用数据；
     * -i：指定安装包的包名；
     * -s：安装到sd卡；
     * -f：安装到系统内置存储中（默认安装位置）；
     * -g：授予应用程序清单中列出的所有权限（只有6.0系统可用）；
     * uninstall [options] ‘pkgname’：卸载命令；
     * -k：卸载应用且保留数据与缓存（如果不加-k则全部删除）；
     * clear ‘pkgname’ ：对指定的package删除所有数据；
     * enable ‘pkgname’ ：使package或component可用。（如：pm enable “package/class”）；
     * disable ‘pkgname’ ：使package或component不可用。（如：pm disable “package/class”）；
     * grant ‘pkgname’：授权给应用；
     * revoke ‘pkgname’：撤销权限；
     * set-install-location ‘location’：设置默认的安装位置。
     * 其中0：让系统自动选择最佳的安装位置。1：安装到内部的设备存储空间。2：安装到外部的设备存储空间；
     * get-install-location ：返回当前的安装位置。返回结果同上参数选项；
     * create-user ‘USER_NAME’ ：增加一个新的USER；
     * remove-user ‘USER_ID’ ：删除一个USER；
     */
    /**
     * 静默安装
     * 必须权限
     * `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param file   文件
     * @param params -i AppUtils.getPageName() -r
     * @return `true`: success<br></br>`false`: fail
     */
    fun installAppSilent(
        file: File,
        params: String = "-r ",
        isRooted: Boolean = isDeviceRooted
    ): Boolean {
        if (!file.exists()) {
            return false
        }
        val filePath = '"'.toString() + file!!.absolutePath + '"'
        val command =
            ("LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm install " + "$params " + filePath)
        val commandResult = ShellUtils.execCmd(command, isRooted)
        return if (commandResult.successMsg != null
            && commandResult.successMsg.toLowerCase().contains("success")
        ) {
            true
        } else {
            LogUtils.e(
                "installAppSilent successMsg: " + commandResult.successMsg +
                        ", errorMsg: " + commandResult.errorMsg
            )
            false
        }
    }

    /**
     * 删除app
     *
     * @param packageName 包名
     */
    fun uninstallApp(packageName: String) {
        if (StringUtils.isSpace(packageName)) {
            return
        }
        AppUtils.app.startActivity(getUninstallAppIntent(packageName, true))
    }

    /**
     * 删除app
     *
     * @param activity
     * @param packageName 包名
     */
    fun uninstallApp(
        activity: Activity,
        packageName: String,
        requestCode: Int
    ) {
        if (StringUtils.isSpace(packageName)) {
            return
        }
        activity.startActivityForResult(getUninstallAppIntent(packageName), requestCode)
    }

    /**
     * 静默删除这个apk
     * 必须权限
     * `<uses-permission android:name="android.permission.DELETE_PACKAGES" />`
     *
     * @param packageName 包名
     * @param isKeepData  是否删除数据
     * @param isRooted    是否是root软件
     * @return `true`: success<br></br>`false`: fail
     */

    fun uninstallAppSilent(
        packageName: String,
        isKeepData: Boolean = false,
        isRooted: Boolean = isDeviceRooted
    ): Boolean {
        if (StringUtils.isSpace(packageName)) {
            return false
        }
        val command = ("LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall "
                + (if (isKeepData) "-k " else "")
                + packageName)
        val commandResult = ShellUtils.execCmd(command, isRooted)
        return if (commandResult.successMsg != null
            && commandResult.successMsg.toLowerCase().contains("success")
        ) {
            true
        } else {
            Log.e(
                "AppUtils", "uninstallAppSilent successMsg: " + commandResult.successMsg +
                        ", errorMsg: " + commandResult.errorMsg
            )
            false
        }
    }

    fun getUninstallAppIntent(packageName: String): Intent {
        return getUninstallAppIntent(packageName, false)
    }

    fun getUninstallAppIntent(packageName: String, isNewTask: Boolean): Intent {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }

    val isDeviceRooted: Boolean
        get() {
            val su = "su"
            val locations = arrayOf(
                "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"
            )
            for (location in locations) {
                if (File(location + su).exists()) {
                    return true
                }
            }
            return false
        }
}
