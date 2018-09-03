package com.ybg.app.base.http.body


import com.ybg.app.base.http.listener.ProgressListener
import okhttp3.OkHttpClient
import okhttp3.RequestBody


object BodyWrapper {

    fun addProgressResponseListener(client: OkHttpClient, progressListener: ProgressListener): OkHttpClient {
        return client.newBuilder().addNetworkInterceptor { chain ->
            val originalResponse = chain.proceed(chain.request())
            originalResponse.newBuilder().body(ResponseProgressBody(originalResponse.body(), progressListener)).build()
        }.build()
    }

    fun addProgressRequestListener(requestBody: RequestBody, progressRequestListener: ProgressListener): RequestProgressBody {
        return RequestProgressBody(requestBody, progressRequestListener)
    }

}