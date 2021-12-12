package com.ashlikun.utils.assist

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.net.NetworkInfo
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.assist.NetworkUtils
import com.ashlikun.utils.assist.NetworkUtils.NetType
import java.lang.reflect.Method
import java.lang.Exception
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.assist.NetworkUtils.NetWorkType

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:05
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：网络状态工具类
 */
@SuppressLint("MissingPermission")
object NetworkUtils {
    /**
     * 获取ConnectivityManager
     */
    fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * 获取ConnectivityManager
     */
    fun getTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    /**
     * 判断网络连接是否有效（此时可传输数据）。
     *
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
     */
    fun isConnected() =
        getConnectivityManager(AppUtils.app).activeNetworkInfo?.isConnected ?: false

    /**
     * 判断有无网络正在连接中（查找网络、校验、获取IP等）。
     *
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
     */
    fun isConnectedOrConnecting(context: Context) =
        getConnectivityManager(context).allNetworkInfo?.find { it.isConnectedOrConnecting } != null

    fun getConnectedType(context: Context): NetType {
        val net = getConnectivityManager(context).activeNetworkInfo
        return if (net != null) {
            when (net.type) {
                ConnectivityManager.TYPE_WIFI -> NetType.Wifi
                ConnectivityManager.TYPE_MOBILE -> NetType.Mobile
                else -> NetType.Other
            }
        } else NetType.None
    }

    /**
     * 是否存在有效的WIFI连接
     */
    fun isWifiConnected(context: Context): Boolean {
        val net = getConnectivityManager(context).activeNetworkInfo
        return net != null && net.type == ConnectivityManager.TYPE_WIFI && net.isConnected
    }

    /**
     * 是否存在有效的移动连接
     *
     * @return boolean
     */
    fun isMobileConnected(context: Context): Boolean {
        val net = getConnectivityManager(context).activeNetworkInfo
        return net != null && net.type == ConnectivityManager.TYPE_MOBILE && net.isConnected
    }

    /**
     * 检测网络是否为可用状态
     */
    fun isAvailable(context: Context): Boolean {
        return isWifiAvailable(context) || isMobileAvailable(context) && isMobileEnabled(context)
    }

    /**
     * 判断是否有可用状态的Wifi，以下情况返回false：
     * 1. 设备wifi开关关掉;
     * 2. 已经打开飞行模式；
     * 3. 设备所在区域没有信号覆盖；
     * 4. 设备在漫游区域，且关闭了网络漫游。
     *
     * @return boolean wifi为可用状态（不一定成功连接，即Connected）即返回ture
     */

    fun isWifiAvailable(context: Context): Boolean {
        val nets = getConnectivityManager(context).allNetworkInfo
        if (nets != null) {
            for (net in nets) {
                if (net.type == ConnectivityManager.TYPE_WIFI) {
                    return net.isAvailable
                }
            }
        }
        return false
    }

    /**
     * 判断有无可用状态的移动网络，注意关掉设备移动网络直接不影响此函数。
     * 也就是即使关掉移动网络，那么移动网络也可能是可用的(彩信等服务)，即返回true。
     * 以下情况它是不可用的，将返回false：
     * 1. 设备打开飞行模式；
     * 2. 设备所在区域没有信号覆盖；
     * 3. 设备在漫游区域，且关闭了网络漫游。
     *
     * @return boolean
     */
    fun isMobileAvailable(context: Context): Boolean {
        val nets = getConnectivityManager(context).allNetworkInfo
        if (nets != null) {
            for (net in nets) {
                if (net.type == ConnectivityManager.TYPE_MOBILE) {
                    return net.isAvailable
                }
            }
        }
        return false
    }

