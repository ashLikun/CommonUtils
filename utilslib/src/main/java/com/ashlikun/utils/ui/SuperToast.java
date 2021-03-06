package com.ashlikun.utils.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashlikun.utils.R;
import com.ashlikun.utils.other.DimensUtils;
import com.ashlikun.utils.other.MainHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ashlikun.utils.AppUtils.getApp;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/9/18　10:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：自定义toast样式
 */

public class SuperToast {
    private static Toast mToast = null;
    private static final String TOAST_VIEW_TAG = "TOAST_VIEW_TAG";
    private static final int Info = 1;//正常
    private static final int Confirm = 2;//完成
    private static final int Warning = 3;//警告 orange
    private static final int Error = 4;//错误 red
    private static final int NO_RES = -1;//
    private static final float COLOR_DEPTH = 0.92f;//
    private static final int INIT_GRAVITY = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;//


    public static int CHANG_GRAVITY = INIT_GRAVITY;//可以改变
    public static int INIT_OFFSET_Y = 0;

    @IntDef(value = {Info, Confirm, Warning, Error})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    AnimatorSet animSet;

    public static void setGravity(int gravity) {
        CHANG_GRAVITY = gravity;
    }

    public static void setGravityToInit() {
        CHANG_GRAVITY = INIT_GRAVITY;
    }

