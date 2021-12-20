package com.ashlikun.utils.animator

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/11 15:00
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：RecycleView的item动画,清楚透明度
 */

class ItemAnimatorClearAlpha : DefaultItemAnimator() {
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        super.animateAdd(holder)
        holder.itemView.alpha = 1f
        return true
    }
}