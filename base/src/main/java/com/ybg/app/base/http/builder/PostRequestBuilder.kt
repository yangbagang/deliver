package com.ybg.app.base.http.builder

import android.text.TextUtils

import com.google.gson.Gson
import com.ybg.app.base.utils.LogUtil
import com.ybg.app.base.http.OkHttpProxy
import com.ybg.app.base.http.callback.OkCallback

import java.io.IOException
import java.util.HashMap
import java.util.IdentityHashMap

import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class PostRequestBuilder : RequestBuilder() {
    private var headers: MutableMap<String, String>? = null

    fun url(url: String): PostRequestBuilder {
        this.url = url
        return this
    }

    fun setParams(params: MutableMap<String, String>): PostRequestBuilder {
        this.params = params
        return this
    }

    fun addParams(key: String, value: Any?): PostRequestBuilder {
        if (params == null) {
            params = IdentityHashMap<String, String>()
        }
        this.params!!.put(key, (value ?: "").toString())
        return this
    }

    fun addParams(map: Map<String, Any>?): PostRequestBuilder {
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

    fun headers(headers: MutableMap<String, String>): PostRequestBuilder {
        this.headers = headers
        return this
    }

    fun addHeader(key: String, values: String): PostRequestBuilder {
        if (headers == null) {
            headers = IdentityHashMap<String, String>()
        }
        headers!!.put(key, values)
        return this
    }

    fun tag(tag: Any): PostRequestBuilder {
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
        val encodingBuilder = FormBody.Builder()
        appendParams(encodingBuilder, params)
        appendHeaders(builder, headers)
        builder.post(encodingBuilder.build())
        val request = builder.build()

        if (callback is OkCallback<*>) {
            callback.onStart()
        }
        val call = OkHttpProxy.instance.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun enqueueJson(callback: Callback): Call {

        if (TextUtils.isEmpty(url)) {
            throw IllegalArgumentException("url can not be null !")
        }

        val builder = Request.Builder().url(url!!)
        if (tag != null) {
            builder.tag(tag)
        }
        builder.post(RequestBody.create(JSON_TYPE, Gson().toJson(params)))
        LogUtil.d(Gson().toJson(params))
        val request = builder.build()
        if (callback is OkCallback<*>) {
            callback.onStart()
        }
        val call = OkHttpProxy.instance.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun enqueueJson(json: String, callback: Callback): Call {

        if (TextUtils.isEmpty(url)) {
            throw IllegalArgumentException("url can not be null !")
        }

        val builder = Request.Builder().url(url!!)
        if (tag != null) {
            builder.tag(tag)
        }
        builder.post(RequestBody.create(JSON_TYPE, json))
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
        val encodingBuilder = FormBody.Builder()
        appendParams(encodingBuilder, params)
        appendHeaders(builder, headers)
        builder.post(encodingBuilder.build())
        val request = builder.build()

        val call = OkHttpProxy.instance.newCall(request)
        return call.execute()
    }

    @Throws(IOException::class)
    fun executeJson(): Response {
        if (TextUtils.isEmpty(url)) {
            throw IllegalArgumentException("url can not be null !")
        }

        val builder = Request.Builder().url(url!!)
        if (tag != null) {
            builder.tag(tag)
        }
        builder.post(RequestBody.create(JSON_TYPE, Gson().toJson(params)))
        val request = builder.build()

        val call = OkHttpProxy.instance.newCall(request)
        return call.execute()
    }

    companion object {
        private val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")
    }

}
