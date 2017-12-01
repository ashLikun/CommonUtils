package com.ashlikun.utils.other;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import com.ashlikun.utils.Utils;

import static com.ashlikun.utils.Utils.getApp;

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
        private CharSequence text;//源文本
        private int flag;
        @ColorInt
        private int foregroundColor;//前景颜色
        @ColorInt
        private int backgroundColor;//背景颜色
        private boolean isLeadingMargin;//是否设置缩进
        private int first;//首行
        private int rest;//其他的缩进

        private float proportion;//文字大小比例
        private float xProportion;//设置横向比例
        private boolean isStrikethrough;//设置删除线
        private boolean isUnderline;//设置下划线
        private boolean isBold;//设置粗体
        private boolean isItalic;//设置斜体
        private Layout.Alignment align;//设置对其
        private boolean isAlignTop;//是否居上对齐
        private float alignTopOffset;//居上对其的偏移量
        private boolean imageIsBitmap;//是否设置图片
        private Bitmap bitmap;//图片
        private boolean imageIsDrawable;//图片
        private Drawable drawable;//图片
        private boolean imageIsUri;//图片
        private Uri uri;//图片
        private boolean imageIsResourceId;//图片
        @DrawableRes
        private int resourceId;//图片

        private ClickableSpan clickSpan;//点击
        private String url;//超链接

        private boolean isBlur;//是否模糊
        private float radius;//模糊半径
        private BlurMaskFilter.Blur style;//模糊的格式

        private boolean isClean = true;//一次结束是否清楚样式

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
            this.foregroundColor = Utils.getApp().getResources().getColor(color);
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
            this.backgroundColor = Utils.getApp().getResources().getColor(color);
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

        //自定义居上对其
        public Builder setAlignTop() {
            setAlignTop(0);
            return this;
        }

        //自定义居上对其
        public Builder setAlignTop(float alignTopOffset) {
            isAlignTop = true;
            this.alignTopOffset = alignTopOffset;
            return this;
        }

        //自定义居上对其
        public Builder setAlignTopDp(float alignTopOffset) {
            isAlignTop = true;
            this.alignTopOffset = DimensUtils.dip2px(Utils.getApp(), alignTopOffset);
            return this;
        }

        //自定义居上对其
        public Builder setAlignTopRes(@DimenRes int alignTopOffset) {
            isAlignTop = true;
            this.alignTopOffset = Utils.getApp().getResources().getDimensionPixelOffset(alignTopOffset);
            return this;
        }

        /**
         * 设置图片
         *
         * @param bitmap 图片位图
         * @return {@link Builder}
         */
        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置图片
         *
         * @param bitmap 图片位图
         * @return {@link Builder}
         */
        public Builder setBitmap(@NonNull Bitmap bitmap) {
            this.bitmap = bitmap;
            imageIsBitmap = true;
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
            this.drawable = drawable;
            imageIsDrawable = true;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置图片
         *
         * @param uri 图片uri
         * @return {@link Builder}
         */
        public Builder setUri(@NonNull Uri uri) {
            this.uri = uri;
            imageIsUri = true;
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
            this.resourceId = resourceId;
            imageIsResourceId = true;
            return this;
        }

        /**
         * 设置点击事件
         * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
         *
         * @param clickSpan 点击事件
         * @return {@link Builder}
         */
        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/29 9:58
         * 方法功能：设置点击事件
         *
         * @param clickSpan 点击事件
         * @return {@link Builder}
         */
        public Builder setClickSpan(@NonNull ClickableSpan clickSpan) {
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
            uri = null;
            imageIsUri = false;
            resourceId = 0;
            imageIsResourceId = false;
            clickSpan = null;
            url = null;
            isBlur = false;

            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        }

        /**
         * 设置样式
         */
        private void setSpan() {
            if (TextUtils.isEmpty(this.text)) {
                clean();
                return;
            }
            int start = mBuilder.length();//开始位置
            mBuilder.append(this.text);
            int end = mBuilder.length();//结束位置
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
            } else if (isBold) {//设置粗体
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
            } else if (isItalic) {//设置斜体
                mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
            }
            //设置对其
            if (align != null) {
                mBuilder.setSpan(new AlignmentSpan.Standard(align), start, end, flag);
            }
            //设置图片
            if (imageIsBitmap || imageIsDrawable || imageIsUri || imageIsResourceId) {
                if (imageIsBitmap) {
                    mBuilder.setSpan(new ImageSpan(getApp(), bitmap), start, end, flag);
                } else if (imageIsDrawable) {
                    mBuilder.setSpan(new ImageSpan(drawable), start, end, flag);
                } else if (imageIsUri) {
                    mBuilder.setSpan(new ImageSpan(Utils.getApp(), uri), start, end, flag);
                } else {
                    mBuilder.setSpan(new ImageSpan(Utils.getApp(), resourceId), start, end, flag);
                }
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
            clean();
        }
    }

    public static class CustomAlignSpan extends ReplacementSpan {

        float alignTopOffset;

        public CustomAlignSpan(float alignTopOffset) {
            this.alignTopOffset = alignTopOffset;
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
            text = text.subSequence(start, end);
            return (int) paint.measureText(text.toString());
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            text = text.subSequence(start, end);
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) - (bottom + top)) + alignTopOffset, paint);    //此处重新计算y坐标，使字体对其
        }

    }
}
