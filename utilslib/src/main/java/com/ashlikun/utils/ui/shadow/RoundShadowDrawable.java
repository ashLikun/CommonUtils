package com.ashlikun.utils.ui.shadow;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.ColorUtils;

/**
 * Created by ldf on 17/6/6.
 * email : 2286767746@qq.com
 * https://github.com/MagicMashRoom
 * <p>
 * 渐变形式实现阴影，性能好，但是效果有一点点差距
 */

public class RoundShadowDrawable extends Drawable {
    // 额外的阴影，以避免卡和阴影之间的差距
    private final float minShadowSize;
    private final RectF bounds;
    private Paint shadowCornerPaint;
    private Paint shadowEdgePaint;
    private Paint bgPaint;
    //小的真实的半径
    private float cornerRadius;
    //大的半径
    private float shadowRadius;
    private Path cornerShadowPath;
    //3个颜色数组
    private int[] shadowColors;
    private boolean dirty = true;

    public RoundShadowDrawable(int shadowColor,
                               int bgColor,
                               float radius, float shadowSize) {
        minShadowSize = 10;
        this.shadowColors = new int[]{ColorUtils.setAlphaComponent(shadowColor, 220),
                ColorUtils.setAlphaComponent(shadowColor, 121),
                ColorUtils.setAlphaComponent(shadowColor, 0)};
        shadowCornerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        shadowCornerPaint.setStyle(Paint.Style.FILL);
        cornerRadius = radius;
        bounds = new RectF();
        shadowEdgePaint = new Paint(shadowCornerPaint);
        shadowEdgePaint.setAntiAlias(false);
        if (bgColor != Color.TRANSPARENT) {
            bgPaint = new Paint(shadowCornerPaint);
            bgPaint.setAntiAlias(true);
            bgPaint.setColor(bgColor);
        }
        configShadowSize(radius, shadowSize);
    }

    @Override
    public void setAlpha(int alpha) {
        shadowCornerPaint.setAlpha(alpha);
        shadowEdgePaint.setAlpha(alpha);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        dirty = true;
    }

    void configShadowSize(float cornerRadius, float shadowSize) {
        if (shadowSize < 0f) {
            throw new IllegalArgumentException("Invalid shadow size " + shadowSize +
                    ". Must be >= 0");
        }

        if (shadowSize < minShadowSize) {
            shadowSize = minShadowSize;
        }
        shadowRadius = shadowSize + cornerRadius;
        dirty = true;
        invalidateSelf();
    }


    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void draw(Canvas canvas) {
        if (dirty) {
            buildComponents(getBounds());
            dirty = false;
        }
        drawShadow(canvas);
    }

    private void buildComponents(Rect bounds) {
        this.bounds.set(bounds.left, bounds.top,
                bounds.right, bounds.bottom);
        buildShadowCorners();
    }

    private void buildShadowCorners() {
        RectF cornerRecf = new RectF(shadowRadius - cornerRadius, shadowRadius - cornerRadius,
                shadowRadius + cornerRadius, shadowRadius + cornerRadius);
        RectF shadowCornerRecf = new RectF(0, 0, 2 * cornerRadius, 2 * cornerRadius);

        if (cornerShadowPath == null) {
            cornerShadowPath = new Path();
        } else {
            cornerShadowPath.reset();
        }
        cornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        if (cornerRadius == 0) {
            //没有圆角
            cornerShadowPath.addRect(0, 0, shadowRadius, shadowRadius, Path.Direction.CW);
        } else {
            cornerShadowPath.moveTo(shadowRadius - cornerRadius, shadowRadius);
            // 内半圆，如果cornerRadius 为零时，将绘制不出阴影的内圆角
            cornerShadowPath.arcTo(cornerRecf, 180f, 90f, false);
            cornerShadowPath.lineTo(shadowRadius, 0);
            cornerShadowPath.lineTo(cornerRadius, 0);
            // 外半圆，始终都会绘制shadow的圆角
            cornerShadowPath.arcTo(shadowCornerRecf, 270f, -90f, false);
            cornerShadowPath.lineTo(0, shadowRadius);
            cornerShadowPath.lineTo(shadowRadius - cornerRadius, shadowRadius);
        }

        cornerShadowPath.close();
        float startPosition = cornerRadius / shadowRadius;

        shadowCornerPaint.setShader(new RadialGradient(shadowRadius, shadowRadius,
                shadowRadius, shadowColors,
                new float[]{0, startPosition, 1f}
                , Shader.TileMode.CLAMP));

        shadowEdgePaint.setShader(new LinearGradient(shadowRadius, shadowRadius,
                shadowRadius, 0,
                shadowColors,
                new float[]{0, startPosition, 1f}, Shader.TileMode.CLAMP));
        shadowEdgePaint.setAntiAlias(false);
    }

