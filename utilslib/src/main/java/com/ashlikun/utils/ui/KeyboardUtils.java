package com.ashlikun.utils.ui;

import static com.ashlikun.utils.AppUtils.getApp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.ashlikun.utils.other.DimensUtils;
import com.ashlikun.utils.other.SharedPreUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/19　10:25
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：软键盘工具
 */
public class KeyboardUtils {

    private static final String EXTRA_DEF_KEYBOARDHEIGHT = "DEF_KEYBOARDHEIGHT";
    private static final int DEF_KEYBOARD_HEAGH_WITH_DP = 300;
    private static int sDefKeyboardHeight = -1;

    public static int getDefKeyboardHeight(Context context) {
        if (sDefKeyboardHeight < 0) {
            sDefKeyboardHeight = DimensUtils.dip2px(context, DEF_KEYBOARD_HEAGH_WITH_DP);
        }
        int height = SharedPreUtils.getInt(context, EXTRA_DEF_KEYBOARDHEIGHT, 0);
        return sDefKeyboardHeight = height > 0 && sDefKeyboardHeight != height ? height : sDefKeyboardHeight;
    }

    public static void setDefKeyboardHeight(Context context, int height) {
        if (sDefKeyboardHeight != height) {
            SharedPreUtils.putInt(context, EXTRA_DEF_KEYBOARDHEIGHT, height);
            sDefKeyboardHeight = height;
        }
    }

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
     * @param activity
     * @return
     */
    public static boolean isOpenInput(Activity activity) {
        return isOpenInput(activity.getWindow().getDecorView());
    }

    /**
     * 软键盘是否打开
     *
     * @param view 页面跟布局
     * @return
     */
    public static boolean isOpenInput(View view) {
        if (view == null) {
            return false;
        }
        //获取当前屏幕内容的高度
        int screenHeight = view.getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return Math.abs(screenHeight - rect.bottom) - 200 > 0;
    }

    public static final void setOnInputChang(final Activity activity, OnResizeListener listener) {
        FrameLayout content = activity.findViewById(android.R.id.content);
        final View mChildOfContent = content.getChildAt(0);
        //界面出现变动都会调用这个监听事件
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalLayoutListener(activity.getWindow().getDecorView(), listener));
    }

    public static final void setOnInputChang(final View view, OnResizeListener listener) {
        //界面出现变动都会调用这个监听事件
        view.getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalLayoutListener(view, listener));
    }

    public static final void setOnInputChang(final Window window, OnResizeListener listener) {
        //界面出现变动都会调用这个监听事件
        window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalLayoutListener(window.getDecorView(), listener));
    }

    public interface OnResizeListener {
        /**
         * 软键盘弹起
         */
        void onSoftPop(int height);

        /**
         * 软键盘关闭
         */
        void onSoftClose();
    }

    public static class KeyboardOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        View view;
        OnResizeListener onResizeListener;
        int mNowh, mOldh;
        int mScreenHeight;

        public KeyboardOnGlobalLayoutListener(View view, OnResizeListener onResizeListener) {
            this.view = view;
            this.onResizeListener = onResizeListener;
        }

        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            view.getWindowVisibleDisplayFrame(r);
            if (mScreenHeight == 0) {
                mScreenHeight = r.bottom;
            } else {
                //防止第一次的时候获取的是假的
                mScreenHeight = Math.max(mScreenHeight, r.bottom);
            }
            mNowh = mScreenHeight - r.bottom;
            if (mOldh != -1 && mNowh != mOldh) {
                if (mNowh > 0) {
                    if (onResizeListener != null) {
                        onResizeListener.onSoftPop(mNowh);
                    }
                } else {
                    if (onResizeListener != null) {
                        onResizeListener.onSoftClose();
                    }
                }
            }
            mOldh = mNowh;
        }
    }
}
