package com.ybg.app.base.http.callback

import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.Model.Progress
import com.ybg.app.base.http.listener.UploadListener
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

/**
 * Created by yangbagang on 2017/12/28.
 */
abstract class UploadCallback : UploadListener() {

    override fun onResponse(call: Call?, response: Response?) {
        response?.let { onSuccess(response) }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        e?.let { onFailure(it) }
    }

    override fun onSuccess(response: Response) {
        val json = JSonResultBean.fromJSON(response.body().string())
        if (json != null && json.success) {
            onJsonSuccess(json.data)
        } else {
            json?.let { onJsonFailure(json.message) }
        }
    }

    override fun onFailure(e: Exception) {

    }

    override fun onUIProgress(progress: Progress) {

    }

    abstract fun onJsonFailure(message: String)

    abstract fun onJsonSuccess(fid: String)
}