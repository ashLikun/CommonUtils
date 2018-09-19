package com.ashlikun.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static com.ashlikun.utils.AppUtils.getApp;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/19　10:25
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：软键盘工具
 */
public class KeyboardUtils {
    private static void exitOrShowInput(IBinder iBinder, View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (iBinder != null) {
            inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
        }
        if (view != null) {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInput(View view) {
        exitOrShowInput(null, view);
    }

    /**
     * 退出软键盘
     *
     * @param activity
     */
    public static void exitInput(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            exitOrShowInput(activity.getCurrentFocus().getWindowToken(), null);
        }
    }

    /**
     * 退出软键盘
     */
    public static void exitInput(IBinder iBinder) {
        exitOrShowInput(iBinder, null);
    }

    /**
     * 退出软键盘
     */
    public static void exitInput(View view) {
        exitOrShowInput(view.getWindowToken(), null);
    }

    /**
     * 软键盘是否打开
     *
     * @param context
     * @return
     */
    public static boolean isOpenInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        return isOpen;
    }
}
