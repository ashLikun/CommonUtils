package com.ashlikun.utils.simple;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/22　16:18
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_notification);
        new Thread() {
            @Override
            public void run() {
                super.run();
            }
        }.start();
    }
}
