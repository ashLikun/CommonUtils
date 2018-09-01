package com.ashlikun.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashlikun.utils.R;
import com.ashlikun.utils.other.DimensUtils;

import java.lang.reflect.Field;

import static com.ashlikun.utils.AppUtils.getApp;


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


    /**
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
     * 设置view高度
     *
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.height = height;
        view.setLayoutParams(params);
    }

    /**
     * 设置view宽度
     *
     * @param view
     * @param width
     */
    public static void setViewWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        params.width = width;
        view.setLayoutParams(params);
    }

    /**
     * 设置view   Margin
     *
     * @param view
     */
    public static void setViewMargin(View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) params).leftMargin = leftMargin;
            ((ViewGroup.MarginLayoutParams) params).topMargin = topMargin;
            ((ViewGroup.MarginLayoutParams) params).rightMargin = rightMargin;
            ((ViewGroup.MarginLayoutParams) params).bottomMargin = bottomMargin;
            view.setLayoutParams(params);
        }
    }

    /**
     * 获取view的Margin
     *
     * @param view
     * @param orientation 那个值，左上右下（0,1,2,3）
     * @return
     */
    public static int getViewMargin(View view, int orientation) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params instanceof ViewGroup.MarginLayoutParams) {
            if (orientation == 0) {
                return ((ViewGroup.MarginLayoutParams) params).leftMargin;
            } else if (orientation == 1) {
                return ((ViewGroup.MarginLayoutParams) params).topMargin;
            } else if (orientation == 2) {
                return ((ViewGroup.MarginLayoutParams) params).rightMargin;
            } else if (orientation == 3) {
                return ((ViewGroup.MarginLayoutParams) params).bottomMargin;
            }
        }
        return -1;
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

    /**
     * 设置Tablayou的底部线宽度和文字一样宽，要在设置完item后设置
     *
     * @param tabLayout
     * @param marginDp  边距距离
     */
    public static void setTabLayoutLine(final TabLayout tabLayout, final int marginDp) {

        //了解源码得知 线的宽度是根据 tabView的宽度来设置的,post是为了可以获取到宽度
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                    int margin = DimensUtils.dip2px(tabLayout.getContext(), marginDp);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        ViewGroup tabView = (ViewGroup) mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        TextView mTextView = null;
                        for (int j = 0; j < tabView.getChildCount(); j++) {
                            if (tabLayout.getChildAt(j) instanceof TextView) {
                                mTextView = (TextView) tabLayout.getChildAt(j);
                            }
                        }
                        //如果没有找到，就反射获取，一半不会
                        if (mTextView == null) {
                            Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                            mTextViewField.setAccessible(true);
                            mTextView = (TextView) mTextViewField.get(tabView);
                        }
                        tabView.setPadding(0, 0, 0, 0);
                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = margin;
                        params.rightMargin = margin;
                        tabView.setLayoutParams(params);
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}