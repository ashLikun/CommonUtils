/*
 * @(#)DeviceHelper.java    Created on 2013-3-14
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.ashlikun.utils.other

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import com.ashlikun.utils.AppUtils.app
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 13:45 Administrator
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：获取设备信息的工具类
 * 权限：android.permission.TELEPHONY_SERVICE   ，  READ_PHONE_STATE
 *
 *
 * public String getTextAAA() {
 * StringBuffer sb = new StringBuffer();
 * sb.append("主板： "+ Build.BOARD);
 * sb.append("\n系统启动程序版本号： "+ Build.BOOTLOADER);
 * sb.append("\n系统定制商： "+ Build.BRAND);
 * sb.append("\ncpu指令集： "+ Build.CPU_ABI);
 * sb.append("\ncpu指令集2 "+ Build.CPU_ABI2);
 * sb.append("\n设置参数： "+ Build.DEVICE);
 * sb.append("\n显示屏参数：" + Build.DISPLAY);
 * sb.append("\n无线电固件版本：" + Build.getRadioVersion());
 * sb.append("\n硬件识别码： "+ Build.FINGERPRINT);
 * sb.append("\n硬件名称： "+ Build.HARDWARE);
 * sb.append("\nHOST: "+ Build.HOST);
 * sb.append("\nBuild.ID);"+ Build.ID);
 * sb.append("\n硬件制造商： "+ Build.MANUFACTURER);
 * sb.append("\n版本： "+ Build.MODEL);
 * sb.append("\n硬件序列号： "+ Build.SERIAL);
 * sb.append("\n手机制造商： "+ Build.PRODUCT);
 * sb.append("\n  描述Build的标签： "+ Build.TAGS);
 * sb.append("\nTIME: "+ Build.TIME);
 * sb.append("\nbuilder类型"+Build.TYPE);
 * sb.append("\nUSER: "+Build.USER);
 * return sb.toString();
 * }
 */
@SuppressLint("MissingPermission")
class DeviceUtil private constructor() {
    var uA = Build.MODEL

    // 唯一的设备ID，GSM手机的 IMEI 和 CDMA手机的 MEID
    var imei: String? = null
        private set

    // SIM卡的序列号：需要权限：READ_PHONE_STATE
    var sIM: String? = null
        private set

    // 设置软件的版本号：需要权限：READ_PHONE_STATE
    var mMobileVersion: String? = null

    // 当前注册的国家环境代码
    var netwrokIso: String? = null
        private set

    // 当前的连网类型
    var netType: String? = null
        private set

    // 唯一设备号
    var deviceID: String? = null
        private set
    var context: Context = app

    // 很多关于手机的信息可以用此类得到
    var telephonyManager: TelephonyManager? = null

    init {
        onRefresh()
    }

    /**
     * 刷新基本信息
     */
    fun onRefresh() {
        findData()
    }

    /**
     * 打印基本信息串
     *
     * @return
     */
    val deviceInfo: String
        get() {
            val info = StringBuffer()
            info.append("IMEI:").append(imei)
            info.append("\n")
            info.append("SIM:").append(sIM)
            info.append("\n")
            info.append("UA:").append(uA)
            info.append("\n")
            info.append("MobileVersion:").append(mMobileVersion)
            info.append("\n")
            info.append(callState)
            info.append("\n")
            info.append("SIM_STATE: ").append(simState)
            info.append("\n")
            info.append("SIM: ").append(sIM)
            info.append("\n")
            info.append(simOpertorName)
            info.append("\n")
            info.append(phoneType)
            info.append("\n")
            info.append(phoneSettings)
            info.append("\n")
            return info.toString()
        }

    /**
     * 获取sim卡的状态
     */
    val simState: String
        get() = when (telephonyManager?.simState) {
            TelephonyManager.SIM_STATE_UNKNOWN -> "未知SIM状态_" + TelephonyManager.SIM_STATE_UNKNOWN
            TelephonyManager.SIM_STATE_ABSENT -> "没插SIM卡_" + TelephonyManager.SIM_STATE_ABSENT
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> "锁定SIM状态_需要用户的PIN码解锁_" + TelephonyManager.SIM_STATE_PIN_REQUIRED
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> "锁定SIM状态_需要用户的PUK码解锁_" + TelephonyManager.SIM_STATE_PUK_REQUIRED
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "锁定SIM状态_需要网络的PIN码解锁_" + TelephonyManager.SIM_STATE_NETWORK_LOCKED
            TelephonyManager.SIM_STATE_READY -> "就绪SIM状态_" + TelephonyManager.SIM_STATE_READY
            else -> "未知SIM状态_" + TelephonyManager.SIM_STATE_UNKNOWN
        }

    /**
     * 获取手机信号类型
     *
     * @return
     */
    val phoneType: String
        get() = when (telephonyManager?.phoneType) {
            TelephonyManager.PHONE_TYPE_NONE -> "PhoneType: 无信号_" + TelephonyManager.PHONE_TYPE_NONE
            TelephonyManager.PHONE_TYPE_GSM -> "PhoneType: GSM信号_" + TelephonyManager.PHONE_TYPE_GSM
            TelephonyManager.PHONE_TYPE_CDMA -> "PhoneType: CDMA信号_" + TelephonyManager.PHONE_TYPE_CDMA
            else -> "PhoneType: 无信号_" + TelephonyManager.PHONE_TYPE_NONE
        }

