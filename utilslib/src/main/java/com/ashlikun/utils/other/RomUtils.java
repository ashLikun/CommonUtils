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
    private final static String ZUKZ1 = "zuk z1";
    private final static String ZTEC2016 = "zte c2016";

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

    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    public static boolean isZUKZ1() {
        final String board = android.os.Build.MODEL;
        return board != null && board.toLowerCase().contains(ZUKZ1);
    }

    public static boolean isZTKC2016() {
        final String board = android.os.Build.MODEL;
        return board != null && board.toLowerCase().contains(ZTEC2016);
    }

    public static boolean isEssentialPhone() {
        return DeviceUtil.getDeviceBrand().contains("essential");
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
