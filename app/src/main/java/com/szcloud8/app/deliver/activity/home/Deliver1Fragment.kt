package com.szcloud8.app.deliver.activity.home

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import android.widget.EditText
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.adapter.DeliverItemAdapter
import com.szcloud8.app.deliver.view.bgarefresh.BGANormalRefreshViewHolder
import com.ybg.app.base.constants.MessageEvent
import com.ybg.app.base.decoration.SpaceItemDecoration
import org.greenrobot.eventbus.Subscribe

class Deliver1Fragment : BaseFragment() {

    override var status = 1

    override fun setContentViewId(): Int = R.layout.fragment_deliver1

    private lateinit var orderNoEdit: EditText
    private lateinit var searchButton: Button

    override fun setUpView() {
        mFreshLayout = mRootView!!.findViewById(R.id.rl_fresh_layout)
        mShowRecyclerView = mRootView!!.findViewById(R.id.rv_deliver_list)

        orderNoEdit = mRootView!!.findViewById(R.id.edit_order_no)
        searchButton = mRootView!!.findViewById(R.id.btn_search)

        mFreshLayout.setRefreshViewHolder(BGANormalRefreshViewHolder(mContext!!, true))
        mFreshLayout.setDelegate(mDelegate)
    }

    override fun init() {
        deliverAdapter = DeliverItemAdapter(mContext!!)
        deliverAdapter.setDataList(deliverInfoList)
        mShowRecyclerView.adapter = deliverAdapter

        val layoutManager = LinearLayoutManager.VERTICAL
        mShowRecyclerView.layoutManager = LinearLayoutManager(mContext!!, layoutManager, false)
        mShowRecyclerView.itemAnimator = DefaultItemAnimator()
        mShowRecyclerView.addItemDecoration(SpaceItemDecoration(2))

        if (showApplication.hasLogin()) {
            mFreshLayout.beginRefreshing()
        }

        searchButton.setOnClickListener {
            orderNo = orderNoEdit.text.toString()
            pageNum = 1
            loadDeliverInfoList()
        }
    }

    override fun onStart() {
        super.onStart()
        mEventBus?.register(this)
    }

    override fun onStop() {
        mEventBus?.unregister(this)
        super.onStop()
    }

    @Subscribe
    fun onEvent(event: MessageEvent) {

    }

}