package com.ashlikun.utils.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.DimensUtils;
import com.ashlikun.utils.other.RomUtils;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/22 15:07
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：屏幕信息工具类
 */
public class ScreenInfoUtils {
    private static DisplayMetrics metric;

    public static int getWidth() {
        init();
        return metric.widthPixels;
    }

    private static void init() {
        if (metric == null) {
            metric = new DisplayMetrics();
            WindowManager wm = (WindowManager) AppUtils.getApp().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metric);
        }
    }


    public static int getHeight() {
        init();
        int screenHeight = metric.heightPixels;
        if (RomUtils.isXiaomi() && xiaomiNavigationGestureEnabled()) {
            screenHeight += getResourceNavHeight(AppUtils.getApp());
        }
        return screenHeight;
    }

    public static int width() {
        return widthSub(0);
    }

    /**
     * @param subDp 减去的dp大小
     * @return
     */
    public static int widthSubDp(int subDp) {
        return widthSub(DimensUtils.dip2px(subDp));
    }

    /**
     * @param subPx 减去的px大小
     * @return
     */
    public static int widthSub(int subPx) {
        if (AppUtils.getApp() == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = AppUtils.getApp().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels - subPx;
    }

    public static int height() {
        return heightSub(0);
    }

    /**
     * @param subDp 减去的dp大小
     * @return
     */
    public static int heightSubDp(int subDp) {
        return heightSub(DimensUtils.dip2px(subDp));
    }

    /**
     * @param subPx 减去的px大小
     * @return
     */
    public static int heightSub(int subPx) {
        if (AppUtils.getApp() == null) {
            return 0;
        }
        return getHeight() - subPx;
    }

    /**
     * 判断是否有状态栏
     *
     * @return
     */
    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        return (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = AppUtils.getApp().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ResUtils.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取虚拟菜单的高度,若无则返回0
     */
    public static int getNavigationBarHeight() {
        if (!isNavMenuExist(AppUtils.getApp())) {
            return 0;
        }
        int resourceNavHeight = getResourceNavHeight(AppUtils.getApp());
        if (resourceNavHeight >= 0) {
            return resourceNavHeight;
        }

        // 小米 MIX 有nav bar, 而 getRealScreenSize(context)[1] - getScreenHeight(context) = 0
        return getRealScreenSize(AppUtils.getApp())[1] - getHeight();
    }

    private static int getResourceNavHeight(Context context) {
        // 小米4没有nav bar, 而 navigation_bar_height 有值
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return ResUtils.getDimensionPixelSize(resourceId);
        }
        return -1;
    }

    /**
     * 获取屏幕的真实宽高
     *
     * @param context
     * @return
     */
    private static int[] getRealScreenSize(Context context) {
        int[] size = new int[2];
        int widthPixels, heightPixels;
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        try {
            // used when 17 > SDK_INT >= 14; includes window decorations (statusbar bar/menu bar)
            widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
            heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
        } catch (Exception ignored) {
        }
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                // used when SDK_INT >= 17; includes window decorations (statusbar bar/menu bar)
                Point realSize = new Point();
                d.getRealSize(realSize);


                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        }

        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }

    public static boolean isNavMenuExist(Context context) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }


    public static float getDensity() {
        init();
        return metric.density;
    }


    public static DisplayMetrics getDisplayMetrics() {
        init();
        return metric;
    }

    public static int getDensityDpi() {
        init();
        return metric.densityDpi;
    }

    // ====================== Setting ===========================
    private static final String VIVO_NAVIGATION_GESTURE = "navigation_gesture_on";
    private static final String HUAWAI_DISPLAY_NOTCH_STATUS = "display_notch_status";
    private static final String XIAOMI_DISPLAY_NOTCH_STATUS = "force_black";
    private static final String XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar";

    /**
     * 获取vivo手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     *
     * @return false 表示使用的是虚拟导航键(NavigationBar)， true 表示使用的是手势， 默认是false
     */
    public static boolean vivoNavigationGestureEnabled() {
        int val = Settings.Secure.getInt(AppUtils.getApp().getContentResolver(), VIVO_NAVIGATION_GESTURE, 0);
        return val != 0;
    }


    public static boolean xiaomiNavigationGestureEnabled() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(AppUtils.getApp().getContentResolver(), XIAOMI_FULLSCREEN_GESTURE, 0) != 0;
        }
        return false;
    }


    public static boolean huaweiIsNotchSetToShowInSetting() {
        // 0: 默认
        // 1: 隐藏显示区域
        int result = Settings.Secure.getInt(AppUtils.getApp().getContentResolver(), HUAWAI_DISPLAY_NOTCH_STATUS, 0);
        return result == 0;
    }

    @TargetApi(17)
    public static boolean xiaomiIsNotchSetToShowInSetting() {
        return Settings.Global.getInt(AppUtils.getApp().getContentResolver(), XIAOMI_DISPLAY_NOTCH_STATUS, 0) == 0;
    }
}
