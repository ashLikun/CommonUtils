package com.ashlikun.utils.ui.modal

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.R
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.ui.extend.dp
import com.ashlikun.utils.ui.resources.ColorUtils
import com.ashlikun.utils.ui.resources.ResUtils.getColor
import com.ashlikun.utils.ui.resources.ResUtils.getDimensionPixelSize
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/14 23:34
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：自定义toast样式
 */
typealias ToastDismissed = () -> Unit

class SuperToast private constructor(builder: Builder) {
    @IntDef(value = [Info, Confirm, Warning, Error])
    @Retention(RetentionPolicy.SOURCE)
    annotation class Type

    var animSet: AnimatorSet? = null

    //要在主线程
    private fun cretae(builder: Builder) {
        initToast(builder)
        val mView: View
        if (builder.layoutId == R.layout.toast_super && mToast!!.view != null && TOAST_VIEW_TAG == mToast!!.view.tag) {
            mView = mToast!!.view
        } else {
            mView = LayoutInflater.from(AppUtils.app).inflate(builder.layoutId, null)
            mView.tag = TOAST_VIEW_TAG
        }
        setViewContent(mView, builder)
        startAnim(builder, mView)
        mToast!!.setGravity(builder.gravity, builder.offsetX, builder.offsetY)
        mToast!!.view = mView
        mToast!!.duration = builder.duration
        mToast!!.show()
        if (builder.isFinish || builder.isCancelable) {
            var dialog: DialogTransparency? = null
            if (builder.activity != null) {
                dialog = DialogTransparency(builder.activity!!)
                dialog.show()
            }
            val dialog2 = dialog
            MainHandle.postDelayed({
                builder.callback?.invoke()
                dialog2?.dismiss()
                if (builder.isFinish && builder.activity != null && !builder.activity!!.isFinishing) {
                    builder.activity!!.finish()
                }
            }, if (mToast!!.duration == Toast.LENGTH_SHORT) 2000 else 3500)
        }
    }

