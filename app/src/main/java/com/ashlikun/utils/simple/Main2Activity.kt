package com.ashlikun.utils.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.utils.animator.AnimUtils
import com.ashlikun.utils.other.LogUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.other.hex.byteToASCIIStrDelCtrl
import com.ashlikun.utils.other.hex.byteToASCIIStrSub00
import com.ashlikun.utils.simple.databinding.MainActivity2Binding
import com.ashlikun.utils.ui.extend.dp
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
        DrawableUtils.createTextDraw(binding.textView, size = 24.dp, drawableId = R.drawable.main_ic_ebike_button_laba_def)
        binding.ceshi.setOnClickListener {
            AnimUtils.updateTextSize(binding.textView, 30f, 50f).apply { start() }
            ToastUtils.show("dddddddd")
            finish()
            val a = 0x10
            //!"#$%&'()*+
            LogUtils.e(byteArrayOf(16, 31, 33, 34, 35, 36, 37, 38, 39, 40, 0, 0, 0).byteToASCIIStrSub00)
            binding.textView.text = byteArrayOf(16, 31, 33, 34, 35, 36, 37, 38, 39, 40, 0, 0, 0).byteToASCIIStrSub00.toString()
            SuperToast["12121"].apply {
                setDuration(10000)
                setFinish(this@Main2Activity)
            }.show()
        }
        aa()
    }

    fun aa(): Unit {
        MainHandle.postDelayed(this, 5000) {
            LogUtils.e("aaaaaaa")
        }
    }

}