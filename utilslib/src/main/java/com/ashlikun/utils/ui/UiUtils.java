package com.ashlikun.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.ashlikun.utils.R;

import static com.ashlikun.utils.Utils.getApp;


public class UiUtils {

    public static void setColorSchemeResources(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayout_1, R.color.SwipeRefreshLayout_2, R.color.SwipeRefreshLayout_3, R.color.SwipeRefreshLayout_4);
    }

    public static void setRefreshing(final SwipeRefreshLayout swipeRefreshLayout, final boolean b) {

        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(b);
            }
        }, 400);

    }

    /**
     * 从资源文件获取一个view
     */
    public static View getInflaterView(Context context, int res) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, null);
        return view;
    }

    /**
     * 从资源文件获取一个view
     */
    public static View getInflaterView(Context context, int res, ViewGroup parent) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, parent);
        return view;
    }

    /**
     * 从资源文件获取一个view
     */
    public static View getInflaterView(Context context, int res, ViewGroup parent, boolean attachToRoot) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, parent, attachToRoot);
        return view;
    }


    /*
     * 设置ImageView渲染（Tint）
     */
    public static void setImageViewTint(final ImageView view, final int color) {
        view.setColorFilter(view.getResources().getColor(color));
    }




    public static View getRootView(Activity context) {
        return context.findViewById(android.R.id.content);
    }

    public static View getDecorView(Activity context) {
        return context.getWindow().getDecorView();
    }


    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight() {

        int result = 0;
        int resourceId = getApp().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getApp().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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


    public static void showInput(View view) {
        exitOrShowInput(null, view);
    }

    public static void exitInput(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            exitOrShowInput(activity.getCurrentFocus().getWindowToken(), null);
        }
    }

    public static void exitInput(IBinder iBinder) {
        exitOrShowInput(iBinder, null);
    }

    public static void exitInput(View view) {
        exitOrShowInput(view.getWindowToken(), null);
    }

    public static boolean isOpenInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        return isOpen;
    }


    /**
     * 按照原始的宽度，根据比例，缩放
     *
     * @param bili (w/h)
     */
    public static void scaleViewByWidth(final View view, final float bili) {
        getViewSize(view, new OnSizeListener() {
            @Override
            public void onSize(int width, int height) {
                scaleViewByWidth(view, width, bili);
            }
        });
    }

    /**
     * 同上
     *
     * @param view
     * @param width
     * @param bili
     */
    public static void scaleViewByWidth(View view, int width, final float bili) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.height = (int) (width / bili);
        view.setLayoutParams(params);
    }

    /**
     * 按照原始的高度，根据比例，缩放
     *
     * @param bili (w/h)
     */
    public static void scaleViewByHeight(final View view, final float bili) {
        getViewSize(view, new OnSizeListener() {
            @Override
            public void onSize(int width, int height) {
                scaleViewByHeight(view, height, bili);
            }
        });
    }

    /**
     * 同上
     *
     * @param view
     * @param height
     * @param bili
     */
    public static void scaleViewByHeight(final View view, int height, final float bili) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.width = (int) (height * bili);
        view.setLayoutParams(params);
    }

    /**
     * 获取view大小
     *
     * @param onSizeListener 监听回调
     */
    public static void getViewSize(final View view, final OnSizeListener onSizeListener) {
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getMeasuredHeight() <= 0 && view.getMeasuredWidth() <= 0) {
                    return;
                }
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                onSizeListener.onSize(view.getMeasuredWidth(), view.getMeasuredHeight());
            }
        });
    }
}