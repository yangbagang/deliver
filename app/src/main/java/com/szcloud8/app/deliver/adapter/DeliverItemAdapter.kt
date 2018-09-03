package com.szcloud8.app.deliver.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.TextView
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.activity.deliver.DeliverActivity
import com.ybg.app.base.bean.DeliverInfo


class DeliverItemAdapter(var context: Context) : RecyclerBaseAdapter<DeliverInfo>(context) {

    private var orderNoText: TextView? = null
    private var orderTimeText: TextView? = null
    private var orderMoneyText: TextView? = null
    private var orderUserText: TextView? = null
    private var orderPhoneText: TextView? = null
    private var orderAddressText: TextView? = null
    private var orderMemoText: TextView? = null
    private var deliverActionBtn:  Button? = null

    override val rootResource: Int
        get() = R.layout.item_deliver

    override fun getView(viewHolder: BaseViewHolder, item: DeliverInfo?, position: Int) {
        orderNoText = viewHolder.getView(R.id.tv_order_no)
        orderTimeText = viewHolder.getView(R.id.tv_order_time)
        orderMoneyText = viewHolder.getView(R.id.tv_order_money)
        orderUserText = viewHolder.getView(R.id.tv_order_user)
        orderPhoneText = viewHolder.getView(R.id.tv_order_phone)
        orderAddressText = viewHolder.getView(R.id.tv_order_address)
        orderMemoText = viewHolder.getView(R.id.tv_order_memo)
        deliverActionBtn = viewHolder.getView(R.id.btn_order_action)

        if (item != null) {
            orderNoText?.text = item.orderNo
            orderTimeText?.text = item.orderTime
            orderMoneyText?.text = "${item.orderMoney}元"
            orderUserText?.text = "联系人：${item.userName}"
            orderPhoneText?.text = "联系电话：${item.phone}"
            orderAddressText?.text = "配送地址：${item.address}"
            orderMemoText?.text = "留言：${item.orderMemo}"
            when(item.deliverStatus) {
                0 -> deliverActionBtn?.text = "开始配送"
                1 -> deliverActionBtn?.text = "送达"
                2 -> deliverActionBtn?.text = "查看详情"
            }

            //事件
            orderPhoneText?.setOnClickListener {
                if (item.deliverStatus < 2) {
                    // 跳转到拨号界面
                    val intent = Intent(Intent.ACTION_DIAL)
                    val data = Uri.parse("tel:${item.phone}")
                    intent.data = data
                    context.startActivity(intent)
                }
            }
            deliverActionBtn?.setOnClickListener {
                // 转到详情界面
                val intent = Intent(context, DeliverActivity::class.java)
                intent.putExtra("deliverId", item.deliverId)
                context.startActivity(intent)
            }
        }
    }

}