package com.ybg.app.base.http.callback

import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.parser.OkStringParser

/**
 * Created by yangbagang on 2017/5/14.
 */
abstract class JsonCallback : OkCallback<String>(OkStringParser()){

    override fun onSuccess(code: Int, response: String) {
        println("response=$response")
        val jsonBean = JSonResultBean.fromJSON(response)
        if (jsonBean != null && jsonBean.success) {
            onJsonSuccess(jsonBean.data)
        } else {
            jsonBean?.let {
                onJsonFail(jsonBean)
            }
        }
    }

    override fun onFailure(e: Throwable) {
        onException(e)
    }

    open fun onJsonSuccess(data: String) {

    }

    open fun onJsonFail(jsonBean: JSonResultBean) {

    }

    open fun onException(e: Throwable) {
        e.printStackTrace()
    }
}