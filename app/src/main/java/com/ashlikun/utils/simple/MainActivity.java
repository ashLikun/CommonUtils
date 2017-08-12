package com.ashlikun.utils.simple;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ArrayMap<Integer, Integer> aaa = new ArrayMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
