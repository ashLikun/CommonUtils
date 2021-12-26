package com.ashlikun.utils.ui

import android.R
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ashlikun.utils.other.DimensUtils.dip2px
import com.ashlikun.utils.ui.extend.getInflaterView
import com.google.android.material.tabs.TabLayout

typealias OnSizeListener = (width: Int, height: Int) -> Unit

object UiUtils {
    /**
     * 从资源文件获取一个view
     * @param context 不同的 context 对应不同的LayoutInflater
     */
    fun getInflaterView(context: Context, res: Int): View {
        return LayoutInflater.from(context).inflate(res, null)
    }

    /**
     * 从资源文件获取一个view
     * @param context 不同的 context 对应不同的LayoutInflater
     */
    fun getInflaterView(context: Context, res: Int, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(res, parent)
    }

    /**
     * 从资源文件获取一个view
     * @param context 不同的 context 对应不同的LayoutInflater
     */
    fun getInflaterView(
        context: Context,
        res: Int,
        parent: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        return LayoutInflater.from(context).inflate(res, parent, attachToRoot)
    }

    /**
     * 设置ImageView渲染（Tint）
     */
    fun setImageViewTint(view: ImageView, color: Int) {
        view.setColorFilter(view.resources.getColor(color))
    }

    @JvmStatic
    fun getRootView(context: Activity): View {
        return context.findViewById(R.id.content)
    }

    fun getDecorView(context: Activity): View {
        return context.window.decorView
    }

    /**
     * 按照原始的宽度，根据比例，缩放
     *
     * @param bili (w/h)
     */
    fun scaleViewByWidth(view: View, bili: Float) {
        getViewSize(view) { width, height -> scaleViewByWidth(view, width, bili) }
    }

    /**
     * 同上
     *
     * @param view
     * @param width
     * @param bili
     */
    fun scaleViewByWidth(view: View, width: Int, bili: Float) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        params.height = (width / bili).toInt()
        view.layoutParams = params
    }

    /**
     * 按照原始的高度，根据比例，缩放
     *
     * @param bili (w/h)
     */
    fun scaleViewByHeight(view: View, bili: Float) {
        getViewSize(view) { width, height -> scaleViewByHeight(view, height, bili) }
    }

    /**
     * 同上
     *
     * @param view
     * @param height
     * @param bili
     */
    fun scaleViewByHeight(view: View, height: Int, bili: Float) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        params.width = (height * bili).toInt()
        view.layoutParams = params
    }

    /**
     * 设置view高度
     *
     * @param view
     * @param height
     */
    fun setViewHeight(view: View, height: Int) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        params.height = height
        view.layoutParams = params
    }

    /**
     * 设置view宽度
     *
     * @param view
     * @param width
     */
    fun setViewWidth(view: View, width: Int) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        params.width = width
        view.layoutParams = params
    }

    /**
     * 设置view大小
     *
     * @param view
     * @param width
     */
    fun setViewSize(view: View, width: Int, height: Int) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        params.width = width
        params.height = height
        view.layoutParams = params
    }

    /**
     * 设置view   Margin
     *
     * @param view
     */
    fun setViewMargin(
        view: View,
        leftMargin: Int,
        topMargin: Int,
        rightMargin: Int,
        bottomMargin: Int
    ) {
        val params = view.layoutParams
        if (params != null && params is MarginLayoutParams) {
            params.leftMargin = leftMargin
            params.topMargin = topMargin
            params.rightMargin = rightMargin
            params.bottomMargin = bottomMargin
            view.layoutParams = params
        }
    }

    /**
     * 获取view的Margin
     *
     * @param view
     * @param orientation 那个值，左上右下（0,1,2,3）
     * @return
     */
    fun getViewMargin(view: View, orientation: Int): Int {
        val params = view.layoutParams
        if (params != null && params is MarginLayoutParams) {
            if (orientation == 0) {
                return params.leftMargin
            } else if (orientation == 1) {
                return params.topMargin
            } else if (orientation == 2) {
                return params.rightMargin
            } else if (orientation == 3) {
                return params.bottomMargin
            }
        }
        return -1
    }

    /**
     * 获取view大小
     */
    fun getViewSize(view: View, onSizeListener: OnSizeListener? = null) {
        if (view.measuredWidth > 0 || view.measuredHeight > 0) {
            onSizeListener?.invoke(view.measuredWidth, view.measuredHeight)
            return
        }
        val observer = view.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (view.measuredHeight <= 0 && view.measuredWidth <= 0) {
                    return
                }
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                onSizeListener?.invoke(view.measuredWidth, view.measuredHeight)
            }
        })
    }

    /**
     * 设置Tablayou的底部线宽度和文字一样宽，要在设置完item后设置
     *
     * @param tabLayout
     * @param marginDp  边距距离
     */
    fun setTabLayoutLine(tabLayout: TabLayout, marginDp: Int) {

        //了解源码得知 线的宽度是根据 tabView的宽度来设置的,post是为了可以获取到宽度
        tabLayout.post {
            try {
                //拿到tabLayout的mTabStrip属性
                val mTabStrip = tabLayout.getChildAt(0) as LinearLayout
                val margin = dip2px(tabLayout.context, marginDp.toFloat())
                for (i in 0 until mTabStrip.childCount) {
                    val tabView = mTabStrip.getChildAt(i) as ViewGroup
                    //拿到tabView的mTextView属性
                    var mTextView: TextView? = null
                    for (j in 0 until tabView.childCount) {
                        if (tabView.getChildAt(j) is TextView) {
                            mTextView = tabView.getChildAt(j) as TextView
                        }
                    }
                    //如果没有找到，就反射获取，一半不会
                    if (mTextView == null) {
                        val mTextViewField = tabView.javaClass.getDeclaredField("textView")
                        mTextViewField.isAccessible = true
                        mTextView = mTextViewField[tabView] as TextView
                        if (mTextView == null) {
                            val mTextViewField2 = tabView.javaClass.getDeclaredField("mTextView")
                            mTextViewField2.isAccessible = true
                            mTextView = mTextViewField2[tabView] as TextView
                        }
                    }
                    tabView.setPadding(0, 0, 0, 0)
                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    var width = 0
                    width = mTextView!!.width
                    if (width == 0) {
                        mTextView.measure(0, 0)
                        width = mTextView.measuredWidth
                    }
                    //设置tab左右间距  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    val params = tabView.layoutParams as LinearLayout.LayoutParams
                    params.width = width
                    params.leftMargin = margin
                    params.rightMargin = margin
                    tabView.layoutParams = params
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
}