    private void drawShadow(Canvas canvas) {
        final boolean drawHorizontalEdges = bounds.width() - 2 * shadowRadius > 0;
        final boolean drawVerticalEdges = bounds.height() - 2 * shadowRadius > 0;
        //背景
        if (bgPaint != null) {
            if (drawHorizontalEdges || drawVerticalEdges) {
                float shadowSize = shadowRadius - cornerRadius;
                RectF rectF = new RectF(shadowSize, shadowSize, bounds.width() - shadowSize, bounds.height() - shadowSize);
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, bgPaint);
            }
        }
        // 上边
        int saved = canvas.save();
        canvas.drawPath(cornerShadowPath, shadowCornerPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(shadowRadius, 0,
                    bounds.width() - shadowRadius, shadowRadius - cornerRadius,
                    shadowEdgePaint);
        }
        canvas.restoreToCount(saved);
        // 下边
        saved = canvas.save();
        canvas.translate(bounds.right, bounds.bottom);
        canvas.rotate(180f);
        canvas.drawPath(cornerShadowPath, shadowCornerPaint);
        if (drawHorizontalEdges) {
            canvas.drawRect(shadowRadius, 0,
                    bounds.width() - shadowRadius, shadowRadius - cornerRadius,
                    shadowEdgePaint);
        }
        canvas.restoreToCount(saved);
        // 左边
        saved = canvas.save();
        canvas.translate(bounds.left, bounds.bottom);
        canvas.rotate(270f);
        canvas.drawPath(cornerShadowPath, shadowCornerPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(shadowRadius, 0,
                    bounds.height() - shadowRadius, shadowRadius - cornerRadius,
                    shadowEdgePaint);
        }
        canvas.restoreToCount(saved);
        // 右边
        saved = canvas.save();
        canvas.translate(bounds.right, bounds.top);
        canvas.rotate(90f);
        canvas.drawPath(cornerShadowPath, shadowCornerPaint);
        if (drawVerticalEdges) {
            canvas.drawRect(shadowRadius, 0,
                    bounds.height() - shadowRadius, shadowRadius - cornerRadius,
                    shadowEdgePaint);
        }
        canvas.restoreToCount(saved);
    }

    float getCornerRadius() {
        return cornerRadius;
    }

    void setCornerRadius(float radius) {
        if (radius < 0f) {
            throw new IllegalArgumentException("Invalid radius " + radius + ". Must be >= 0");
        }
        radius = (int) (radius + .5f);
        if (cornerRadius == radius) {
            return;
        }
        cornerRadius = radius;
        dirty = true;
        invalidateSelf();
    }

    void getMaxShadowAndCornerPadding(Rect into) {
        getPadding(into);
    }

    float getShadowRadius() {
        return shadowRadius;
    }

    void setShadowSize(float size) {
        configShadowSize(cornerRadius, size);
    }

    float getMinWidth() {
        final float content = 2 *
                Math.max(shadowRadius, cornerRadius + minShadowSize + shadowRadius / 2);
        return content + (shadowRadius + minShadowSize) * 2;
    }

    float getMinHeight() {
        final float content = 2 * Math.max(shadowRadius, cornerRadius + minShadowSize
                + shadowRadius / 2);
        return content + (shadowRadius + minShadowSize) * 2;
    }
}
