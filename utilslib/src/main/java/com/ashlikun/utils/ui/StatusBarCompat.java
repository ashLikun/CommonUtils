package com.ashlikun.utils.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ashlikun.utils.other.RomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/7/5　13:27
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：状态栏的兼容
 */
public class StatusBarCompat {
    /**
     * 半透明颜色值
     */
    public static final int HALF_COLOR = 0x88aaaaaa;
    private Window window;
    private Context context;

    public StatusBarCompat(Activity activity) {
        this.window = activity.getWindow();
        this.context = activity;
    }

    public StatusBarCompat(Context context, Window window) {
        this.window = window;
        this.context = context;
    }

    /**
     * 设置状态栏字体颜色为深色
     */
    public void setStatusDarkColor() {
        setStatusTextColor(true);
    }

    /**
     * 设置状态栏字体浅色
     */
    public void setStatusLightColor() {
        setStatusTextColor(false);
    }

    public void setStatusTextColor(boolean drak) {
        if (!isSetStatusTextColor()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断当前是不是6.0以上的系统
            if (window != null) {
                View view = window.getDecorView();
                if (view != null) {
                    if (drak) {
                        //黑色
                        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        //白色,就是去除View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        if ((view.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0) {
                            view.setSystemUiVisibility(view.getSystemUiVisibility() ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }
                    }
                }
            }
        }

        //小米
        if (RomUtils.isXiaomi()) {
            miuiSetStatusBarLightMode(window, drak);
        }
        //魅族
        if (RomUtils.isFlyme()) {
            flymeSetStatusBarLightMode(window, drak);
        }
    }

    /**
     * 根据颜色值自动设置状态栏字体颜色
     *
     * @param color
     */
    public void autoStatueTextColor(int color) {
        if (isColorDrak(color)) {
            //浅色文字
            setStatusLightColor();
        } else {
            //深色文字
            setStatusDarkColor();
        }
    }


    public void setStatusBarColorRes(@ColorRes int statusColor) {
        setStatusBarColor(context.getResources().getColor(statusColor));
    }

    public void setStatusBarColorWhite() {
        setStatusBarColor(0xffffffff);
    }

