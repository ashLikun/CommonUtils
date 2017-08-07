package com.ashlikun.utils.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.ashlikun.utils.other.DimensUtils;

import static com.ashlikun.utils.Utils.getApp;


public class ToastUtils {

    private static Toast myToast = null;
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;


    /**
     * 默认Toast，时间短
     *
     * @param content
     */
    public static void getToastShort(String content) {
        myToast.makeText(getApp(), content, Toast.LENGTH_SHORT).show();
    }


    /**
     * 默认Toast，时间长
     *
     * @param content 内容
     */
    public static void getToastLong(String content) {
        myToast.makeText(getApp(), content, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, String text, int duration) {
        if (TextUtils.isEmpty(text)) return;
        initToast(context);
        if (myToast != null) {
            myToast.setGravity(Gravity.BOTTOM, DimensUtils.dip2px(context, 0),
                    DimensUtils.dip2px(context, 60));
            myToast.setText(text);
            myToast.setDuration(duration);
            myToast.show();
        }
    }


    public static void showLong(Context context, String text) {
        show(context, text, LENGTH_LONG);
    }

    public static void showShort(Context context, String text) {
        show(context, text, LENGTH_SHORT);
    }

    @SuppressLint("ShowToast")
    private static void initToast(Context context) {
        if (myToast == null) {
            myToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    public static void show(Context context, String text, int duration,
                            int gravity, int xOffsetDp, int yOffsetDp) {
        if (TextUtils.isEmpty(text)) return;
        initToast(context);
        if (myToast != null) {
            myToast.setGravity(gravity, DimensUtils.dip2px(context, xOffsetDp),
                    DimensUtils.dip2px(context, yOffsetDp));
            myToast.setText(text);
            myToast.setDuration(duration);
            myToast.show();
        }

    }


}
