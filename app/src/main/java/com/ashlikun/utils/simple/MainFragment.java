package com.ashlikun.utils.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.utils.ui.StatusBarCompat;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/14　15:57
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView().findViewById(R.id.toolbar);
        StatusBarCompat.setTransparentViewMargin(view);

    }
}
