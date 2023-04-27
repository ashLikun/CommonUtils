package com.ashlikun.utils.other

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.collection.SimpleArrayMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ashlikun.utils.other.store.putStore
import com.ashlikun.utils.other.store.storeContains

/**
 * 作者　　: 李坤
 * 创建时间: 2023/4/27　16:13
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：权限工具,参考https://github.com/permissions-dispatcher/PermissionsDispatcher/tree/master/library/src/main/java/permissions/dispatcher
 */

enum class PermissionStatus {
    //默认
    DEFAULT,

    //成功
    SUCCESS,

    //单次拒绝
    REFUSE,

    //永久拒绝
    REFUSE_PERMANENT,

}

object PermissionUtils {
    const val SP_REQUEST_FLAG_NAME = "Permissions Request Flag"

    // 在以后的框架版本中引入的危险权限的映射。用于有条件地绕过旧设备上的权限保留检查。
    // 参考：https://developer.android.comreferenceceandroidManifest.permission
    private var MIN_SDK_PERMISSIONS: SimpleArrayMap<String, Int> = SimpleArrayMap()

    init {
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14)
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20)
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23)
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23)
        MIN_SDK_PERMISSIONS.put("android.permission.READ_PHONE_NUMBERS", 26)
        MIN_SDK_PERMISSIONS.put("android.permission.ANSWER_PHONE_CALLS", 26)
        MIN_SDK_PERMISSIONS.put("android.permission.ACCEPT_HANDOVER", 28)
        MIN_SDK_PERMISSIONS.put("android.permission.ACTIVITY_RECOGNITION", 29)
        MIN_SDK_PERMISSIONS.put("android.permission.ACCESS_MEDIA_LOCATION", 29)
        MIN_SDK_PERMISSIONS.put("android.permission.ACCESS_BACKGROUND_LOCATION", 29)
    }

    private fun PermissionUtils() {}

    /**
     * 检查是否已授予所有给定的权限。
     *
     * @param grantResults results
     * @return 如果已授予所有权限，则返回true。
     */
    fun verifyPermissions(vararg grantResults: Int): Boolean {
        if (grantResults.size == 0) {
            return false
        }
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 如果此SDK版本中存在权限，则返回true
     *
     * @param permission permission
     * @return 如果此SDK版本中存在权限，则返回true
     */
    private fun permissionExists(permission: String): Boolean {
        // 检查此设备上是否可能缺少权限
        val minVersion = MIN_SDK_PERMISSIONS[permission]
        // 如果上述调用返回null，则无需对权限进行设备API级检查；
        // 否则，我们检查其最低API级别要求是否满足
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion
    }

    /**
     * 如果“活动”或“片段”有权访问所有给定的权限，则返回true。
     *
     * @param context     context
     * @param permissions permission list
     * @return 如果“活动”或“片段”有权访问所有给定的权限，则返回true。
     */
    fun hasSelfPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false
            }
        }
        return true
    }

    /**
     * 确定上下文是否有权访问给定的权限。
     *
     *
     * 这是针对ParcelreadException的RuntimeException的解决方法。
     * 有关更多详细信息，请查看此问题https://github.comhotchemePermissionsDispatcherissue107
     *
     * @param context    context
     * @param permission permission
     * @return 如果上下文有权访问给定的权限，则为true，否则为false。
     */
    private fun hasSelfPermission(context: Context, permission: String): Boolean {
        return try {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        } catch (t: RuntimeException) {
            false
        }
    }

    /**
     * 需要对给定的权限进行检查，以显示理由。
     * 1:未请求权限，返回false
     * 2:请求了权限，用户选择了拒绝权限，但不是永久拒绝，下次申请权限还会弹窗，这时候返回值为true
     * 3:请求了权限，用户决绝了权限，并且是永久拒绝（选择了拒绝且不在提示），此时返回值为false
     * 4:用户同意了权限，此时返回值为false
     * @param activity    activity
     * @param permissions permission list
     * @return 如果需要其中一个权限来显示基本原理，则返回true。
     */
    fun shouldShowRequestPermissionRationale(activity: Activity, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 需要对给定的权限进行检查，以显示理由。
     *
     * @param fragment    fragment
     * @param permissions permission list
     * @return 如果需要其中一个权限来显示基本原理，则返回true。
     */
    fun shouldShowRequestPermissionRationale(fragment: Fragment, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 权限回调后调用这个方法
     * 存储永久拒绝的标识,true 请求过，false或者null，未请求过
     */
    fun savePerFlag(map: Map<String, Boolean>) {
        map.forEach { per -> per.key.putStore(true, SP_REQUEST_FLAG_NAME) }
    }

    /**
     * 获取权限状态
     */
    fun getStatus(activity: Activity, vararg permissions: String): PermissionStatus {
        //是否有权限
        if (hasSelfPermissions(activity, *permissions)) {
            return PermissionStatus.SUCCESS
        }
        if (shouldShowRequestPermissionRationale(activity, *permissions)) {
            //单次拒绝
            return PermissionStatus.REFUSE
        }
        if (permissions.map { it.storeContains(SP_REQUEST_FLAG_NAME) }.contains(false)) {
            //未请求过
            return PermissionStatus.DEFAULT
        }
        //永久拒绝
        return PermissionStatus.REFUSE_PERMANENT
    }
}