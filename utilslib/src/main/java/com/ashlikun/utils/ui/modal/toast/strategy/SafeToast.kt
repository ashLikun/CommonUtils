package com.ashlikun.utils.ui.modal.toast.strategy

import android.annotation.TargetApi
import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Message
import android.view.WindowManager.BadTokenException
import android.widget.Toast

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 20:39
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍：Toast 显示安全处理
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class SafeToast(application: Application) : NotificationToast(application) {
    /**
     * 是否已经 Hook 了一次 TN 内部类
     */
    private var mHookTN = false
    override fun show() {
        hookToastTN()
        super.show()
    }

    private fun hookToastTN() {
        if (mHookTN) {
            return
        }
        mHookTN = true
        try {
            // 获取 Toast.mTN 字段对象
            val mTNField = Toast::class.java.getDeclaredField("mTN")
            mTNField.isAccessible = true
            val mTN = mTNField[this]

            // 获取 mTN 中的 mHandler 字段对象
            val mHandlerField = mTNField.type.getDeclaredField("mHandler")
            mHandlerField.isAccessible = true
            val mHandler = mHandlerField[mTN] as Handler

            // 如果这个对象已经被反射替换过了
            if (mHandler is SafeHandler) {
                return
            }

            // 偷梁换柱
            mHandlerField[mTN] = SafeHandler(mHandler)
        } catch (e: IllegalAccessException) {
            // Android 9.0 上反射会出现报错
            // Accessing hidden field Landroid/widget/Toast;->mTN:Landroid/widget/Toast$TN;
            // java.lang.NoSuchFieldException: No field mTN in class Landroid/widget/Toast;
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }

    /**
     *
     * Toast 显示安全处理
     */
    internal class SafeHandler(private val mHandler: Handler) : Handler(mHandler.looper) {
        override fun handleMessage(msg: Message) {
            // 捕获这个异常，避免程序崩溃
            try {
                // 目前发现在 Android 7.1 主线程被阻塞之后弹吐司会导致崩溃，可使用 Thread.sleep(5000) 进行复现
                // 查看源码得知 Google 已经在 Android 8.0 已经修复了此问题
                // 主线程阻塞之后 Toast 也会被阻塞，Toast 因为显示超时导致 Window Token 失效
                mHandler.handleMessage(msg)
            } catch (e: BadTokenException) {
                // android.view.WindowManager$BadTokenException：Unable to add window -- token android.os.BinderProxy is not valid; is your activity running?
                // java.lang.IllegalStateException：View android.widget.TextView has already been added to the window manager.
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }
}