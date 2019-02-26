package com.ashlikun.utils.ui;

import android.content.Context;
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
            edits.getTextView().addTextChangedListener(new MyTextWatcher(mEdithelpdatas, edits));
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
        ArrayList<EditCheckData> allEdits;

        public MyTextWatcher(ArrayList<EditCheckData> allEdits, EditCheckData edits) {
            this.allEdits = allEdits;
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
            //检查其他的
            for (EditCheckData e : allEdits) {
                if (e != edits) {
                    if (e.isCheckOk == null) {
                        e.check();
                    }
                    if (!e.isCheckOk) {
                        if (button != null) {
                            for (View v : button) {
                                v.setEnabled(false);
                            }
                        }
                        return;
                    }
                }
            }
            boolean isCheck = edits.check();
            if (mIEditStatusChang != null) {
                isCheck = mIEditStatusChang.onEditChang(edits.getTextView(), s, isCheck);
            }
            if (button != null) {
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
         * @return 是否消耗了这个处理，（关系到设置setEnabled）
         */
        boolean onEditChang(TextView textView, CharSequence s, boolean isCheck);
    }

    public static class EditCheckData {

        TextView view;
        String regex;
        /**
         * 是否满足
         */
        Boolean isCheckOk = null;
        private IEditStatusChang mIEditStatusChang;

        public EditCheckData(TextView textView, String regex) {
            this.regex = regex;
            this.view = textView;
        }


        public EditCheckData(TextView textView, int regexMaxLenght) {
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

        public EditCheckData setRegex(String regex) {
            this.regex = regex;
            return this;
        }

        public EditCheckData setEditStatusChang(IEditStatusChang mIEditStatusChang) {
            this.mIEditStatusChang = mIEditStatusChang;
            return this;
        }

        public boolean check() {
            isCheckOk = view != null && view.getText().toString().matches(regex);
            if (mIEditStatusChang != null) {
                isCheckOk = mIEditStatusChang.onEditChang(getTextView(), view.getText(), isCheckOk);
            }
            return isCheckOk;
        }

    }
}
