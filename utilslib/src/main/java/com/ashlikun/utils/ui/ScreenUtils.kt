package com.ashlikun.utils.ui

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.AppUtils.app
import com.ashlikun.utils.other.RomUtils.isXiaomi
import com.ashlikun.utils.ui.extend.dp
import com.ashlikun.utils.ui.resources.ResUtils.getDimensionPixelSize
import kotlin.math.max

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.15 15:39
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：屏幕信息工具类
 */
object ScreenUtils {
    val metric: DisplayMetrics by lazy {
        val m = DisplayMetrics()
        val wm = app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(m)
        m
    }
    val width: Int
        get() = metric.widthPixels
    val density: Float
        get() = metric.density
    val densityDpi: Int
        get() = metric.densityDpi


    val height: Int
        get() {
            var screenHeight = metric.heightPixels
            if (isXiaomi && xiaomiNavigationGestureEnabled()) {
                screenHeight += getResourceNavHeight(app)
            }
            return screenHeight
        }


    /**
     * @param subDp 减去的dp大小
     * @return
     */
    fun width(subDp: Int = 0, subPx: Int = 0): Int {
        val displayMetrics = AppUtils.appResources.displayMetrics
        return displayMetrics.widthPixels - max(subPx, subDp.dp)
    }

    /**
     * @param subDp 减去的dp大小
     */
    fun height(subDp: Int = 0, subPx: Int = 0) = height - max(subPx, subDp.dp)


    /**
     * 判断是否有状态栏
     */
    fun hasStatusBar(activity: Activity): Boolean {
        val attrs = activity.window.attributes
        return attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != WindowManager.LayoutParams.FLAG_FULLSCREEN
    }

    /**
     * 获取状态栏高度
     */
    val statusBarHeight: Int
        get() {
            val resourceId = AppUtils.appResources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                return getDimensionPixelSize(resourceId)
            }
            return 0
        }

    /**
     * 获取虚拟菜单的高度,若无则返回0
     */
    val navigationBarHeight: Int
        get() {
            if (!isNavMenuExist(app)) return 0
            val resourceNavHeight = getResourceNavHeight(app)
            return if (resourceNavHeight >= 0) resourceNavHeight else getRealScreenSize(app)[1] - height
        }

    /**
     * 小米 MIX 有nav bar, 而 getRealScreenSize(context)[1] - getScreenHeight(context) = 0
     */
    private fun getResourceNavHeight(context: Context): Int {
        // 小米4没有nav bar, 而 navigation_bar_height 有值
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            getDimensionPixelSize(resourceId)
        } else -1
    }

    /**
     * 获取屏幕的真实宽高
     */
    private fun getRealScreenSize(context: Context): IntArray {
        val size = IntArray(2)
        var widthPixels: Int
        var heightPixels: Int
        val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels
        heightPixels = metrics.heightPixels
        try {
            // used when 17 > SDK_INT >= 14; includes window decorations (statusbar bar/menu bar)
            widthPixels = Display::class.java.getMethod("getRawWidth").invoke(d) as Int
            heightPixels = Display::class.java.getMethod("getRawHeight").invoke(d) as Int
        } catch (ignored: Exception) {
        }
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                // used when SDK_INT >= 17; includes window decorations (statusbar bar/menu bar)
                val realSize = Point()
                d.getRealSize(realSize)
                Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
                widthPixels = realSize.x
                heightPixels = realSize.y
            } catch (ignored: Exception) {
            }
        }
        size[0] = widthPixels
        size[1] = heightPixels
        return size
    }

    fun isNavMenuExist(context: Context): Boolean {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey && !hasBackKey
    }


    // ====================== Setting ===========================
    private const val VIVO_NAVIGATION_GESTURE = "navigation_gesture_on"
    private const val HUAWAI_DISPLAY_NOTCH_STATUS = "display_notch_status"
    private const val XIAOMI_DISPLAY_NOTCH_STATUS = "force_black"
    private const val XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar"

    /**
     * 获取vivo手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     *
     * @return false 表示使用的是虚拟导航键(NavigationBar)， true 表示使用的是手势， 默认是false
     */
    fun vivoNavigationGestureEnabled() =
        Settings.Secure.getInt(app.contentResolver, VIVO_NAVIGATION_GESTURE, 0) != 0

    fun xiaomiNavigationGestureEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(
                app.contentResolver,
                XIAOMI_FULLSCREEN_GESTURE,
                0
            ) != 0
        } else false
    }

    /**
     *  // 0: 默认
    // 1: 隐藏显示区域
     */
    fun huaweiIsNotchSetToShowInSetting() =
        Settings.Secure.getInt(app.contentResolver, HUAWAI_DISPLAY_NOTCH_STATUS, 0) == 0

    fun xiaomiIsNotchSetToShowInSetting() =
        Settings.Global.getInt(app.contentResolver, XIAOMI_DISPLAY_NOTCH_STATUS, 0) == 0
}