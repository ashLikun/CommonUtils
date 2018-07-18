package com.ashlikun.utils.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

import com.ashlikun.utils.other.DimensUtils;


/**
 * 作者　　: 李坤
 * 创建时间: 2016/3/29 11:29
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：Drawable 常用的工具
 */
public class DrawableUtils {
    Context context;

    public DrawableUtils(Context context) {
        this.context = context;
    }

    public static DrawableUtils get(Context context) {
        return new DrawableUtils(context);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/29 11:30
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：获取ColorStateList ，，对TextView设置不同状态时其文字颜色。
     */
    public ColorStateList createColorStateList(@ColorRes int normal, @ColorRes int pressed, @ColorRes int select, @ColorRes int enable) {
        int[] colors = new int[]{getColor(pressed), getColor(select), getColor(enable), getColor(normal)};
        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{-android.R.attr.state_enabled};
        states[3] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/11 17:41
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取StateListDrawable实例
     *
     * @param normal 默认的资源
     * @param select 选择的资源
     */
    public ColorStateList createColorSelect(@ColorRes int normal, @ColorRes int select) {
        int[] colors = new int[]{getColor(select), getColor(normal)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/12 9:49
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：同上
     */
    public ColorStateList createColorStateList(@ColorRes int normal, @ColorRes int pressed) {
        int[] colors = new int[]{getColor(pressed), getColor(normal)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/12 9:49
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：同上
     */
    public ColorStateList createColorStateList(@ColorRes int normal, @ColorRes int pressed, @ColorRes int enable) {
        int[] colors = new int[]{getColor(pressed), getColor(enable), getColor(normal)};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/11 17:39
     * 邮箱　　：496546144@qq.com
     * 方法功能：
     *
     * @param fillColorId:填充的颜色
     * @param strokeColorId:边框颜色
     * @param roundRadius:圆角半径   dp
     * @param strokeWidth:边框宽度   dp
     */
    @SuppressLint("ResourceType")
    public GradientDrawable getGradientDrawable(@ColorRes int fillColorId, @ColorRes int strokeColorId, float roundRadius, float strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getColor(fillColorId));
        if (strokeColorId > 0) {
            drawable.setStroke(DimensUtils.dip2px(context, strokeWidth), getColor(strokeColorId));
        }
        if (roundRadius > 0) {
            drawable.setCornerRadius(DimensUtils.dip2px(context, roundRadius));
        }
        return drawable;
    }

    @SuppressLint("ResourceType")
    public GradientDrawable getGradientDrawablePx(@ColorRes int fillColorId, @ColorRes int strokeColorId, int roundRadius, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getColor(fillColorId));
        if (strokeColorId > 0) {
            drawable.setStroke(strokeWidth, getColor(strokeColorId));
        }
        if (roundRadius > 0) {
            drawable.setCornerRadius(roundRadius);
        }
        return drawable;
    }

    public GradientDrawable getGradientDrawableNoStroke(@ColorRes int fillColorId, float roundRadius) {
        return getGradientDrawable(fillColorId, 0, roundRadius, 0);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/12 10:42
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：同上
     */

    public GradientDrawable getGradientDrawable(@ColorRes int fillColorId, float roundRadius) {
        return getGradientDrawable(fillColorId, -1, roundRadius, -1);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/11 17:41
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取StateListDrawable实例
     *
     * @param normal  默认的资源
     * @param pressed 按下的资源
     * @param enabled 不可用的资源
     */
    public StateListDrawable getStateListDrawable(Drawable normal, Drawable pressed, Drawable enabled) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{-android.R.attr.state_enabled}, enabled);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/11 17:41
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取StateListDrawable实例
     *
     * @param normal 默认的资源
     * @param select 选择的资源
     */
    public StateListDrawable getSelectDrawable(Drawable normal, Drawable select) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_selected}, select);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    public StateListDrawable getSelectDrawable(@DrawableRes int normalId, @DrawableRes int selectId) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_selected}, context.getResources().getDrawable(selectId));
        bg.addState(new int[]{}, context.getResources().getDrawable(normalId));
        return bg;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/11 17:41
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取StateListDrawable实例
     *
     * @param idNormal  默认的资源id
     * @param idPressed 按下的资源id
     * @param idEbable  不可用的资源id
     */
    public StateListDrawable getStateListDrawable(@DrawableRes int idNormal, @DrawableRes int idPressed, @DrawableRes int idEbable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable enabled = idEbable == -1 ? null : context.getResources().getDrawable(idEbable);
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{-android.R.attr.state_enabled}, enabled);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/11 17:41
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取StateListDrawable实例
     *
     * @param idNormal  默认的资源id
     * @param idPressed 按下的资源id
     */
    public StateListDrawable getStateListDrawable(@DrawableRes int idNormal, @DrawableRes int idPressed) {
        return getStateListDrawable(idNormal, idPressed, idPressed);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/12 10:45
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取StateListDrawable实例
     * 之定义资源，例如形状，边框，颜色
     *
     * @param idNormal     默认的颜色
     * @param idPressed    按下的颜色
     * @param idEnabled    不可用的颜色的颜色
     * @param strokeColor  边框颜色
     * @param cornerRadius 圆角半径  DP
     * @param strokeWidth  边框宽度 dP
     * @return
     */
    public StateListDrawable getStateListDrawable(@ColorRes int idNormal, @ColorRes int idPressed, @ColorRes int idEnabled, @ColorRes int strokeColor,
                                                  float cornerRadius, float strokeWidth) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = getGradientDrawable(idNormal, strokeColor, cornerRadius, strokeWidth);
        Drawable pressed = getGradientDrawable(idPressed, strokeColor, cornerRadius, strokeWidth);
        Drawable enabled = getGradientDrawable(idEnabled, strokeColor, cornerRadius, strokeWidth);
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{-android.R.attr.state_enabled}, enabled);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/12 10:48
     * 邮箱　　：496546144@qq.com
     * 方法功能：同上
     */
    public StateListDrawable getStateListDrawable(@ColorRes int idNormal, @ColorRes int idPressed, @ColorRes int idEnabled,
                                                  float cornerRadius) {
        Drawable normal = getGradientDrawable(idNormal, cornerRadius);
        Drawable pressed = getGradientDrawable(idPressed, cornerRadius);
        Drawable enabled = getGradientDrawable(idEnabled, cornerRadius);
        return getStateListDrawable(normal, pressed, enabled);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/12/12 9:52
     * <p>
     * 功能：创建水波纹效果
     */
    public static class RippleBuilder {
        Context context;
        ColorStateList rippleColor;
        Drawable normalDrawable;
        Drawable pressedDrawable;
        Drawable selectDrawable;
        Drawable enableDrawable;

        public RippleBuilder(Context context) {
            this.context = context;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/7/11 15:26
         * 邮箱　　：496546144@qq.com
         * 方法功能：设置失效的资源
         */
        public RippleBuilder setEnableDrawable(Drawable enableDrawable) {
            this.enableDrawable = enableDrawable;
            return this;
        }

        public RippleBuilder setSelectDrawable(Drawable selectDrawable) {
            this.selectDrawable = selectDrawable;
            return this;
        }

        public RippleBuilder setEnableColor(@ColorRes int enable) {
            this.enableDrawable = new ColorDrawable(getColor(enable));
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/7/11 15:26
         * 邮箱　　：496546144@qq.com
         * 方法功能：设置涟漪的颜色
         */
        public RippleBuilder setRippleColor(ColorStateList rippleColor) {
            this.rippleColor = rippleColor;
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/7/11 15:26
         * 邮箱　　：496546144@qq.com
         * 方法功能：设置默认
         */
        public RippleBuilder setNormalDrawable(Drawable normalDrawable) {
            this.normalDrawable = normalDrawable;
            return this;
        }

        public RippleBuilder setNormalColor(@ColorRes int normalColor) {
            this.normalDrawable = new ColorDrawable(getColor(normalColor));
            return this;
        }

        /**
         * 作者　　: 李坤
         * 创建时间: 2017/7/11 15:26
         * 邮箱　　：496546144@qq.com
         * 方法功能：设置按下
         */
        public RippleBuilder setPressedDrawable(Drawable pressedDrawable) {
            this.pressedDrawable = pressedDrawable;
            return this;
        }

        public RippleBuilder setPressedColor(@ColorRes int pressedColor) {
            this.pressedDrawable = new ColorDrawable(getColor(pressedColor));
            if (rippleColor == null) {
                rippleColor = ColorStateList.valueOf(getColor(pressedColor));
            }
            return this;
        }


        public Drawable create() {

            if (normalDrawable == null) {
                normalDrawable = new ColorDrawable(Color.TRANSPARENT);
            }
            if (enableDrawable == null) {
                enableDrawable = new ColorDrawable(Color.GRAY);
            }
            if (rippleColor == null) {
                rippleColor = ColorStateList.valueOf(Color.LTGRAY);
            }
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{-android.R.attr.state_enabled}, enableDrawable);
            drawable.addState(new int[]{android.R.attr.state_selected},
                    selectDrawable == null ? pressedDrawable : selectDrawable);
            drawable.addState(new int[]{}, normalDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return new RippleDrawable(rippleColor, drawable, normalDrawable);
            } else {
                if (pressedDrawable != null) {
                    drawable.addState(new int[]{android.R.attr.state_pressed},
                            pressedDrawable);
                    drawable.addState(new int[]{android.R.attr.state_focused},
                            pressedDrawable);
                }
                return drawable;
            }
        }

        private int getColor(@ColorRes int colorId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return context.getResources().getColor(colorId, context.getTheme());
            }
            return context.getResources().getColor(colorId);
        }
    }

    private int getColor(@ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorId, context.getTheme());
        }
        return context.getResources().getColor(colorId);
    }

    /**
     * 把drawable渲染成指定的颜色
     */
    public static Drawable getTintDrawable(Drawable drawable, @ColorInt int color) {
        Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    /**
     * 改变Drawable大小
     */
    public static Drawable changDrawSize(Drawable drawable, int width, int height) {
        drawable = DrawableCompat.wrap(drawable).mutate();
        drawable.setBounds(0, 0, width, height);
        return drawable;
    }

    public static Drawable changDrawSizeWidth(Drawable drawable, int width) {
        //高度被设置了，那么久按照比例设置宽度
        float drawWidth = drawable.getMinimumWidth();
        float drawHeight = drawable.getMinimumHeight();
        return changDrawSize(drawable, width, (int) (width / drawWidth * drawHeight));
    }

    public static Drawable changDrawHeight(Drawable drawable, int height) {
        //高度被设置了，那么久按照比例设置宽度
        float drawWidth = drawable.getMinimumWidth();
        float drawHeight = drawable.getMinimumHeight();
        return changDrawSize(drawable, (int) (height / drawHeight * drawWidth), height);
    }

    /**
     * 创建一个TextView的上下左右Drawable
     *
     * @return
     */
    public static BuilderTvd createTextDraw(TextView textView, Drawable drawable) {
        return new BuilderTvd(textView, drawable);
    }

    public static BuilderTvd createTextDraw(TextView textView, @DrawableRes int drawable) {
        return new BuilderTvd(textView, textView.getResources().getDrawable(drawable));
    }


    /**
     * @author　　: 李坤
     * 创建时间: 2018/5/28 0028 下午 3:00
     * 邮箱　　：496546144@qq.com
     * <p>
     * 功能介绍：TextView的上下左右Drawable，兼容大小
     */
    public static final class BuilderTvd {
        private Context context;
        private int width = 0;
        private int height = 0;
        private int tintColor = -1;
        private Drawable drawable;
        private TextView textView;
        /**
         * 左：1
         * 上：2
         * 右：3
         * 下：4
         * 默认 右
         */
        int location = 3;

        public BuilderTvd(TextView textView, Drawable drawable) {
            this.context = textView.getContext();
            this.textView = textView;
            this.drawable = drawable;
        }

        public BuilderTvd width(int val) {
            width = val;
            return this;
        }

        public BuilderTvd height(int val) {
            height = val;
            return this;
        }

        public BuilderTvd widthDp(int val) {
            width = DimensUtils.dip2px(context, val);
            return this;
        }

        public BuilderTvd heightDp(int val) {
            height = DimensUtils.dip2px(context, val);
            return this;
        }

        public BuilderTvd drawable(Drawable val) {
            drawable = val;
            return this;
        }

        public BuilderTvd tintColorId(int val) {
            return tintColor(context.getResources().getColor(val));
        }

        public BuilderTvd tintColor(int val) {
            tintColor = val;
            return this;
        }

        public BuilderTvd textView(TextView val) {
            textView = val;
            return this;
        }

        /**
         * 左：1
         * 上：2
         * 右：3
         * 下：4
         * 默认 右
         */
        public BuilderTvd location(int location) {
            this.location = location;
            return this;
        }

        public Drawable getDrawable() {
            //是否改变宽高
            boolean isChang = true;
            float drawWidth = drawable.getMinimumWidth();
            float drawHeight = drawable.getMinimumHeight();
            if (width == 0 && height == 0) {
                width = (int) drawWidth;
                height = (int) drawHeight;
                isChang = false;
            } else if (width == 0) {
                //高度被设置了，那么久按照比例设置宽度
                width = (int) (height / drawHeight * drawWidth);
            } else if (height == 0) {
                //高度被设置了，那么久按照比例设置宽度
                height = (int) (width / drawWidth * drawHeight);
            }
            //如果使用tint，必须使用DrawableCompat.wrap
            if (isChang || tintColor != -1) {
                drawable = DrawableCompat.wrap(drawable).mutate();
                if (tintColor != -1) {
                    DrawableCompat.setTint(drawable, tintColor);
                }
            }
            drawable.setBounds(0, 0, width, height);
            return drawable;
        }

        public void set() {
            getDrawable();
            Drawable[] yiyou = textView.getCompoundDrawables();
            switch (location) {
                case 1:
                    textView.setCompoundDrawables(drawable, yiyou[1], yiyou[2], yiyou[3]);
                    break;
                case 2:
                    textView.setCompoundDrawables(yiyou[0], drawable, yiyou[2], yiyou[3]);
                    break;
                case 3:
                    textView.setCompoundDrawables(yiyou[0], yiyou[1], drawable, yiyou[3]);
                    break;
                case 4:
                    textView.setCompoundDrawables(yiyou[0], yiyou[1], yiyou[2], drawable);
                    break;
            }
        }
    }
}

