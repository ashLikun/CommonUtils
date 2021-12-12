package com.ashlikun.utils.ui;


import static com.ashlikun.utils.AppUtils.getApp;

import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.ashlikun.utils.other.DimensUtils;
import com.ashlikun.utils.other.MainHandle;


public class ToastUtils {

    private static Toast myToast = null;
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;

    public static Toast getMyToast() {
        initToast(false);
        return myToast;
    }

    /**
     * 默认Toast，时间短
     *
     * @param content
     */
    public static void getToastShort(String content) {
        Toast.makeText(getApp(), content, Toast.LENGTH_SHORT).show();
    }


    /**
     * 默认Toast，时间长
     *
     * @param content 内容
     */
    public static void getToastLong(String content) {
        Toast.makeText(getApp(), content, Toast.LENGTH_LONG).show();
    }

    public static void show(String text, int duration) {
        show(text, false, duration);
    }

    public static void show(String text, boolean cancelBefore, int duration) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MainHandle.post(new Runnable() {
                @Override
                public void run() {
                    cretae(text, cancelBefore, duration, Gravity.BOTTOM, 0, DimensUtils.dip2px(getApp(), 20));
                }
            });
        } else {
            cretae(text, cancelBefore, duration, Gravity.BOTTOM, 0, DimensUtils.dip2px(getApp(), 20));
        }
    }


    public static void showLong(String text) {
        show(text, false, LENGTH_LONG);
    }

    public static void showShort(String text) {
        show(text, false, LENGTH_SHORT);
    }

    public static void showLong(String text, boolean cancelBefore) {
        show(text, cancelBefore, LENGTH_LONG);
    }

    public static void showShort(String text, boolean cancelBefore) {
        show(text, cancelBefore, LENGTH_SHORT);
    }

    private static void initToast(boolean cancelBefore) {
        if (myToast == null) {
            myToast = Toast.makeText(getApp(), "", Toast.LENGTH_SHORT);
        } else {
            if (cancelBefore) {
                myToast.cancel();
            }
            myToast = Toast.makeText(getApp(), "", Toast.LENGTH_SHORT);
        }
    }

    public static void show(String text, boolean cancelBefore, int duration,
                            int gravity, int xOffsetDp, int yOffsetDp) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MainHandle.post(new Runnable() {
                @Override
                public void run() {
                    cretae(text, cancelBefore, duration, gravity, xOffsetDp, yOffsetDp);
                }
            });
        } else {
            cretae(text, cancelBefore, duration, gravity, xOffsetDp, yOffsetDp);
        }
    }

    //要在主线程
    public static void cretae(String text, boolean cancelBefore, int duration,
                              int gravity, int xOffsetDp, int yOffsetDp) {
        initToast(cancelBefore);
        if (myToast != null) {
            myToast.setGravity(gravity, DimensUtils.dip2px(getApp(), xOffsetDp),
                    DimensUtils.dip2px(getApp(), yOffsetDp));
            myToast.setText(text);
            myToast.setDuration(duration);
            myToast.show();
        }
    }


}
