package com.ybg.app.base.http.builder

import java.io.IOException

import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response

abstract class RequestBuilder {

    protected var url: String? = null
    protected var params: MutableMap<String, String>? = null
    protected var tag: Any? = null

    internal abstract fun enqueue(callback: Callback): Call?

    @Throws(IOException::class)
    internal abstract fun execute(): Response?

    protected fun appendHeaders(builder: Request.Builder, headers: Map<String, String>?) {
        val headerBuilder = Headers.Builder()
        if (headers == null || headers.isEmpty()) return

        for (key in headers.keys) {
            headerBuilder.add(key, headers[key])
        }
        builder.headers(headerBuilder.build())
    }

    protected fun appendParams(builder: FormBody.Builder, params: Map<String, String>?) {

        if (params != null && !params.isEmpty()) {
            for (key in params.keys) {
                builder.add(key, params[key])
            }
        }
    }

    protected fun appendParams(url: String, params: Map<String, String>?): String {
        var sb = StringBuilder()
        sb.append(url + "?")
        if (params != null && !params.isEmpty()) {
            for (key in params.keys) {
                sb.append(key).append("=").append(params[key]).append("&")
            }
        }

        sb = sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

}