    public void setStatusBarColorBlack() {
        setStatusBarColor(0xff000000);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/5 13:42
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：兼容设置状态栏颜色,要在设置完布局后设置
     * 本方法会自动设置状态栏字体颜色
     *
     * @param statusColor 状态栏颜色RES
     */
    public void setStatusBarColor(int statusColor) {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (!isSetStatusTextColor()) {
            //不能设置状态栏字体颜色时候
            if (!isColorDrak(statusColor)) {
                //颜色浅色,设置半透明
                statusColor = blendColor(HALF_COLOR, statusColor);
            }
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusColor);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
        autoStatueTextColor(statusColor);
    }

    /**
     * 使用跟布局插入状态栏
     * 这里没有设置状态栏字体颜色
     * 一般用于这个页面有类似微信的查看大图，解决返回的时候抖动问题
     */
    public void setFitsSystemWindows() {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mContentView != null) {
            mChildView.setFitsSystemWindows(true);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/3 0003 22:24
     * <p>
     * 方法功能：设置透明的状态栏，实际布局内容在状态栏里面
     */
    public void translucentStatusBar() {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (!isSetStatusTextColor()) {
            //不能设置状态栏字体颜色时候
            window.setStatusBarColor(StatusBarCompat.HALF_COLOR);
        } else {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        if (mContentView != null) {
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/3 0003 22:24
     * <p>
     * 方法功能：设置透明的状态栏，实际布局内容在状态栏里面
     */
    public void setStatusBarColorForCollapsingToolbar(final AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout,
                                                      Toolbar toolbar, final int statusColor) {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                return insets;
            }
        });

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }

        ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
        appBarLayout.setFitsSystemWindows(false);

        toolbar.setFitsSystemWindows(false);
        if (toolbar.getTag() == null) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            int statusBarHeight = getStatusBarHeight(context);
            lp.height += statusBarHeight;
            toolbar.setLayoutParams(lp);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
            toolbar.setTag(true);
        }

        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
            int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
            if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                window.setStatusBarColor(statusColor);
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        } else {
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        collapsingToolbarLayout.setFitsSystemWindows(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @SuppressLint("NewApi")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    if (window.getStatusBarColor() != statusColor) {
                        startColorAnimation(window.getStatusBarColor(), statusColor, collapsingToolbarLayout.getScrimAnimationDuration(), window);
                    }
                } else {
                    if (window.getStatusBarColor() != Color.TRANSPARENT) {
                        startColorAnimation(window.getStatusBarColor(), Color.TRANSPARENT, collapsingToolbarLayout.getScrimAnimationDuration(), window);
                    }
                }
            }
        });
        collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
        collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
    }

    /**
     * 设置页面顶部一个null的view的高度，
     * 只有当设置状态栏透明的时候用到,因为此时跟布局会顶到状态栏里面
     *
     * @param view
     */
    public static void setEmptyHeight(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int h = getStatusBarHeight(view.getContext());
            if (isSetHaleColor()) {
                view.setBackgroundColor(StatusBarCompat.HALF_COLOR);
            }
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            params.height = h;
            view.setLayoutParams(params);
        }
    }


    /**
     * 设置底部导航栏透明
     *
     * @param isTransparent 是否透明
     */
    public void setNavigationTransparent(boolean isTransparent) {
        if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isTransparent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/14 17:52
     * 邮箱　　：496546144@qq.com
     * 方法功能：一般用于fragment是透明状态栏得时候,调用这个方法
     */

    public static void setTransparentViewMargin(View viewTop) {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        ViewGroup.LayoutParams params = viewTop.getLayoutParams();
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) params).topMargin = ((ViewGroup.MarginLayoutParams) params).topMargin
                    + getStatusBarHeight(viewTop.getContext());
        }
        viewTop.setLayoutParams(params);
    }

    public static void setTransparentViewPadding(View viewTop) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewTop.setPadding(viewTop.getPaddingLeft(), viewTop.getPaddingTop() + getStatusBarHeight(viewTop.getContext())
                    , viewTop.getPaddingRight(), viewTop.getPaddingBottom());
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/5 13:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/3 0003 21:59
     * <p>
     * 方法功能：获取底部导航栏高度
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 这个颜色是不是深色的
     *
     * @param color
     * @return
     */
    public static boolean isColorDrak(int color) {
        //int t = (color >> 24) & 0xFF;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return r * 0.299 + g * 0.578 + b * 0.114 <= 192;
    }

    /**
     * 2个颜色混合
     *
     * @param fg 前景
     * @param bg 背景
     * @return
     */
    public static int blendColor(int fg, int bg) {
        int sca = Color.alpha(fg);
        int scr = Color.red(fg);
        int scg = Color.green(fg);
        int scb = Color.blue(fg);

        int dca = Color.alpha(bg);
        int dcr = Color.red(bg);
        int dcg = Color.green(bg);
        int dcb = Color.blue(bg);

        int color_r = dcr * (0xff - sca) / 0xff + scr * sca / 0xff;
        int color_g = dcg * (0xff - sca) / 0xff + scg * sca / 0xff;
        int color_b = dcb * (0xff - sca) / 0xff + scb * sca / 0xff;
        return ((color_r << 16) + (color_g << 8) + color_b) | (0xff000000);
    }

    /**
     * 是否设置半透明颜色
     * 6.0以下不能设置状态栏文字颜色,这里处理,5.0-6.0已经在设置状态栏颜色的时候设置了
     *
     * @return
     */
    public static boolean isSetHaleColor() {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    /**
     * use ValueAnimator to change statusBarColor when using collapsingToolbarLayout
     */
    @SuppressLint("NewApi")
    static void startColorAnimation(int startColor, int endColor, long duration, final Window window) {
        if (sAnimator != null) {
            sAnimator.cancel();
        }
        sAnimator = ValueAnimator.ofArgb(startColor, endColor)
                .setDuration(duration);
        sAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (window != null) {
                    window.setStatusBarColor((Integer) valueAnimator.getAnimatedValue());
                }
            }
        });
        sAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                sAnimator = null;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                sAnimator = null;
            }
        });
        sAnimator.start();
    }

    private static ValueAnimator sAnimator;

    /**
     * 是否可以设置状态栏颜色
     *
     * @return
     */
    public static boolean isSetStatusTextColor() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                (RomUtils.isHuawei() || RomUtils.isFlyme() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean flymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param window
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean miuiSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
}
