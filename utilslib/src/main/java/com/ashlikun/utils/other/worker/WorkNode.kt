package com.ashlikun.utils.other.worker

import com.ashlikun.utils.other.MainHandle
import com.ashlikun.utils.other.ThreadUtils


/**
 * 工作者
 */
/**
 * 作者　　: 李坤
 * 创建时间: 2019/11/26　14:33
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：节点实现
 * @param worker 节点工作者,只会在主线程调用
 */
class WorkNode(var worker: Worker) : Node {
    var callBack: CallBack? = null

    /**
     * 任务完成时触发
     */
    override fun onOk() {
        callBack?.invoke()
    }

    override fun onWork(callBack: CallBack) {
        this.callBack = callBack
        //限制线程
        if (ThreadUtils.isMainThread()) {
            worker.invoke(this)
        } else {
            MainHandle.post { worker.invoke(this) }
        }
    }

    companion object {
        @JvmStatic
        fun build(worker: Worker): WorkNode {
            return WorkNode(worker)
        }
    }
}