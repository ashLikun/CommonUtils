package com.ashlikun.utils.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/14 23:35
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：输入框检查工具
 * 1：最后再调用check正则判断
 * 2：可监听输入状态，然后回掉接口判断
 */

/**
 * 当edittext改变的时候调用的方法
 * 实体类必须实现这个接口，实现具体的逻辑
 *
 * @param textView 宿主控件
 * @param s        改变的字符串
 * @param isCheck  是否验证通过
 * @return 是否消耗了这个处理，（关系到设置setEnabled）
 */
typealias IEditStatusChang = (textView: TextView, s: CharSequence, isCheck: Boolean) -> Boolean

class EditCheck(
    val context: Context,
    var statusChang: IEditStatusChang? = null,
    var helpDatas: MutableList<EditCheckData> = mutableListOf(),
    /**
     * 设置按钮,会调用setEnabled
     */
    var button: Array<View>? = null
) {


    /**
     * 多个被检测的EditView对象
     */
    fun setEditText(vararg edits: EditCheckData) {
        helpDatas.clear()
        for (e in edits) {
            addEditText(e)
        }
    }

    fun addEditText(edits: EditCheckData) {
        helpDatas.add(edits)
        edits.textView.addTextChangedListener(MyTextWatcher(helpDatas, edits))
    }

    /**
     * 清空
     */
    fun clear() {
        helpDatas.clear()
    }

    inner class MyTextWatcher(var allEdits: MutableList<EditCheckData>, var edits: EditCheckData) :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            //检查其他的
            for (e in allEdits) {
                if (e !== edits) {
                    e.check()
                    if (!e.isCheckOk) {
                        if (button != null) {
                            for (v in button!!) {
                                v.isEnabled = false
                            }
                        }
                        return
                    }
                }
            }
            var isCheck = edits.check()
            if (statusChang != null) {
                isCheck = statusChang!!.invoke(edits.textView, s, isCheck)
            }
            button?.forEach {
                it.isEnabled = isCheck
            }
        }
    }


    class EditCheckData(
        var textView: TextView,
        var regex: String? = null,
        regexMaxLenght: String? = null,
        var statusChang: IEditStatusChang? = null
    ) {
        init {
            if (regex.isNullOrEmpty() && !regexMaxLenght.isNullOrEmpty()) {
                regex = "[\\S]{1,$regexMaxLenght}"
            }
            if (regex.isNullOrEmpty()) throw RuntimeException("regex is  not null")
        }

        /**
         * 是否满足
         */
        var isCheckOk = false


        fun setView(view: TextView) {
            textView = view
        }

        fun setRegex(regex: String): EditCheckData {
            this.regex = regex
            return this
        }

        fun check(): Boolean {
            isCheckOk = textView.text.toString().matches(regex!!.toRegex())
            if (statusChang != null) {
                isCheckOk = statusChang!!.invoke(textView, textView!!.text, isCheckOk)
            }
            return isCheckOk
        }
    }
}