package com.ashlikun.utils.other;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/3　13:42
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：手机rom的工具
 */
public class RomUtils {
    public static final String ROM_XIAOMI = "XIAOMI";
    public static final String ROM_HUAWEI = "HUAWEI";
    public static final String ROM_FLYME = "FLYME";
    public static final String ROM_OPPO = "OPPO";
    //锤子
    public static final String ROM_SMARTISAN = "SMARTISAN";
    public static final String ROM_SAMSUNG = "SAMSUNG";
    public static final String ROM_VIVO = "VIVO";
    //360
    public static final String ROM_QIKU = "QIKU";
    public static final String ROM_360 = "360";


    public static boolean isXiaomi() {
        return check(ROM_XIAOMI);
    }

    public static boolean isHuawei() {
        return check(ROM_HUAWEI);
    }

    public static boolean isVivo() {
        return check(ROM_VIVO);
    }

    public static boolean isOppo() {
        return check(ROM_OPPO);
    }

    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public static boolean is360() {
        return check(ROM_QIKU) || check(ROM_360);
    }

    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public static boolean isSamsung() {
        return check(ROM_SAMSUNG);
    }

    private static boolean check(String rom) {
        String brand = DeviceUtil.getDeviceBrand();
        String manufacturer = DeviceUtil.getManufacturer();
        if (brand != null && rom.equals(brand.toUpperCase())) {
            return true;
        }
        if (manufacturer != null && rom.equals(manufacturer.toUpperCase())) {
            return true;
        }
        return false;
    }
}
