package com.szcloud8.app.deliver.activity.home

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.szcloud8.app.deliver.adapter.DeliverItemAdapter
import com.szcloud8.app.deliver.app.ShowApplication
import com.szcloud8.app.deliver.http.SendRequest
import com.szcloud8.app.deliver.view.bgarefresh.BGARefreshLayout
import com.ybg.app.base.bean.DeliverInfo
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.constants.MessageEvent
import com.ybg.app.base.http.OkHttpProxy
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.ToastUtil
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment : Fragment() {

    protected var mContext: Activity? = null
    protected var mGson: Gson? = null
    protected var mRootView: View? = null
    protected var mEventBus: EventBus? = null

    protected val showApplication = ShowApplication.instance!!

    protected val deliverInfoList = ArrayList<DeliverInfo>()

    protected var hasMore = true
    protected var pageNum = 1
    protected var pageSize = 10

    protected val TYPE_REFRESH = 0//下拉刷新
    protected val TYPE_LOADMORE = 1//上拉加载

    protected var orderNo = ""
    protected open var status = 0

    protected lateinit var mFreshLayout: BGARefreshLayout
    protected lateinit var mShowRecyclerView: RecyclerView
    protected lateinit var deliverAdapter: DeliverItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mGson = GsonBuilder().serializeNulls().create()
        mEventBus = EventBus.getDefault()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(setContentViewId(), container, false)

        setUpView()
        init()
        return mRootView
    }

    override fun onDestroyView() {
        mContext?.let { OkHttpProxy.cancel(it) }
        super.onDestroyView()
    }

    /**
     * 设置布局文件
     */
    @LayoutRes
    protected abstract fun setContentViewId(): Int

    /**
     * 初始化View的方法
     */
    protected abstract fun setUpView()

    /**
     * 初始化方法
     */
    protected abstract fun init()

    protected fun loadDeliverInfoList() {
        SendRequest.getDeliverList(activity!!, showApplication.token, pageNum, pageSize, status,
                orderNo, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                if (pageNum == 1) {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_REFRESH
                    message.obj = data
                    mShowHandler.sendMessage(message)
                } else {
                    val message = mShowHandler.obtainMessage()
                    message.what = TYPE_LOADMORE
                    message.obj = data
                    mShowHandler.sendMessage(message)
                }
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                if (jsonBean.message.contains("重新登录")) {
                    EventBus.getDefault().post(MessageEvent(MessageEvent.MESSAGE_USER_LOGOUT))
                }
            }
        })
    }

    /**
     * 模拟请求网络数据
     */
    protected val mShowHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val gson = Gson()
            val list = gson.fromJson<List<DeliverInfo>>(msg.obj.toString(), object
                : TypeToken<List<DeliverInfo>>() {}.type)

            hasMore = list.size == pageSize

            when (msg.what) {
                TYPE_REFRESH -> {
                    mFreshLayout.endRefreshing()
                    deliverInfoList.clear()
                    deliverInfoList.addAll(list)
                }
                TYPE_LOADMORE -> {
                    mFreshLayout.endLoadingMore()
                    deliverInfoList.addAll(list)
                }
            }
            deliverAdapter.setDataList(deliverInfoList)
            deliverAdapter.notifyDataSetChanged()
        }
    }


    /**
     * 监听 刷新或者上拉
     */
    protected val mDelegate = object : BGARefreshLayout.BGARefreshLayoutDelegate {
        override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {
            pageNum = 1
            loadDeliverInfoList()
        }

        override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
            if (hasMore) {
                pageNum += 1
                loadDeliverInfoList()
            } else {
                ToastUtil.show(showApplication, "没有更多数据!")
                return false//不显示更多加载
            }
            return true
        }
    }

}