package com.ybg.app.base.bean

data class DeliverInfo(var id: Long = 0L,
                       var deliverId: Long = 0L,
                       var deliverNo: String,
                       var orderMoney: Float = 0F,
                       var orderNo: String,
                       var orderTime: String = "",
                       var userName: String = "",
                       var phone: String = "",
                       var address: String = "",
                       var orderMemo: String = "",
                       var deliverStatus: Int = 0,
                       var status: Int = 0)