package com.ashlikun.utils.ui.extend

/**
 * 作者　　: 李坤
 * 创建时间: 2020/9/18　9:45
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
/**
 * 替换可变数组里面的元素
 */
inline fun <T> MutableList<T>.replacesAll(action: (T) -> T?) {
    this.forEachIndexed { index, t ->
        val replace = action(t)
        if (replace != null) {
            this[index] = replace
        }
    }
}