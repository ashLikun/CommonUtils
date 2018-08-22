package com.ashlikun.utils.simple;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.SpannableUtils;
import com.ashlikun.utils.ui.DrawableUtils;
import com.ashlikun.utils.ui.FocusLinkMovementMethod;
import com.ashlikun.utils.ui.NotificationUtil;
import com.ashlikun.utils.ui.StatusBarCompat;
import com.ashlikun.utils.ui.SuperToast;


public class MainActivity extends AppCompatActivity {
    ArrayMap<Integer, Integer> aaa = new ArrayMap<>();
    ViewPager viewPager;
    DrawableUtils drawableUtils;
    StatusBarCompat statusBarCompat;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.init(getApplication());
        AppUtils.setDebug(true);
        drawableUtils = new DrawableUtils(this);
        setContentView(R.layout.main_viewgroup_activity);
        statusBarCompat = new StatusBarCompat(this);
        statusBarCompat.setStatusBarColor(0xffffffff);
        tv = findViewById(R.id.view4);
//        StringBuilder sb = new StringBuilder();
//        sb.append("getDeviceBrand   =  " + DeviceUtil.getDeviceBrand());
//        sb.append("\ngetSystemModel   =  " + DeviceUtil.getSystemModel());
//        sb.append("\ngetManufacturer   =  " + DeviceUtil.getManufacturer());
//        sb.append("\ngetSystemVersion   =  " + DeviceUtil.getSystemVersion());
//
//
//        sb.append("\n\n\n\nisXiaomi   =  " + RomUtils.isXiaomi());
//        sb.append("\nisFlyme   =  " + RomUtils.isFlyme());
//        sb.append("\nisHuawei   =  " + RomUtils.isHuawei());
//        sb.append("\nisOppo   =  " + RomUtils.isOppo());
//        sb.append("\nisVivo   =  " + RomUtils.isVivo());
//        sb.append("\nisSamsung   =  " + RomUtils.isSamsung());
//        sb.append("\nisSmartisan   =  " + RomUtils.isSmartisan());
//        sb.append("\nis360   =  " + RomUtils.is360());
        tv.setHighlightColor(Color.LTGRAY);
        tv.setMovementMethod(FocusLinkMovementMethod.getInstance());
        tv.setText(SpannableUtils.getBuilder("1111111111").setClickSpan(new MyClickableSpan()).setBackgroundColor(0xffff0000)
                .append("\n\n")
                .append("22222222222222").setClickSpan(new MyClickableSpan())
                .append("\n\n")
                .append("3333333333").setClickSpan(new MyClickableSpan())
                .append("\n\n")
                .append("4444444444444").setClickSpan(new MyClickableSpan())
                .append("\n\n")
                .append("5555555555555555555").setClickSpan(new MyClickableSpan())
                .create());
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

    private class MyClickableSpan extends SpannableUtils.XClickableSpan {


        @Override
        public void onClick(View widget) {
            SuperToast.get("aaaaaaa").info();
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            NotificationUtil.notification(intent, 123, R.mipmap.ic_launcher_round, "标题", "你收到通知啦");
        }
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
