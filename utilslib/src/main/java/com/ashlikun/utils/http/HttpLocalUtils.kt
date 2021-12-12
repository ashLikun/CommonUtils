package com.ashlikun.utils.http

import com.ashlikun.utils.AppUtils

object HttpLocalUtils {
    /**
     * 判断是否有网络
     */

    fun isNetworkAvailable(): Boolean {
        return NetWorkHelper.isNetworkAvailable()
    }

    /**
     * 判断mobile网络是否可用
     */
    fun isMobileDataEnable(): Boolean {
        return try {
            NetWorkHelper.isMobileDataEnable()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断wifi网络是否可用
     */
    fun isWifiDataEnable(): Boolean {
        return try {
            NetWorkHelper.isWifiDataEnable()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/3 14:59
     * 邮箱　　：496546144@qq.com
     *
     *
     * 方法功能：判断是否为漫游
     */
    fun isNetworkRoaming(): Boolean {
        return NetWorkHelper.isNetworkRoaming()
    }

    /**
     * 对网络资源文件判断路径 如果是已http开头的就返回这个值 否则在前面加上域名
     */
    fun getHttpFileUrl(baseUrl: String, url: String): String {
        var res = ""
        if (url != null) {
            res = if (url.startsWith("http://")) {
                url
            } else if (url.startsWith("/storage") || url.startsWith("storage") || url.startsWith("/data") || url.startsWith(
                    "data"
                )
            ) {
                url
            } else if (url.startsWith("/")) {
                baseUrl + url
            } else {
                "$baseUrl/$url"
            }
        }
        return res
    }
}