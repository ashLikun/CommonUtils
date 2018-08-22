package com.ashlikun.utils.ui;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 * 作者　　: 李坤
 * 创建时间:2016/11/2　18:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：设置TextView(setClickSpan)点击时候的背景
 * 是否设置选择--->TextViewCompat || widget.getLineSpacingExtra() > 0 || widget.getLineSpacingMultiplier() != 1
 * 满足一个就不设置
 */

public class FocusLinkMovementMethod extends LinkMovementMethod {

    private static FocusLinkMovementMethod sInstance;

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new FocusLinkMovementMethod();
        }

        return sInstance;
    }

    @Override
    protected boolean up(TextView widget, Spannable buffer) {
        return false;
    }

    @Override
    protected boolean down(TextView widget, Spannable buffer) {
        return false;
    }

    @Override
    protected boolean left(TextView widget, Spannable buffer) {
        return false;
    }

    @Override
    protected boolean right(TextView widget, Spannable buffer) {
        return false;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();
        //是否设置选择
        boolean isOpenSelect = true;
        if (widget.getClass().getSimpleName().equals("TextViewCompat") || widget.getLineSpacingExtra() > 0 || widget.getLineSpacingMultiplier() != 1) {
            isOpenSelect = false;
        }
        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);

            if (links.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    if (isOpenSelect) {
                        Selection.removeSelection(buffer);
                    }
                    links[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    if (isOpenSelect) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(links[0]),
                                buffer.getSpanEnd(links[0]));
                    }
                }
                return true;
            } else {
                if (isOpenSelect) {
                    Selection.removeSelection(buffer);
                }
            }
        } else if (action != MotionEvent.ACTION_MOVE) {
            Selection.removeSelection(buffer);
        }
        if (isOpenSelect) {
            return super.onTouchEvent(widget, buffer, event);
        }
        //如果调用父类会滚动
        return true;
    }
}
