package com.ashlikun.utils.ui.status

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.RomUtils.isEssentialPhone
import com.ashlikun.utils.other.RomUtils.isMeizu
import com.ashlikun.utils.other.RomUtils.isHuawei
import com.ashlikun.utils.other.RomUtils.isXiaomi
import com.ashlikun.utils.other.RomUtils.isZTKC2016
import com.ashlikun.utils.other.RomUtils.isZUKZ1
import com.ashlikun.utils.ui.ScreenUtils
import com.ashlikun.utils.ui.extend.setLightStatusBars
import com.ashlikun.utils.ui.extend.showStatusBar
import com.ashlikun.utils.ui.resources.ColorUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 17:02
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：状态栏的兼容
 */

class StatusBarCompat(
    var activity: Activity
) {
    //是否适配刘海屏
    //在非全屏模式下，这个方法（LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES）在部分10.0手机会导致水波纹卡顿，所以不用
    var isNotch = StatusBarCompat.isNotchFullscreen
    var window = activity.window


    /**
     * 设置状态栏字体颜色为深色
     */
    fun setStatusDarkColor() {
        setStatusTextColor(true)
    }

    /**
     * 设置状态栏字体浅色
     */
    fun setStatusLightColor() {
        setStatusTextColor(false)
    }

    fun setStatusTextColor(drak: Boolean) {
        if (!isSetStatusTextColor) {
            return
        }
        window.setLightStatusBars(drak)

        //小米
        if (isXiaomi) {
            miuiSetStatusBarLightMode(window, drak)
        }
        //魅族
        if (isMeizu) {
            flymeSetStatusBarLightMode(window, drak)
        }
    }

    /**
     * 根据颜色值自动设置状态栏字体颜色
     *
     * @param color
     */
    fun autoStatueTextColor(color: Int) {
        if (ColorUtils.isColorDrak(color)) {
            //浅色文字
            setStatusLightColor()
        } else {
            //深色文字
            setStatusDarkColor()
        }
    }

    fun setStatusBarColorRes(@ColorRes statusColor: Int) {
        setStatusBarColor(activity.resources.getColor(statusColor))
    }

    fun setStatusBarColorWhite() {
        setStatusBarColor(-0x1)
    }

    fun setStatusBarColorBlack() {
        setStatusBarColor(-0x1000000)
    }

    /**
     * 兼容设置状态栏颜色,要在设置完布局后设置
     * 本方法会自动设置状态栏字体颜色
     *
     * @param statusColor 状态栏颜色RES
     */
    fun setStatusBarColor(statusColor: Int) {
        //5.0以下不设置
        var statusColor = statusColor
        if (!isSetStatusColor) {
            return
        }
        if (isNotch) {
            notch()
        }
        if (!isSetStatusTextColor) {
            //不能设置状态栏字体颜色时候
            if (!ColorUtils.isColorDrak(statusColor)) {
                //颜色浅色,设置半透明
                statusColor = ColorUtils.blendColor(HALF_COLOR, statusColor)
            }
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = statusColor
        window.showStatusBar()
        window.decorView.fitsSystemWindows = false
        window.decorView.requestApplyInsets()
        autoStatueTextColor(statusColor)
    }

    /**
     * 使用跟布局插入状态栏
     * 这里没有设置状态栏字体颜色
     * 一般用于这个页面有类似微信的查看大图，解决返回的时候抖动问题
     */
    fun setFitsSystemWindows() {
        //5.0以下不设置
        if (!isSetStatusColor) {
            return
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.fitsSystemWindows = false
        window.decorView.requestApplyInsets()
    }

    /**
     * 设置底部导航栏透明
     *
     * @param isTransparent 是否透明
     */
    fun setNavigationTransparent(isTransparent: Boolean = true) {
        if (window != null && isSetStatusColor) {
            if (isTransparent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    /**
     * 设置透明状态栏时候，顶部的padding
     *
     * @param isNeedAndroidMHalf 6.0以下是否绘制半透明,因为不能设置状态栏字体颜色
     */
    fun translucentStatusBar(isNeedAndroidMHalf: Boolean = false) {
        //5.0以下不设置
        if (!isSetStatusColor) {
            return
        }
        notch()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (isNeedAndroidMHalf && !isSetStatusTextColor) {
            //不能设置状态栏字体颜色时候
            window.statusBarColor = HALF_COLOR
        } else {
            window.statusBarColor = Color.TRANSPARENT
        }
        //透明全屏
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.fitsSystemWindows = false
        window.decorView.requestApplyInsets()
    }

    /**
     * 刘海屏适配
     */
    fun notch() {
        if (NotchHelper.isNotchOfficialSupport()) {
            handleDisplayCutoutMode(window)
        }
    }

    /**
     * 设置透明的状态栏，实际布局内容在状态栏里面
     */
    fun setStatusBarColorForCollapsingToolbar(
        appBarLayout: AppBarLayout, collapsingToolbarLayout: CollapsingToolbarLayout,
        toolbar: Toolbar, statusColor: Int
    ) {
        //5.0以下不设置
        if (!isSetStatusColor) {
            return
        }
        notch()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout) { v, insets -> insets }
        window.decorView.fitsSystemWindows = false
        window.decorView.requestApplyInsets()
        (appBarLayout.parent as View).fitsSystemWindows = false
        appBarLayout.fitsSystemWindows = false
        toolbar.fitsSystemWindows = false
        if (toolbar.tag == null) {
            val lp = toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
            val statusBarHeight = ScreenUtils.statusBarHeight
            lp.height += statusBarHeight
            toolbar.layoutParams = lp
            toolbar.setPadding(
                toolbar.paddingLeft,
                toolbar.paddingTop + statusBarHeight,
                toolbar.paddingRight,
                toolbar.paddingBottom
            )
            toolbar.tag = true
        }
        val behavior = (appBarLayout.layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior != null && behavior is AppBarLayout.Behavior) {
            val verticalOffset = behavior.topAndBottomOffset
            if (Math.abs(verticalOffset) > appBarLayout.height - collapsingToolbarLayout.scrimVisibleHeightTrigger) {
                window.statusBarColor = statusColor
            } else {
                window.statusBarColor = Color.TRANSPARENT
            }
        } else {
            window.statusBarColor = Color.TRANSPARENT
        }
        collapsingToolbarLayout.fitsSystemWindows = false
        appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) > appBarLayout.height - collapsingToolbarLayout.scrimVisibleHeightTrigger) {
                if (window.statusBarColor != statusColor) {
                    startColorAnimation(
                        window.statusBarColor,
                        statusColor,
                        collapsingToolbarLayout.scrimAnimationDuration,
                        window
                    )
                }
            } else {
                if (window.statusBarColor != Color.TRANSPARENT) {
                    startColorAnimation(
                        window.statusBarColor,
                        Color.TRANSPARENT,
                        collapsingToolbarLayout.scrimAnimationDuration,
                        window
                    )
                }
            }
        })
        collapsingToolbarLayout.getChildAt(0).fitsSystemWindows = false
        collapsingToolbarLayout.setStatusBarScrimColor(statusColor)
    }

    companion object {
        //是否适配刘海屏
        //在非全屏模式下，这个方法（LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES）在部分10.0手机会导致水波纹卡顿，所以不用
        var isNotchFullscreen = true

        /**
         * 半透明颜色值
         */
        const val HALF_COLOR = -0x77555556
        private var sAnimator: ValueAnimator? = null

        /**
         * 设置状态栏空View的高度
         * 设置页面顶部一个null的view的高度，
         * 只有当设置状态栏透明的时候用到,因为此时跟布局会顶到状态栏里面
         */
        fun setEmptyHeight(view: View?, isNeedAndroidMHalf: Boolean = false) {
            if (view == null) return
            if (isSetStatusColor) {
                val h = ScreenUtils.statusBarHeight
                if (isNeedAndroidMHalf && isSetHaleColor) {
                    view.setBackgroundColor(HALF_COLOR)
                }
                var params = view.layoutParams as ViewGroup.LayoutParams
                if (params == null) {
                    params = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                params.height = h
                view.layoutParams = params
            }
        }

        /**
         * 一般用于fragment是透明状态栏得时候,调用这个方法
         */
        fun setTransparentViewMargin(viewTop: View?) {
            if (viewTop == null) return
            //5.0以下不设置
            if (!isSetStatusColor) {
                return
            }
            val params = viewTop.layoutParams
            if (params is MarginLayoutParams) {
                params.topMargin = (params.topMargin
                        + ScreenUtils.statusBarHeight)
            }
            viewTop.layoutParams = params
        }

        /**
         * 设置状态栏view的padding
         */
        fun setTransparentViewPadding(viewTop: View?) {
            if (viewTop == null) return
            if (isSetStatusColor) {
                viewTop.setPadding(
                    viewTop.paddingLeft,
                    viewTop.paddingTop + ScreenUtils.statusBarHeight,
                    viewTop.paddingRight,
                    viewTop.paddingBottom
                )
            }
        }

        /**
         * 获取底部导航栏高度
         */
        fun getNavigationHeight(): Int {
            var result = 0
            val resourceId =
                AppUtils.defaultResources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = AppUtils.defaultResources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        /**
         * 是否设置半透明颜色
         * 6.0以下不能设置状态栏文字颜色,这里处理,5.0-6.0已经在设置状态栏颜色的时候设置了
         */
        val isSetHaleColor: Boolean
            get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

        /**
         * 当使用collapsingToolbarLayout布局时，使用ValueAnimator来改变statusBarColor
         */
        @SuppressLint("NewApi")
        fun startColorAnimation(startColor: Int, endColor: Int, duration: Long, window: Window?) {
            sAnimator?.cancel()
            sAnimator = ValueAnimator.ofArgb(startColor, endColor)
                .setDuration(duration)
            sAnimator?.addUpdateListener { valueAnimator ->
                if (window != null) {
                    window.statusBarColor = (valueAnimator.animatedValue as Int)
                }
            }
            sAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    sAnimator = null
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    sAnimator = null
                }
            })
            sAnimator?.start()
        }


        /**
         * 是否可以设置状态栏字体颜色
         */
        val isSetStatusTextColor: Boolean
            // Essential Phone 在 Android 8 之前沉浸式做得不全，系统不从状态栏顶部开始布局却会下发 WindowInsets
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    (isHuawei || isMeizu ||
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !(isZUKZ1 || isZTKC2016))

        /**
         * 是否可以设置状态栏颜色
         */
        val isSetStatusColor: Boolean
            // Essential Phone 在 Android 8 之前沉浸式做得不全，系统不从状态栏顶部开始布局却会下发 WindowInsets
            get() = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && !(isEssentialPhone && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP))

        /**
         * 设置状态栏图标为深色和魅族特定的文字风格
         * 可以用来判断是否为Flyme用户
         *
         * @param window 需要设置的窗口
         * @param dark   是否把状态栏文字及图标颜色设置为深色
         * @return boolean 成功执行返回true
         */
        fun flymeSetStatusBarLightMode(window: Window, dark: Boolean): Boolean {
            var result = false
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
            }
            return result
        }

        /**
         * 需要MIUIV6以上
         *
         * @param window
         * @param dark   是否把状态栏文字及图标颜色设置为深色
         * @return boolean 成功执行返回true
         */
        fun miuiSetStatusBarLightMode(window: Window, dark: Boolean): Boolean {
            var result = false
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
                } else {
                    //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag)
                }
                result = true
            } catch (e: Exception) {
            }
            return result
        }

        /**
         * 刘海屏状态栏
         *
         * @param window
         */
        @TargetApi(28)
        private fun handleDisplayCutoutMode(window: Window) {
//            val decorView = window.decorView
//            if (decorView != null) {
//                if (ViewCompat.isAttachedToWindow(decorView)) {
//                    realHandleDisplayCutoutMode(window, decorView)
//                } else {
//                    decorView.addOnAttachStateChangeListener(object :
//                        View.OnAttachStateChangeListener {
//                        override fun onViewAttachedToWindow(v: View) {
//                            v.removeOnAttachStateChangeListener(this)
//                            realHandleDisplayCutoutMode(window, v)
//                        }
//
//                        override fun onViewDetachedFromWindow(v: View) {}
//                    })
//                }
//            }
            realHandleDisplayCutoutMode(window)
        }

        /**
         * 刘海屏状态栏
         */
        @TargetApi(28)
        private fun realHandleDisplayCutoutMode(window: Window) {
            val params = window.attributes
            //该窗口始终允许延伸到屏幕短边上的DisplayCutout区域
            //在非全屏模式下，这个方法（LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES）在部分10.0手机会导致水波纹卡顿，所以不用
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }
}