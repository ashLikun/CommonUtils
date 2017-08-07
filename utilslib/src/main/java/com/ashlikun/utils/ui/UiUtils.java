package com.ashlikun.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
     * 获得一个空间的宽高   防0
     */

    public static void setViewMeasure(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    /**
     * 获得一个空间的宽   防0
     */

    public static void setViewWidth(View view) {
        setViewMeasure(view);
        view.getMeasuredWidth();
    }

    public static void setViewHeight(View view) {
        setViewMeasure(view);
        view.getMeasuredHeight();
    }

    /**
     * 从资源文件获取一个view
     */
    public static View getInflaterView(Context context, int res) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, null);
        applyFont(view);
        return view;
    }

    /**
     * 从资源文件获取一个view
     */
    public static View getInflaterView(Context context, int res, ViewGroup parent) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, parent);
        applyFont(view);
        return view;
    }

    /**
     * 从资源文件获取一个view
     */
    public static View getInflaterView(Context context, int res, ViewGroup parent, boolean attachToRoot) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res, parent, attachToRoot);
        applyFont(view);
        return view;
    }


    /*
     * 设置ImageView渲染（Tint）
     */
    public static void setImageViewTint(final ImageView view, final int color) {
        view.setColorFilter(view.getResources().getColor(color));
    }


    /*
     * 设置字体
     */
    public static void applyFont(final View root) {
//        try {
//            if (root instanceof ViewGroup) {
//                ViewGroup viewGroup = (ViewGroup) root;
//                for (int i = 0; i < viewGroup.getChildCount(); i++)
//                    applyFont(viewGroup.getChildAt(i));
//            } else if (root instanceof TextView)
//
////                ((TextView) root).setTypeface(MyApplication.myApp.typeface);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static View getRootView(Activity context) {
        return context.getWindow().getDecorView()
                .findViewById(android.R.id.content);
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

}