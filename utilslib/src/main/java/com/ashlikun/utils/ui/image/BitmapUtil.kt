package com.ashlikun.utils.ui.image

import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.graphics.BitmapFactory.Options
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.AppUtils.app
import com.ashlikun.utils.encryption.Base64Utils.decode
import com.ashlikun.utils.encryption.Base64Utils.encodeToStr
import com.ashlikun.utils.other.MediaFile
import com.ashlikun.utils.other.file.FileIOUtils
import com.ashlikun.utils.ui.ScreenUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt


/**
 * @author　　: 李坤
 * 创建时间: 2021.12.14 18:59
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：Bitmap操作的一些工具
 */
inline fun Bitmap?.recycle() = BitmapUtil.recycle(this)
inline fun String.base64Bitmap() = BitmapUtil.base64Bitmap(this)
inline fun Bitmap?.bitmap2Base64() = BitmapUtil.bitmap2Base64(this)
inline fun Bitmap?.scaleImageTo(newWidth: Int, newHeight: Int) = BitmapUtil.scaleImageTo(this, newWidth, newHeight)
inline fun Bitmap?.scaleImage(scaleWidth: Float, scaleHeight: Float) = BitmapUtil.scaleImage(this, scaleWidth, scaleHeight)
inline fun Bitmap?.toCircle() = BitmapUtil.toCircle(this)
inline fun Bitmap?.saveBitmap(file: File, quality: Int = 100) = BitmapUtil.saveBitmap(this, file, quality)
inline fun File.updatePhotoMedia() = BitmapUtil.updatePhotoMedia(this)
inline fun Bitmap.saveImageToGallery(file: File, value: ContentValues = BitmapUtil.getImageContentValues(file), quality: Int = 100) =
    BitmapUtil.saveImageToGallery(this, file, value, quality)

object BitmapUtil {
    /**
     * 获取视频文件的一帧
     */
    fun getVideoFrame(path: String, wDp: Int, hDp: Int): Bitmap? {
        var wDp = wDp
        var hDp = hDp
        val scale = AppUtils.appResources.displayMetrics.density
        wDp = (wDp * scale + 0.5f).toInt()
        hDp = (hDp * scale + 0.5f).toInt()
        val media = MediaMetadataRetriever()
        media.setDataSource(path)
        return scaleImage(media.frameAtTime, wDp.toFloat(), hDp.toFloat())
    }

    /**
     * bitmap转换成byte数组
     */
    fun bitmapToByte(b: Bitmap?, quality: Int = 100): ByteArray {
        if (b == null) return ByteArray(0)
        val o = ByteArrayOutputStream()
        b?.compress(Bitmap.CompressFormat.JPEG, quality, o)
        return o.toByteArray()
    }

    /**
     * 裁剪图片
     */
    fun cropBitmap(bitmap: Bitmap?, width: Int = ScreenUtils.width(), height: Int = ScreenUtils.height(), x: Int = 0, y: Int = 0): Bitmap? {
        if (bitmap == null) return null
        // 得到图片的宽，高
        val w = bitmap.width
        //按比例计算
        val newHeight = (height * (w / (width * 1f))).toInt()
        return Bitmap.createBitmap(bitmap, x, y, w, newHeight)
    }

