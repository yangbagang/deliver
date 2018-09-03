package com.ybg.app.base.bean

data class DeliverHistory(var id: Long, var createTime: String, var creator: String,
                          var operationContent: String, var orderNo: String, var deliverNo: String)