    private fun startAnim(builder: Builder, mView: View) {
        mView.clearAnimation()
        if (animSet == null) {
            animSet = AnimatorSet()
        } else {
            animSet?.cancel()
        }
        if (builder.animator == null) {
            builder.animator = AnimatorSet()
            val alpha = ObjectAnimator.ofFloat(mView, "alpha", 0f, 1f)
            val scaleX = ObjectAnimator.ofFloat(mView, "scaleX", 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(mView, "scaleY", 0f, 1f)
            (builder.animator as AnimatorSet?)!!.playTogether(scaleX, scaleY, alpha)
            (builder.animator as AnimatorSet?)!!.duration = 300
        }
        builder.animator!!.start()
    }

    private fun setViewContent(view: View, builder: Builder) {
        if (!builder.isCustom) {
            if (builder.backGroundDrawable != null) {
                ViewCompat.setBackground(view, builder.backGroundDrawable)
            } else {
                val drawable = GradientDrawable()
                drawable.setColor(builder.backgroundColor)
                drawable.setStroke(1.dp, getBackgroundShen(builder.backgroundColor))
                drawable.cornerRadius = 4.dp.toFloat()
                ViewCompat.setBackground(view, drawable)
            }
            val imageView = view.findViewById<View>(R.id.img) as ImageView
            if (!builder.isShowIcon) {
                imageView.visibility = View.GONE
            } else {
                var draw = ContextCompat.getDrawable(view.context, builder.iconRes)
                //自动设置文字颜色
                if ("auto_tintColor" == imageView.tag) {
                    draw = DrawableCompat.wrap(draw!!)
                    DrawableCompat.setTint(
                        draw,
                        if (ColorUtils.isColorDrak(builder.backgroundColor)) -0x1 else -0x1000000
                    )
                }
                imageView.setImageDrawable(draw)
            }
            val textView = view.findViewById<View>(R.id.msg) as TextView
            //自动设置文字颜色
            if ("auto_textColor" == textView.tag) {
                textView.setTextColor(if (ColorUtils.isColorDrak(builder.backgroundColor)) -0x1 else -0x1000000)
            }
            textView.text = builder.msg
        } else {
            (view.findViewById<View>(R.id.msg) as TextView).text = builder.msg
        }
    }

    private fun getBackgroundShen(backgroundColor: Int): Int {
        val red = (Color.red(backgroundColor) * COLOR_DEPTH).toInt()
        val green = (Color.green(backgroundColor) * COLOR_DEPTH).toInt()
        val blue = (Color.blue(backgroundColor) * COLOR_DEPTH).toInt()
        return Color.argb(Color.alpha(backgroundColor), red, green, blue)
    }

    class Builder(val msg: String) {
        var isShowIcon = true

        @DrawableRes
        var iconRes = NO_RES
        var backgroundColor = NO_RES

        /**
         * 背景
         * 如果设置那么内部就不会设置其他背景
         */
        var backGroundDrawable: Drawable? = null
        var duration = Toast.LENGTH_SHORT
        var gravity = CHANG_GRAVITY
        var offsetX = 0
        var offsetY = 0

        @Type
        private var type = Info

        @LayoutRes
        var layoutId = R.layout.toast_super

        /**
         * 这个Layout 是否是自定义的，如果是，那么工具就不会这是其他属性
         */
        var isCustom = false
        var isFinish = false
        var isCancelable = false

        //是否取消之前正在显示的
        var isCancelBefore = true

        //要finish的activity
        var activity: Activity? = null

        //toast销毁的回调
        var callback: ToastDismissed? = null

        //toast的动画
        var animator: Animator? = null

        fun setIconRes(@DrawableRes iconRes: Int): Builder {
            this.iconRes = iconRes
            return this
        }

        fun setNoIcon(): Builder {
            isShowIcon = false
            return this
        }

        fun setBackgroundColor(@ColorRes backgroundColor: Int): Builder {
            this.backgroundColor = getColor(backgroundColor)
            return this
        }

        fun setBackgroundDrawable(backGroundDrawable: Drawable?): Builder {
            this.backGroundDrawable = backGroundDrawable
            return this
        }

        fun setDuration(duration: Int): Builder {
            this.duration = duration
            return this
        }

        fun setType(type: Int): Builder {
            this.type = type
            return this
        }

        fun setLayoutId(layoutId: Int): Builder {
            this.layoutId = layoutId
            return this
        }

        fun setLayoutId(layoutId: Int, isCustom: Boolean): Builder {
            this.layoutId = layoutId
            this.isCustom = isCustom
            return this
        }

        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        fun setOffsetX(offsetX: Int): Builder {
            this.offsetX = offsetX
            return this
        }

        fun setOffsetY(offsetY: Int): Builder {
            this.offsetY = offsetY
            return this
        }

        fun setFinish(activity: Activity?): Builder {
            if (activity != null) {
                isFinish = true
                this.activity = activity
            }
            return this
        }

        fun setCancelable(activity: Activity?): Builder {
            isCancelable = true
            this.activity = activity
            return this
        }

        /**
         * 是否取消之前正在显示的
         *
         * @return
         */
        fun setCancelBefore(isCancelBefore: Boolean): Builder {
            this.isCancelBefore = isCancelBefore
            return this
        }

        fun setFinish(): Builder {
            isFinish = true
            return this
        }

        fun setFinishCallback(callback: ToastDismissed): Builder {
            this.callback = callback
            isFinish = true
            return this
        }

        fun cancelBefore(): Boolean {
            return isCancelBefore
        }

        fun setAnimator(animator: Animator?): Builder {
            this.animator = animator
            return this
        }

        fun ok() {
            type = Confirm
            show()
        }

        fun info() {
            type = Info
            show()
        }

        fun error() {
            type = Error
            show()
        }

        fun warn() {
            type = Warning
            show()
        }

        fun show() {
            if (type == Info) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getColor(R.color.super_toast_color_info)
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_info
                }
            } else if (type == Confirm) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getColor(R.color.super_toast_color_confirm)
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_confirm
                }
            } else if (type == Warning) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getColor(R.color.super_toast_color_warning)
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_warning
                }
            } else if (type == Error) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getColor(R.color.super_toast_color_error)
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_error
                }
            }
            if (INIT_OFFSET_Y == 0) {
                val resourceId: Int =
                    AppUtils.app.getResources().getIdentifier("toast_y_offset", "dimen", "android")
                if (resourceId > 0) {
                    INIT_OFFSET_Y = getDimensionPixelSize(resourceId)
                }
            }
            if (offsetY == 0 && gravity == INIT_GRAVITY) {
                offsetY = INIT_OFFSET_Y
            }
            SuperToast(this)
        }
    }


    companion object {
        private var mToast: Toast? = null
        private const val TOAST_VIEW_TAG = "TOAST_VIEW_TAG"
        private const val Info = 1 //正常
        private const val Confirm = 2 //完成
        private const val Warning = 3 //警告 orange
        private const val Error = 4 //错误 red
        private const val NO_RES = -1 //
        private const val COLOR_DEPTH = 0.92f //
        private const val INIT_GRAVITY = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM //
        var CHANG_GRAVITY = INIT_GRAVITY //可以改变
        var INIT_OFFSET_Y = 0
        fun setGravity(gravity: Int) {
            CHANG_GRAVITY = gravity
        }

        fun setGravityToInit() {
            CHANG_GRAVITY = INIT_GRAVITY
        }

        private fun initToast(builder: Builder) {
            if (mToast == null) {
                mToast = Toast.makeText(AppUtils.app, "", Toast.LENGTH_SHORT)
            } else {
                if (builder.cancelBefore()) {
                    mToast!!.cancel()
                }
                mToast = Toast.makeText(AppUtils.app, "", Toast.LENGTH_SHORT)
            }
        }

        operator fun get(msg: String): Builder {
            return Builder(msg)
        }

        /**
         * 显示完成信息
         */
        fun showConfirmMessage(result: String) {
            this[result].ok()
        }

        /**
         * 显示错误信息
         */
        fun showErrorMessage(result: String) {
            this[result].error()
        }

        /**
         * 显示警告信息
         */
        fun showWarningMessage(result: String) {
            this[result].warn()
        }

        /**
         * 显示提示信息
         *
         * @param activity 是否销毁，null：不管
         */
        fun showInfoMessage(result: String, activity: Activity? = null) {
            this[result].setFinish(activity).info()
        }

        /**
         * 显示提示信息
         *
         * @param activity 是否销毁，null：不管
         */
        fun showConfirmMessage(result: String, activity: Activity? = null) {
            this[result].setFinish(activity).ok()
        }
    }

    init {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MainHandle.post { cretae(builder) }
        } else {
            cretae(builder)
        }
    }
}