package com.ashlikun.utils.simple;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.other.DimensUtils;
import com.ashlikun.utils.other.SpannableUtils;
import com.ashlikun.utils.other.spannable.XClickableSpan;
import com.ashlikun.utils.ui.BitmapUtil;
import com.ashlikun.utils.ui.DrawableUtils;
import com.ashlikun.utils.ui.NotificationUtil;
import com.ashlikun.utils.ui.ScreenInfoUtils;
import com.ashlikun.utils.ui.StatusBarCompat;
import com.ashlikun.utils.ui.SuperToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    DrawableUtils drawableUtils;
    StatusBarCompat statusBarCompat;
    ImageView imageView;
    TextViewCompat textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.init(getApplication());
        AppUtils.setDebug(true);
        drawableUtils = new DrawableUtils(this);
        setContentView(R.layout.main_viewgroup_activity);
        statusBarCompat = new StatusBarCompat(this);
        statusBarCompat.setStatusBarColor(0xffffffff);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.text);

        GradientDrawable drawable = new GradientDrawable();
        int size = DimensUtils.dip2px(this, 4f);
        drawable.setBounds(0, 0, size, size);
        drawable.setColor(0xfffa5c4f);
        drawable.setCornerRadius(size / 2f);

        textView.setText(SpannableUtils.getBuilder("").setLineSpacingExtra(textView.getLineSpacingExtra()).setDrawable(drawable)
                .append("文案已复制\n")
                .append("").setDrawable(drawable)
                .append("分享二维码已保存\n")
                .append("").setDrawable(drawable)
                .append("图案已保存到相册")
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

    private class MyClickableSpan extends XClickableSpan {


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
        SuperToast.showInfoMessage("aaaaaaaaaaaaaaaaaa");
        Bitmap logo = BitmapUtil.decodeResource(this, R.mipmap.ic_launcher_round,
                DimensUtils.dip2px(this, 50),
                DimensUtils.dip2px(this, 50));
//        imageView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
//        imageView.setDrawingCacheEnabled(false);
        Bitmap bitmap = BitmapUtil.decodeResource(this, R.mipmap.timg, ScreenInfoUtils.getWidth(), ScreenInfoUtils.getHeight());
        imageView.setImageBitmap(BitmapUtil.getWaterMaskImage(bitmap, logo, null));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getLocation();
    }

    public void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("aaa", "location lat:" + location.getLatitude() + ",lon:" + location.getLongitude());
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                } catch (IOException ioException) {

                    Log.e("aaa", "服务不可用！", ioException);

                }

                if (addresses == null || addresses.size() == 0) {
                    Log.e("aaa", "没有找到相关地址!");
                } else {
                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<String>();
                    String curAddr = "";
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                        curAddr = curAddr + address.getAddressLine(i);
                    }

                    if (!TextUtils.isEmpty(address.getFeatureName())
                            && !addressFragments.isEmpty()
                            && !addressFragments.get(addressFragments.size() - 1).equals(address.getFeatureName())) {
                        addressFragments.add(address.getFeatureName());
                        curAddr = curAddr + address.getFeatureName();
                    }
                    Log.e("", "详情地址已经找到,地址:" + curAddr);
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }
}
