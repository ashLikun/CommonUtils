package com.ashlikun.utils.ui.modal

import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.DimensUtils.dip2px
import com.ashlikun.utils.other.MainHandle

object ToastUtils {
    private var myToast: Toast? = null
    var LENGTH_SHORT = Toast.LENGTH_SHORT
    var LENGTH_LONG = Toast.LENGTH_LONG
    fun getMyToast(): Toast {
        initToast(false)
        return myToast!!
    }

    /**
     * 默认Toast，时间短
     *
     * @param content
     */
    fun getToastShort(content: String?) {
        Toast.makeText(AppUtils.app, content, Toast.LENGTH_SHORT).show()
    }

    /**
     * 默认Toast，时间长
     *
     * @param content 内容
     */
    fun getToastLong(content: String?) {
        Toast.makeText(AppUtils.app, content, Toast.LENGTH_LONG).show()
    }

    fun show(text: String, duration: Int) {
        show(text, false, duration)
    }

    fun show(text: String, cancelBefore: Boolean = false, duration: Int = LENGTH_LONG) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MainHandle.post {
                cretae(
                    text,
                    cancelBefore,
                    duration,
                    Gravity.BOTTOM,
                    0,
                    dip2px(AppUtils.app, 20f)
                )
            }
        } else {
            cretae(text, cancelBefore, duration, Gravity.BOTTOM, 0, dip2px(AppUtils.app, 20f))
        }
    }

    fun showLong(text: String) {
        show(text, false, LENGTH_LONG)
    }

    fun showShort(text: String) {
        show(text, false, LENGTH_SHORT)
    }

    fun showLong(text: String, cancelBefore: Boolean) {
        show(text, cancelBefore, LENGTH_LONG)
    }

    fun showShort(text: String, cancelBefore: Boolean) {
        show(text, cancelBefore, LENGTH_SHORT)
    }

    private fun initToast(cancelBefore: Boolean) {
        if (myToast == null) {
            myToast = Toast.makeText(AppUtils.app, "", Toast.LENGTH_SHORT)
        } else {
            if (cancelBefore) {
                myToast!!.cancel()
            }
            myToast = Toast.makeText(AppUtils.app, "", Toast.LENGTH_SHORT)
        }
    }

    fun show(
        text: String, cancelBefore: Boolean, duration: Int,
        gravity: Int, xOffsetDp: Int, yOffsetDp: Int
    ) {
        if (text.isEmpty()) {
            return
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MainHandle.post { cretae(text, cancelBefore, duration, gravity, xOffsetDp, yOffsetDp) }
        } else {
            cretae(text, cancelBefore, duration, gravity, xOffsetDp, yOffsetDp)
        }
    }

    //要在主线程
    fun cretae(
        text: String?, cancelBefore: Boolean, duration: Int,
        gravity: Int, xOffsetDp: Int, yOffsetDp: Int
    ) {
        initToast(cancelBefore)
        if (myToast != null) {
            myToast!!.setGravity(
                gravity, dip2px(AppUtils.app, xOffsetDp.toFloat()),
                dip2px(AppUtils.app, yOffsetDp.toFloat())
            )
            myToast!!.setText(text)
            myToast!!.duration = duration
            myToast!!.show()
        }
    }
}