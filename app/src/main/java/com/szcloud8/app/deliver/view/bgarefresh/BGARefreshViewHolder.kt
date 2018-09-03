/**
 * Copyright 2015 bingoogolapple
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.szcloud8.app.deliver.view.bgarefresh

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.szcloud8.app.deliver.R

abstract class BGARefreshViewHolder
/**
 * @param context
 * *
 * @param isLoadingMoreEnabled 上拉加载更多是否可用
 */
(protected var mContext: Context, isLoadingMoreEnabled: Boolean) {
    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值，默认1.8f
     */
    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值

     * @return
     */
    var paddingTopScale = PULL_DISTANCE_SCALE
        private set
    /**
     * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值，默认0.4f
     */
    /**
     * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值

     * @return
     */
    /**
     * 设置下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值，不能小于0，如果刷新控件比较高，建议将该值设置小一些

     * @param springDistanceScale
     */
    var springDistanceScale = SPRING_DISTANCE_SCALE
        set(springDistanceScale) {
            if (springDistanceScale < 0) {
                throw RuntimeException("下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值springDistanceScale不能小于0")
            }
            field = springDistanceScale
        }
    /**
     * 下拉刷新上拉加载更多控件
     */
    protected var mRefreshLayout: BGARefreshLayout? = null
    /**
     * 下拉刷新控件
     */
    protected var mRefreshHeaderView: View? = null
    /**
     * 上拉加载更多控件
     */
    protected var mLoadMoreFooterView: View? = null
    /**
     * 底部加载更多提示控件
     */
    protected var mFooterStatusTv: TextView? = null
    /**
     * 底部加载更多菊花控件
     */
    protected var mFooterChrysanthemumIv: ImageView? = null
    /**
     * 底部加载更多菊花drawable
     */
    protected var mFooterChrysanthemumAd: AnimationDrawable? = null
    /**
     * 正在加载更多时的文本
     */
    protected var mLoadingMoreText: String
    /**
     * 是否开启加载更多功能
     */
    private var mIsLoadingMoreEnabled = true
    /**
     * 整个加载更多控件的背景颜色资源id
     */
    private var mLoadMoreBackgroundColorRes = -1
    /**
     * 整个加载更多控件的背景drawable资源id
     */
    private var mLoadMoreBackgroundDrawableRes = -1
    /**
     * 下拉刷新控件的背景颜色资源id
     */
    protected var mRefreshViewBackgroundColorRes = -1
    /**
     * 下拉刷新控件的背景drawable资源id
     */
    protected var mRefreshViewBackgroundDrawableRes = -1
    /**
     * 头部控件移动动画时常
     */
    /**
     * 获取顶部未满足下拉刷新条件时回弹到初始状态、满足刷新条件时回弹到正在刷新状态、刷新完毕后回弹到初始状态的动画时间，默认为500毫秒

     * @return
     */
    /**
     * 设置顶部未满足下拉刷新条件时回弹到初始状态、满足刷新条件时回弹到正在刷新状态、刷新完毕后回弹到初始状态的动画时间，默认为300毫秒

     * @param topAnimDuration
     */
    var topAnimDuration = 500

    init {
        mLoadingMoreText = mContext.getString(R.string.refreshing_text)
        mIsLoadingMoreEnabled = isLoadingMoreEnabled
    }

    /**
     * 设置正在加载更多时的文本

     * @param loadingMoreText
     */
    fun setLoadingMoreText(loadingMoreText: String) {
        mLoadingMoreText = loadingMoreText
    }

    /**
     * 设置整个加载更多控件的背景颜色资源id

     * @param loadMoreBackgroundColorRes
     */
    fun setLoadMoreBackgroundColorRes(@ColorRes loadMoreBackgroundColorRes: Int) {
        mLoadMoreBackgroundColorRes = loadMoreBackgroundColorRes
    }

    /**
     * 设置整个加载更多控件的背景drawable资源id

     * @param loadMoreBackgroundDrawableRes
     */
    fun setLoadMoreBackgroundDrawableRes(@DrawableRes loadMoreBackgroundDrawableRes: Int) {
        mLoadMoreBackgroundDrawableRes = loadMoreBackgroundDrawableRes
    }

    /**
     * 设置下拉刷新控件的背景颜色资源id

     * @param refreshViewBackgroundColorRes
     */
    fun setRefreshViewBackgroundColorRes(@ColorRes refreshViewBackgroundColorRes: Int) {
        mRefreshViewBackgroundColorRes = refreshViewBackgroundColorRes
    }

    /**
     * 设置下拉刷新控件的背景drawable资源id

     * @param refreshViewBackgroundDrawableRes
     */
    fun setRefreshViewBackgroundDrawableRes(@DrawableRes refreshViewBackgroundDrawableRes: Int) {
        mRefreshViewBackgroundDrawableRes = refreshViewBackgroundDrawableRes
    }

    /**
     * 获取上拉加载更多控件，如果不喜欢这种上拉刷新风格可重写该方法实现自定义LoadMoreFooterView

     * @return
     */
    val loadMoreFooterView: View?
        get() {
            if (!mIsLoadingMoreEnabled) {
                return null
            }
            if (mLoadMoreFooterView == null) {
                mLoadMoreFooterView = View.inflate(mContext, R.layout.view_normal_refresh_footer, null)
                mLoadMoreFooterView!!.setBackgroundColor(Color.TRANSPARENT)
                if (mLoadMoreBackgroundColorRes != -1) {
                    mLoadMoreFooterView!!.setBackgroundResource(mLoadMoreBackgroundColorRes)
                }
                if (mLoadMoreBackgroundDrawableRes != -1) {
                    mLoadMoreFooterView!!.setBackgroundResource(mLoadMoreBackgroundDrawableRes)
                }
                mFooterStatusTv = mLoadMoreFooterView!!.findViewById<TextView>(R.id.tv_normal_refresh_footer_status)
                mFooterChrysanthemumIv = mLoadMoreFooterView!!.findViewById<ImageView>(R.id.iv_normal_refresh_footer_chrysanthemum)
                mFooterChrysanthemumAd = mFooterChrysanthemumIv?.drawable as AnimationDrawable
                mFooterStatusTv?.text = mLoadingMoreText
            }
            return mLoadMoreFooterView
        }

    /**
     * 获取头部下拉刷新控件

     * @return
     */
    abstract val refreshHeaderView: View

    /**
     * 下拉刷新控件可见时，处理上下拉进度

     * @param scale         下拉过程0 到 1，回弹过程1 到 0，没有加上弹簧距离移动时的比例
     * *
     * @param moveYDistance 整个下拉刷新控件paddingTop变化的值，如果有弹簧距离，会大于整个下拉刷新控件的高度
     */
    abstract fun handleScale(scale: Float, moveYDistance: Int)

    /**
     * 进入到未处理下拉刷新状态
     */
    abstract fun changeToIdle()

    /**
     * 进入下拉状态
     */
    abstract fun changeToPullDown()

    /**
     * 进入释放刷新状态
     */
    abstract fun changeToReleaseRefresh()

    /**
     * 进入正在刷新状态
     */
    abstract fun changeToRefreshing()

    /**
     * 结束下拉刷新
     */
    abstract fun onEndRefreshing()

    /**
     * 设置手指移动距离与下拉刷新控件paddingTop移动距离的比值

     * @param pullDistanceScale
     */
    fun setPullDistanceScale(pullDistanceScale: Float) {
        paddingTopScale = pullDistanceScale
    }

    /**
     * 是处于能够进入刷新状态

     * @return
     */
    fun canChangeToRefreshingStatus(): Boolean {
        return false
    }

    /**
     * 进入加载更多状态
     */
    fun changeToLoadingMore() {
        if (mIsLoadingMoreEnabled && mFooterChrysanthemumAd != null) {
            mFooterChrysanthemumAd!!.start()
        }
    }

    /**
     * 结束上拉加载更多
     */
    fun onEndLoadingMore() {
        if (mIsLoadingMoreEnabled && mFooterChrysanthemumAd != null) {
            mFooterChrysanthemumAd!!.stop()
        }
    }

    /**
     * 获取下拉刷新控件的高度，如果初始化时的高度和最后展开的最大高度不一致，需重写该方法返回最大高度

     * @return
     */
    // 测量下拉刷新控件的高度
    val refreshHeaderViewHeight: Int
        get() {
            if (mRefreshHeaderView != null) {
                mRefreshHeaderView!!.measure(0, 0)
                return mRefreshHeaderView!!.measuredHeight
            }
            return 0
        }

    /**
     * 改变整个下拉刷新头部控件移动一定的距离（带动画），自定义刷新控件进入刷新状态前后的高度有变化时可以使用该方法（参考BGAStickinessRefreshView）

     * @param distance
     */
    fun startChangeWholeHeaderViewPaddingTop(distance: Int) {
        mRefreshLayout?.startChangeWholeHeaderViewPaddingTop(distance)
    }

    /**
     * 设置下拉刷新上拉加载更多控件，该方法是设置BGARefreshViewHolder给BGARefreshLayout时由BGARefreshLayout调用

     * @param refreshLayout
     */
    fun setRefreshLayout(refreshLayout: BGARefreshLayout) {
        mRefreshLayout = refreshLayout
    }

    companion object {
        /**
         * 手指移动距离与下拉刷新控件paddingTop移动距离的比值
         */
        private val PULL_DISTANCE_SCALE = 1.8f
        /**
         * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值
         */
        private val SPRING_DISTANCE_SCALE = 0.4f
    }

}