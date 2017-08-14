package com.ashlikun.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/7/5　13:27
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：状态栏的兼容
 */
public class StatusBarCompat {

    private Activity activity;

    public StatusBarCompat(Activity activity) {
        this.activity = activity;
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
    public void setColorBar(@ColorRes int statusColor, int alpha) {
        //4.4以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        int color = calculateColor(statusColor, alpha);//计算最终颜色
        Window window = activity.getWindow();
        //5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
        //4.4版本
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView();
            contentView.addView(createStatusBarView(color));
            setFitsSystemWindows(true);
        }
    }

    public void setColorBar(@ColorRes int statusColor) {
        setColorBar(statusColor, 0);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/3 0003 22:24
     * <p>
     * 方法功能：设置透明的状态栏，实际布局内容在状态栏里面
     */
    public void setTransparentBar(@ColorRes int statusColor, int alpha) {
        //5.0以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        Window window = activity.getWindow();
        int colorInt = activity.getResources().getColor(statusColor);
        //计算最终颜色
        int color = (alpha == 0 || colorInt == 0) ? Color.TRANSPARENT :
                Color.argb(alpha, Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt));
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        window.setStatusBarColor(color);

    }

    public void setTransparentBar(@ColorRes int statusColor) {
        setTransparentBar(statusColor, 255);
    }

    public void setTransparentBar() {
        setTransparentBar(android.R.color.transparent, 0);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/3 0003 22:02
     * <p>
     * 方法功能：把第一个viewgroup顶到状态栏里面去
     */

    private void setFitsSystemWindows(boolean fit) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        parent.setFitsSystemWindows(fit);
        parent.setClipToPadding(fit);
    }

    private View createStatusBarView(@ColorInt int color) {
        View mStatusBarTintView = new View(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        params.gravity = Gravity.TOP;
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(color);
        return mStatusBarTintView;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/3 0003 22:11
     * <p>
     * 方法功能：计算颜色深度
     */

    @ColorInt
    private int calculateColor(@ColorRes int color, int alpha) {
        int colorint = activity.getResources().getColor(color);
        if (alpha <= 0) {
            return colorint;
        }
        float a = 1 - alpha / 255f;
        int red = colorint >> 16 & 0xff;
        int green = colorint >> 8 & 0xff;
        int blue = colorint & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
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

    public static void setTextDarkColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