    /**
     * 服务商名称：例如：中国移动、联通 　　 SIM卡的状态必须是 SIM_STATE_READY就绪状态(使用getSimState()判断).
     */
    val simOpertorName: String
        get() = if (telephonyManager?.simState == TelephonyManager.SIM_STATE_READY) {
            val sb = StringBuffer()
            sb.append("SimOperatorName: ").append(
                telephonyManager!!.simOperatorName
            )
            sb.append("\n")
            sb.append("SimOperator: ")
                .append(telephonyManager!!.simOperator)
            sb.append("\n")
            sb.append("Phone:").append(telephonyManager!!.line1Number)
            sb.toString()
        } else {
            val sb = StringBuffer()
            sb.append("SimOperatorName: ").append("未知")
            sb.append("\n")
            sb.append("SimOperator: ").append("未知")
            sb.toString()
        }

    /**
     * 获取手机的基本设置
     */
    val phoneSettings: String
        get() {
            val buf = StringBuffer()
            var str = Secure.getString(
                context.contentResolver,
                Secure.BLUETOOTH_ON
            )
            buf.append("蓝牙:")
            if (str == "0") {
                buf.append("禁用")
            } else {
                buf.append("开启")
            }
            str = Secure.getString(
                context.contentResolver,
                Secure.BLUETOOTH_ON
            )
            buf.append("WIFI:")
            buf.append(str)
            str = Secure.getString(
                context.contentResolver,
                Secure.INSTALL_NON_MARKET_APPS
            )
            buf.append("APP位置来源:")
            buf.append(str)
            return buf.toString()
        }

    /**
     * 电话活动的状态
     *
     * @return
     */
    val callState: String
        get() = when (telephonyManager!!.callState) {
            TelephonyManager.CALL_STATE_IDLE -> "电话状态[CallState]: 挂断"
            TelephonyManager.CALL_STATE_OFFHOOK -> "电话状态[CallState]: 接听"
            TelephonyManager.CALL_STATE_RINGING -> "电话状态[CallState]: 来电"
            else -> "电话状态[CallState]: 未知"
        }

    // 设置基本信息
    fun findData() {
        telephonyManager = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            imei = telephonyManager!!.deviceId
        } catch (e: Exception) {
        }
        try {
            mMobileVersion = telephonyManager!!.deviceSoftwareVersion
        } catch (e: Exception) {
        }
        try {
            netwrokIso = telephonyManager!!.networkCountryIso
        } catch (e: Exception) {
        }
        try {
            sIM = telephonyManager!!.simSerialNumber
        } catch (e: Exception) {
        }
        deviceID = deviceId
        try {
            val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            // WIFI/MOBILE
            netType = info.typeName
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {

        private val instance by lazy { DeviceUtil() }
        fun get(): DeviceUtil = instance

        /**
         * 设置手机立刻震动
         * @param milliseconds milliseconds/1000(S)
         */
        fun vibrate(milliseconds: Long) {
            val vib = app.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(milliseconds)
        }

        /**
         * 获得android设备-唯一标识，android2.2 之前无法稳定运行
         */
        val deviceId: String
            get() = Secure
                .getString(app.contentResolver, Secure.ANDROID_ID) ?: ""

        /**
         * 根据内容调用手机通讯录
         *
         * @param context
         * @param content
         */
        fun sendSmsByContent(context: Context, content: String) {
            val uri = Uri.parse("smsto:")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", content)
            context.startActivity(intent)
        }

        /**
         * androidId > imeiId > UUID
         * 组合设备唯一标识符，防止为空
         * androidId
         * imei标识符
         * 如果一个都没有，就生成一个UUID并持久化保存
         */
        val soleDeviceId: String
            get() {
                var res = deviceId //androidId
                if (res.isNotEmpty()) return res
                res = get().imei ?: "" //imei标识符
                if (res.isNotEmpty()) return res
                //如果一个都没有，就生成一个UUID并持久化保存
                return uUID
            }

        /**
         * 创建一个UUID并保存
         */
        val uUID: String
            get() {
                val sp = app.getSharedPreferences("Cache", Context.MODE_PRIVATE)
                var uuid = sp?.getString("uuid", "") ?: ""
                if (uuid.isEmpty()) {
                    uuid = UUID.randomUUID().toString()
                    sp?.edit()?.putString("uuid", uuid)?.apply()
                }
                return uuid
            }

        /**
         * 获取当前手机系统语言。
         *
         * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
         */
        val systemLanguage: String
            get() = Locale.getDefault().language

        /**
         * 获取当前系统上的语言列表(Locale列表)
         *
         * @return 语言列表
         */
        val systemLanguageList: Array<Locale>
            get() = Locale.getAvailableLocales()

        /**
         * 获取当前手机系统版本号
         *
         * @return 系统版本号
         */
        val systemVersion: String
            get() = Build.VERSION.RELEASE

        /**
         * 获取手机型号
         *
         * @return 手机型号
         */
        val systemModel: String
            get() = Build.MODEL

        /**
         * 生产/硬件的制造商。
         *
         * @return
         */
        @JvmStatic
        val manufacturer: String
            get() = Build.MANUFACTURER

        /**
         * 获取手机厂商
         *
         * @return 手机厂商
         */
        @JvmStatic
        val deviceBrand: String
            get() = Build.BRAND
    }


}