    /**
     * byte数组转换成bitmap
     */
    fun byteToBitmap(b: ByteArray?): Bitmap? {
        return if (b?.isEmpty() != false) null else BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    /**
     * 把bitmap转换成Base64编码String
     */
    fun bitmap2Base64(bitmap: Bitmap?, quality: Int = 100): String {
        return encodeToStr(bitmapToByte(bitmap, quality))
    }

    /**
     * 把bitmap  base64 解码
     */
    fun base64Bitmap(data: String): Bitmap? {
        return byteToBitmap(decode(data))
    }

    /**
     * 按照指定宽高缩放
     */
    fun scaleImageTo(org: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        if (org == null) return null
        return scaleImage(org, newWidth.toFloat() / org.width, newHeight.toFloat() / org.height)
    }

    /**
     * 按照指定宽高系数缩放bitmap
     */
    fun scaleImage(org: Bitmap?, scaleWidth: Float, scaleHeight: Float): Bitmap? {
        if (org == null) return null
        return Bitmap.createBitmap(org, 0, 0, org.width, org.height, Matrix().apply { postScale(scaleWidth, scaleHeight) }, true)
    }

    /**
     * 获取圆形图片
     */
    fun toCircle(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null
        val height = bitmap.height
        val width = bitmap.height
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.TRANSPARENT
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    /**
     * 保存图片
     */
    fun saveBitmap(bitmap: Bitmap?, file: File, quality: Int = 100) =
        FileIOUtils.writeImage(bitmap, file, quality)

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    fun rotateBitmapByDegree(bm: Bitmap?, degree: Int): Bitmap? {
        if (bm == null) return null
        if (degree % 360 == 0) return bm
        // 根据旋转角度，生成旋转矩阵
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        val new = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, Matrix().apply { setRotate(degree.toFloat()) }, true)
        recycle(bm)
        return new
    }

    /**
     * drawable转换成bitmap
     * 如果不是BitmapDrawable 那么要注意宽高
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) drawable.bitmap
        else {
            //获得drawable的基本信息
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            //建立对应的画布
            val canvas = Canvas(bitmap)
            //设置大小
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            //把drawable内容画到画布中
            drawable.draw(canvas)
            bitmap
        }
    }

    /**
     * 从资源中获取Bitmap   按照指定的宽高缩放
     *
     * @param width  希望的宽度
     * @param height 希望的高度
     */
    fun decodeResource(resourseId: Int, width: Int, height: Int): Bitmap? {
        // 获取资源图片
        var opts: Options? = null
        if (width > 0 && height > 0) {
            opts = Options()
            opts.inJustDecodeBounds = true
            BitmapFactory.decodeResource(AppUtils.appResources, resourseId, opts)
            // 计算图片缩放比例
            opts.inSampleSize = computeSampleSize(opts, width, height)
            opts.inJustDecodeBounds = false
            opts.inInputShareable = true
            opts.inPurgeable = true
        }
        try {
            // decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，无�?再使用java层的createBitmap，从而节省了java层的空间
            return BitmapFactory.decodeResource(AppUtils.appResources, resourseId, opts)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 按照路径加载图片,按照指定的宽高缩放
     *
     * @param width  希望的宽度
     * @param height 希望的高度
     */
    fun decodeFile(dst: File, width: Int, height: Int): Bitmap? {
        if (dst.exists()) {
            var opts: Options? = null
            if (width > 0 && height > 0) {
                opts = Options()
                opts.inJustDecodeBounds = true
                BitmapFactory.decodeFile(dst.path, opts)
                opts.inSampleSize = computeSampleSize(opts, width, height)
                opts.inJustDecodeBounds = false
                opts.inInputShareable = true
                opts.inPurgeable = true
            }
            try {
                return BitmapFactory.decodeFile(dst.path, opts)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 加载源数据图片
     *
     * @param width  希望的宽度
     * @param height 希望的高度
     */
    fun decodeByte(dst: ByteArray, width: Int, height: Int): Bitmap? {
        if (dst.isNotEmpty()) {
            var opts: Options? = null
            if (width > 0 && height > 0) {
                opts = Options()
                opts.inJustDecodeBounds = true
                BitmapFactory.decodeByteArray(dst, 0, dst.size, opts)
                // 计算图片缩放比例
                opts.inSampleSize = computeSampleSize(opts, width, height)
                opts.inJustDecodeBounds = false
                opts.inInputShareable = true
                opts.inPurgeable = true
            }
            try {
                return BitmapFactory.decodeByteArray(dst, 0, dst.size, opts)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 计算inSampleSize
     *
     * @param reqWidth 希望的宽度
     * @param reqWidth 希望的高度
     */
    fun computeSampleSize(options: Options, reqWidth: Int, reqHeight: Int): Int {
        val initialSize = computeInitialSampleSize(options, reqWidth, reqHeight)
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else roundedSize = (initialSize + 7) / 8 * 8
        return roundedSize
    }

    /**
     * 计算初始的SampleSize
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private fun computeInitialSampleSize(options: Options, reqWidth: Int, reqHeight: Int): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val maxNumOfPixels = reqWidth * reqHeight
        val minLength = Math.min(reqWidth, reqHeight)
        val lowerBound = if (maxNumOfPixels == -1) 1 else ceil(sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minLength == -1) 128 else floor(w / minLength).coerceAtMost(floor(h / minLength)).toInt()
        if (upperBound < lowerBound) return lowerBound
        return if (maxNumOfPixels == -1 && minLength == -1) 1 else if (minLength == -1) lowerBound else upperBound
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
    fun createBitmapSafely(
        width: Int,
        height: Int,
        config: Bitmap.Config,
        retryCount: Int
    ): Bitmap? {
        return try {
            Bitmap.createBitmap(width, height, config)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            if (retryCount > 0) {
                System.gc()
                return createBitmapSafely(width, height, config, retryCount - 1)
            }
            null
        }
    }

    /**
     * 截取viewGroup内容，生成图片
     *
     * @param view  传入一个 View，会获取这个 View 的内容创建 Bitmap。
     * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
     * @return 图片bitmap
     */
    fun getViewBitmap(view: View, scale: Float = 1f): Bitmap {
        if (view.measuredWidth == 0 || view.measuredHeight == 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(ScreenUtils.width, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(ScreenUtils.height * 100, View.MeasureSpec.AT_MOST))
        }
        if (view.width == 0 || view.height == 0) {
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        }
        // 创建相应大小的bitmap
        val bitmap = createBitmapSafely((view.width * scale).toInt(), (view.height * scale).toInt(), Bitmap.Config.ARGB_8888, 1)
        val canvas = Canvas(bitmap!!)
        canvas.save()
        canvas.scale(scale, scale)
        //绘制viewGroup内容
        view.draw(canvas)
        canvas.restore()
        canvas.setBitmap(null)
        return bitmap
    }

    /**
     * 保存文件到相册
     * @param file 这里为了兼容android11 的分区存储 一定得用/storage/emulated/0/Android/data/包名/xxx
     * @return true 成功，false:失败
     */
    fun saveImageToGallery(bmp: Bitmap?, file: File, value: ContentValues = getImageContentValues(file), quality: Int = 100): Uri? {
        return if (saveBitmap(bmp, file, quality)) updatePhotoMedia(file, value) else null
    }

    /**
     * 刷新相册
     * 建议使用MediaStore ，这里自动刷新
     * @return true 成功，false:失败
     */
    fun updatePhotoMediaOld(file: File) = updatePhotoMediaOld(Uri.fromFile(file))
    fun updatePhotoMediaOld(uri: Uri?): Boolean {
        //保存图片后发送广播通知更新数据库
        if (uri != null) {
            runCatching {
                app.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                return true
            }
        }
        return false
    }

    /**
     * 刷新相册 MediaStore
     * @return true 成功，false:失败
     */
    fun updatePhotoMedia(file: File, value: ContentValues = getImageContentValues(file)): Uri? {
        //保存图片后发送广播通知更新数据库
        if (file.exists()) {
            runCatching {
                val uri = app.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
                if (uri != null)
                    copyFileAfterQ(file, uri)
                //防止不更新
                if (updatePhotoMediaOld(uri)) uri else null
            }.onFailure {
                //防止不更新
                val uri = Uri.fromFile(file)
                return if (updatePhotoMediaOld(uri)) uri else null
            }
        }
        return null
    }

    /**
     * 拷贝文件到相册的uri,android11及以上得这么干，否则不会显示。可以参考ScreenMediaRecorder的save方法
     */
    fun copyFileAfterQ(file: File, uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && app.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q) {
            //拷贝文件到相册的uri,android11及以上得这么干，否则不会显示。可以参考ScreenMediaRecorder的save方法
            runCatching {
                val os = app.contentResolver.openOutputStream(uri, "w")
//                FileIOUtils.copyFile(file, os)
                Files.copy(file.toPath(), os)
                os?.close()
                file.deleteOnExit()
            }
        }
    }

    /**
     * 获取 图片 ContentValue
     */
    fun getImageContentValues(file: File,
        mimeType: String = MediaFile.getFileType(file.absolutePath)?.mimeType ?: "image/jpeg",
        desc: String = "App Save Image",
        currentTime: Long = System.currentTimeMillis()) =
        ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, file.name)
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.DATE_ADDED, currentTime)
            put(MediaStore.Images.Media.DATE_MODIFIED, currentTime)
            put(MediaStore.Images.Media.DATE_TAKEN, currentTime)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.DESCRIPTION, desc)
            put(MediaStore.Images.Media.ORIENTATION, 0)
            put(MediaStore.Images.Media.DATA, file.absolutePath)
            put(MediaStore.Images.Media.SIZE, file.length())
        }


    /**
     * 添加水印
     *
     * @param src    原图片
     * @param logo   logo
     * @param matrix logo对应的变换矩阵（大小位置）,默认移动到底部右边的位置
     * @return 合成水印图片
     */
    fun getWaterMaskImage(src: Bitmap?, logo: Bitmap?, matrix: Matrix = Matrix()): Bitmap? {
        if (src == null || logo == null) {
            return null
        }
        //原图宽高
        val w = src.width
        val h = src.height
        //logo宽高
        val ww = logo.width
        val wh = logo.height
        //创建一个和原图宽高一样的bitmap
        val newBitmap = Bitmap.createBitmap(w, h, src.config)
        //创建Canvas
        val canvas = Canvas(newBitmap)
        //绘制原始图片
        canvas.drawBitmap(src, 0f, 0f, null)
        //对矩阵作位置偏移，移动到底部右边的位置
        matrix.postTranslate((w - ww).toFloat(), (h - wh).toFloat())
        //将logo绘制到画布上并做矩阵变换
        canvas.drawBitmap(logo, matrix, null)
        // 保存状态
        canvas.save()
        // 恢复状态
        canvas.restore()
        recycle(src)
        recycle(logo)
        return newBitmap
    }

    fun recycle(b: Bitmap?) {
        if (b?.isRecycled == false) {
            b?.recycle()
        }
    }
}