package com.ashlikun.utils.simple;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashlikun.utils.ui.StatusBarCompat;
<<<<<<< HEAD

=======
>>>>>>> 930f9a2fa0a77a8a3bc5e5ed30021cd7eb103537

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ArrayMap<Integer, Integer> aaa = new ArrayMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new StatusBarCompat(this).setTransparentBar(android.R.color.transparent);
        setContentView(R.layout.main_activity);

        aaa.put(11111111, 1);
        aaa.put(2, 2);
        aaa.put(3, 3);
        aaa.put(4, 4);
        aaa.put(5, 5);
        for (Map.Entry<Integer, Integer> e : aaa.entrySet()) {
            Log.e("aaa", e.getKey() + "   " + e.getValue());
        }
    }
}
