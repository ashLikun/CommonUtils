package com.ashlikun.utils.http

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import com.ashlikun.utils.AppUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 22:51
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：网络连接帮助类
 * android.permission.ACCESS_NETWORK_STATE
 */
@SuppressLint("MissingPermission")
object NetWorkHelper {
    /**
     * 判断是否有网络连
     */

    fun isNetworkAvailable(): Boolean {
        val connectivity = AppUtils.app
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 是否有网络
     */
    fun checkNetState(): Boolean {
        var netstate = false
        val connectivity = AppUtils.app
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        netstate = true
                        break
                    }
                }
            }
        }
        return netstate
    }

    /**
     * 判断网络是否为漫游
     */
    fun isNetworkRoaming(): Boolean {
        val connectivity = AppUtils.app
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
        } else {
            val info = connectivity.activeNetworkInfo
            if (info != null
                && info.type == ConnectivityManager.TYPE_MOBILE
            ) {
                val tm = AppUtils.app
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (tm != null && tm.isNetworkRoaming) {
                    return true
                } else {
                }
            } else {
            }
        }
        return false
    }

    /**
     * 判断MOBILE网络是否可用
     */
    @Throws(Exception::class)
    fun isMobileDataEnable(): Boolean {
        val connectivityManager = AppUtils.app
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkInfo(
            ConnectivityManager.TYPE_MOBILE
        )?.isConnectedOrConnecting ?: false
    }

    /**
     * 判断wifi 是否可用
     */
    @Throws(Exception::class)
    fun isWifiDataEnable(): Boolean {
        val connectivityManager = AppUtils.app
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkInfo(
            ConnectivityManager.TYPE_WIFI
        )?.isConnectedOrConnecting ?: false
    }
}