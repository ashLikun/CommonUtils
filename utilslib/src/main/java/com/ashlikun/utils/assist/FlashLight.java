package com.ashlikun.utils.assist;

import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/6 0006  14:31
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：手电筒
 * <uses-permission android:name="android.permission.FLASHLIGHT"/>
 * <uses-permission android:name="android.permission.CAMERA"/>
 */

public class FlashLight {

    private Camera camera;
    private Handler handler = new Handler();
    private boolean isAutoOff;//是否自动关闭
    /**
     * 超过3分钟自动关闭，防止损伤硬件
     */
    private static final int OFF_TIME = 3 * 60 * 1000;

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/6 0006 14:56
     * <p>
     * 方法功能：打开闪光灯
     */
    public boolean on() {
        if (camera == null) {
            camera = Camera.open();
            camera.startPreview();
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
            handler.removeCallbacksAndMessages(null);
            if (isAutoOff) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        off();
                    }
                }, OFF_TIME);
            }
        }
        return true;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/6 0006 14:56
     * <p>
     * 方法功能：关闭闪光灯
     */
    public boolean off() {
        if (camera != null) {
            handler.removeCallbacksAndMessages(null);
            Camera.Parameters parameter = camera.getParameters();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                parameter.set("flash-mode", "off");
            }
            camera.setParameters(parameter);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        return true;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/8/6 0006 14:57
     * <p>
     * 方法功能：是否自动关闭
     */

    public void setAutoOff(boolean autoOff) {
        isAutoOff = autoOff;
    }
}
