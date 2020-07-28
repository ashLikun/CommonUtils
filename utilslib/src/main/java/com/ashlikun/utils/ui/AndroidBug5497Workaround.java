package com.ashlikun.utils.ui;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;

import com.ashlikun.utils.other.DimensUtils;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/22 14:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 * 在非全屏模式下，将activity的windowSoftInputMode的属性设置为
 * ：adjustResize。同时在View的onSizeChanged(int w, int h, int oldw, int oldh)里可以得到变化后的尺寸，
 * 然后根据前后变化的结果来计算屏幕需要移动的距离。
 * <p>
 * 在你的Activity的oncreate()方法里调用AndroidBug5497Workaround.assistActivity(this);即可。注意：在setContentView(R.layout.xxx)之后调用。
 */
public class AndroidBug5497Workaround {
    public static AndroidBug5497Workaround get(Activity activity) {
        return new AndroidBug5497Workaround(activity.getWindow());
    }

    public static AndroidBug5497Workaround get(Window window) {
        return new AndroidBug5497Workaround(window);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private int contentHeight;
    private boolean isfirst = true;
    private int statusBarHeight;
    private OnInputChang onInputChang;

    public void setOnInputChang(OnInputChang onInputChang) {
        this.onInputChang = onInputChang;
    }

    private AndroidBug5497Workaround(Window window) {
        //获取状态栏的高度
        int resourceId = window.getDecorView().getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = window.getDecorView().getResources().getDimensionPixelSize(resourceId);
        FrameLayout content = (FrameLayout) window.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);

        //界面出现变动都会调用这个监听事件
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isfirst) {
                    //兼容华为等机型
                    contentHeight = mChildOfContent.getHeight();
                    isfirst = false;
                }
                possiblyResizeChildOfContent();
            }
        });

        frameLayoutParams = (FrameLayout.LayoutParams)
                mChildOfContent.getLayoutParams();
    }

    /**
     * 重新调整跟布局的高度
     */
    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        //当前可见高度和上一次可见高度不一致 布局变动
        if (usableHeightNow != usableHeightPrevious) {
            //int usableHeightSansKeyboard2 = mChildOfContent.getHeight();//兼容华为等机型
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight;
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
            } else {
                frameLayoutParams.height = contentHeight;
            }

            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;

            if (onInputChang != null) {
                if (ScreenInfoUtils.getHeight() - usableHeightNow > DimensUtils.dip2px(150)) {
                    onInputChang.onChangOpen();
                } else {
                    onInputChang.onChangOff();
                }
            }
        }
    }

    /**
     * 计算mChildOfContent可见高度     ** @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    /**
     * 输入法开启与关闭监听
     */
    public interface OnInputChang {
        /**
         * 关闭
         */
        void onChangOff();

        /**
         * 打开
         */
        void onChangOpen();
    }
}

