package com.ashlikun.utils.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.ashlikun.utils.animator.AnimUtils.startShakeLeft
import com.ashlikun.utils.ui.modal.SuperToast
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/**
 * 作者　　: 李坤
 * 创建时间: 2017/6/28 13:30
 *
 *
 * 输入框检查工具
 * 1：最后再调用check正则判断
 * 2：可监听输入状态，然后回掉接口判断
 */
class EditHelper(var context: Context) {
    private var isAnim = IS_ANIM
    private var mEdithelpdatas: ArrayList<EditHelperData>? = null
    fun setAnim(anim: Boolean): EditHelper {
        isAnim = anim
        return this
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:32
     *
     *
     * 设置监听的输入框
     *
     * @param edits 多个被检测的EditView对象
     */
    fun setEditText(vararg edits: EditHelperData?): EditHelper {
        if (mEdithelpdatas == null) {
            mEdithelpdatas = ArrayList()
        }
        mEdithelpdatas!!.clear()
        for (e in edits) {
            addEditHelperData(e)
        }
        return this
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:33
     *
     *
     * 清空
     */
    fun clear(): EditHelper {
        if (mEdithelpdatas != null) {
            mEdithelpdatas!!.clear()
        }
        return this
    }

    fun addEditHelperData(edits: EditHelperData?): EditHelper {
        if (edits != null) {
            if (mEdithelpdatas == null) {
                mEdithelpdatas = ArrayList()
            }
            mEdithelpdatas!!.add(edits)
            if (!isAnim) {
                edits.setAnim(false)
            }
            addTextChangedListener(edits)
        }
        return this
    }

    private fun addTextChangedListener(edits: EditHelperData) {
        if (edits.view is TextInputLayout) {
            val v = edits.textView
            v?.addTextChangedListener(MyTextWatcher(edits))
        }
    }

    fun getEditHelperData(index: Int): EditHelperData {
        return mEdithelpdatas!![index]
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:37
     *
     *
     * 检查是否满足
     */
    fun check(): Boolean {
        return try {
            if (mEdithelpdatas == null) {
                return false
            }
            for (i in mEdithelpdatas!!.indices) {
                val e = mEdithelpdatas!![i]
                if (!e.check(context)) {
                    return false
                }
            }
            true
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            true
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:37
     *
     *
     * 检查某个view是否满足
     */
    fun check(index: Int): Boolean {
        return try {
            if (mEdithelpdatas == null) {
                return false
            }
            val e = mEdithelpdatas!![index]
            if (e == null || !e.check(context)) {
                false
            } else true
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            true
        }
    }

    class EditHelperData {
        var view: View
        var msg: String
        var regex: String
        var isAnim = true

        constructor(textView: View, regex: String, msg: String) {
            this.regex = regex
            view = textView
            this.msg = msg
        }

        constructor(textView: View, regex: String, @StringRes msgStringId: Int) {
            this.regex = regex
            view = textView
            msg = textView.context.getString(msgStringId)
        }

        constructor(textView: View, regexMaxLenght: Int, @StringRes msgStringId: Int) {
            regex = "[\\S]{1,$regexMaxLenght}"
            view = textView
            msg = textView.context.getString(msgStringId)
        }

        fun setAnim(anim: Boolean): EditHelperData {
            isAnim = anim
            return this
        }

        val textView: TextView?
            get() = if (view is TextInputLayout) {
                (view as TextInputLayout).editText
            } else if (view is TextView) {
                view as TextView?
            } else {
                null
            }

        fun setView(view: View): EditHelperData {
            this.view = view
            return this
        }

        fun setMsg(msg: String): EditHelperData {
            this.msg = msg
            return this
        }

        fun setMsg(context: Context, @StringRes msgStringId: Int): EditHelperData {
            msg = context.getString(msgStringId)
            return this
        }

        fun setRegex(regex: String): EditHelperData {
            this.regex = regex
            return this
        }

        fun check(context: Context): Boolean {
            if (view == null || !textView!!.text.toString().matches(regex.toRegex())) {
                if (view is TextInputLayout && (view as TextInputLayout).isErrorEnabled) {
                    (view as TextInputLayout).error = msg
                    if (isAnim) {
                        startShakeLeft(view, 0.95f, 3f)
                    }
                } else {
                    SuperToast.get(msg).warn()
                    if (isAnim) {
                        startShakeLeft(view!!, 0.85f, 6f)
                    }
                }
                view!!.requestFocus()
                return false
            }
            if (view is TextInputLayout && (view as TextInputLayout).isErrorEnabled) {
                (view as TextInputLayout).error = ""
            }
            return true
        }
    }

    inner class MyTextWatcher(var editHelperData: EditHelperData) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            if (editHelperData.view is TextInputLayout) {
                val maxLength = (editHelperData.view as TextInputLayout?)!!.counterMaxLength
                if (maxLength < s.length) {
                    editHelperData.check(context)
                } else {
                    (editHelperData.view as TextInputLayout?)!!.error = ""
                }
            }
        }
    }

    companion object {
        /**
         * 全局默认的是否动画
         */
        var IS_ANIM = true
    }
}