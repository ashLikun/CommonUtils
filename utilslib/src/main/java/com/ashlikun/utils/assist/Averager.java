package com.ashlikun.utils.assist;

import com.ashlikun.utils.other.LogUtils;

import java.util.ArrayList;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/8/6 0006 14:22
 * <p>
 * 方法功能：用以统计平均数
 */
public class Averager {
    private ArrayList<Number> numList = new ArrayList<Number>();

    /**
     * 添加一个数字
     */
    public synchronized void add(Number num) {
        numList.add(num);
    }

    /**
     * 清除全部
     */
    public void clear() {
        numList.clear();
    }

    /**
     * 返回参与均值计算的数字个数
     */
    public Number size() {
        return numList.size();
    }

    /**
     * 获取平均数
     */
    public Number getAverage() {
        if (numList.size() == 0) {
            return 0;
        } else {
            Float sum = 0f;
            for (int i = 0, size = numList.size(); i < size; i++) {
                sum = sum.floatValue() + numList.get(i).floatValue();
            }
            return sum / numList.size();
        }
    }

    /**
     * 打印数字列
     */
    public String print() {
        String str = "PrintList(" + size() + "): " + numList;
        LogUtils.i(str);
        return str;
    }

}
