package com.ashlikun.utils.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Layout;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

import com.ashlikun.utils.ui.FocusLinkMovementMethod;

/**
 * 作者　　: 李坤
 * 创建时间:2017/9/2 0002　14:53
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：textView的兼容
 * 1：lineSpacingExtra   兼容
 */

public class TextViewCompat extends AppCompatTextView {
    float mSpacingAdd;
    public boolean movementMethodClick = false;

    public TextViewCompat(Context context) {
        this(context, null);
    }

    public TextViewCompat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.lineSpacingExtra});
        mSpacingAdd = a.getDimensionPixelSize(0, 0);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        a.recycle();

    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        mSpacingAdd = add;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() - calculateExtraSpace());
    }

    //计算需要兼容的底部多余的高度
    public int calculateExtraSpace() {
        int result = 0;
        int lastLineIndex = getLineCount() - 1;

        if (lastLineIndex >= 0) {
            Layout layout = getLayout();
            if (getMeasuredHeight() - getPaddingTop() - getPaddingBottom() == getLayout().getHeight()) {
                Rect mRect = new Rect();
                int baseline = getLineBounds(lastLineIndex, mRect);
                result = mRect.bottom - (baseline + layout.getPaint().getFontMetricsInt().bottom);
            }
        }
        return result;
    }

    /**
     * 设置了ClickableSpan导致的上层View点击事件无法响应解决方案
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        MovementMethod movementMethod = getMovementMethod();
        if (movementMethod != null && movementMethod instanceof FocusLinkMovementMethod) {
            movementMethodClick = false;
            boolean result = super.onTouchEvent(event);
            movementMethodClick = ((FocusLinkMovementMethod) movementMethod).clickUp;
            return result;
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 设置了ClickableSpan导致的上层View点击事件无法响应解决方案
     */
    public void setMovementMethods(MovementMethod movement) {
        boolean focusable = isFocusable();
        boolean isClickable = isClickable();
        boolean isLongClickable = isLongClickable();
        super.setMovementMethod(movement);
        setFocusable(focusable);
        setClickable(isClickable);
        setLongClickable(isLongClickable);
    }

    /**
     * 设置了ClickableSpan导致的上层View点击事件无法响应解决方案
     */
    @Override
    public boolean performClick() {
        if (!movementMethodClick) {
            return super.performClick();
        }
        return false;
    }
}
