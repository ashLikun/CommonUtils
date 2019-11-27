package com.ashlikun.utils.other.worker

import android.util.SparseArray


/**
 * 作者　　: 李坤
 * 创建时间: 2019/11/26　14:42
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：节点流
 *
 * 首先作为所有节点的管理者,当然要把它们存下来,用什么数据结构来存呢?
 * 回顾一下我的需求: 可以灵活控制节点的执行顺序,
 * so…经过反复筛选,我最终选择了SparseArray来存放我们所有的节点,因为我们为每个节点提供id作为key:
 *
 * 一来可以提高代码可读性。
 *
 * 二来,SparseArray内部是数组实现的,而且是按照key的大小升序排列的,基于这个特性,
 * 我们只需要改变定义Key值的大小关系就可以改变它们在数组中的顺序。
 */
class WorkFlow private constructor(var flowNodes: SparseArray<Node>) {
    /**
     * 开始工作，默认从第一个节点
     */
    fun start() {
        startWithNode(flowNodes.keyAt(0))
    }

    /**
     * 基于某个节点Id 开始工作
     *
     * @param startNodeId 节点id
     */
    fun startWithNode(startNodeId: Int) {
        val startIndex = flowNodes.indexOfKey(startNodeId)
        val startNode = flowNodes.valueAt(startIndex)
        startNode.onWork {
            findAndExecuteNextNodeIfExist(startIndex)
        }
    }

    /**
     * 执行下一个任务
     */
    private fun findAndExecuteNextNodeIfExist(startIndex: Int) {
        val nextIndex = startIndex + 1
        val nextNode = flowNodes.valueAt(nextIndex)
        nextNode?.onWork {
            findAndExecuteNextNodeIfExist(nextIndex)
        }
    }

    class Builder {
        var flowNodes: SparseArray<Node> = SparseArray()

        fun addWork(sort: Int = if (flowNodes.size() == 0) 0 else {
            flowNodes.keyAt(flowNodes.size() - 1) + 1
        }, node: Node): Builder {
            flowNodes.put(sort, node)
            return this
        }

        fun addWork(sort: Int = if (flowNodes.size() == 0) 0 else {
            flowNodes.keyAt(flowNodes.size() - 1) + 1
        }, work: Worker): Builder {
            flowNodes.put(sort, WorkNode(work))
            return this
        }

        fun build(): WorkFlow {
            return WorkFlow(flowNodes)
        }
    }
}