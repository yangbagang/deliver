package com.szcloud8.app.deliver.view.bgarefresh

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.ScrollView

object ScrollingUtil {

    fun isScrollViewOrWebViewToTop(view: View?): Boolean {
        return view != null && view.scrollY == 0
    }

    fun isAbsListViewToTop(absListView: AbsListView?): Boolean {
        if (absListView != null) {
            var firstChildTop = 0
            if (absListView.childCount > 0) {
                // 如果AdapterView的子控件数量不为0，获取第一个子控件的top
                firstChildTop = absListView.getChildAt(0).top - absListView.paddingTop
            }
            if (absListView.firstVisiblePosition == 0 && firstChildTop == 0) {
                return true
            }
        }
        return false
    }

    fun isRecyclerViewToTop(recyclerView: RecyclerView?): Boolean {
        if (recyclerView != null) {
            var firstChildTop = 0
            if (recyclerView.childCount > 0) {
                // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top

                // 解决item的topMargin不为0时不能触发下拉刷新
                val layoutParams = recyclerView.getChildAt(0).layoutParams as ViewGroup.MarginLayoutParams
                firstChildTop = recyclerView.getChildAt(0).top - layoutParams.topMargin - recyclerView.paddingTop
            }

            val manager = recyclerView.layoutManager ?: return true
            if (manager.itemCount == 0) {
                return true
            }

            if (manager is LinearLayoutManager) {
                if (manager.findFirstCompletelyVisibleItemPosition() < 1 && firstChildTop == 0) {
                    return true
                }
            } else if (manager is StaggeredGridLayoutManager) {

                val out = manager.findFirstCompletelyVisibleItemPositions(null)
                if (out[0] < 1) {
                    return true
                }
            }
        }
        return false
    }

    fun isStickyNavLayoutToTop(stickyNavLayout: BGAStickyNavLayout): Boolean {
        return isScrollViewOrWebViewToTop(stickyNavLayout) && stickyNavLayout.isContentViewToTop
    }

    fun isWebViewToBottom(webView: WebView?): Boolean {
        return webView != null && webView.contentHeight * webView.scale == (webView.scrollY + webView.measuredHeight).toFloat()
    }

    fun isScrollViewToBottom(scrollView: ScrollView?): Boolean {
        if (scrollView != null) {
            val scrollContentHeight = scrollView.scrollY + scrollView.measuredHeight - scrollView.paddingTop - scrollView.paddingBottom
            val realContentHeight = scrollView.getChildAt(0).measuredHeight
            if (scrollContentHeight == realContentHeight) {
                return true
            }
        }
        return false
    }

    fun scrollToBottom(scrollView: ScrollView?) {
        scrollView?.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    fun scrollToBottom(absListView: AbsListView?) {
        if (absListView != null) {
            if (absListView.adapter != null && absListView.adapter.count > 0) {
                absListView.post(Runnable { absListView.setSelection(absListView.adapter.count - 1) })
            }
        }
    }

    fun scrollToBottom(recyclerView: RecyclerView?) {
        if (recyclerView != null) {
            val layoutManager = recyclerView.layoutManager
            if (recyclerView.adapter != null && recyclerView.adapter.itemCount > 0) {
                layoutManager.scrollToPosition(recyclerView.adapter.itemCount - 1)
            }
        }
    }
}