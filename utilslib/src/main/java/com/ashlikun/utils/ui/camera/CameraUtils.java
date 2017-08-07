package com.ashlikun.utils.ui.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/5　14:06
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class CameraUtils implements SurfaceHolder.Callback {
    //是否正在预览
    private boolean isReady = false;
    private int orientation = 0;//旋转角度
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    int cameraId = 0;
    private Activity context;
    private String flashMode = Camera.Parameters.FLASH_MODE_OFF;
    public boolean cameraFront = false;//是否是前摄像头
    private boolean flash = false;//是否开启闪光灯
    CamerListener camerListener;

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/5 14:30
     * <p>
     * 方法功能：打开或关闭闪光灯
     */

    public void setFlashMode(String mode) {
        flashMode = mode;
        try {
            if (context.getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)
                    && mCamera != null) {
                //  mCamera.stopPreview();
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(flashMode);
                mCamera.setParameters(parameters);
                // mCamera.startPreview();
                flash = flashMode == Camera.Parameters.FLASH_MODE_TORCH;
                if (camerListener != null) {
                    camerListener.onFlashImg(flash);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFlashMode() {
        if (!flash) {
            flashMode = Camera.Parameters.FLASH_MODE_TORCH;

        } else {
            flashMode = Camera.Parameters.FLASH_MODE_OFF;
        }
        setFlashMode(flashMode);
    }


    /**
     * 切换摄像头
     */
    public void switchCamera() {
        int camerasNumber = Camera.getNumberOfCameras();
        if (camerasNumber > 1) {
            releaseCamera();
            chooseCamera();
        } else {
            Toast toast = Toast.makeText(context, "抱歉，您的设备只有一个摄像头", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void chooseCamera() {
        cameraFront = !cameraFront;
        if (!cameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                refreshCamera();
                if (camerListener != null) {
                    camerListener.setFlashImgVisibility(View.VISIBLE);
                }
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                if (flash) {
                    flash = false;
                    setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

                    if (camerListener != null) {
                        camerListener.onFlashImg(flash);
                        camerListener.setFlashImgVisibility(View.GONE);
                    }
                }
                refreshCamera();
            }
        }
    }

    /**
     * 初始化摄像头
     *
     * @throws IOException
     * @author lip
     * @date 2015-3-16
     */
    public void initCamera() throws IOException {
        if (mCamera != null) {
            freeCameraResource();
        }

        try {
            mCamera = Camera.open(findBackFacingCamera());
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null)
            return;
        refreshCamera();

    }


    public CameraUtils(Activity context) {
        this.context = context;
    }

    public void setCamerListener(CamerListener camerListener) {
        this.camerListener = camerListener;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        surfaceHolder.addCallback(this);
    }

    public Surface getSurface() {
        return surfaceHolder.getSurface();
    }

    public Camera getmCamera() {
        return mCamera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            initCamera();
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                optimizeCameraDimens(mCamera);
                mCamera.startPreview();
            }
            isReady = true;
        } catch (IOException e) {
            e.printStackTrace();
            isReady = false;
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isReady = false;
    }

    /**
     * 释放摄像头资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mCamera != null) {
                mCamera.setDisplayOrientation(orientation = 90);
            }
        }

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);

            optimizeCameraDimens(mCamera);

            mCamera.startPreview();
        } catch (Exception e) {
        }
    }

    private void optimizeCameraDimens(Camera mCamera) {
        Rect rect = surfaceHolder.getSurfaceFrame();
        int width = rect.width();
        int height = rect.height();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = rect.height();
            height = rect.width();
        }

        final List<Camera.Size> mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size mPreviewSize;
        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            parameters.setPictureSize(mPreviewSize.width, mPreviewSize.height);
            parameters.setFlashMode(flashMode);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
            mCamera.setParameters(parameters);
            mCamera.cancelAutoFocus();
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/11/25 14:55
     * <p>
     * 方法功能：从摄像头的大小参数里面获取与指定的大小相似的大小
     */

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        float targetRatio = h / (w * 1.0f);

        if (w > h)
            targetRatio = (w * 1.0f) / h;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        float minDiff = Float.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            float height = size.height * 1.0f;
            float width = size.width * 1.0f;

            float ratio = width / height;
            if (height >= width)
                ratio = height / width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Float.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    public int getScreenHeight() {

        WindowManager wm = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();

    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();

    }


    private int findFrontFacingCamera() {
        cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public int getCameraId() {
        return cameraId;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/11/28 10:34
     * <p>
     * 方法功能：是否准备好了
     */

    public boolean isReady() {
        return isReady;
    }

    public int getOrientation() {
        return orientation;
    }
}