    private SuperToast(final Builder builder) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MainHandle.get().post(new Runnable() {
                @Override
                public void run() {
                    cretae(builder);
                }
            });
        } else {
            cretae(builder);
        }
    }

    private static void initToast() {
        if (mToast == null) {
            mToast = Toast.makeText(getApp(), "", Toast.LENGTH_SHORT);
        }
    }

    //要在主线程
    private void cretae(final Builder builder) {
        initToast();
        View mView;
        if (builder.layoutId == R.layout.toast_super &&
                mToast.getView() != null && TOAST_VIEW_TAG.equals(mToast.getView().getTag())) {
            mView = mToast.getView();
        } else {
            mView = LayoutInflater.from(getApp()).inflate(builder.layoutId, null);
            mView.setTag(TOAST_VIEW_TAG);
        }
        setViewContent(mView, builder);
        startAnim(builder, mView);
        mToast.setGravity(builder.gravity, builder.offsetX, builder.offsetY);
        mToast.setView(mView);
        mToast.setDuration(builder.duration);
        mToast.show();
        if (builder.isFinish) {
            final DialogTransparency dialog = new DialogTransparency(builder.activity);
            dialog.show();
            MainHandle.get().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (builder.callback != null) {
                        builder.callback.onDismissed();
                    }
                    if (builder.activity != null && !builder.activity.isFinishing()) {
                        builder.activity.finish();
                    }
                }
            }, mToast.getDuration() == Toast.LENGTH_SHORT ? 2000 : 3500);
        }
    }

    private void startAnim(final Builder builder, View mView) {
        mView.clearAnimation();

        if (animSet == null) {
            animSet = new AnimatorSet();
        } else {
            animSet.cancel();
        }
        if (builder.animator == null) {
            builder.animator = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mView, "alpha", 0, 1);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mView, "scaleX", 0, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mView, "scaleY", 0, 1);
            ((AnimatorSet) builder.animator).playTogether(scaleX, scaleY, alpha);
            ((AnimatorSet) builder.animator).setDuration(300);
        }
        builder.animator.start();

    }

    private void setViewContent(View view, Builder builder) {
        if (!builder.isCustom) {
            if (builder.backGroundDrawable != null) {
                ViewCompat.setBackground(view, builder.backGroundDrawable);
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(builder.backgroundColor);
                drawable.setStroke(DimensUtils.dip2px(getApp(), 1f), getBackgroundShen(builder.backgroundColor));
                drawable.setCornerRadius(DimensUtils.dip2px(getApp(), 4));
                ViewCompat.setBackground(view, drawable);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.img);
            if (!builder.isShowIcon) {
                imageView.setVisibility(View.GONE);
            } else {
                Drawable draw = ContextCompat.getDrawable(view.getContext(), builder.iconRes);
                //自动设置文字颜色
                if ("auto_tintColor".equals(imageView.getTag())) {
                    draw = DrawableCompat.wrap(draw);
                    DrawableCompat.setTint(draw, StatusBarCompat.isColorDrak(builder.backgroundColor) ? 0xffffffff :
                            0xff000000);
                }
                imageView.setImageDrawable(draw);

            }
            TextView textView = (TextView) view.findViewById(R.id.msg);
            //自动设置文字颜色
            if ("auto_textColor".equals(textView.getTag())) {
                textView.setTextColor(StatusBarCompat.isColorDrak(builder.backgroundColor) ? 0xffffffff :
                        0xff000000);
            }
            textView.setText(builder.msg);
        } else {
            ((TextView) view.findViewById(R.id.msg)).setText(builder.msg);
        }
    }


    private int getBackgroundShen(int backgroundColor) {
        int red = (int) (Color.red(backgroundColor) * COLOR_DEPTH);
        int green = (int) (Color.green(backgroundColor) * COLOR_DEPTH);
        int blue = (int) (Color.blue(backgroundColor) * COLOR_DEPTH);
        return Color.argb(Color.alpha(backgroundColor), red, green, blue);
    }

    public static Builder get(String msg) {
        return new Builder(msg);
    }

    public static class Builder {
        private boolean isShowIcon = true;
        @DrawableRes
        private int iconRes = NO_RES;
        private int backgroundColor = NO_RES;
        /**
         * 背景
         * 如果设置那么内部就不会设置其他背景
         */
        private Drawable backGroundDrawable = null;
        private String msg;
        private int duration = Toast.LENGTH_SHORT;
        private int gravity = CHANG_GRAVITY;
        private int offsetX = 0;
        private int offsetY = 0;
        @Type
        private int type = Info;
        @LayoutRes
        private int layoutId = R.layout.toast_super;
        /**
         * 这个Layout 是否是自定义的，如果是，那么工具就不会这是其他属性
         */
        private boolean isCustom = false;
        boolean isFinish = false;
        Activity activity;//要finish的activity
        Callback callback;//toast销毁的回调
        Animator animator;//toast的动画

        protected Builder(String msg) {
            this.msg = msg;
        }

        public Builder setIconRes(@DrawableRes int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Builder setNoIcon() {
            this.isShowIcon = false;
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int backgroundColor) {
            this.backgroundColor = getApp().getResources().getColor(backgroundColor);
            return this;
        }

        public Builder setBackgroundDrawable(Drawable backGroundDrawable) {
            this.backGroundDrawable = backGroundDrawable;
            return this;
        }


        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setLayoutId(int layoutId, boolean isCustom) {
            this.layoutId = layoutId;
            this.isCustom = isCustom;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setOffsetX(int offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public Builder setOffsetY(int offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public Builder setFinish(Activity activity) {
            if (activity != null) {
                isFinish = true;
                this.activity = activity;
            }
            return this;
        }

        public Builder setFinishCallback(Callback callback) {
            if (callback != null) {
                this.callback = callback;
                isFinish = true;
            }
            return this;
        }

        public Builder setAnimator(Animator animator) {
            this.animator = animator;
            return this;
        }

        public void ok() {
            type = Confirm;
            show();
        }

        public void info() {
            type = Info;
            show();
        }

        public void error() {
            type = Error;
            show();
        }

        public void warn() {
            type = Warning;
            show();
        }

        public void show() {

            if (type == Info) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getApp().getResources().getColor(R.color.super_toast_color_info);
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_info;
                }
            } else if (type == Confirm) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getApp().getResources().getColor(R.color.super_toast_color_confirm);
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_confirm;
                }
            } else if (type == Warning) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getApp().getResources().getColor(R.color.super_toast_color_warning);
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_warning;
                }
            } else if (type == Error) {
                if (backgroundColor == NO_RES) {
                    backgroundColor = getApp().getResources().getColor(R.color.super_toast_color_error);
                }
                if (isShowIcon && iconRes == NO_RES) {
                    iconRes = R.drawable.ic_toast_super_error;
                }
            }

            if (INIT_OFFSET_Y == 0) {
                int resourceId = getApp().getResources().getIdentifier("toast_y_offset", "dimen", "android");
                if (resourceId > 0) {
                    INIT_OFFSET_Y = getApp().getResources().getDimensionPixelSize(resourceId);
                }
            }

            if (offsetY == 0 && gravity == INIT_GRAVITY) {
                offsetY = INIT_OFFSET_Y;
            }


            new SuperToast(this);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/22 11:11
     * <p>
     * 方法功能：显示错误信息
     */
    public static void showErrorMessage(String result) {
        if (result != null) {
            SuperToast.get(result).error();
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/22 11:11
     * <p>
     * 方法功能：显示警告信息
     */
    public static void showWarningMessage(String result) {
        if (result != null) {
            SuperToast.get(result).warn();
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/22 11:11
     * <p>
     * 方法功能：显示提示信息
     */
    public static void showInfoMessage(String result) {
        if (result != null) {
            SuperToast.get(result).info();
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/22 11:11
     * <p>
     * 方法功能：显示提示信息
     *
     * @param activity 是否销毁，null：不管
     */
    public static void showInfoMessage(String result, Activity activity) {
        if (result != null) {
            SuperToast.get(result).setFinish(activity).info();
        }
    }

    public static class Callback {
        public void onDismissed() {
        }
    }
}

