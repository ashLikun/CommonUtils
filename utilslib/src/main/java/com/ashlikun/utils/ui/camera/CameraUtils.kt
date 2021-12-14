package com.ashlikun.utils.ui.camera

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.ashlikun.utils.AppUtils
import java.io.IOException
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/5　14:06
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：相机的工具库
 */
class CameraUtils(private val context: Context = AppUtils.app) : SurfaceHolder.Callback {
    //是否正在预览
    var isReady = false
        private set

    //旋转角度
    var orientation = 0
        private set
    var surfaceHolder: SurfaceHolder? = null
        set(value) {
            field = value
            field?.addCallback(this)
        }
    var camera: Camera? = null
    var cameraId = 0


    var flashMode = Camera.Parameters.FLASH_MODE_OFF
        set(value) {
            field = value
            try {
                if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
                    && camera != null
                ) {
                    //  mCamera.stopPreview();
                    val parameters = camera!!.parameters
                    parameters.flashMode = field
                    camera?.parameters = parameters
                    // mCamera.startPreview();
                    flash = field === Camera.Parameters.FLASH_MODE_TORCH
                    camerListener?.onFlashImg(flash)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    var cameraFront = false //是否是前摄像头
    var flash = false //是否开启闪光灯
    var camerListener: CamerListener? = null

    /**
     * 打开或关闭闪光灯
     */
    fun setFlashMode() {
        flashMode =
            if (!flash) Camera.Parameters.FLASH_MODE_TORCH else Camera.Parameters.FLASH_MODE_OFF
    }

    /**
     * 切换摄像头
     */
    fun switchCamera() {
        val camerasNumber = Camera.getNumberOfCameras()
        if (camerasNumber > 1) {
            releaseCamera()
            chooseCamera()
        } else {
            val toast = Toast.makeText(context, "抱歉，您的设备只有一个摄像头", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    fun chooseCamera() {
        cameraFront = !cameraFront
        if (!cameraFront) {
            val cameraId = findBackFacingCamera()
            if (cameraId >= 0) {
                camera = Camera.open(cameraId)
                refreshCamera()
                camerListener?.setFlashImgVisibility(View.VISIBLE)
            }
        } else {
            val cameraId = findFrontFacingCamera()
            if (cameraId >= 0) {
                camera = Camera.open(cameraId)
                if (flash) {
                    flash = false
                    flashMode = Camera.Parameters.FLASH_MODE_OFF
                    camerListener?.onFlashImg(flash)
                    camerListener?.setFlashImgVisibility(View.GONE)
                }
                refreshCamera()
            }
        }
    }

    /**
     * 初始化摄像头
     */
    @Throws(IOException::class)
    fun initCamera() {
        if (camera != null) {
            freeCameraResource()
        }
        try {
            camera = Camera.open(findBackFacingCamera())
        } catch (e: Exception) {
            e.printStackTrace()
            freeCameraResource()
        }
        if (camera == null) return
        refreshCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        isReady = try {
            initCamera()
            camera?.setPreviewDisplay(holder)
            optimizeCameraDimens()
            camera?.startPreview()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isReady = false
    }

    /**
     * 释放摄像头资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    fun freeCameraResource() {
        camera?.setPreviewCallback(null)
        camera?.stopPreview()
        camera?.lock()
        camera?.release()
        camera = null
    }

    private fun releaseCamera() {
        // stop and release camera
        camera?.release()
        camera = null
    }

    fun refreshCamera() {
        if (surfaceHolder?.surface == null) {
            return
        }
        try {
            camera?.stopPreview()
        } catch (e: Exception) {
        }
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera?.setDisplayOrientation(90.also { orientation = it })
        }

        // start preview with new settings
        try {
            camera?.setPreviewDisplay(surfaceHolder)
            optimizeCameraDimens()
            camera?.startPreview()
        } catch (e: Exception) {
        }
    }

    private fun optimizeCameraDimens() {
        val rect = surfaceHolder?.surfaceFrame
        var width = rect?.width() ?: 0
        var height = rect?.height() ?: 0
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = rect?.height() ?: 0
            height = rect?.width() ?: 0
        }
        val mSupportedPreviewSizes = camera?.parameters?.supportedPreviewSizes
        val mPreviewSize: Camera.Size?
        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height)
            val parameters = camera?.parameters
            parameters?.setPreviewSize(mPreviewSize?.width ?: 0, mPreviewSize?.height ?: 0)
            parameters?.setPictureSize(mPreviewSize?.width ?: 0, mPreviewSize?.height ?: 0)
            parameters?.flashMode = flashMode
            parameters?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE //1连续对焦
            camera?.parameters = parameters
            camera?.cancelAutoFocus()
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/11/25 14:55
     *
     *
     * 方法功能：从摄像头的大小参数里面获取与指定的大小相似的大小
     */
    private fun getOptimalPreviewSize(sizes: List<Camera.Size>, w: Int, h: Int): Camera.Size {
        val ASPECT_TOLERANCE = 0.1
        var targetRatio = h / (w * 1.0f)
        if (w > h) targetRatio = w * 1.0f / h
        var optimalSize: Camera.Size? = null
        var minDiff = Float.MAX_VALUE
        for (size in sizes) {
            val height = size.height * 1.0f
            val width = size.width * 1.0f
            var ratio = width / height
            if (height >= width) ratio = height / width
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(height - h) < minDiff) {
                optimalSize = size
                minDiff = abs(size.height - h).toFloat()
            }
        }
        if (optimalSize == null) {
            minDiff = Float.MAX_VALUE
            for (size in sizes) {
                if (abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = abs(size.height - h).toFloat()
                }
            }
        }
        return optimalSize!!
    }

    val screenHeight: Int
        get() {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            return display.height
        }
    val screenWidth: Int
        get() {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            return display.width
        }

    private fun findFrontFacingCamera(): Int {
        cameraId = -1
        // Search for the front facing camera
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i
                break
            }
        }
        return cameraId
    }

    private fun findBackFacingCamera(): Int {
        cameraId = -1
        // Search for the back facing camera
        // get the number of cameras
        val numberOfCameras = Camera.getNumberOfCameras()
        // for every camera check
        for (i in 0 until numberOfCameras) {
            val info = CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i
                break
            }
        }
        return cameraId
    }
}