package com.ashlikun.utils.other.spannable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Parcel
import android.text.Layout
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import com.ashlikun.utils.ui.text.SpannableUtils

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/12 16:33
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：系统的项目符号不能改变大小，只能绘制圆形（这里可口重写）,还有对其方式
 */
open class XBulletSpan(
    open var gapWidthX: Int = STANDARD_GAP_WIDTH,
    open var wantColor: Boolean,
    open var colorX: Int,
    open var radius: Int
) : BulletSpan(), LeadingMarginSpan {

    companion object {
        const val STANDARD_GAP_WIDTH = 2
        protected var sBulletPath: Path? = null
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(gapWidthX)
        dest.writeInt(if (wantColor) 1 else 0)
        dest.writeInt(colorX)
        dest.writeInt(radius)
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return 2 * radius + gapWidthX
    }

    override fun drawLeadingMargin(
        c: Canvas, p: Paint, x: Int, dir: Int,
        top: Int, baseline: Int, bottom: Int,
        text: CharSequence, start: Int, end: Int,
        first: Boolean, l: Layout?
    ) {
        if ((text as Spanned).getSpanStart(this) == start) {
            val style = p.style
            var oldcolor = 0
            if (wantColor) {
                oldcolor = p.color
                p.color = colorX
            }
            p.style = Paint.Style.FILL
            if (l != null) {
                drawBullet(c, p, x, dir, top, bottom, l)
            }
            //还原
            if (wantColor) {
                p.color = oldcolor
            }
            p.style = style
        }
    }

    fun drawBullet(c: Canvas, p: Paint?, x: Int, dir: Int, top: Int, bottom: Int, l: Layout) {
        val transY = SpannableUtils.getSpanDrawCententY(top, bottom, l)
        if (c.isHardwareAccelerated) {
            if (sBulletPath == null) {
                sBulletPath = Path()
                sBulletPath!!.addCircle(0.0f, 0.0f, radius.toFloat(), Path.Direction.CW)
            }
            c.save()
            c.translate((x + dir * radius).toFloat(), transY)
            c.drawPath(sBulletPath!!, p!!)
            c.restore()
        } else {
            c.drawCircle((x + dir * radius).toFloat(), transY, radius.toFloat(), p!!)
        }
    }

}