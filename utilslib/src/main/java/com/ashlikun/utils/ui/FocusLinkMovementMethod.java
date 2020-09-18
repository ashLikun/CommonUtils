package com.ashlikun.utils.ui;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
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

public class FocusLinkMovementMethod extends LinkMovementMethod implements View.OnLongClickListener {

    private static FocusLinkMovementMethod sInstance;
    public boolean clickDown = false;
    public boolean clickUp = false;

    @Override
    public boolean onLongClick(View v) {
        return clickUp;
    }

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new FocusLinkMovementMethod();
        }

        return sInstance;
    }

    //是否设置选择
    public boolean isOpenSelect() {
        return true;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        clickUp = false;
        int action = event.getAction();
        boolean isOpenSelect = isOpenSelect();
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
                    clickUp = true;
                    return false;
                } else if (action == MotionEvent.ACTION_DOWN) {
                    if (isOpenSelect) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(links[0]),
                                buffer.getSpanEnd(links[0]));
                    }
                    clickDown = true;
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
        clickDown = false;
        //true :调用TextView,false 事件结束
        return widget.isClickable() && super.onTouchEvent(widget, buffer, event);
    }


}
