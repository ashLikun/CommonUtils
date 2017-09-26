package com.ashlikun.utils.ui;

import android.os.Handler;
import android.os.Looper;

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/27 0027　2:41
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class MainHandle extends Handler {
    static MainHandle mainHandle;

    private MainHandle(Looper looper) {
        super(looper);
    }

    public static MainHandle get() {
        if (mainHandle == null) {
            synchronized (MainHandle.class) {
                if (mainHandle == null) {
                    mainHandle = new MainHandle(Looper.getMainLooper());
                }
            }
        }
        return mainHandle;
    }

}
