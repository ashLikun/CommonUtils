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
    //结束时候文字的格式化样式
    private String endTextFomat;
    CountDownTimer countDownTimer;

    public CountdownUtils(Bulider bulider) {
        this.totalTime = bulider.totalTime;
        this.intervalTime = bulider.intervalTime;
        this.msg = bulider.msg;
        this.endTextFomat = bulider.endTextFomat;
    }

    public static class Bulider {
        private long totalTime = 180000;
        private long intervalTime = 1000;
        private String msg;
        private String endTextFomat;


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

        public Bulider endTextFomat(String endTextFomat) {
            this.endTextFomat = endTextFomat;
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
                hint.setText(endTextFomat != null ?
                        String.format(endTextFomat, currentTime / 1000) : currentTime / 1000 + "秒");
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
