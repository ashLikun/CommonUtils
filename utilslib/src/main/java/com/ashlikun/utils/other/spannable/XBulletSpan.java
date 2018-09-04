package com.ashlikun.utils.other.spannable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

import com.ashlikun.utils.other.SpannableUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/9/3　14:54
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：系统的项目符号不能改变大小，只能绘制圆形（这里可口重写）,还有对其方式
 */
public class XBulletSpan implements LeadingMarginSpan, ParcelableSpan {
    public static final int STANDARD_GAP_WIDTH = 2;
    protected int mGapWidth;
    protected boolean mWantColor;
    protected int mColor;
    protected int mRadius;
    protected static Path sBulletPath = null;

    public XBulletSpan() {
        mGapWidth = STANDARD_GAP_WIDTH;
        mWantColor = false;
        mColor = 0;
    }

    public XBulletSpan(int gapWidth) {
        mGapWidth = gapWidth;
        mWantColor = false;
        mColor = 0;
    }

    public XBulletSpan(int mColor, int mRadius) {
        this.mColor = mColor;
        this.mRadius = mRadius;
    }

    public XBulletSpan(int radius, int color, int gapWidth) {
        mGapWidth = gapWidth;
        mRadius = radius;
        mWantColor = true;
        mColor = color;
    }

    public XBulletSpan(Parcel src) {
        mGapWidth = src.readInt();
        mWantColor = src.readInt() != 0;
        mColor = src.readInt();
    }

    /**
     * TextUtils#BULLET_SPAN
     *
     * @return
     */
    @Override
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /**
     * @hide
     */
    public int getSpanTypeIdInternal() {
        return 8;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /**
     * @hide
     */
    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mGapWidth);
        dest.writeInt(mWantColor ? 1 : 0);
        dest.writeInt(mColor);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return 2 * mRadius + mGapWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            int oldcolor = 0;
            if (mWantColor) {
                oldcolor = p.getColor();
                p.setColor(mColor);
            }
            p.setStyle(Paint.Style.FILL);
            drawBullet(c, p, x, dir, top, bottom, l);

            //还原
            if (mWantColor) {
                p.setColor(oldcolor);
            }
            p.setStyle(style);
        }
    }

    public void drawBullet(Canvas c, Paint p, int x, int dir, int top, int bottom, Layout l) {
        float transY = SpannableUtils.getSpanDrawCententY(top, bottom, l);
        if (c.isHardwareAccelerated()) {
            if (sBulletPath == null) {
                sBulletPath = new Path();
                sBulletPath.addCircle(0.0f, 0.0f, mRadius, Path.Direction.CW);
            }
            c.save();
            c.translate(x + dir * mRadius, transY);
            c.drawPath(sBulletPath, p);
            c.restore();
        } else {
            c.drawCircle(x + dir * mRadius, transY, mRadius, p);
        }
    }
}