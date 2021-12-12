package com.ashlikun.utils.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ashlikun.utils.simple.databinding.MainNotificationBinding

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/22　16:18
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class NotificationActivity : AppCompatActivity() {
    val binding by lazy {
        MainNotificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        object : Thread() {
        }.start()
    }
}