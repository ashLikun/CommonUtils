package com.ashlikun.utils.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashlikun.utils.R;


/**
 * 作者　　: 李坤
 * 创建时间: 16:35 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：底部弹出消息Snackbar的工具类
 */

public class SnackbarUtil {

    public static final int Info = 1;
    public static final int Confirm = 2;
    public static final int Warning = 3;//警告 orange
    public static final int Error = 4;//错误 red


    public static int red = 0xfff44336;
    public static int green = 0xff4caf50;
    public static int blue = 0xff2195f3;
    public static int orange = 0xffffc107;

    /**
     * 自定义时常显示Snackbar，自定义颜色
     *
     * @param view
     * @param message
     * @param messageColor
     * @param backgroundColor
     * @return
     */
    public static Snackbar showIndefinite(View view, String message, int duration, int messageColor, int backgroundColor) {
        if (view == null || message == null) {
            return null;
        }
        KeyboardUtils.exitInput(view);
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setDuration(duration);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        return snackbar;
    }

    public static Snackbar showShort(Activity activity, String message, int type) {
        if (activity == null || message == null) {
            return null;
        }
        KeyboardUtils.exitInput(activity);
        Snackbar snackbar = Snackbar.make(UiUtils.getRootView(activity), message, Snackbar.LENGTH_SHORT);
        switchType(snackbar, type);
        return snackbar;
    }

    public static Snackbar showLong(Activity activity, String message, int type) {
        if (activity == null || message == null) {
            return null;
        }
        KeyboardUtils.exitInput(activity);
        Snackbar snackbar = Snackbar.make(UiUtils.getRootView(activity), message, Snackbar.LENGTH_LONG);
        switchType(snackbar, type);
        return snackbar;
    }

    public static Snackbar showIndefinite(Activity activity, String message, int type) {
        if (activity == null || message == null) {
            return null;
        }
        KeyboardUtils.exitInput(activity);
        Snackbar snackbar = Snackbar.make(UiUtils.getRootView(activity), message, Snackbar.LENGTH_INDEFINITE);
        switchType(snackbar, type);
        return snackbar;
    }

    /**
     * 短显示Snackbar，可选预设类型
     *
     * @param view
     * @param message
     * @param type
     * @return
     */
    public static Snackbar showShort(View view, String message, int type) {
        if (view == null || message == null) {
            return null;
        }
        KeyboardUtils.exitInput(view);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        switchType(snackbar, type);
        return snackbar;
    }

    /**
     * 长显示Snackbar，可选预设类型
     *
     * @param view
     * @param message
     * @param type
     * @return
     */
    public static Snackbar showLong(View view, String message, int type) {
        if (view == null || message == null) {
            return null;
        }
        KeyboardUtils.exitInput(view);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        switchType(snackbar, type);
        return snackbar;
    }

    /**
     * 自定义时常显示Snackbar，可选预设类型
     *
     * @param view
     * @param message
     * @param type
     * @return
     */
    public static Snackbar showIndefinite(View view, String message, int duration, int type) {
        if (view == null || message == null) {
            return null;
        }
        KeyboardUtils.exitInput(view);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setDuration(duration);
        switchType(snackbar, type);
        return snackbar;
    }

    /**
     * 选择预设类型
     * @param snackbar
     * @param type
     */
    private static void switchType(Snackbar snackbar, int type) {
        switch (type) {
            case Info:
                setSnackbarColor(snackbar, Color.WHITE, blue);
                break;
            case Confirm:
                setSnackbarColor(snackbar, Color.WHITE, green);
                break;
            case Warning:
                setSnackbarColor(snackbar, Color.WHITE, orange);
                break;
            case Error:
                setSnackbarColor(snackbar, Color.YELLOW, red);
                break;
        }
    }

    /**
     * 设置Snackbar背景颜色
     *
     * @param snackbar
     * @param backgroundColor
     */
    public static void setSnackbarColor(Snackbar snackbar, int backgroundColor) {
        View view = snackbar.getView();
        view.setFitsSystemWindows(false);
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
        }
    }

    /**
     * 设置Snackbar文字和背景颜色
     *
     * @param snackbar
     * @param messageColor
     * @param backgroundColor
     */
    public static void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
        View view = snackbar.getView();
        view.setFitsSystemWindows(false);
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(messageColor);
        }
    }

    /**
     * 向Snackbar中添加view
     *
     * @param snackbar
     * @param layoutId
     * @param index    新加布局在Snackbar中的位置
     */
    public static void SnackbarAddView(Snackbar snackbar, int layoutId, int index) {
        View snackbarview = snackbar.getView();
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarview;

        View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId, null);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = Gravity.CENTER_VERTICAL;

        snackbarLayout.addView(add_view, index, p);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/22 11:12
     * <p>
     * 方法功能：显示信息
     */
    public static void showSnackbar(final Activity activity, View view, String result, int type, boolean isFinish, final Snackbar.Callback callback) {
        if (result != null) {
            final Snackbar snackbar = SnackbarUtil.showLong(view, result, type);

            if (isFinish) {
                final DialogTransparency dialog = new DialogTransparency(activity);
                dialog.show();
                snackbar.setCallback(isFinish ? new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        dialog.dismiss();
                        if (callback != null) {
                            callback.onDismissed(snackbar, event);
                        } else {
                            activity.finish();
                        }
                    }

                } : null);
            } else {
                snackbar.setAction(R.string.snackbar_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }


            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }
    }

}