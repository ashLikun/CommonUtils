package com.ashlikun.utils.bug;

import com.ashlikun.utils.other.ThreadPoolManage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者　　: 李坤
 * 创建时间: 2019/9/26　10:49
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：一些bug的处理
 */
public class BugUtils {
    public static void init() {
        ThreadPoolManage.get().execute(new Runnable() {
            @Override
            public void run() {
                finalizeTimedOut();
            }
        });
    }

    /**
     * 部分OPPO机型 AssetManager.finalize() timed out的修复
     * https://www.jianshu.com/p/89e2719be9c7
     * 仔细核查后发现多发在OPPO系列的机型中，包括R9 A33 A59等等。猜测是OPPO的定制ROM在底层做了什么修改。为了减少这样的报错，只有对OPPO进行特殊处理。
     * 查找了一些资料后发现这类错误是由于回收对象时间过长，由FinalizerWatchdogDaemon负责计时，超时后抛出异常关闭VM的。
     * 那么有两种解决办法：1关掉这个负责计时的，2延长计时时间
     */
    public static void finalizeTimedOut() {

        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