    /**
     * 设备是否打开移动网络开关
     *
     * @return boolean 打开移动网络返回true，反之false
     */
    fun isMobileEnabled(context: Context): Boolean {
        try {
            val getMobileDataEnabledMethod =
                ConnectivityManager::class.java.getDeclaredMethod("getMobileDataEnabled")
            getMobileDataEnabledMethod.isAccessible = true
            return getMobileDataEnabledMethod.invoke(getConnectivityManager(context)) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 反射失败，默认开启
        return true
    }

    /**
     * 打印当前各种网络状态
     *
     * @return boolean
     */
    fun printNetworkInfo(context: Context): Boolean {
        val connectivity =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val `in` = connectivity.activeNetworkInfo
            LogUtils.i("getActiveNetworkInfo: $`in`")
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    // if (info[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    LogUtils.i("NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable)
                    LogUtils.i("NetworkInfo[" + i + "]isConnected : " + info[i].isConnected)
                    LogUtils.i("NetworkInfo[" + i + "]isConnectedOrConnecting : " + info[i].isConnectedOrConnecting)
                    LogUtils.i("NetworkInfo[" + i + "]: " + info[i])
                    // }
                }
                LogUtils.i("\n")
            } else {
                LogUtils.i("getAllNetworkInfo is null")
            }
        }
        return false
    }

    /**
     * get connected network type by [ConnectivityManager]
     *
     *
     * such as WIFI, MOBILE, ETHERNET, BLUETOOTH, etc.
     *
     * @return [ConnectivityManager.TYPE_WIFI], [ConnectivityManager.TYPE_MOBILE],
     * [ConnectivityManager.TYPE_ETHERNET]...
     */
    fun getConnectedTypeINT(context: Context): Int {
        val net = getConnectivityManager(context).activeNetworkInfo
        if (net != null) {
            LogUtils.i("NetworkInfo: $net")
            return net.type
        }
        return -1
    }

    /**
     * get network type by [TelephonyManager]
     *
     *
     * such as 2G, 3G, 4G, etc.
     *
     * @return [TelephonyManager.NETWORK_TYPE_CDMA], [TelephonyManager.NETWORK_TYPE_GPRS],
     * [TelephonyManager.NETWORK_TYPE_LTE]...
     */
    fun getTelNetworkTypeINT(context: Context): Int {
        return getTelephonyManager(context).networkType
    }

    /**
     * GPRS    2G(2.5) General Packet Radia Service 114kbps
     * EDGE    2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
     * UMTS    3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
     * CDMA    2G 电信 Code Division Multiple Access 码分多址
     * EVDO_0  3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
     * EVDO_A  3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
     * 1xRTT   2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
     * HSDPA   3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
     * HSUPA   3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
     * HSPA    3G (分HSDPA,HSUPA) High Speed Packet Access
     * IDEN    2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
     * EVDO_B  3G EV-DO Rev.B 14.7Mbps 下行 3.5G
     * LTE     4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
     * EHRPD   3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
     * HSPAP   3G HSPAP 比 HSDPA 快些
     *
     * @return [NetWorkType]
     */
    fun getNetworkType(context: Context): NetWorkType {
        val type = getConnectedTypeINT(context)
        return when (type) {
            ConnectivityManager.TYPE_WIFI -> NetWorkType.Wifi
            ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, ConnectivityManager.TYPE_MOBILE_HIPRI, ConnectivityManager.TYPE_MOBILE_MMS, ConnectivityManager.TYPE_MOBILE_SUPL -> {
                val teleType = getTelephonyManager(context).networkType
                when (teleType) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NetWorkType.Net2G
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NetWorkType.Net3G
                    TelephonyManager.NETWORK_TYPE_LTE -> NetWorkType.Net4G
                    else -> NetWorkType.UnKnown
                }
            }
            else -> NetWorkType.UnKnown
        }
    }

    enum class NetType(var value: Int) {
        None(1), Mobile(2), Wifi(4), Other(8);
    }

    enum class NetWorkType(var value: Int) {
        UnKnown(-1), Wifi(1), Net2G(2), Net3G(3), Net4G(4);
    }
}