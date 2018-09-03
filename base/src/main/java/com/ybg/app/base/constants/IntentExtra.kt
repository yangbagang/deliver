package com.ybg.app.base.constants

class IntentExtra {

    object RequestCode {
        val REQUEST_CODE_SIGN = 1000//签名请求
    }

    object ResultCode {
        val RESULT_CODE_SIGN = 1001//签名响应
    }

    companion object {

        val EXTRA_PASSWORD = "extra_password"
        val EXTRA_MOBILE = "extra_mobile"
        val EXTRA_PHOTO_RESULT = "extra_photo_result"
        val PICTURE_LIST = "picture_list"
    }
}
