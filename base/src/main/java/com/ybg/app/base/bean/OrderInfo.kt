package com.ybg.app.base.bean

data class OrderInfo(var id: Long,
                     var orderNo: String,
                     var userInfoId: Long,
                     var totalMoney: Float,
                     var realMoney: Float,
                     var createTime: String,
                     var expressNo:	String,
                     var shippingDetailAddress: String,
                     var shippingUserName: String,
                     var shippingUserMobile: String,
                     var expressCompany: String,
                     var expressCompanyCode: String,
                     var userMessage: String,
                     //var invoiceType	null
                     var invoiceTitle: String,
                     var invoiceNo: String,
                     var mallShopInfoId: Int,
                     var mallShopName: String,
                     var updateTime: String,
                     var provinceId: Int,
                     var cityId: Int,
                     var areaId: Int,
                     var provinceName: String,
                     var cityName: String,
                     var areaName: String,
                     var payType: Int,
                     var freight: Int,
                     var payStatus: Int,
                     var sendStatus: Int,
                     var apraiseStatus: Int,
                     var orderStatus: Int,
                     var payTime: String,
                     var sendTime: String,
                     var receiveTime: String,
                     var completeTime: String,
                     var expressPicture: String
                    //var shareSaleMoney	null
                    //var mallStartGroupBuyId	null
                    //var discountPrice	0
                    //var nowLessPrice	null
                    //var companyInfoId	null
                    )