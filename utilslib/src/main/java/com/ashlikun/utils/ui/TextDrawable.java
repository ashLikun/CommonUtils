package com.ashlikun.utils.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.text.TextUtils;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.DimensUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/16 0016　下午 1:06
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：显示文字的GradientDrawable, 显示背景，边框，圆角，文字
 */
public class TextDrawable extends GradientDrawable {

    private String text;
    private float textSize;
    private int textColor;
    private Paint paint;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int stokeWidth;

    public TextDrawable() {
        super();
        init();
    }

    public TextDrawable(Orientation orientation, int[] colors) {
        super(orientation, colors);
        init();
    }

    private void init() {
        setPadding(3, 2, 3, 2);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        setTextSize(12);
    }

    private void setPadding(float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        this.paddingLeft = (int) (paddingLeft * 3f);
        this.paddingTop = (int) (paddingTop * 3f);
        this.paddingRight = (int) (paddingRight * 3f);
        this.paddingBottom = (int) (paddingBottom * 3f);
    }

    private void setDrawableSize() {
        if (TextUtils.isEmpty(text)) {
            setSize(0, 0);
            setBounds(0, 0, 0, 0);
            return;
        }
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width() + paddingLeft + paddingRight + stokeWidth * 2;
        int height = rect.height() + paddingTop + paddingBottom + stokeWidth * 2;
        setSize(width, height);
        setBounds(0, 0, width, height);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        super.draw(canvas);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getIntrinsicHeight() - fontMetrics.top - fontMetrics.bottom) / 2;
        canvas.drawText(text, 0, text.length(), getIntrinsicWidth() / 2, baseline, paint);
    }

    public TextDrawable setText(String text) {
        this.text = text;
        setDrawableSize();
        return this;
    }

    public TextDrawable setTextColor(int textColor) {
        this.textColor = textColor;
        paint.setColor(this.textColor);
        setDrawableSize();
        return this;
    }

    public TextDrawable setTextSize(float textSizeDp) {
        textSize = DimensUtils.dip2px(AppUtils.getApp(), textSizeDp);
        paint.setTextSize(textSize);
        setDrawableSize();
        return this;
    }

    /**
     * 设置边框
     *
     * @param widthDp
     * @param color
     */
    public TextDrawable setStrokeNew(float widthDp, @ColorInt int color) {
        stokeWidth = DimensUtils.dip2px(AppUtils.getApp(), widthDp);
        setStroke(stokeWidth, color);
        return this;
    }

    public TextDrawable setColorNew(int argb) {
        super.setColor(argb);
        return this;
    }

    public TextDrawable setCornerRadiusNew(float radiusDp) {
        super.setCornerRadius(DimensUtils.dip2px(AppUtils.getApp(), radiusDp));
        return this;
    }
}