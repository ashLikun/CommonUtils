package com.ashlikun.utils.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.simple.databinding.MainActivity2Binding
import com.ashlikun.utils.ui.extend.resDrawable
import com.ashlikun.utils.ui.image.DrawableUtils
import com.ashlikun.utils.ui.modal.SuperToast
import com.ashlikun.utils.ui.modal.ToastUtils

/**
 * 作者　　: 李坤
 * 创建时间: 2020/11/2　15:40
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class Main2Activity : AppCompatActivity() {
    val binding by lazy {
        MainActivity2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        DrawableUtils.getStateListDrawable(R.mipmap.aaaaaa.resDrawable)
        binding.ceshi.setOnClickListener {
//            AnimUtils.updateTextSize(binding.textView, 30f, 50f).apply { start() }
//            ToastUtils.show("dddddddd")
//            finish()

            SuperToast["12121"].apply {
                setFinish(this@Main2Activity)
            }.show()
        }
        aa()
    }

    fun aa(): Unit {
        MainHandle.postDelayed(this, {
            LogUtils.e("aaaaaaa")
        }, 5000)
    }

}