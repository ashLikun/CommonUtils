package com.ashlikun.utils.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.ashlikun.utils.animator.AnimUtils;

import java.util.ArrayList;

import static android.R.attr.id;


/**
 * 作者　　: 李坤
 * 创建时间: 2017/6/28 13:30
 * <p>
 * 方法功能：输入框检查工具
 * 1：最后再调用check正则判断
 * 2：可监听输入状态，然后回掉接口判断
 */
public class EditHelper {
    /**
     * 全局默认的是否动画
     */
    public static boolean IS_ANIM = true;
    public Context context;
    private boolean isAnim = IS_ANIM;
    private ArrayList<EditHelperData> mEdithelpdatas;

    public EditHelper(Context context) {
        this.context = context;
    }

    public EditHelper setAnim(boolean anim) {
        isAnim = anim;
        return this;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:32
     * <p>
     * 方法功能：设置监听的输入框
     *
     * @param edits 多个被检测的EditView对象
     */
    public EditHelper setEditText(EditHelperData... edits) {
        if (mEdithelpdatas == null) {
            mEdithelpdatas = new ArrayList<>();
        }
        mEdithelpdatas.clear();
        for (EditHelperData e : edits) {
            addEditHelperData(e);
        }
        return this;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:33
     * <p>
     * 方法功能：清空
     */

    public EditHelper clear() {
        if (mEdithelpdatas != null) {
            mEdithelpdatas.clear();
        }
        return this;
    }

    public EditHelper addEditHelperData(EditHelperData edits) {
        if (edits != null) {
            if (mEdithelpdatas == null) {
                mEdithelpdatas = new ArrayList<>();
            }
            mEdithelpdatas.add(edits);
            if (!isAnim) {
                edits.setAnim(false);
            }
            addTextChangedListener(edits);
        }
        return this;
    }

    private void addTextChangedListener(EditHelperData edits) {
        if (edits.getView() instanceof TextInputLayout) {
            TextView v = edits.getTextView();
            if (v != null) {
                v.addTextChangedListener(new EditHelper.MyTextWatcher(edits));
            }
        }
    }


    public EditHelperData getEditHelperData(int index) {
        return mEdithelpdatas.get(index);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:37
     * <p>
     * 方法功能：检查是否满足
     */
    public boolean check() {
        try {
            if (mEdithelpdatas == null) {
                return false;
            }
            for (int i = 0; i < mEdithelpdatas.size(); i++) {
                EditHelperData e = mEdithelpdatas.get(i);
                if (!e.check(context)) {
                    return false;
                }
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return true;
        }

    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:37
     * <p>
     * 方法功能：检查某个view是否满足
     */

    public boolean check(int index) {
        try {
            if (mEdithelpdatas == null) {
                return false;
            }
            EditHelperData e = mEdithelpdatas.get(id);
            if (e == null || !e.check(context)) {
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static class EditHelperData {

        View view;
        String msg;
        String regex;
        boolean isAnim = true;

        public EditHelperData(View textView, String regex, String msg) {
            this.regex = regex;
            this.view = textView;
            this.msg = msg;
        }

        public EditHelperData(View textView, String regex, @StringRes int msgStringId) {
            this.regex = regex;
            this.view = textView;
            this.msg = textView.getContext().getString(msgStringId);
        }


        public EditHelperData(View textView, int regexMaxLenght, @StringRes int msgStringId) {
            this.regex = "[\\S]{1," + regexMaxLenght + "}";
            this.view = textView;
            this.msg = textView.getContext().getString(msgStringId);
        }

        public EditHelperData setAnim(boolean anim) {
            isAnim = anim;
            return this;
        }

        public TextView getTextView() {

            if (view instanceof TextInputLayout) {
                return ((TextInputLayout) view).getEditText();
            } else if (view instanceof TextView) {
                return (TextView) view;
            } else {
                return null;
            }
        }

        public View getView() {
            return view;
        }

        public EditHelperData setView(View view) {
            this.view = view;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public EditHelperData setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public EditHelperData setMsg(Context context, @StringRes int msgStringId) {
            this.msg = context.getString(msgStringId);
            return this;
        }

        public String getRegex() {
            return regex;
        }

        public EditHelperData setRegex(String regex) {
            this.regex = regex;
            return this;
        }


        public boolean check(Context context) {
            if (view == null || !getTextView().getText().toString().matches(regex)) {
                if (view instanceof TextInputLayout && ((TextInputLayout) view).isErrorEnabled()) {
                    ((TextInputLayout) view).setError(msg);
                    if (isAnim) {
                        AnimUtils.shakeLeft(view, 0.95f, 3f);
                    }
                } else {
                    SuperToast.get(msg).warn();
                    if (isAnim) {
                        AnimUtils.shakeLeft(view, 0.85f, 6f);
                    }
                }
                view.requestFocus();

                return false;
            }
            if (view instanceof TextInputLayout && ((TextInputLayout) view).isErrorEnabled()) {
                ((TextInputLayout) view).setError("");
            }
            return true;
        }

    }

    public class MyTextWatcher implements TextWatcher {

        EditHelperData editHelperData;

        public MyTextWatcher(EditHelperData editHelperData) {
            this.editHelperData = editHelperData;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (editHelperData.getView() instanceof TextInputLayout) {
                int maxLength = ((TextInputLayout) editHelperData.getView()).getCounterMaxLength();
                if (maxLength < s.length()) {
                    editHelperData.check(EditHelper.this.context);
                } else {
                    ((TextInputLayout) editHelperData.getView()).setError("");
                }
            }
        }
    }
}
