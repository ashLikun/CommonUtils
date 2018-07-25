package com.ashlikun.utils.simple;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.SpannableUtils;
import com.ashlikun.utils.ui.DrawableUtils;
import com.ashlikun.utils.ui.StatusBarCompat;
import com.ashlikun.utils.ui.SuperToast;

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ArrayMap<Integer, Integer> aaa = new ArrayMap<>();
    ViewPager viewPager;
    DrawableUtils drawableUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.init(getApplication());
        AppUtils.setDebug(true);
        drawableUtils = new DrawableUtils(this);
        setContentView(R.layout.main_viewgroup_activity);
        new StatusBarCompat(this).setTransparentBar(android.R.color.transparent);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new MainFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }
        });

        aaa.put(11111111, 1);
        aaa.put(2, 2);
        aaa.put(3, 3);
        aaa.put(4, 4);
        aaa.put(5, 5);
        for (Map.Entry<Integer, Integer> e : aaa.entrySet()) {
            Log.e("aaa", e.getKey() + "   " + e.getValue());
        }

        TextView view = (TextView) findViewById(R.id.view);
        view.setTextColor(drawableUtils.createColorSelect(R.color.black, R.color.white));

        view.setSelected(false);

//        for (int i = 0; i < 10; i++) {
//            ToastUtils.showShort("aaa" + i);
//        }
        TextView tv = (TextView) findViewById(R.id.view4);
//        tv.setText(SpannableUtils.getBuilder("￥").setProportion(0.6f).setAlignTopDp(7)
//                .append("123.33")
//                .create());
        SpannableStringBuilder aa = SpannableUtils.getBuilder("第一个:").setAlign(Layout.Alignment.ALIGN_NORMAL)
                .append("123.33\n").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .append("第二个")
                .append("aaaaa\n").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .create();
        String text = "Sometimes I ";
        AlignmentSpan.Standard alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(text);
        spannableString.setSpan(alignmentSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
    }

    public void onView1Click(View view) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SuperToast.get("aaaaaaa").info();
            }
        }.start();

    }

    public void onView2Click(View view) {
        SuperToast.get("aaaaaaa").ok();
    }

    public void onView3Click(View view) {
        SuperToast.get("aaaaaaa").warn();
    }

    public void onView4Click(View view) {
        SuperToast.get("aaaaaaa").error();
    }

}
