package com.ashlikun.utils.other;

import android.os.CountDownTimer;
import android.widget.TextView;


/**
 * 作者　　: 李坤
 * 创建时间: 2016/10/12 10:46
 * <p>
 * 方法功能： 倒计时工具
 */

public class CountdownUtils {
    /**
     * 验证码按钮倒计时 总时长和间隔时长，默认为60秒
     */
    private long totalTime;
    private long intervalTime;
    private String msg;
    CountDownTimer countDownTimer;

    public CountdownUtils(Bulider bulider) {
        this.totalTime = bulider.totalTime;
        this.intervalTime = bulider.intervalTime;
        this.msg = bulider.msg;
    }

    public static class Bulider {
        private long totalTime = 180000;
        private long intervalTime = 1000;
        private String msg;


        public Bulider setTotalTime(long totalTime) {
            this.totalTime = totalTime;
            return this;
        }

        public Bulider setIntervalTime(long intervalTime) {
            this.intervalTime = intervalTime;
            return this;
        }

        public Bulider setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public CountdownUtils bulid() {
            return new CountdownUtils(this);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:36
     * <p>
     * 方法功能：开始倒计时
     */
    public void start(final TextView hint) {

        /**
         * 记录当下剩余时间
         */
        countDownTimer = new CountDownTimer(totalTime, intervalTime) {
            @Override
            public void onFinish() {// 计时完毕时触发
                hint.setText(msg);
                hint.setEnabled(true);
            }

            @Override
            public void onTick(long currentTime) {// 计时过程显示
                hint.setEnabled(false);
                hint.setText(currentTime / 1000 + "秒");
            }
        };
        countDownTimer.start();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 10:36
     * <p>
     * 方法功能：取消倒计时
     */
    public void cancel() {
        countDownTimer.cancel();
    }
}
