package com.ashlikun.utils.ui.modal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.ashlikun.utils.R;
import com.ashlikun.utils.ui.ScreenInfoUtils;

/**
 * 作者　　: 李坤
 * 创建时间:2017/9/26 0026　21:27
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：全屏的透明对话框
 */

public class DialogTransparency extends Dialog {
    private Context context;

    public DialogTransparency(Context context) {
        this(context, R.style.SnackBarTransparency);
    }

    public DialogTransparency(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        View view = new View(context);
        setContentView(view);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //设置宽度
        lp.width = (ScreenInfoUtils.getWidth());
        //设置宽度
        lp.height = (ScreenInfoUtils.getHeight());
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER;
    }


}
