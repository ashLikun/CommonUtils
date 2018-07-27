package com.ashlikun.utils.ui;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ashlikun.utils.other.LogUtils;


/**
 * 作者　　: 李坤
 * 创建时间:2016/11/2　18:56
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：设置TextView(setClickSpan)点击时候的背景
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
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

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

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    Selection.removeSelection(buffer);
                    link[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    widget.setFocusable(true);
                    widget.setFocusableInTouchMode(true);
                    widget.requestFocus();
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        } else if (action != MotionEvent.ACTION_MOVE) {
            Selection.removeSelection(buffer);
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}
