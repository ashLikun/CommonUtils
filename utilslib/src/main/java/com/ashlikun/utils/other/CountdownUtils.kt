package com.ashlikun.utils.other

import android.os.CountDownTimer
import android.widget.TextView

/**
 * @author　　: 李坤
 * 创建时间: 2021.12.13 11:40
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：倒计时工具
 */
class CountdownUtils(
    /**
     * 验证码按钮倒计时 总时长和间隔时长，默认为60秒
     */
    private val totalTime: Long = 180000,
    private val intervalTime: Long = 1000,
    private val msg: String?,
    //结束时候文字的格式化样式
    private val endTextFomat: String?
) {

    var countDownTimer: CountDownTimer? = null

    /**
     * 开始倒计时
     */
    fun start(hint: TextView) {
        /**
         * 记录当下剩余时间
         */
        countDownTimer = object : CountDownTimer(totalTime, intervalTime) {
            override fun onFinish() { // 计时完毕时触发
                hint.text = msg
                hint.isEnabled = true
            }

            override fun onTick(currentTime: Long) { // 计时过程显示
                hint.isEnabled = false
                hint.text = if (endTextFomat != null) String.format(
                    endTextFomat,
                    currentTime / 1000
                ) else "${currentTime / 1000}秒"
            }
        }
        countDownTimer?.start()
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:36
     *
     *
     * 取消倒计时
     */
    fun cancel() {
        countDownTimer?.cancel()
    }


}