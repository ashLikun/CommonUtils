package com.ashlikun.utils.other;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.spannable.CentreImageSpan;
import com.ashlikun.utils.other.spannable.CustomAlignSpan;
import com.ashlikun.utils.other.spannable.XBulletSpan;
import com.ashlikun.utils.other.spannable.XClickableSpan;


/**
 * 作者　　: 李坤
 * 创建时间:2016/11/2　14:01
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：复合文本的工具类
 */

public class SpannableUtils {
    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/10 15:51
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：获取建造者
     *
     * @return {@link Builder}
     */
    public static Builder getBuilder(@NonNull CharSequence text) {
        return new Builder(text);
    }

    public static class Builder {
        private int defaultValue = 0x12000000;
        /**
         * 源文本
         */
        private CharSequence text;
        private int flag;
        /**
         * 前景颜色
         */
        @ColorInt
        private int foregroundColor;
        /**
         * 背景颜色
         */
        @ColorInt
        private int backgroundColor;
        /**
         * 是否设置缩进
         */
        private boolean isLeadingMargin;
        /**
         * 首行
         */
        private int first;
        /**
         * 其他的缩进
         */
        private int rest;

        /**
         * 文字大小比例
         */
        private float proportion;
        /**
         * 设置横向比例
         */
        private float xProportion;
        /**
         * 设置删除线
         */
        private boolean isStrikethrough;
        /**
         * 设置下划线
         */
        private boolean isUnderline;
        /**
         * 设置粗体
         */
        private boolean isBold;
        /**
         * 设置斜体
         */
        private boolean isItalic;
        /**
         * 设置对其
         */
        private Layout.Alignment align;
        /**
         * 是否居上对齐
         */
        private boolean isAlignTop;
        /**
         * 居上对其的偏移量
         */
        private float alignTopOffset;
        /**
         * 是否设置图片
         */
        private boolean imageIsBitmap;
        /**
         * 是否改变大小和文字一样大
         */
        boolean isChangImageSize = false;
        /**
         * 下面都是图片
         */
        private Bitmap bitmap;
        private boolean imageIsDrawable;
        private Drawable drawable;
        private boolean imageIsResourceId;
        @DrawableRes
        private int resourceId;
        /**
         * 图片对齐方式
         */
        private int imageAlign = 0;
        /**
         * 点击
         */
        private ClickableSpan clickSpan;
        /**
         * 超链接
         */
        private String url;

        /**
         * 是否模糊
         */
        private boolean isBlur;
        /**
         * 模糊半径
         */
        private float radius;
        /**
         * 模糊的格式
         */
        private BlurMaskFilter.Blur style;
        /**
         * 项目符号间距
         */
        private int bulletGapWidth = 0;
        /**
         * 项目符号宽度 > 0才会有
         */
        private int bulletWidth = 0;
        /**
         * 项目符号的颜色
         */
        @ColorInt
        private int bulletColor;

        /**
         * 行间距，谁要使用就传递给谁，一般用于对齐 ,  一次设置后不清空
         */
        private float lineSpacingExtra;
        /**
         * 一次结束是否清楚样式
         */
        private boolean isClean = true;

        private SpannableStringBuilder mBuilder;

