package com.ashlikun.utils.assist

import android.hardware.Camera
import android.os.Build
import com.ashlikun.utils.other.coroutines.taskLaunch
import com.ashlikun.utils.other.coroutines.taskLaunchMain
import kotlinx.coroutines.Job

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 14:59
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：手电筒
 * <uses-permission android:name="android.permission.FLASHLIGHT"></uses-permission>
 * <uses-permission android:name="android.permission.CAMERA"></uses-permission>
 */

class FlashLight(var camera: Camera? = null) {

    companion object {
        /**
         * 超过3分钟自动关闭，防止损伤硬件
         */
        private const val OFF_TIME = 3 * 60 * 1000L
    }

    //是否自动关闭
    var isAutoOff = false
    var job: Job? = null

    /**
     * 打开闪光灯
     */
    fun on(): Boolean {
        if (camera == null) {
            camera = Camera.open()
        }
        camera?.startPreview()
        val parameter = camera!!.getParameters()
        parameter.flashMode = Camera.Parameters.FLASH_MODE_TORCH
        camera?.setParameters(parameter)
        job?.cancel()
        if (isAutoOff) {
            job = taskLaunchMain(delayTime = OFF_TIME) {
                off()
            }
        }
        return true
    }

    /**
     * 关闭闪光灯
     */
    fun off(): Boolean {
        if (camera != null) {
            job?.cancel()
            val parameter = camera!!.parameters
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.flashMode = Camera.Parameters.FLASH_MODE_OFF
            } else {
                parameter["flash-mode"] = "off"
            }
            camera?.parameters = parameter
            camera?.stopPreview()
            camera?.release()
        }
        return true
    }


}