package com.ashlikun.utils.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.file.FileUtils;
import com.ashlikun.utils.other.file.PathUtils;

import java.io.File;

import static com.ashlikun.utils.other.StringUtils.isSpace;

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

    /**
     * 安装apk
     */
    public static void installApp(final Activity activity,
                                  final String filePath,
                                  final int requestCode) {
        installApp(activity, PathUtils.getFileByPath(filePath), requestCode);
    }

    /**
     * 安装apk
     * <p>目标sdk大于25 必须加权限
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(final Activity activity,
                                  final File file,
                                  final int requestCode) {
        if (!FileUtils.isFileExists(file)) {
            return;
        }

        activity.startActivityForResult(getInstallAppIntent(file), requestCode);
    }

    /**
     * 安装apk
     * <p>目标sdk大于25 必须加权限
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(final File file) {
        if (!FileUtils.isFileExists(file)) {
            return;
        }
        AppUtils.getApp().startActivity(getInstallAppIntent(file, true));
    }

    private static Intent getInstallAppIntent(final File file) {
        return getInstallAppIntent(file, false);
    }

    private static Intent getInstallAppIntent(final File file, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(AppUtils.getUri(file), type);
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    /**
     * 是否可以安装未知app权限
     */
    public static boolean canRequestPackageInstalls() {
        //兼容8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return AppUtils.getApp().getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    public static Intent unknownAppInstall() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 这个apk是否已经安装了
     *
     * @param packagename
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppInstalled(@NonNull final String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = AppUtils.getApp().getPackageManager().getPackageInfo(packagename, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 静默安装
     * 必须权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 文件
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean installAppSilent(final String filePath) {
        return installAppSilent(PathUtils.getFileByPath(filePath), null);
    }

    /**
     * 静默安装
     * 必须权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param file 文件
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean installAppSilent(final File file) {
        return installAppSilent(file, "-r ");
    }


    /**
     * 静默安装
     * 必须权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param filePath 文件
     * @param params
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean installAppSilent(final String filePath, final String params) {
        return installAppSilent(PathUtils.getFileByPath(filePath), params);
    }

    /**
     * 静默安装
     * 必须权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param file   文件
     * @param params -i AppUtils.getPageName() -r
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean installAppSilent(final File file, final String params) {
        return installAppSilent(file, params, isDeviceRooted());
    }

    /**
     * 静默安装
     * 必须权限
     * {@code <uses-permission android:name="android.permission.INSTALL_PACKAGES" />}</p>
     *
     * @param file     文件
     * @param params
     * @param isRooted 是否是rooot
     * @return {@code true}: success<br>{@code false}: fail
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
    public static boolean installAppSilent(final File file,
                                           final String params,
                                           final boolean isRooted) {
        if (!FileUtils.isFileExists(file)) {
            return false;
        }
        String filePath = '"' + file.getAbsolutePath() + '"';
        String command = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm install " +
                (params == null ? "" : params + " ")
                + filePath;
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, isRooted);
        if (commandResult.successMsg != null
                && commandResult.successMsg.toLowerCase().contains("success")) {
            return true;
        } else {
            LogUtils.e("installAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg);
            return false;
        }
    }

    /**
     * 删除app
     *
     * @param packageName 包名
     */
    public static void uninstallApp(final String packageName) {
        if (StringUtils.isSpace(packageName)) {
            return;
        }
        AppUtils.getApp().startActivity(getUninstallAppIntent(packageName, true));
    }

    /**
     * 删除app
     *
     * @param activity
     * @param packageName 包名
     */
    public static void uninstallApp(final Activity activity,
                                    final String packageName,
                                    final int requestCode) {
        if (StringUtils.isSpace(packageName)) {
            return;
        }
        activity.startActivityForResult(getUninstallAppIntent(packageName), requestCode);
    }

    /**
     * 静默删除这个apk
     * 必须权限
     * {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>
     *
     * @param packageName 包名
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean uninstallAppSilent(final String packageName) {
        return uninstallAppSilent(packageName, false);
    }

    /**
     * 静默删除这个apk
     * 必须权限
     * {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>
     *
     * @param packageName 包名
     * @param isKeepData  是否删除数据
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean uninstallAppSilent(final String packageName, final boolean isKeepData) {
        return uninstallAppSilent(packageName, isKeepData, isDeviceRooted());
    }

    /**
     * 静默删除这个apk
     * 必须权限
     * {@code <uses-permission android:name="android.permission.DELETE_PACKAGES" />}</p>
     *
     * @param packageName 包名
     * @param isKeepData  是否删除数据
     * @param isRooted    是否是root软件
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean uninstallAppSilent(final String packageName,
                                             final boolean isKeepData,
                                             final boolean isRooted) {
        if (isSpace(packageName)) {
            return false;
        }
        String command = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall "
                + (isKeepData ? "-k " : "")
                + packageName;
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, isRooted);
        if (commandResult.successMsg != null
                && commandResult.successMsg.toLowerCase().contains("success")) {
            return true;
        } else {
            Log.e("AppUtils", "uninstallAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg);
            return false;
        }
    }

    private static Intent getUninstallAppIntent(final String packageName) {
        return getUninstallAppIntent(packageName, false);
    }

    private static Intent getUninstallAppIntent(final String packageName, final boolean isNewTask) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }
}