        private Builder(@NonNull CharSequence text) {
            this.text = text;
            clean();
            mBuilder = new SpannableStringBuilder();
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 17:37
         * 方法功能：设置标识
         *
         * @param flag {@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}
         *             {@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}
         *             {@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}
         *             {@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}
         * @return {@link Builder}
         */

        public Builder setFlag(int flag) {
            this.flag = flag;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 17:38
         * <p>
         * 方法功能：设置前景色
         *
         * @param color 前景色
         * @return {@link Builder}
         */
        public Builder setForegroundColorRes(@ColorRes int color) {
            this.foregroundColor = AppUtils.getApp().getResources().getColor(color);
            return this;
        }

        public Builder setForegroundColor(@ColorInt int color) {
            this.foregroundColor = color;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 17:39
         * <p>
         * 方法功能：设置背景色
         *
         * @param color 背景色
         * @return {@link Builder}
         */
        public Builder setBackgroundColorRes(@ColorRes int color) {
            this.backgroundColor = AppUtils.getApp().getResources().getColor(color);
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 17:40
         * <p>
         * 方法功能：设置缩进
         *
         * @param first 首行缩进
         * @param rest  剩余行缩进
         * @return {@link Builder}
         */
        public Builder setLeadingMargin(int first, int rest) {
            isLeadingMargin = true;
            this.first = first;
            this.rest = rest;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 17:41
         * 方法功能：设置文字大小比例
         *
         * @param proportion 比例
         * @return {@link Builder}
         */
        public Builder setProportion(float proportion) {
            this.proportion = proportion;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 17:41
         * 方法功能：设置字体横向比例
         *
         * @param proportion 比例
         * @return {@link Builder}
         */
        public Builder setXProportion(float proportion) {
            this.xProportion = proportion;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 17:41
         * <p>
         * 方法功能：设置删除线
         *
         * @return {@link Builder}
         */
        public Builder setStrikethrough() {
            this.isStrikethrough = true;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:56
         * <p>
         * 方法功能：设置下划线
         *
         * @return {@link Builder}
         */
        public Builder setUnderline() {
            this.isUnderline = true;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:57
         * 方法功能：设置粗体
         *
         * @return {@link Builder}
         */
        public Builder setBold() {
            isBold = true;
            return this;
        }


        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * <p>
         * 方法功能：设置斜体
         *
         * @return {@link Builder}
         */
        public Builder setItalic() {
            isItalic = true;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * <p>
         * 方法功能：设置粗斜体
         *
         * @return {@link Builder}
         */
        public Builder setBoldItalic() {
            isItalic = true;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置对齐
         *
         * @param align {@link Layout.Alignment#ALIGN_NORMAL}正常
         *              {@link Layout.Alignment#ALIGN_OPPOSITE}相反
         *              {@link Layout.Alignment#ALIGN_CENTER}居中
         * @return {@link Builder}
         */
        public Builder setAlign(@Nullable Layout.Alignment align) {
            this.align = align;
            return this;
        }

        /**
         * 自定义居上对其
         *
         * @return
         */
        public Builder setAlignTop() {
            setAlignTop(0);
            return this;
        }

        /**
         * 自定义居上对其
         *
         * @param alignTopOffset
         * @return
         */
        public Builder setAlignTop(float alignTopOffset) {
            isAlignTop = true;
            this.alignTopOffset = alignTopOffset;
            return this;
        }

        /**
         * 自定义居上对其
         *
         * @param alignTopOffset
         * @return
         */
        public Builder setAlignTopDp(float alignTopOffset) {
            isAlignTop = true;
            this.alignTopOffset = DimensUtils.dip2px(AppUtils.getApp(), alignTopOffset);
            return this;
        }

        /**
         * 自定义居上对其
         *
         * @param alignTopOffset
         * @return
         */
        public Builder setAlignTopRes(@DimenRes int alignTopOffset) {
            isAlignTop = true;
            this.alignTopOffset = AppUtils.getApp().getResources().getDimensionPixelOffset(alignTopOffset);
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置图片
         *
         * @param bitmap 图片位图
         * @return {@link Builder}
         */
        public Builder setBitmap(@NonNull Bitmap bitmap) {
            if (bitmap != null) {
                this.bitmap = bitmap;
                imageIsBitmap = true;
            }
            return this;
        }


        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置图片
         *
         * @param drawable 图片资源
         * @return {@link Builder}
         */
        public Builder setDrawable(@NonNull Drawable drawable) {
            if (drawable != null) {
                this.drawable = drawable;
                imageIsDrawable = true;
            }
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置图片
         *
         * @param resourceId 图片资源id
         * @return {@link Builder}
         */
        public Builder setResourceId(@DrawableRes int resourceId) {
            if (resourceId != 0) {
                this.resourceId = resourceId;
                imageIsResourceId = true;
            }
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置点击事件
         * <p>
         * 设置点击事件
         * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
         *
         * @param clickSpan 点击事件
         * @return {@link Builder}
         */
        public Builder setClickSpan(@NonNull XClickableSpan clickSpan) {
            this.clickSpan = clickSpan;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 10:09
         * <p>
         * 方法功能：设置超链接
         * 需添加view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param url 超链接
         * @return {@link Builder}
         */
        public Builder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 10:09
         * <p>
         * 方法功能：设置模糊 推荐还是把所有字体都模糊这样使用
         *
         * @param radius 模糊半径（需大于0）
         * @param style  模糊样式<ul>
         *               {@link BlurMaskFilter.Blur#NORMAL}
         *               {@link BlurMaskFilter.Blur#SOLID}
         *               {@link BlurMaskFilter.Blur#OUTER}
         *               {@link BlurMaskFilter.Blur#INNER}
         * @return {@link Builder}
         */
        public Builder setBlur(float radius, BlurMaskFilter.Blur style) {
            this.radius = radius;
            this.style = style;
            this.isBlur = true;
            return this;
        }

        /**
         * 设置项目符号
         *
         * @param bulletWidth 宽度
         * @param color       颜色
         * @return
         */
        public Builder setBullet(int bulletWidth, @ColorInt int color) {
            this.bulletWidth = bulletWidth;
            this.bulletColor = color;
            return this;
        }

        /**
         * 设置项目符号
         *
         * @param bulletWidth    宽度
         * @param color          颜色
         * @param bulletGapWidth 间距
         * @return
         */
        public Builder setBullet(int bulletWidth, @ColorInt int color, int bulletGapWidth) {
            this.bulletWidth = bulletWidth;
            this.bulletGapWidth = bulletGapWidth;
            this.bulletColor = color;
            return this;
        }

        /**
         * 行间距，谁要使用就传递给谁，一般用于对齐,一次设置后不清空
         * 类似于偏移量,相对于Y偏移
         *
         * @param lineSpacingExtra
         * @return
         */
        public Builder setLineSpacingExtra(float lineSpacingExtra) {
            this.lineSpacingExtra = lineSpacingExtra;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 10:11
         * <p>
         * 方法功能：追加样式字符串
         *
         * @param text 样式字符串文本
         * @return {@link Builder}
         */
        public Builder append(@NonNull CharSequence text) {
            setSpan();
            this.text = text;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 10:12
         * <p>
         * 方法功能：创建样式字符串
         *
         * @return 样式字符串
         */
        public SpannableStringBuilder create() {
            setSpan();
            return mBuilder;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 10:13
         * <p>
         * 方法功能：清空样式 一般调用append 或者 create 就会主动清空，以便后续设置
         */
        private void clean() {
            foregroundColor = defaultValue;
            backgroundColor = defaultValue;
            isLeadingMargin = false;
            proportion = -1;
            xProportion = -1;
            isStrikethrough = false;
            isUnderline = false;
            isBold = false;
            isItalic = false;
            align = null;
            isAlignTop = false;
            alignTopOffset = 0;
            bitmap = null;
            imageIsBitmap = false;
            drawable = null;
            imageIsDrawable = false;
            resourceId = 0;
            imageIsResourceId = false;
            clickSpan = null;
            url = null;
            isBlur = false;
            bulletGapWidth = 0;
            bulletWidth = 0;
            bulletColor = 0;
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
            isChangImageSize = false;
            imageAlign = 0;
        }

        /**
         * @author　　: 李坤
         * 创建时间: 2018/6/1 0001 下午 4:22
         * 邮箱　　：496546144@qq.com
         * <p>
         * 方法功能：设置图片与文字高度一致
         */
        public Builder changImageSize() {
            isChangImageSize = true;
            return this;
        }

        /**
         * 图片对齐方式
         * 0：居中
         * 1:上
         * 2：下
         */
        public Builder imageAlign(int imageAlign) {
            this.imageAlign = imageAlign;
            return this;
        }

        public boolean isSetImage() {
            return (imageIsBitmap || imageIsDrawable || imageIsResourceId);
        }

        /**
         * 设置样式
         */
        private void setSpan() {
            if (isSetImage()) {
                //如果设置图片，就强制设置文字为"."
                this.text = ".";
            }
            if (TextUtils.isEmpty(this.text)) {
                clean();
                return;
            }
            //开始位置
            int start = mBuilder.length();
            mBuilder.append(this.text);
            //结束位置
            int end = mBuilder.length();
            //前景色
            if (foregroundColor != defaultValue) {
                mBuilder.setSpan(new ForegroundColorSpan(foregroundColor), start, end, flag);
            }
            //背景色
            if (backgroundColor != defaultValue) {
                mBuilder.setSpan(new BackgroundColorSpan(backgroundColor), start, end, flag);
            }
            //是否设置缩进
            if (isLeadingMargin) {
                mBuilder.setSpan(new LeadingMarginSpan.Standard(first, rest), start, end, flag);
            }

            //文字大小比例
            if (proportion != -1) {
                mBuilder.setSpan(new RelativeSizeSpan(proportion), start, end, flag);
            }
            //是否居上对齐
            if (isAlignTop) {
                mBuilder.setSpan(new CustomAlignSpan(alignTopOffset), start, end, flag);
            }
            //文字X缩放
            if (xProportion != -1) {
                mBuilder.setSpan(new ScaleXSpan(xProportion), start, end, flag);
            }
            //设置删除线
            if (isStrikethrough) {
                mBuilder.setSpan(new StrikethroughSpan(), start, end, flag);
            }
            //设置下划线
            if (isUnderline) {
                mBuilder.setSpan(new UnderlineSpan(), start, end, flag);
            }
            if (isBold && isItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, flag);
            } else if (isBold) {
                //设置粗体
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
            } else if (isItalic) {
                //设置斜体
                mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
            }
            //设置对其
            if (align != null) {
                mBuilder.setSpan(new AlignmentSpan.Standard(align), start, end, flag);
            }
            //设置图片
            if (isSetImage()) {
                CentreImageSpan span = null;
                if (imageIsBitmap) {
                    drawable = new BitmapDrawable(AppUtils.getApp().getResources(), bitmap);
                } else if (imageIsResourceId) {
                    drawable = AppUtils.getApp().getResources().getDrawable(resourceId);
                }
                if (imageIsBitmap || imageIsResourceId) {
                    int width = drawable.getIntrinsicWidth();
                    int height = drawable.getIntrinsicHeight();
                    drawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
                }
                if (isChangImageSize) {
                    drawable = DrawableCompat.wrap(drawable).mutate();
                }
                mBuilder.setSpan(span = new CentreImageSpan(drawable), start, end, flag);
                span.setChangSizeToText(isChangImageSize);
                span.setLineSpacingExtra(lineSpacingExtra);
                span.setImageAlign(imageAlign);
            }
            //设置点击
            if (clickSpan != null) {
                mBuilder.setSpan(clickSpan, start, end, flag);
            }
            if (url != null) {
                mBuilder.setSpan(new URLSpan(url), start, end, flag);
            }
            if (isBlur) {
                mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(radius, style)), start, end, flag);
            }
            if (bulletWidth > 0) {
                mBuilder.setSpan(new XBulletSpan(bulletWidth, bulletColor, bulletGapWidth), start, end, flag);
            }
            clean();
        }
    }


    /**
     * 获取span  Draw的Y中心
     *
     * @param top
     * @param bottom
     * @param l
     * @return
     */
    public static float getSpanDrawCententY(int top, int bottom, Layout l) {
        float spacing = l.getSpacingAdd() * l.getSpacingMultiplier();
        if (l.getHeight() <= bottom + 3) {
            //最后一行不要行间距
            spacing = 0;
        }
        return (bottom - spacing - top) / 2 + top;
    }

}
