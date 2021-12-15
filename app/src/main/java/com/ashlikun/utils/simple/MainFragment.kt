package com.ashlikun.utils.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ashlikun.utils.simple.R
import com.ashlikun.utils.ui.status.StatusBarCompat

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/14　15:57
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val view = view!!.findViewById<View>(R.id.toolbar)
        StatusBarCompat.setTransparentViewMargin(view)
    }
}