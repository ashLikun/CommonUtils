package com.ashlikun.utils.other

import android.os.Build
import com.ashlikun.utils.other.DeviceUtil.Companion.deviceBrand
import com.ashlikun.utils.other.DeviceUtil.Companion.manufacturer

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 16:38
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：手机rom的工具
 */

object RomUtils {
    const val ROM_XIAOMI = "XIAOMI"
    const val ROM_HUAWEI = "HUAWEI"
    const val ROM_FLYME = "FLYME"
    const val ROM_MEIZU = "MEIZU"
    const val ROM_OPPO = "OPPO"
    const val ROM_OnePlus = "OnePlus"

    //锤子
    const val ROM_SMARTISAN = "SMARTISAN"
    const val ROM_SAMSUNG = "SAMSUNG"
    const val ROM_VIVO = "VIVO"

    //360
    const val ROM_QIKU = "QIKU"
    const val ROM_360 = "360"
    const val ZUKZ1 = "zuk z1"
    const val ZTEC2016 = "zte c2016"

    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    val isZUKZ1: Boolean
        get() = Build.MODEL != null && Build.MODEL.contains(ZUKZ1, true)

    val isXiaomi: Boolean
        get() = check(ROM_XIAOMI)
    val isHuawei: Boolean
        get() = check(ROM_HUAWEI)
    val isVivo: Boolean
        get() = check(ROM_VIVO)
    val isOppo: Boolean
        get() = check(ROM_OPPO)
    val isMeizu: Boolean
        get() = check(ROM_FLYME) || check(ROM_MEIZU)
    val isOnePlus: Boolean
        get() = check(ROM_OnePlus)

    fun is360(): Boolean {
        return check(ROM_QIKU) || check(ROM_360)
    }

    val isSmartisan: Boolean
        get() = check(ROM_SMARTISAN)
    val isSamsung: Boolean
        get() = check(ROM_SAMSUNG)
    val isZTKC2016: Boolean
        get() {
            val board = Build.MODEL
            return board != null && board.uppercase().contains(ZTEC2016)
        }
    val isEssentialPhone: Boolean
        get() = deviceBrand.contains("essential")

    private fun check(rom: String): Boolean {
        if (rom.uppercase() == deviceBrand.uppercase()) return true
        if (rom.uppercase() == manufacturer.uppercase()) return true
        return false
    }
}