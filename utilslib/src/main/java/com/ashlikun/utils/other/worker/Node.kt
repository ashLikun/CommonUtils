package com.ashlikun.utils.other.worker


typealias CallBack = () -> Unit

/**
 * 工作者
 */
typealias Worker = (workNode: WorkNode) -> Unit

/**
 * 作者　　: 李坤
 * 创建时间: 2019/11/26　14:32
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：事件的节点
 */
interface Node {
    /**
     * 任务完成时触发
     */
    fun onOk()

    /**
     * 任务执行是触发
     */
    fun onWork(callBack: CallBack)
}