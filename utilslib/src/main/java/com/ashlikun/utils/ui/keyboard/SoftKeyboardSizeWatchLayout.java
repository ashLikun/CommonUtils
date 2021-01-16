package com.ashlikun.utils.ui.keyboard;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * use XhsEmotionsKeyboard(https://github.com/w446108264/XhsEmoticonsKeyboard)
 * author: sj
 */
public class SoftKeyboardSizeWatchLayout extends RelativeLayout {

    private int mOldh = -1;
    private int mNowh = -1;
    protected int mScreenHeight = 0;
    protected boolean mIsSoftKeyboardPop = false;
    public SoftKeyboardSizeWatchLayout(Context context) {
        this(context, null);
    }

    public SoftKeyboardSizeWatchLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftKeyboardSizeWatchLayout(Context context, AttributeSet attrs,int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                getRootView().getWindowVisibleDisplayFrame(r);
                if (mScreenHeight == 0) {
                    mScreenHeight = r.bottom;
                } else {
                    //防止第一次的时候获取的是假的
                    mScreenHeight = Math.max(mScreenHeight, r.bottom);
                }
                mNowh = mScreenHeight - r.bottom;
                if (mOldh != -1 && mNowh != mOldh) {
                    if (mNowh > 0) {
                        mIsSoftKeyboardPop = true;
                        if (mListenerList != null) {
                            for (OnResizeListener l : mListenerList) {
                                l.onSoftPop(mNowh);
                            }
                        }
                    } else {
                        mIsSoftKeyboardPop = false;
                        if (mListenerList != null) {
                            for (OnResizeListener l : mListenerList) {
                                l.onSoftClose();
                            }
                        }
                    }
                }
                mOldh = mNowh;
            }
        });
    }

    public boolean isSoftKeyboardPop() {
        return mIsSoftKeyboardPop;
    }


    private List<OnResizeListener> mListenerList;

    public void addOnResizeListener(OnResizeListener l) {
        if (mListenerList == null) {
            mListenerList = new ArrayList<>();
        }
        mListenerList.add(l);
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
}
