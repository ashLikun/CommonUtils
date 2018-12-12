package com.ashlikun.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

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
        //获取当前屏幕内容的高度
        int screenHeight = view.getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    public static final void setOnInputChang(final Activity activity, OnResizeListener listener) {
        FrameLayout content = activity.findViewById(android.R.id.content);
        final View mChildOfContent = content.getChildAt(0);
        //界面出现变动都会调用这个监听事件
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener(activity, listener));
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

    private static class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        Activity activity;
        OnResizeListener onResizeListener;
        int mNowh, mOldh;
        int mScreenHeight;

        public MyOnGlobalLayoutListener(Activity activity, OnResizeListener onResizeListener) {
            this.activity = activity;
            this.onResizeListener = onResizeListener;
        }

        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            if (mScreenHeight == 0) {
                mScreenHeight = r.bottom;
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
