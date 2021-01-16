package com.ashlikun.utils.ui.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.ashlikun.utils.ui.KeyboardUtils;

public class AutoHeightLayout extends SoftKeyboardSizeWatchLayout implements SoftKeyboardSizeWatchLayout.OnResizeListener {
    protected Context mContext;
    protected int mMaxParentHeight;
    protected int mSoftKeyboardHeight;
    protected boolean mConfigurationChangedFlag = false;

    public AutoHeightLayout(Context context) {
        this(context, null);
    }

    public AutoHeightLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoHeightLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mSoftKeyboardHeight = KeyboardUtils.getDefKeyboardHeight(mContext);
        addOnResizeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mMaxParentHeight == 0) {
            mMaxParentHeight = h;
        }
    }

    public void updateMaxParentHeight(int maxParentHeight) {
        this.mMaxParentHeight = maxParentHeight;
        if (maxParentHeightChangeListener != null) {
            maxParentHeightChangeListener.onMaxParentHeightChange(maxParentHeight);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mConfigurationChangedFlag = true;
        mScreenHeight = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mConfigurationChangedFlag) {
            mConfigurationChangedFlag = false;
            Rect r = new Rect();
            ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            if (mScreenHeight == 0) {
                mScreenHeight = r.bottom;
            }
            int mNowh = mScreenHeight - r.bottom;
            mMaxParentHeight = mNowh;
        }

        if (mMaxParentHeight != 0) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = MeasureSpec.makeMeasureSpec(mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onSoftPop(final int height) {
        if (mSoftKeyboardHeight != height) {
            mSoftKeyboardHeight = height;
            KeyboardUtils.setDefKeyboardHeight(mContext, mSoftKeyboardHeight);
            onSoftKeyboardHeightChanged(mSoftKeyboardHeight);
        }
    }

    @Override
    public void onSoftClose() {
    }

    /**
     * 键盘高度改变了
     */
    public void onSoftKeyboardHeightChanged(int height) {

    }

    private OnMaxParentHeightChangeListener maxParentHeightChangeListener;

    public interface OnMaxParentHeightChangeListener {
        void onMaxParentHeightChange(int height);
    }

    public void setOnMaxParentHeightChangeListener(OnMaxParentHeightChangeListener listener) {
        this.maxParentHeightChangeListener = listener;
    }
}