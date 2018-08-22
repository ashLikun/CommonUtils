package com.ashlikun.utils.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * 作者　　: 李坤
 * 创建时间: 2017/6/28 13:30
 * <p>
 * 方法功能：输入框检查工具
 * 1：最后再调用check正则判断
 * 2：可监听输入状态，然后回掉接口判断
 */


public class EditCheck {

    private Context context;
    private IEditStatusChang mIEditStatusChang;
    private ArrayList<EditCheckData> mEdithelpdatas;
    private View[] button;

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:26
     * <p>
     * 方法功能：
     */

    public EditCheck(Context context) {
        this.context = context;
    }

    public void setEditStatusChang(IEditStatusChang mIEditStatusChang) {
        this.mIEditStatusChang = mIEditStatusChang;
    }

    /**
     * 设置按钮,会调用setEnabled
     */
    public void setButton(View... button) {
        this.button = button;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:32
     * <p>
     * 方法功能：设置监听的输入框
     *
     * @param edits 多个被检测的EditView对象
     */
    public void setEditText(EditCheckData... edits) {
        if (mEdithelpdatas == null) {
            mEdithelpdatas = new ArrayList<>();
        }
        mEdithelpdatas.clear();
        for (EditCheckData e : edits) {
            addEditText(e);
        }
    }

    public void addEditText(EditCheckData edits) {
        if (edits != null && edits.getTextView() != null) {
            if (mEdithelpdatas == null) {
                mEdithelpdatas = new ArrayList<>();
            }
            mEdithelpdatas.add(edits);
            edits.getTextView().addTextChangedListener(new MyTextWatcher(edits));
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:33
     * <p>
     * 方法功能：清空
     */

    public void clear() {
        if (mEdithelpdatas != null) {
            mEdithelpdatas.clear();
        }
    }


    public class MyTextWatcher implements TextWatcher {

        EditCheckData edits;

        public MyTextWatcher(EditCheckData edits) {
            this.edits = edits;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isHandle = false;
            boolean isCheck = edits.check();
            if (mIEditStatusChang != null) {
                isHandle = mIEditStatusChang.onEditChang(edits.getTextView(), s, isCheck);
            }
            if (!isHandle && button != null) {
                for (View v : button) {
                    v.setEnabled(isCheck);
                }

            }
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28　16:18
     * 邮箱　　：496546144@qq.com
     * <p>
     * 功能介绍 文本改变的接口
     */

    public interface IEditStatusChang {
        /**
         * 作者　　: 李坤
         * 创建时间: 2017/6/28 16:19
         * 方法功能：当edittext改变的时候调用的方法
         * 实体类必须实现这个接口，实现具体的逻辑
         *
         * @param textView 宿主控件
         * @param s        改变的字符串
         * @param isCheck  是否验证通过
         * @return 是否消耗了这个处理，（关系到设置button）false设置，true不设置
         */
        boolean onEditChang(TextView textView, Editable s, boolean isCheck);
    }

    public static class EditCheckData {

        TextView view;
        String regex;

        public EditCheckData(TextView textView, String regex, String msg) {
            this.regex = regex;
            this.view = textView;
        }

        public EditCheckData(TextView textView, String regex) {
            this.regex = regex;
            this.view = textView;
        }


        public EditCheckData(TextView textView, int regexMaxLenght, @StringRes int msgStringId) {
            this.regex = "[\\S]{1," + regexMaxLenght + "}";
            this.view = textView;
        }

        public TextView getTextView() {
            return view;
        }

        public void setView(TextView view) {
            this.view = view;
        }


        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }


        public boolean check() {
            if (view == null || !getTextView().getText().toString().matches(regex)) {
                return false;
            }
            return true;
        }

    }
}
