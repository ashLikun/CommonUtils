package com.ashlikun.utils.ui;

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
    private Activity activity;

    public StatusBarCompat(Activity activity) {
        this.activity = activity;
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
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断当前是不是6.0以上的系统
            Window mWindow = activity.getWindow();
            if (mWindow != null) {
                View view = mWindow.getDecorView();
                if (view != null) {
                    if (drak) {
                        //黑色
                        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        //白色
                        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            }

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

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/5 13:42
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：兼容设置状态栏颜色,要在设置完布局后设置
     *
     * @param statusColor 状态栏颜色RES
     * @param alpha       第二个参数是颜色深度值
     */
    @SuppressLint("ResourceAsColor")
    public void setStatusBarColor(@ColorRes int statusColor, int alpha) {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //6.0以下不能设置状态栏文字颜色,这里处理以下
            if (!isColorDrak(statusColor)) {
                //颜色浅色,设置半透明
                statusColor = blendColor(HALF_COLOR, statusColor);
            }
        }
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusColor);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
        autoStatueTextColor(statusColor);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/3 0003 22:24
     * <p>
     * 方法功能：设置透明的状态栏，实际布局内容在状态栏里面
     */
    public void translucentStatusBar() {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //6.0以下加一个半透明
            window.setStatusBarColor(StatusBarCompat.HALF_COLOR);
        } else {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        final Window window = activity.getWindow();

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
            int statusBarHeight = getStatusBarHeight(activity);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
        if (activity != null && !activity.isFinishing() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isTransparent) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
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
     * 这里只设置4.4-5.0
     *
     * @return
     */
    public static boolean isSetHaleColor() {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
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
        sAnimator.start();
    }

    private static ValueAnimator sAnimator;
}
