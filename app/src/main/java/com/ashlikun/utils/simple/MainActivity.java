package com.ashlikun.utils.simple;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.DeviceUtil;
import com.ashlikun.utils.other.RomUtils;
import com.ashlikun.utils.ui.DrawableUtils;
import com.ashlikun.utils.ui.StatusBarCompat;


public class MainActivity extends AppCompatActivity {
    ArrayMap<Integer, Integer> aaa = new ArrayMap<>();
    ViewPager viewPager;
    DrawableUtils drawableUtils;
    StatusBarCompat statusBarCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.init(getApplication());
        AppUtils.setDebug(true);
        drawableUtils = new DrawableUtils(this);
        setContentView(R.layout.main_viewgroup_activity);
        statusBarCompat = new StatusBarCompat(this);
        statusBarCompat.setStatusBarColor(0xffffffff);
        TextView tv = findViewById(R.id.view4);
        StringBuilder sb = new StringBuilder();
        sb.append("getDeviceBrand   =  " + DeviceUtil.getDeviceBrand());
        sb.append("\ngetSystemModel   =  " + DeviceUtil.getSystemModel());
        sb.append("\ngetManufacturer   =  " + DeviceUtil.getManufacturer());
        sb.append("\ngetSystemVersion   =  " + DeviceUtil.getSystemVersion());


        sb.append("\n\n\n\nisXiaomi   =  " + RomUtils.isXiaomi());
        sb.append("\nisFlyme   =  " + RomUtils.isFlyme());
        sb.append("\nisHuawei   =  " + RomUtils.isHuawei());
        sb.append("\nisOppo   =  " + RomUtils.isOppo());
        sb.append("\nisVivo   =  " + RomUtils.isVivo());
        sb.append("\nisSamsung   =  " + RomUtils.isSamsung());
        sb.append("\nisSmartisan   =  " + RomUtils.isSmartisan());
        sb.append("\nis360   =  " + RomUtils.is360());
        tv.setText(sb);
    }

    public void onView1Click(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //SuperToast.get("aaaaaaa").info();
            }
        }.start();

    }

    int statusColor = 0xffff0000;

    public void onView2Click(View view) {
        statusColor = statusColor + 10;
        statusBarCompat.setStatusBarColor(statusColor);
        //SuperToast.get("aaaaaaa").ok();
    }

    public void onView3Click(View view) {
        // SuperToast.get("aaaaaaa").warn();
    }

    public void onView4Click(View view) {
        //SuperToast.get("aaaaaaa").error();
    }

}
