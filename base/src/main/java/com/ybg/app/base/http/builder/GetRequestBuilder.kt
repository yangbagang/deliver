package com.ybg.app.base.http.builder

import android.text.TextUtils
import com.ybg.app.base.http.OkHttpProxy
import com.ybg.app.base.http.callback.OkCallback
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

class GetRequestBuilder : RequestBuilder() {

    fun url(url: String): GetRequestBuilder {
        this.url = url
        return this
    }

    fun setParams(params: MutableMap<String, String>): GetRequestBuilder {
        this.params = params
        return this
    }

    fun addParams(key: String, value: Any): GetRequestBuilder {
        if (params == null) {
            params = HashMap<String, String>()
        }
        this.params!!.put(key, value.toString())
        return this
    }

    fun addParams(map: Map<String, Any>?): GetRequestBuilder {
        if (map == null) {
            return this
        }
        if (params == null) {
            params = HashMap<String, String>()
        }
        for (key in map.keys) {
            params!!.put(key, map[key].toString())
        }
        return this
    }

    fun tag(tag: Any): GetRequestBuilder {
        this.tag = tag
        return this
    }

    public override fun enqueue(callback: Callback): Call {

        if (TextUtils.isEmpty(url)) {
            throw IllegalArgumentException("url can not be null !")
        }

        val builder = Request.Builder().url(url!!)

        if (tag != null) {
            builder.tag(tag)
        }

        if (params != null && params!!.isNotEmpty()) {
            url = appendParams(url!!, params)
        }

        val request = builder.build()

        if (callback is OkCallback<*>) {
            callback.onStart()
        }

        val call = OkHttpProxy.instance.newCall(request)
        call.enqueue(callback)
        return call
    }

    @Throws(IOException::class)
    public override fun execute(): Response {

        if (TextUtils.isEmpty(url)) {
            throw IllegalArgumentException("url can not be null !")
        }

        val builder = Request.Builder().url(url!!)

        if (tag != null) {
            builder.tag(tag)
        }

        if (params != null && params!!.isNotEmpty()) {
            url = appendParams(url!!, params)
        }

        val request = builder.build()

        val call = OkHttpProxy.instance.newCall(request)
        return call.execute()
    }

}
