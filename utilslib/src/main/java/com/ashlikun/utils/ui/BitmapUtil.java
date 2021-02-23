package com.ashlikun.utils.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.View;

import com.ashlikun.utils.AppUtils;
import com.ashlikun.utils.encryption.Base64Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/22 15:15
 * 邮箱　　：496546144@qq.com
 * <p>
 * 方法功能：Bitmap操作的一些工具
 */

public class BitmapUtil {

    /**
     * 获取视频文件的一帧
     */
    public static Bitmap getVideoFrame(String path, int wDp, int hDp) {
        final float scale = AppUtils.getApp().getResources().getDisplayMetrics().density;
        wDp = (int) (wDp * scale + 0.5f);
        hDp = (int) (hDp * scale + 0.5f);
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return BitmapUtil.scaleImage(media.getFrameAtTime(), wDp, hDp);
    }

    /**
     * bitmap转换成byte数组
     */
    public static byte[] bitmapToByte(Bitmap b, int quality) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, quality, o);
        return o.toByteArray();
    }

    /**
     * 裁剪图片
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int height) {
        return cropBitmap(bitmap, 0, 0, ScreenInfoUtils.width(), height);
    }

    /**
     * 裁剪图片
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int x, int y, int realWidth, int height) {
        // 得到图片的宽，高
        int w = bitmap.getWidth();
        //按比例计算
        int newHeight = (int) (height * (w / (realWidth * 1f)));
        return Bitmap.createBitmap(bitmap, x, y, w, newHeight);
    }

    /**
     * byte数组转换成bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * 把bitmap转换成Base64编码String
     */
    public static String base64Bitmap(Bitmap bitmap, int quality) {
        return Base64Utils.encodeToStr(bitmapToByte(bitmap, quality));
    }

    /**
     * 把bitmap  base64 解码
     */
    public static Bitmap base64Bitmap(String data) {
        return byteToBitmap(Base64Utils.decode(data));
    }

    /**
     * 按照指定宽高缩放
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * 按照指定宽高系数缩放bitmap
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

    /**
     * 获取圆形图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 保存图片
     */
    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        if (degree % 360 == 0) {
            return bm;
        }
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
            BitmapUtil.recycle(bm);
        } catch (OutOfMemoryError e) {
        }
        return returnBm;
    }


    /**
     * drawable转换成bitmap
     * 如果不是BitmapDrawable 那么要注意宽高
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            //获得drawable的基本信息
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            Config.ARGB_8888);
            //建立对应的画布
            Canvas canvas = new Canvas(bitmap);
            //设置大小
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            //把drawable内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        }
    }


    /**
     * 从资源中获取Bitmap   按照指定的宽高缩放
     *
     * @param width  希望的宽度
     * @param height 希望的高度
     */
    public static Bitmap decodeResource(Context context, int resourseId,
                                        int width, int height) {
        // 获取资源图片
        BitmapFactory.Options opts = null;
        if (width > 0 && height > 0) {
            opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resourseId, opts);
            // 计算图片缩放比例
            opts.inSampleSize = computeSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
        }
        try {
            // decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，无�?再使用java层的createBitmap，从而节省了java层的空间
            return BitmapFactory.decodeResource(context.getResources(), resourseId, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照路径加载图片,按照指定的宽高缩放
     *
     * @param width  希望的宽度
     * @param height 希望的高度
     */
    public static Bitmap decodeFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                opts.inSampleSize = computeSampleSize(opts,
                        width, height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 加载源数据图片
     *
     * @param width  希望的宽度
     * @param height 希望的高度
     */
    public static Bitmap decodeByte(byte[] dst, int width, int height) {
        if (null != dst && dst.length > 0) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(dst, 0, dst.length, opts);
                // 计算图片缩放比例
                opts.inSampleSize = computeSampleSize(opts,
                        width, height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeByteArray(dst, 0, dst.length, opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 计算inSampleSize
     *
     * @param reqWidth 希望的宽度
     * @param reqWidth 希望的高度
     */
    public static int computeSampleSize(BitmapFactory.Options options,
                                        int reqWidth, int reqHeight) {
        int initialSize = computeInitialSampleSize(options, reqWidth,
                reqHeight);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /**
     * 计算初始的SampleSize
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int reqWidth, int reqHeight) {
        double w = options.outWidth;
        double h = options.outHeight;
        int maxNumOfPixels = reqWidth * reqHeight;
        int minLength = Math.min(reqWidth, reqHeight);

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minLength), Math.floor(h / minLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minLength == -1)) {
            return 1;
        } else if (minLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 安全的创建bitmap。
     * 如果新建 Bitmap 时产生了 OOM，可以主动进行一次 GC - System.gc()，然后再次尝试创建。
     *
     * @param width      Bitmap 宽度。
     * @param height     Bitmap 高度。
     * @param config     传入一个 Bitmap.Config。
     * @param retryCount 创建 Bitmap 时产生 OOM 后，主动重试的次数。
     * @return 返回创建的 Bitmap。
     */
    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
            return null;
        }
    }

    public static Bitmap getViewBitmap(View view) {
        return getViewBitmap(view, 1f);
    }

    /**
     * 截取viewGroup内容，生成图片
     *
     * @param view  传入一个 View，会获取这个 View 的内容创建 Bitmap。
     * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
     * @return 图片bitmap
     */
    public static Bitmap getViewBitmap(View view, float scale) {
        if (view == null) {
            return null;
        }
        if (view.getMeasuredWidth() == 0 || view.getMeasuredHeight() == 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(ScreenInfoUtils.getWidth(), View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(ScreenInfoUtils.getWidth() * 10, View.MeasureSpec.AT_MOST));
        }
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        // 创建相应大小的bitmap
        Bitmap bitmap = createBitmapSafely((int) (view.getWidth() * scale),
                (int) (view.getHeight() * scale), Bitmap.Config.ARGB_8888, 1);
        final Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.scale(scale, scale);
        //绘制viewGroup内容
        view.draw(canvas);
        canvas.restore();
        canvas.setBitmap(null);
        return bitmap;
    }

    /**
     * 保存文件到相册
     *
     * @return true 成功，false:失败
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp, File file) {
        if (saveBitmap(bmp, file)) {
            return updatePhotoMedia(context, file);
        }
        return false;
    }

    /**
     * 刷新相册
     *
     * @return true 成功，false:失败
     */
    public static boolean updatePhotoMedia(Context context, File file) {
        //保存图片后发送广播通知更新数据库
        if (file.exists()) {
            try {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                return true;
            } catch (Exception e) {

            }
        }
        return false;
    }

    /**
     * 添加水印
     *
     * @param src    原图片
     * @param logo   logo
     * @param matrix logo对应的变换矩阵（大小位置）,默认移动到底部右边的位置
     * @return 合成水印图片
     */
    public static Bitmap getWaterMaskImage(Bitmap src, Bitmap logo, Matrix matrix) {
        if (src == null || logo == null) {
            return null;
        }
        //原图宽高
        int w = src.getWidth();
        int h = src.getHeight();
        //logo宽高
        int ww = logo.getWidth();
        int wh = logo.getHeight();
        //创建一个和原图宽高一样的bitmap
        Bitmap newBitmap = Bitmap.createBitmap(w, h, src.getConfig());
        //创建Canvas
        Canvas canvas = new Canvas(newBitmap);
        //绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);

        if (matrix == null) {
            //新建矩阵
            matrix = new Matrix();
            //对矩阵作位置偏移，移动到底部右边的位置
            matrix.postTranslate(w - ww, h - wh);
        }
        //将logo绘制到画布上并做矩阵变换
        canvas.drawBitmap(logo, matrix, null);
        // 保存状态
        canvas.save();
        // 恢复状态
        canvas.restore();
        recycle(src);
        recycle(logo);
        return newBitmap;
    }

    /**
     * 回收不用的bitmap
     *
     * @param b
     */
    public static void recycle(Bitmap b) {
        if (b != null && !b.isRecycled()) {
            b.recycle();
            b = null;
        }
    }


}
