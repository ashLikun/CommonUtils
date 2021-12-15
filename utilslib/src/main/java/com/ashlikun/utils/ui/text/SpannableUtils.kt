package com.ashlikun.utils.ui.text

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import com.ashlikun.utils.AppUtils.app
import com.ashlikun.utils.other.DimensUtils.dip2px
import com.ashlikun.utils.other.spannable.*
import com.ashlikun.utils.ui.resources.ResUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 22:53
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：复合文本的工具类
 */

object SpannableUtils {
    /**
     * 获取建造者
     */
    fun getBuilder(text: CharSequence, context: Context = app): Builder {
        return Builder(context, text)
    }

    /**
     * 获取span  Draw的Y中心
     */
    fun getSpanDrawCententY(top: Int, bottom: Int, l: Layout): Float {
        var spacing = l.spacingAdd * l.spacingMultiplier
        if (l.height <= bottom + 3) {
            //最后一行不要行间距
            spacing = 0f
        }
        return (bottom - spacing - top) / 2 + top
    }

    class Builder constructor(
        val context: Context,
        //源文本
        var text: CharSequence
    ) {
        /**
         * 是否需要添加
         */
        private var isAppendText = true

        /**
         * 如果不添加（isAppendText）的时候是否只匹配第一次
         */
        private var isMatchFirst = true
        private var flag = 0

        /**
         * 前景颜色
         */
        @ColorInt
        private var foregroundColor: Int? = null

        /**
         * 背景颜色
         */
        @ColorInt
        private var backgroundColor: Int? = null


        /**
         * 首行的缩进
         */
        private var first: Int? = null

        /**
         * 其他的缩进
         */
        private var rest: Int? = null

        /**
         * 文字大小比例
         */
        private var proportion = 0f

        /**
         * 设置横向比例
         */
        private var xProportion = 0f

        /**
         * 设置删除线
         */
        private var isStrikethrough = false

        /**
         * 设置下划线
         */
        private var isUnderline = false

        /**
         * 设置粗体
         */
        private var isBold = false

        /**
         * 设置斜体
         */
        private var isItalic = false

        /**
         * 设置对其
         */
        private var align: Layout.Alignment? = null

        /**
         * 是否居上对齐
         */
        private var isAlignTop = false

        /**
         * 居上对其的偏移量
         */
        private var alignTopOffset = 0f


        /**
         * 是否改变大小和文字一样大
         */
        var isChangImageSize = false

        /**
         * 下面都是图片
         */
        private var bitmap: Bitmap? = null
        private var drawable: Drawable? = null

        @DrawableRes
        private var imageResourceId: Int? = null

        private var imageWidth = 0
        private var imageHidth = 0

        /**
         * 图片对齐方式
         */
        private var imageAlign = 0

        /**
         * 点击
         */
        private var clickSpan: ClickableSpan? = null

        /**
         * 超链接
         */
        private var url: String? = null

        /**
         * 是否模糊
         */
        private var isBlur = false

        /**
         * 模糊半径
         */
        private var radius = 0f

        /**
         * 模糊的格式
         */
        private var style: Blur? = null

        /**
         * 项目符号间距
         */
        private var bulletGapWidth = 0

        /**
         * 项目符号宽度 > 0才会有
         */
        private var bulletWidth = 0

        /**
         * 项目符号的颜色
         */
        @ColorInt
        private var bulletColor: Int? = null

        /**
         * 空白行的高度
         */
        private var blockSpaceHeight = 0
        private val mBuilder: SpannableStringBuilder

        /**
         * 设置标识
         *
         * @param flag [Spanned.SPAN_INCLUSIVE_EXCLUSIVE]
         * [Spanned.SPAN_INCLUSIVE_INCLUSIVE]
         * [Spanned.SPAN_EXCLUSIVE_EXCLUSIVE]
         * [Spanned.SPAN_EXCLUSIVE_INCLUSIVE]
         */
        fun setFlag(flag: Int): Builder {
            this.flag = flag
            return this
        }

        /**
         * 设置前景色
         *
         * @param color 前景色
         */
        fun setForegroundColorRes(@ColorRes color: Int): Builder {
            foregroundColor = ResUtils.getColor(context, color)
            return this
        }

        fun setForegroundColor(@ColorInt color: Int): Builder {
            foregroundColor = color
            return this
        }

        /**
         * 设置背景色
         *
         * @param color 背景色
         */
        fun setBackgroundColorRes(@ColorRes color: Int): Builder {
            backgroundColor = ResUtils.getColor(context, color)
            return this
        }

        fun setBackgroundColor(@ColorInt color: Int): Builder {
            backgroundColor = color
            return this
        }

        /**
         * 设置缩进
         *
         * @param first 首行缩进
         * @param rest  剩余行缩进
         */
        fun setLeadingMargin(first: Int, rest: Int): Builder {
            this.first = first
            this.rest = rest
            return this
        }

        /**
         * 设置文字大小比例
         *
         * @param proportion 比例
         */
        fun setProportion(proportion: Float): Builder {
            this.proportion = proportion
            return this
        }

        /**
         * 设置字体横向比例
         *
         * @param proportion 比例
         */
        fun setXProportion(proportion: Float): Builder {
            xProportion = proportion
            return this
        }

        /**
         * 设置删除线
         */
        fun setStrikethrough(): Builder {
            isStrikethrough = true
            return this
        }

        /**
         * 设置下划线
         */
        fun setUnderline(): Builder {
            isUnderline = true
            return this
        }

        /**
         * 设置粗体
         */
        fun setBold(): Builder {
            isBold = true
            return this
        }

        /**
         * 设置斜体
         */
        fun setItalic(): Builder {
            isItalic = true
            return this
        }

        /**
         * 设置粗斜体
         */
        fun setBoldItalic(): Builder {
            isItalic = true
            return this
        }

        /**
         * 设置对齐
         *
         * @param align [Layout.Alignment.ALIGN_NORMAL]正常
         * [Layout.Alignment.ALIGN_OPPOSITE]相反
         * [Layout.Alignment.ALIGN_CENTER]居中
         */
        fun setAlign(align: Layout.Alignment): Builder {
            this.align = align
            return this
        }

        /**
         * 自定义居上对其
         */
        fun setAlignTop(): Builder {
            setAlignTop(0f)
            return this
        }

        /**
         * 自定义居上对其
         *
         * @param alignTopOffset
         */
        fun setAlignTop(alignTopOffset: Float): Builder {
            isAlignTop = true
            this.alignTopOffset = alignTopOffset
            return this
        }

        /**
         * 自定义居上对其
         *
         * @param alignTopOffset
         */
        fun setAlignTopDp(alignTopOffset: Float): Builder {
            isAlignTop = true
            this.alignTopOffset = dip2px(context, alignTopOffset).toFloat()
            return this
        }

        /**
         * 自定义居上对其
         *
         * @param alignTopOffset
         */
        fun setAlignTopRes(@DimenRes alignTopOffset: Int): Builder {
            isAlignTop = true
            this.alignTopOffset =
                ResUtils.getDimensionPixelOffset(context, alignTopOffset).toFloat()
            return this
        }

        /**
         * @param bitmap 图片位图
         */
        fun setBitmap(bitmap: Bitmap): Builder {
            this.bitmap = bitmap
            return this
        }

        /**
         * @param drawable 图片资源
         */
        fun setDrawable(drawable: Drawable): Builder {
            this.drawable = drawable
            return this
        }

        /**
         * @param resourceId 图片资源id
         */
        fun setResourceId(@DrawableRes resourceId: Int): Builder {
            this.imageResourceId = resourceId
            return this
        }

        /**
         * 设置图片大小
         */
        fun setImageSize(width: Int, height: Int): Builder {
            imageWidth = width
            imageHidth = height
            return this
        }

        /**
         * 设置点击事件
         *
         * 需添加view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param clickSpan 点击事件
         */
        fun setClickSpan(clickSpan: XClickableSpan): Builder {
            this.clickSpan = clickSpan
            return this
        }

        /**
         * 设置超链接
         * 需添加view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @param url 超链接
         */
        fun setUrl(url: String): Builder {
            this.url = url
            return this
        }

        /**
         * 设置模糊 推荐还是把所有字体都模糊这样使用
         *
         * @param radius 模糊半径（需大于0）
         * @param style  模糊样式
         * [BlurMaskFilter.Blur.NORMAL]
         * [BlurMaskFilter.Blur.SOLID]
         * [BlurMaskFilter.Blur.OUTER]
         * [BlurMaskFilter.Blur.INNER]
         */
        fun setBlur(radius: Float, style: Blur?): Builder {
            this.radius = radius
            this.style = style
            isBlur = true
            return this
        }

        /**
         * 设置项目符号
         *
         * @param bulletWidth 宽度
         * @param color       颜色
         */
        fun setBullet(bulletWidth: Int, @ColorInt color: Int): Builder {
            this.bulletWidth = bulletWidth
            bulletColor = color
            return this
        }

        /**
         * 设置项目符号
         *
         * @param bulletWidth    宽度
         * @param color          颜色
         * @param bulletGapWidth 间距
         */
        fun setBullet(bulletWidth: Int, @ColorInt color: Int, bulletGapWidth: Int): Builder {
            this.bulletWidth = bulletWidth
            this.bulletGapWidth = bulletGapWidth
            bulletColor = color
            return this
        }

        /**
         * 追加样式字符串
         *
         * @param text 样式字符串文本
         */
        fun append(text: CharSequence): Builder {
            setSpan()
            this.text = text
            return this
        }

        /**
         * 样式字符串
         *
         * @param text 样式字符串文本
         */
        fun appendStyle(text: CharSequence, isMatchFirst: Boolean): Builder {
            setSpan()
            this.text = text
            isAppendText = false
            this.isMatchFirst = isMatchFirst
            return this
        }

        fun isNoAppendText(isMatchFirst: Boolean): Builder {
            isAppendText = false
            this.isMatchFirst = isMatchFirst
            return this
        }

        /**
         * 创建样式字符串
         * @return 样式字符串
         */
        fun create(): SpannableStringBuilder {
            setSpan()
            return mBuilder
        }


        /**
         * 设置图片与文字高度一致
         */
        fun changImageSize(): Builder {
            isChangImageSize = true
            return this
        }

        /**
         * 图片对齐方式
         * 0：居中
         * 1:上
         * 2：下
         */
        fun imageAlign(imageAlign: Int): Builder {
            this.imageAlign = imageAlign
            return this
        }

        /**
         * 空白行的高度
         */
        fun blockSpaceHeight(blockSpaceHeight: Int): Builder {
            this.blockSpaceHeight = blockSpaceHeight
            return this
        }

        val isSetImage: Boolean
            get() = bitmap != null || drawable != null || imageResourceId != null

        /**
         * 清空样式 一般调用append 或者 create 就会主动清空，以便后续设置
         */
        private fun clean() {
            foregroundColor = null
            backgroundColor = null
            first = null
            rest = null
            proportion = -1f
            xProportion = -1f
            isStrikethrough = false
            isUnderline = false
            isBold = false
            isItalic = false
            align = null
            isAlignTop = false
            alignTopOffset = 0f
            bitmap = null
            drawable = null
            imageResourceId = null
            clickSpan = null
            url = null
            isBlur = false
            bulletGapWidth = 0
            bulletWidth = 0
            bulletColor = null
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            isChangImageSize = false
            imageAlign = 0
            blockSpaceHeight = 0
        }

        /**
         * 设置样式
         */
        private fun setSpan() {
            if (isSetImage) {
                //如果设置图片，就强制设置文字为"."
                text = "."
            }
            if (TextUtils.isEmpty(text)) {
                clean()
                return
            }
            if (isAppendText) {
                //开始位置
                val start = mBuilder.length
                mBuilder.append(text)
                //结束位置
                val end = mBuilder.length
                handText(start, end)
            } else {
                val tt = mBuilder.toString()
                var start = 0
                var end = 0
                while (start >= 0) {
                    start = tt.indexOf(text.toString(), end)
                    end = start + text.length
                    if (start >= 0) {
                        handText(start, end)
                        if (isMatchFirst) {
                            break
                        }
                    }
                }
            }
            clean()
        }

        private fun handText(start: Int, end: Int) {
            //前景色
            if (foregroundColor != null) {
                mBuilder.setSpan(ForegroundColorSpan(foregroundColor!!), start, end, flag)
            }
            //背景色
            if (backgroundColor != null) {
                mBuilder.setSpan(BackgroundColorSpan(backgroundColor!!), start, end, flag)
            }
            //是否设置缩进
            if (first != null || rest != null) {
                mBuilder.setSpan(
                    LeadingMarginSpan.Standard(first ?: 0, rest ?: 0),
                    start,
                    end,
                    flag
                )
            }

            //文字大小比例
            if (proportion != -1f) {
                mBuilder.setSpan(RelativeSizeSpan(proportion), start, end, flag)
            }

            //文字X缩放
            if (xProportion != -1f) {
                mBuilder.setSpan(ScaleXSpan(xProportion), start, end, flag)
            }
            //设置删除线
            if (isStrikethrough) {
                mBuilder.setSpan(StrikethroughSpan(), start, end, flag)
            }
            //设置下划线
            if (isUnderline) {
                mBuilder.setSpan(UnderlineSpan(), start, end, flag)
            }
            if (isBold && isItalic) {
                mBuilder.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, flag)
            } else if (isBold) {
                //设置粗体
                mBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, flag)
            } else if (isItalic) {
                //设置斜体
                mBuilder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flag)
            }
            //设置对其
            if (align != null) {
                mBuilder.setSpan(AlignmentSpan.Standard(align!!), start, end, flag)
            }
            //设置图片
            if (isSetImage) {
                if (bitmap != null) {
                    drawable = BitmapDrawable(context.resources, bitmap)
                } else if (imageResourceId != null) {
                    drawable = ResUtils.getDrawable(context, imageResourceId!!)
                }
                if (drawable != null) {
                    val width = drawable!!.intrinsicWidth
                    val height = drawable!!.intrinsicHeight
                    drawable?.setBounds(
                        0,
                        0,
                        if (width > 0) width else 0,
                        if (height > 0) height else 0
                    )
                    if (isChangImageSize) {
                        drawable = DrawableCompat.wrap(drawable!!).mutate()
                    }
                    if (imageHidth != 0 && imageWidth != 0) {
                        drawable?.setBounds(0, 0, imageWidth, imageHidth)
                    }
                    mBuilder.setSpan(
                        CentreImageSpan(drawable!!).also {
                            it.isChangSizeToText = isChangImageSize
                            it.imageAlign = imageAlign
                        },
                        start,
                        end,
                        flag
                    )

                }

            }
            //设置点击
            if (clickSpan != null) {
                mBuilder.setSpan(clickSpan, start, end, flag)
            }
            if (url != null) {
                mBuilder.setSpan(URLSpan(url), start, end, flag)
            }
            if (isBlur) {
                mBuilder.setSpan(MaskFilterSpan(BlurMaskFilter(radius, style)), start, end, flag)
            }
            if (bulletWidth > 0 && bulletColor != null) {
                mBuilder.setSpan(
                    XBulletSpan(bulletWidth, true, bulletColor!!, bulletGapWidth),
                    start,
                    end,
                    flag
                )
            }
            //是否居上对齐
            if (isAlignTop) {
                mBuilder.setSpan(
                    CustomAlignSpan(alignTopOffset, foregroundColor, backgroundColor),
                    start,
                    end,
                    flag
                )
            }
            if (blockSpaceHeight > 0) {
                mBuilder.setSpan(BlockSpaceSpan(blockSpaceHeight), start, end, flag)
            }
        }

        init {
            clean()
            mBuilder = SpannableStringBuilder()
        }
    }
}