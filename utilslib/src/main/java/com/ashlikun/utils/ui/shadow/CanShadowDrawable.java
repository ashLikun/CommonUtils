package com.ashlikun.utils.ui.shadow;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * 作者　　: 李坤
 * 创建时间: 2019/6/28　10:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：阴影效果
 * 优点：使用setShadowLayer绘制阴影,效果好
 * 缺点：会生成bitmap占用空间,滚动的时候卡顿（关闭了硬件加速）
 */

public class CanShadowDrawable extends Drawable {

    private Paint paint;
    private Paint paintNoShadow;


    private RectF drawRect;
    //  圆角半径
    private int radius;
    private int corners;

    private float offsetLeft;
    private float offsetTop;
    private float offsetRight;
    private float offsetBottom;
    private Bitmap bitmap = null;


    public static final int CORNER_TOP_LEFT = 1;
    public static final int CORNER_TOP_RIGHT = 1 << 1;
    public static final int CORNER_BOTTOM_LEFT = 1 << 2;
    public static final int CORNER_BOTTOM_RIGHT = 1 << 3;
    public static final int CORNER_ALL = CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT;


    public CanShadowDrawable(int bgColor, float shadowRange, float shadowDx, float shadowDy, int shadowColor) {

        paint = new Paint();
        paint.setAntiAlias(true);

        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(bgColor);
//      设置阴影
        paint.setShadowLayer(shadowRange, shadowDx, shadowDy, shadowColor);
        paintNoShadow = new Paint();
        paintNoShadow.setAntiAlias(true);

        paintNoShadow.setFilterBitmap(true);
        paintNoShadow.setDither(true);
        paintNoShadow.setStyle(Paint.Style.FILL);
        paintNoShadow.setColor(bgColor);


        drawRect = new RectF();
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (bounds.right - bounds.left > 0 && bounds.bottom - bounds.top > 0) {
            int width = bounds.right - bounds.left;
            int height = bounds.bottom - bounds.top;
            drawRect = new RectF(offsetLeft, offsetTop, width - offsetRight, height - offsetBottom);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
            }
            drawBitmap();
            invalidateSelf();
        }
    }


    /**
     * 因为使用setShadowLayer 必须关闭硬件加速，所以这边只能绘制到bitmap上，然后再绘制到View上
     */
    private void drawBitmap() {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRoundRect(
                drawRect,
                radius, radius,
                paint
        );

        if (corners == 0) {
            corners = CORNER_ALL;
        }
        int notRoundedCorners = corners ^ CORNER_ALL;
        //哪个角不是圆角我再把你用矩形画出来
        if ((notRoundedCorners & CORNER_TOP_LEFT) != 0) {
            canvas.drawRect(offsetLeft, offsetTop, radius + offsetLeft, radius + offsetTop, paintNoShadow);
        }
        if ((notRoundedCorners & CORNER_TOP_RIGHT) != 0) {
            canvas.drawRect(drawRect.right - radius, offsetTop, drawRect.right, radius + offsetTop, paintNoShadow);
        }
        if ((notRoundedCorners & CORNER_BOTTOM_LEFT) != 0) {
            canvas.drawRect(offsetLeft, drawRect.bottom - radius, radius + offsetLeft, drawRect.bottom, paintNoShadow);
        }
        if ((notRoundedCorners & CORNER_BOTTOM_RIGHT) != 0) {
            canvas.drawRect(drawRect.right - radius, drawRect.bottom - radius, drawRect.right, drawRect.bottom, paintNoShadow);
        }
    }


    public void setCorners(int corners) {
        this.corners = corners;
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }


    public float getOffsetLeft() {
        return offsetLeft;
    }

    public void setOffsetLeft(float offsetLeft) {
        this.offsetLeft = offsetLeft;
    }

    public float getOffsetTop() {
        return offsetTop;
    }

    public void setOffsetTop(float offsetTop) {
        this.offsetTop = offsetTop;
    }

    public float getOffsetRight() {
        return offsetRight;
    }

    public void setOffsetRight(float offsetRight) {
        this.offsetRight = offsetRight;
    }

    public float getOffsetBottom() {
        return offsetBottom;
    }

    public void setOffsetBottom(float offsetBottom) {
        this.offsetBottom = offsetBottom;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public CanShadowDrawable setColor(int color) {
        paint.setColor(color);
        return this;
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    public static class Builder {


        private View view;
        //      paddingLeft
        private boolean isSetPadding;
        private float offsetLeft;
        //        paddingTop
        private float offsetTop;
        //        paddingRight
        private float offsetRight;
        //        paddingBottom
        private float offsetBottom;
        //       圆角半径
        private int radius;
        private int corners;
        //       阴影范围
        private float shadowRange;
        //       阴影x轴偏移
        private float shadowDx;
        //        阴影y轴偏移
        private float shadowDy;
        //        阴影颜色
        private int shadowColor = Color.GRAY;
        //        背景颜色
        private int bgColor = Color.WHITE;

        public Builder(View view) {
            this.view = view;
        }


        public static Builder on(View view) {
            return new Builder(view);
        }


        /**
         * PaddingLeft
         *
         * @param offsetLeft
         * @return
         */
        public Builder offsetLeft(float offsetLeft) {
            this.offsetLeft = offsetLeft;
            isSetPadding = true;
            return this;
        }

        /**
         * PaddingTop
         *
         * @param offsetTop
         * @return
         */
        public Builder offsetTop(float offsetTop) {
            this.offsetTop = offsetTop;
            isSetPadding = true;
            return this;
        }

        /**
         * PaddingRight
         *
         * @param offsetRight
         * @return
         */
        public Builder offsetRight(float offsetRight) {
            this.offsetRight = offsetRight;
            isSetPadding = true;
            return this;
        }

        /**
         * PaddingBottom
         *
         * @param offsetBottom
         * @return
         */
        public Builder offsetBottom(float offsetBottom) {
            this.offsetBottom = offsetBottom;
            isSetPadding = true;
            return this;
        }

        /**
         * 指定圆角
         *
         * @param corners
         * @return
         */
        public Builder corners(int corners) {
            this.corners = corners;
            return this;
        }


        /**
         * 圆角半径
         *
         * @param radius
         * @return
         */
        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }


        /**
         * 阴影范围
         *
         * @param shadowRange
         * @return
         */
        public Builder shadowRange(float shadowRange) {
            this.shadowRange = shadowRange;
            return this;
        }


        /**
         * 阴影x轴偏移
         *
         * @param shadowDx
         * @return
         */
        public Builder shadowDx(float shadowDx) {
            this.shadowDx = shadowDx;
            return this;
        }

        /**
         * 阴影y轴偏移
         *
         * @param shadowDy
         * @return
         */
        public Builder shadowDy(float shadowDy) {
            this.shadowDy = shadowDy;
            return this;
        }

        /**
         * 阴影颜色
         *
         * @param shadowColor
         * @return
         */
        public Builder shadowColor(int shadowColor) {
            this.shadowColor = shadowColor;
            return this;
        }

        /**
         * 背景颜色
         *
         * @param bgColor
         * @return
         */
        public Builder bgColor(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }


        public CanShadowDrawable create() {

            final CanShadowDrawable shadowViewDrawable = new CanShadowDrawable(bgColor, shadowRange, shadowDx, shadowDy, shadowColor);
            if (offsetLeft == 0) {
                offsetLeft = view.getPaddingLeft();
            }
            if (offsetTop == 0) {
                offsetTop = view.getPaddingTop();
            }
            if (offsetRight == 0) {
                offsetRight = view.getPaddingRight();
            }
            if (offsetBottom == 0) {
                offsetBottom = view.getPaddingBottom();
            }
            if (isSetPadding) {
                view.setPadding(view.getPaddingLeft() + (int) offsetLeft, view.getPaddingTop() + (int) offsetTop, view.getPaddingRight() + (int) offsetRight, view.getPaddingBottom() + (int) offsetBottom);
            }
            shadowViewDrawable.setOffsetLeft(offsetLeft);
            shadowViewDrawable.setOffsetTop(offsetTop);
            shadowViewDrawable.setOffsetBottom(offsetBottom);
            shadowViewDrawable.setOffsetRight(offsetRight);
            shadowViewDrawable.setRadius(radius);
            shadowViewDrawable.setCorners(corners);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(shadowViewDrawable);
            } else {
                view.setBackground(shadowViewDrawable);
            }
            return shadowViewDrawable;
        }
    }
}