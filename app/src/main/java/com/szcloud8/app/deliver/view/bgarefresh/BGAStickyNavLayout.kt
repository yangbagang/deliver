package com.szcloud8.app.deliver.view.bgarefresh

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.OverScroller
import android.widget.ScrollView

class BGAStickyNavLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var mHeaderView: View? = null
    private var mNavView: View? = null
    private var mContentView: View? = null

    private var mDirectNormalView: View? = null
    private var mDirectRecyclerView: RecyclerView? = null
    private var mDirectAbsListView: AbsListView? = null
    private var mDirectScrollView: ScrollView? = null
    private var mDirectWebView: WebView? = null
    private var mDirectViewPager: ViewPager? = null

    private var mNestedContentView: View? = null
    private var mNestedNormalView: View? = null
    private var mNestedRecyclerView: RecyclerView? = null
    private var mNestedAbsListView: AbsListView? = null
    private var mNestedScrollView: ScrollView? = null
    private var mNestedWebView: WebView? = null

    private var mOverScroller: OverScroller? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var mTouchSlop: Int = 0
    private var mMaximumVelocity: Int = 0
    private var mMinimumVelocity: Int = 0

    private var mIsInControl = true

    private var mLastDispatchY: Float = 0.toFloat()
    private var mLastTouchY: Float = 0.toFloat()

    var mRefreshLayout: BGARefreshLayout? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        orientation = LinearLayout.VERTICAL

        mOverScroller = OverScroller(context)
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
    }

    override fun setOrientation(orientation: Int) {
        if (LinearLayout.VERTICAL == orientation) {
            super.setOrientation(LinearLayout.VERTICAL)
        }
    }

    public override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount != 3) {
            throw IllegalStateException(BGAStickyNavLayout::class.java.simpleName + "必须有且只有三个子控件")
        }

        mHeaderView = getChildAt(0)
        mNavView = getChildAt(1)
        mContentView = getChildAt(2)

        if (mContentView is AbsListView) {
            mDirectAbsListView = mContentView as AbsListView?
            mDirectAbsListView!!.setOnScrollListener(mLvOnScrollListener)
        } else if (mContentView is RecyclerView) {
            mDirectRecyclerView = mContentView as RecyclerView?
            mDirectRecyclerView!!.addOnScrollListener(mRvOnScrollListener)
        } else if (mContentView is ScrollView) {
            mDirectScrollView = mContentView as ScrollView?
        } else if (mContentView is WebView) {
            mDirectWebView = mContentView as WebView?
        } else if (mContentView is ViewPager) {
            mDirectViewPager = mContentView as ViewPager?
            mDirectViewPager!!.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    regetNestedContentView()
                }
            })
        } else {
            mDirectNormalView = mContentView
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(mContentView, widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec) - navViewHeight, View.MeasureSpec.EXACTLY))
    }

    override fun computeScroll() {
        if (mOverScroller!!.computeScrollOffset()) {
            scrollTo(0, mOverScroller!!.currY)
            invalidate()
        }
    }

    fun fling(velocityY: Int) {
        mOverScroller!!.fling(0, scrollY, 0, velocityY, 0, 0, 0, headerViewHeight)
        invalidate()
    }

    override fun scrollTo(x: Int, y: Int) {
        var y = y
        if (y < 0) {
            y = 0
        }

        val headerViewHeight = headerViewHeight
        if (y > headerViewHeight) {
            y = headerViewHeight
        }

        if (y != scrollY) {
            super.scrollTo(x, y)
        }
    }

    /**
     * 获取头部视图高度，包括topMargin和bottomMargin

     * @return
     */
    private val headerViewHeight: Int
        get() {
            val layoutParams = mHeaderView!!.layoutParams as ViewGroup.MarginLayoutParams
            return mHeaderView!!.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
        }

    /**
     * 获取导航视图的高度，包括topMargin和bottomMargin

     * @return
     */
    private val navViewHeight: Int
        get() {
            val layoutParams = mNavView!!.layoutParams as ViewGroup.MarginLayoutParams
            return mNavView!!.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
        }

    /**
     * 头部视图是否已经完全隐藏

     * @return
     */
    private // 0表示x，1表示y
            //            debug("头部视图完全隐藏  navViewTopOnScreenY = " + navViewTopOnScreenY + "   contentOnScreenTopY = " + contentOnScreenTopY);
            //            debug("头部视图没有完全隐藏  navViewTopOnScreenY = " + navViewTopOnScreenY + "   contentOnScreenTopY = " + contentOnScreenTopY);
    val isHeaderViewCompleteInvisible: Boolean
        get() {
            val location = IntArray(2)
            getLocationOnScreen(location)
            val contentOnScreenTopY = location[1] + paddingTop

            mNavView!!.getLocationOnScreen(location)
            val params = mNavView!!.layoutParams as ViewGroup.MarginLayoutParams
            val navViewTopOnScreenY = location[1] - params.topMargin
            return navViewTopOnScreenY == contentOnScreenTopY
        }

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val currentTouchY = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mLastDispatchY = currentTouchY
            MotionEvent.ACTION_MOVE -> {
                val differentY = currentTouchY - mLastDispatchY
                mLastDispatchY = currentTouchY
                if (isContentViewToTop && isHeaderViewCompleteInvisible) {
                    if (differentY >= 0 && !mIsInControl) {
                        mIsInControl = true

                        return resetDispatchTouchEvent(ev)
                    }

                    if (differentY <= 0 && mIsInControl) {
                        mIsInControl = false

                        return resetDispatchTouchEvent(ev)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun resetDispatchTouchEvent(ev: MotionEvent): Boolean {
        val newEvent = MotionEvent.obtain(ev)

        ev.action = MotionEvent.ACTION_CANCEL
        dispatchTouchEvent(ev)

        newEvent.action = MotionEvent.ACTION_DOWN
        return dispatchTouchEvent(newEvent)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val currentTouchY = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mLastTouchY = currentTouchY
            MotionEvent.ACTION_MOVE -> {
                val differentY = currentTouchY - mLastTouchY
                if (Math.abs(differentY) > mTouchSlop) {
                    if (!isHeaderViewCompleteInvisible || isContentViewToTop && isHeaderViewCompleteInvisible && mIsInControl) {
                        mLastTouchY = currentTouchY
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        initVelocityTrackerIfNotExists()
        mVelocityTracker!!.addMovement(event)

        val currentTouchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mOverScroller!!.isFinished) {
                    mOverScroller!!.abortAnimation()
                }

                mLastTouchY = currentTouchY
            }
            MotionEvent.ACTION_MOVE -> {
                val differentY = currentTouchY - mLastTouchY
                mLastTouchY = currentTouchY
                if (Math.abs(differentY) > 0) {
                    scrollBy(0, (-differentY).toInt())
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                recycleVelocityTracker()
                if (!mOverScroller!!.isFinished) {
                    mOverScroller!!.abortAnimation()
                }
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val initialVelocity = mVelocityTracker!!.yVelocity.toInt()
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    fling(-initialVelocity)
                }
                recycleVelocityTracker()
            }
        }
        return true
    }

    val isContentViewToTop: Boolean
        get() {
            if (mDirectNormalView != null) {
                return true
            }

            if (ScrollingUtil.isScrollViewOrWebViewToTop(mDirectWebView)) {
                return true
            }

            if (ScrollingUtil.isScrollViewOrWebViewToTop(mDirectScrollView)) {
                return true
            }

            if (ScrollingUtil.isAbsListViewToTop(mDirectAbsListView)) {
                return true
            }

            if (ScrollingUtil.isRecyclerViewToTop(mDirectRecyclerView)) {
                return true
            }

            if (mDirectViewPager != null) {
                return isViewPagerContentViewToTop
            }

            return false
        }

    private val isViewPagerContentViewToTop: Boolean
        get() {
            if (mNestedContentView == null) {
                regetNestedContentView()
            }

            if (mDirectNormalView != null) {
                return true
            }

            if (ScrollingUtil.isScrollViewOrWebViewToTop(mNestedWebView)) {
                return true
            }

            if (ScrollingUtil.isScrollViewOrWebViewToTop(mNestedScrollView)) {
                return true
            }

            if (ScrollingUtil.isAbsListViewToTop(mNestedAbsListView)) {
                return true
            }

            return ScrollingUtil.isRecyclerViewToTop(mNestedRecyclerView)

        }

    /**
     * 重新获取嵌套的内容视图
     */
    private fun regetNestedContentView() {
        val currentItem = mDirectViewPager!!.currentItem
        val adapter = mDirectViewPager!!.adapter
        if (adapter is FragmentPagerAdapter || adapter is FragmentStatePagerAdapter) {
            val item = adapter.instantiateItem(mDirectViewPager!!, currentItem) as Fragment
            mNestedContentView = item.view

            // 清空之前的
            mNestedNormalView = null
            mNestedAbsListView = null
            mNestedRecyclerView = null
            mNestedScrollView = null
            mNestedWebView = null

            if (mNestedContentView is AbsListView) {
                mNestedAbsListView = mNestedContentView as AbsListView?
                mNestedAbsListView!!.setOnScrollListener(mLvOnScrollListener)

                if (!isHeaderViewCompleteInvisible) {
                    mNestedAbsListView!!.smoothScrollToPosition(0)
                }
            } else if (mNestedContentView is RecyclerView) {
                mNestedRecyclerView = mNestedContentView as RecyclerView?
                mNestedRecyclerView!!.removeOnScrollListener(mRvOnScrollListener)
                mNestedRecyclerView!!.addOnScrollListener(mRvOnScrollListener)

                if (!isHeaderViewCompleteInvisible) {
                    mNestedRecyclerView!!.scrollToPosition(0)
                }
            } else if (mNestedContentView is ScrollView) {
                mNestedScrollView = mNestedContentView as ScrollView?

                if (!isHeaderViewCompleteInvisible) {
                    mNestedScrollView!!.scrollTo(mNestedScrollView!!.scrollX, 0)
                }
            } else if (mNestedContentView is WebView) {
                mNestedWebView = mNestedContentView as WebView?

                if (!isHeaderViewCompleteInvisible) {
                    mNestedWebView!!.scrollTo(mNestedWebView!!.scrollX, 0)
                }
            } else {
                mNestedNormalView = mNestedContentView
            }
        } else {
            throw IllegalStateException(BGAStickyNavLayout::class.java.simpleName + "的第三个子控件为ViewPager时，其adapter必须是FragmentPagerAdapter或者FragmentStatePagerAdapter")
        }
    }

    fun setRefreshLayout(refreshLayout: BGARefreshLayout) {
        mRefreshLayout = refreshLayout
    }

    private val mRvOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            if ((newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView
                    .SCROLL_STATE_SETTLING) && mRefreshLayout != null && mRefreshLayout!!
                    .shouldHandleRecyclerViewLoadingMore(recyclerView!!)) {
                mRefreshLayout!!.beginLoadingMore()
            }
        }
    }

    private val mLvOnScrollListener = object : AbsListView.OnScrollListener {
        override fun onScrollStateChanged(absListView: AbsListView, scrollState: Int) {
            if ((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) && mRefreshLayout != null && mRefreshLayout!!.shouldHandleAbsListViewLoadingMore(absListView)) {
                mRefreshLayout!!.beginLoadingMore()
            }
        }

        override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        }
    }

    fun shouldHandleLoadingMore(): Boolean {
        if (mRefreshLayout == null) {
            return false
        }

        if (mDirectNormalView != null) {
            return true
        }

        if (ScrollingUtil.isWebViewToBottom(mDirectWebView)) {
            return true
        }

        if (ScrollingUtil.isScrollViewToBottom(mDirectScrollView)) {
            return true
        }

        if (mDirectAbsListView != null) {
            return mRefreshLayout!!.shouldHandleAbsListViewLoadingMore(mDirectAbsListView)
        }

        if (mDirectRecyclerView != null) {
            return mRefreshLayout!!.shouldHandleRecyclerViewLoadingMore(mDirectRecyclerView!!)
        }

        if (mDirectViewPager != null) {
            if (mNestedContentView == null) {
                regetNestedContentView()
            }

            if (mNestedNormalView != null) {
                return true
            }

            if (ScrollingUtil.isWebViewToBottom(mNestedWebView)) {
                return true
            }

            if (ScrollingUtil.isScrollViewToBottom(mNestedScrollView)) {
                return true
            }

            if (mNestedAbsListView != null) {
                return mRefreshLayout!!.shouldHandleAbsListViewLoadingMore(mNestedAbsListView)
            }

            if (mNestedRecyclerView != null) {
                return mRefreshLayout!!.shouldHandleRecyclerViewLoadingMore(mNestedRecyclerView!!)
            }
        }

        return false
    }

    fun scrollToBottom() {
        ScrollingUtil.scrollToBottom(mDirectScrollView)
        ScrollingUtil.scrollToBottom(mDirectRecyclerView)
        ScrollingUtil.scrollToBottom(mDirectAbsListView)

        if (mDirectViewPager != null) {
            if (mNestedContentView == null) {
                regetNestedContentView()
            }
            ScrollingUtil.scrollToBottom(mNestedScrollView)
            ScrollingUtil.scrollToBottom(mNestedRecyclerView)
            ScrollingUtil.scrollToBottom(mNestedAbsListView)
        }
    }
}