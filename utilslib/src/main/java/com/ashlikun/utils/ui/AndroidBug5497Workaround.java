package com.ashlikun.utils.ui;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.ashlikun.utils.other.DimensUtils;


/**
 * Created by Administrator on 2016/5/28.
 * 在非全屏模式下，将activity的windowSoftInputMode的属性设置为
 * ：adjustResize。同时在View的onSizeChanged(int w, int h, int oldw, int oldh)里可以得到变化后的尺寸，
 * 然后根据前后变化的结果来计算屏幕需要移动的距离。
 */
public class AndroidBug5497Workaround {

    // private int statusHeight = 0;
    private ScreenInfoUtils screenInfoUtils;
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
    private OnInputChang onInputChang;

    public void setOnInputChang(OnInputChang onInputChang) {
        this.onInputChang = onInputChang;
    }

    public static AndroidBug5497Workaround assistActivity(Activity activity) {
        return new AndroidBug5497Workaround(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround(final Activity activity) {
        screenInfoUtils = new ScreenInfoUtils();
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent(activity);
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent(final Activity activity) {
        int usableHeightNow = computeUsableHeight();//现在的高度
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;//差距的高度  软键盘高度
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard - UiUtils.getStatusHeight();
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
            if (onInputChang != null) {
                if (screenInfoUtils.getHeight() - usableHeightNow > DimensUtils.dip2px(activity, 150)) {
                    onInputChang.onChangOpen();
                } else {
                    onInputChang.onChangOff();
                }
            }
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public interface OnInputChang {
        void onChangOff();

        void onChangOpen();
    }

}
