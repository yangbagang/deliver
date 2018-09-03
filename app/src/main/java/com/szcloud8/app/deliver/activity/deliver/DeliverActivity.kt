package com.szcloud8.app.deliver.activity.deliver

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.app.ShowApplication
import com.szcloud8.app.deliver.http.SendRequest
import com.ybg.app.base.bean.*
import com.ybg.app.base.constants.IntentExtra
import com.ybg.app.base.constants.MessageEvent
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.ToastUtil

import kotlinx.android.synthetic.main.activity_deliver.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class DeliverActivity : AppCompatActivity() {

    private val showApplication = ShowApplication.instance!!

    private var deliverInfoId = 0L
    private var deliverStatus = -1
    private var signId = ""
    private var orderNo = ""
    private var receiverUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deliver)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent != null) {
            deliverInfoId = intent.extras.getLong("deliverId", 0L)
        }

        if (deliverInfoId != 0L) {
            getDeliverInfo()
        }

        btn_deliver_action.setOnClickListener {
            updateDeliverStatus()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentExtra.RequestCode.REQUEST_CODE_SIGN && resultCode == IntentExtra
                        .ResultCode.RESULT_CODE_SIGN && data != null) {
            signId = data.extras.getString("signFid")
            btn_deliver_action.text = "完成配送"
        }
    }

    private fun getDeliverInfo() {
        SendRequest.getDeliverInfo(this, deliverInfoId, object: JsonCallback() {
            override fun onJsonSuccess(data: String) {
                //println(data)
                val json = JSONObject(data)
                val gson = Gson()
                //准备数据
                val deliverString = json.getString("deliverInfo")
                val historyString = json.getString("deliverHistory")
                val orderString = json.getString("orderInfo")
                val goodsString = json.getString("goodsList")
                //加载配送信息
                val deliverInfo = gson.fromJson<DeliverInfo>(deliverString,
                        object : TypeToken<DeliverInfo>(){}.type)
                deliverStatus = deliverInfo.status
                runOnUiThread {
                    when(deliverStatus) {
                        0 -> btn_deliver_action.text = "开始配送"
                        1 -> btn_deliver_action.text = "客户签名"
                        2 -> btn_deliver_action.text = "关闭"
                    }
                }
                //加载订单信息
                val orderInfo = gson.fromJson<OrderInfo>(orderString, object :
                        TypeToken<OrderInfo>(){}.type)
                runOnUiThread {
                    tv_order_no.text = "订单号：${orderInfo.orderNo}"
                    tv_order_user.text = "联系人：${orderInfo.shippingUserName}"
                    tv_order_phone.text = "手机号：${orderInfo.shippingUserMobile}"
                    tv_order_address.text = "配送地址：${orderInfo.shippingDetailAddress}"
                }
                //加载商品列表
                val goodsList = gson.fromJson<List<GoodsInfo>>(goodsString, object :
                        TypeToken<List<GoodsInfo>>(){}.type)
                runOnUiThread {
                    ll_goods_list.removeAllViews()
                    goodsList.forEach { goodsInfo ->
                        //ll_goods_list.addView(getGoodsItem(goodsInfo))
                        getGoodsItem(goodsInfo)
                    }
                }
                //加载历史记录
                if (deliverStatus == 2) {
                    val historyList = gson.fromJson<List<DeliverHistory>>(historyString, object :
                            TypeToken<List<DeliverHistory>>(){}.type)
                    runOnUiThread {
                        ll_history_list.removeAllViews()
                        historyList.forEach { historyItem ->
                            //ll_history_list.addView(getHistoryItem(historyItem))
                            getHistoryItem(historyItem)
                        }
                    }
                }
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                println("errorMsg: ${jsonBean.message}")
                if (jsonBean.message.contains("重新登录")) {
                    EventBus.getDefault().post(MessageEvent(MessageEvent.MESSAGE_USER_LOGOUT))
                    finish()
                } else {
                    ToastUtil.show(showApplication, jsonBean.message)
                }
            }
        })
    }

    private fun updateDeliverStatus() {
        if (deliverStatus == -1) {
            ToastUtil.show(showApplication, "信息未加载，请稍后再试。")
        } else if (deliverStatus == 0) {
            assignDeliver()
        } else if (deliverStatus == 1) {
            if (signId == "") {
                startActivityForResult(Intent(this@DeliverActivity, SignActivity::class.java),
                        IntentExtra.RequestCode.REQUEST_CODE_SIGN)
            } else {
                finishDeliver()
            }
        } else {
            finish()
        }
    }

    private fun assignDeliver() {
        SendRequest.assignDeliver(this, showApplication.token, deliverInfoId, object :
                JsonCallback() {
            override fun onJsonSuccess(data: String) {
                deliverStatus = 1
                runOnUiThread {
                    btn_deliver_action.text = "客户签名"
                }
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                println("errorMsg: ${jsonBean.message}")
                if (jsonBean.message.contains("重新登录")) {
                    EventBus.getDefault().post(MessageEvent(MessageEvent.MESSAGE_USER_LOGOUT))
                    finish()
                } else {
                    ToastUtil.show(showApplication, jsonBean.message)
                }
            }
        })
    }

    private fun finishDeliver() {
        SendRequest.finishDeliver(this, showApplication.token, deliverInfoId, receiverUser,
                signId, orderNo, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                deliverStatus = 2
                runOnUiThread {
                    btn_deliver_action.text = "关闭"
                }
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                println("errorMsg: ${jsonBean.message}")
                if (jsonBean.message.contains("重新登录")) {
                    EventBus.getDefault().post(MessageEvent(MessageEvent.MESSAGE_USER_LOGOUT))
                    finish()
                } else {
                    ToastUtil.show(showApplication, jsonBean.message)
                }
            }
        })
    }

    private fun getGoodsItem(goodsInfo: GoodsInfo) : View {
        val view = LayoutInflater.from(this).inflate(R.layout.item_goods, ll_goods_list)
        val nameText = view.findViewById<TextView>(R.id.tv_goods_name)
        val unitText = view.findViewById<TextView>(R.id.tv_goods_unit)
        val numText = view.findViewById<TextView>(R.id.tv_goods_num)
        val priceText = view.findViewById<TextView>(R.id.tv_goods_price)

        nameText.text = "商品名称：${goodsInfo.goodsName}"
        unitText.text = "规格：${goodsInfo.specificationsName}"
        numText.text = "数量：${goodsInfo.num}"
        priceText.text = "单价：${goodsInfo.price}"
        return view
    }

    private fun getHistoryItem(deliverHistory: DeliverHistory) : View {
        val view = LayoutInflater.from(this).inflate(R.layout.item_history, ll_goods_list)
        val timeText = view.findViewById<TextView>(R.id.tv_time)
        val userText = view.findViewById<TextView>(R.id.tv_user)
        val contentText = view.findViewById<TextView>(R.id.tv_content)

        timeText.text = deliverHistory.createTime
        userText.text = deliverHistory.creator
        contentText.text = deliverHistory.operationContent
        return view
    }
}
