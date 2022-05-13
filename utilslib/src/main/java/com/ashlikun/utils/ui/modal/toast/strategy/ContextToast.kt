package com.ashlikun.utils.ui.modal.toast.strategy

import com.ashlikun.utils.AppUtils
import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.ui.modal.toast.ToastImpl
import com.ashlikun.utils.ui.modal.toast.config.OnCallback

/**
 * @author　　: 李坤
 * 创建时间: 2022/3/26 2:40
 * 邮箱　　：496546144@qq.com
 * 参考https://github.com/getActivity/ToastUtils
 * 功能介绍： 利用 Activity(WindowManager) 或者 Application 弹出 Toast
 */

open class ContextToast : CustomToast() {

    override var callback: OnCallback? = null
        set(value) {
            if (field != value) {
                field = value
                mToastImpl.callback = value
            }
        }

    /** Toast 实现类  */
    private val mToastImpl by lazy {
        ToastImpl(this)
    }

    override fun show() {
        MainHandle.get().removeCallbacks(mShowRunnable)
        // 延迟一段时间之后再执行，因为在没有通知栏权限的情况下，Toast 只能显示当前 Activity
        // 如果当前 Activity 在 ToastUtils.show 之后进行 finish 了，那么这个时候 Toast 可能会显示不出来
        // 因为 Toast 会显示在销毁 Activity 界面上，而不会显示在新跳转的 Activity 上面
        MainHandle.get().postDelayed(runnable = mShowRunnable, delayMillis = DELAY_TIMEOUT)
    }

    override fun cancel() {
        MainHandle.get().removeCallbacks(mCancelRunnable)
        MainHandle.get().post(runnable = mCancelRunnable)
    }

    /**
     * 显示任务
     */
    protected val mShowRunnable = Runnable {
        mCancelRunnable.run()
        // 替换成 WindowManager 来显示
        mToastImpl.show()
    }

    /**
     * 取消任务
     */
    protected val mCancelRunnable = Runnable {
        // 取消 WindowManager 的显示
        mToastImpl.cancel()
    }
}