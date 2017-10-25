package com.ashlikun.utils.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashlikun.utils.R;
import com.ashlikun.utils.other.DimensUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ashlikun.utils.Utils.getApp;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/9/18　10:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：自定义toast样式
 */

public class SuperToast {
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

    //要在主线程
    private void cretae(final Builder builder) {
        Toast mToast = ToastUtils.getMyToast();
        View mView;
        if (mToast.getView() != null && TOAST_VIEW_TAG.equals(mToast.getView().getTag())) {
            mView = mToast.getView();
        } else {
            mView = LayoutInflater.from(getApp()).inflate(builder.layoutId, null);
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
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(builder.backgroundColor);
        drawable.setStroke(DimensUtils.dip2px(getApp(), 1f), getBackgroundShen(builder.backgroundColor));
        drawable.setCornerRadius(DimensUtils.dip2px(getApp(), 4));
        DrawableUtils.setBackground(view, drawable);

        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        if (!builder.isShowIcon) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageResource(builder.iconRes);
        }
        TextView textView = (TextView) view.findViewById(R.id.msg);
        textView.setText(builder.msg);
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
        private String msg;
        private int duration = Toast.LENGTH_SHORT;
        private int gravity = CHANG_GRAVITY;
        private int offsetX = 0;
        private int offsetY = 0;
        @Type
        private int type = Info;
        @LayoutRes
        private int layoutId = R.layout.toast_super;
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

    public static class Callback {
        public void onDismissed() {
        }
    }
}
