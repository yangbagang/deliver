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
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView

import com.szcloud8.app.deliver.R

class BGANormalRefreshViewHolder
/**
 * @param context
 * *
 * @param isLoadingMoreEnabled 上拉加载更多是否可用
 */
(context: Context, isLoadingMoreEnabled: Boolean) : BGARefreshViewHolder(context, isLoadingMoreEnabled) {
    private var mHeaderStatusTv: TextView? = null
    private var mHeaderArrowIv: ImageView? = null
    private var mHeaderChrysanthemumIv: ImageView? = null
    private var mHeaderChrysanthemumAd: AnimationDrawable? = null
    private var mUpAnim: RotateAnimation? = null
    private var mDownAnim: RotateAnimation? = null

    private var mPullDownRefreshText: String? = null
    private var mReleaseRefreshText: String? = null
    private var mRefreshingText: String? = null

    init {
        mPullDownRefreshText = context.getString(R.string.pull_refresh_text)
        mReleaseRefreshText = context.getString(R.string.release_refresh_text)
        mRefreshingText = context.getString(R.string.refreshing_text)
        initAnimation()
    }

    private fun initAnimation() {
        mUpAnim = RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        mUpAnim!!.duration = 150
        mUpAnim!!.fillAfter = true

        mDownAnim = RotateAnimation(-180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        mDownAnim!!.fillAfter = true
    }

    /**
     * 设置未满足刷新条件，提示继续往下拉的文本

     * @param pullDownRefreshText
     */
    fun setPullDownRefreshText(pullDownRefreshText: String) {
        mPullDownRefreshText = pullDownRefreshText
    }

    /**
     * 设置满足刷新条件时的文本

     * @param releaseRefreshText
     */
    fun setReleaseRefreshText(releaseRefreshText: String) {
        mReleaseRefreshText = releaseRefreshText
    }

    /**
     * 设置正在刷新时的文本

     * @param refreshingText
     */
    fun setRefreshingText(refreshingText: String) {
        mRefreshingText = refreshingText
    }

    override val refreshHeaderView: View
        get() {
            if (mRefreshHeaderView == null) {
                mRefreshHeaderView = View.inflate(mContext, R.layout.view_refresh_header_normal, null)
                mRefreshHeaderView!!.setBackgroundColor(Color.TRANSPARENT)
                if (mRefreshViewBackgroundColorRes != -1) {
                    mRefreshHeaderView!!.setBackgroundResource(mRefreshViewBackgroundColorRes)
                }
                if (mRefreshViewBackgroundDrawableRes != -1) {
                    mRefreshHeaderView!!.setBackgroundResource(mRefreshViewBackgroundDrawableRes)
                }
                mHeaderStatusTv = mRefreshHeaderView!!.findViewById<TextView>(R.id.tv_normal_refresh_header_status)
                mHeaderArrowIv = mRefreshHeaderView!!.findViewById<ImageView>(R.id.iv_normal_refresh_header_arrow)
                mHeaderChrysanthemumIv = mRefreshHeaderView!!.findViewById<ImageView>(R.id.iv_normal_refresh_header_chrysanthemum)
                mHeaderChrysanthemumAd = mHeaderChrysanthemumIv!!.drawable as AnimationDrawable
                mHeaderStatusTv!!.text = mPullDownRefreshText
            }
            return mRefreshHeaderView!!
        }

    override fun handleScale(scale: Float, moveYDistance: Int) {
    }

    override fun changeToIdle() {
    }

    override fun changeToPullDown() {
        mHeaderStatusTv!!.text = mPullDownRefreshText
        mHeaderChrysanthemumIv!!.visibility = View.INVISIBLE
        mHeaderChrysanthemumAd!!.stop()
        mHeaderArrowIv!!.visibility = View.VISIBLE
        mDownAnim!!.duration = 150
        mHeaderArrowIv!!.startAnimation(mDownAnim)
    }

    override fun changeToReleaseRefresh() {
        mHeaderStatusTv!!.text = mReleaseRefreshText
        mHeaderChrysanthemumIv!!.visibility = View.INVISIBLE
        mHeaderChrysanthemumAd!!.stop()
        mHeaderArrowIv!!.visibility = View.VISIBLE
        mHeaderArrowIv!!.startAnimation(mUpAnim)
    }

    override fun changeToRefreshing() {
        mHeaderStatusTv!!.text = mRefreshingText
        // 必须把动画清空才能隐藏成功
        mHeaderArrowIv!!.clearAnimation()
        mHeaderArrowIv!!.visibility = View.INVISIBLE
        mHeaderChrysanthemumIv!!.visibility = View.VISIBLE
        mHeaderChrysanthemumAd!!.start()
    }

    override fun onEndRefreshing() {
        mHeaderStatusTv!!.text = mPullDownRefreshText
        mHeaderChrysanthemumIv!!.visibility = View.INVISIBLE
        mHeaderChrysanthemumAd!!.stop()
        mHeaderArrowIv!!.visibility = View.VISIBLE
        mDownAnim!!.duration = 0
        mHeaderArrowIv!!.startAnimation(mDownAnim)
    }

}