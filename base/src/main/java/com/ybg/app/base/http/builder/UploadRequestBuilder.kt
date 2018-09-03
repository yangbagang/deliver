package com.ybg.app.base.http.builder

import android.util.Pair

import com.ybg.app.base.http.OkHttpProxy
import com.ybg.app.base.http.body.BodyWrapper
import com.ybg.app.base.http.listener.UploadListener

import java.io.File
import java.io.IOException
import java.net.URLConnection
import java.util.IdentityHashMap
import java.util.concurrent.TimeUnit

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class UploadRequestBuilder : RequestBuilder() {

    private var connectTimeOut: Int = 0
    private var writeTimeOut: Int = 0
    private var readTimeOut: Int = 0
    private var file: Pair<String, File>? = null
    private var headers: MutableMap<String, String>? = null

    fun file(file: Pair<String, File>): UploadRequestBuilder {
        this.file = file
        return this
    }

    fun url(url: String): UploadRequestBuilder {
        this.url = url
        return this
    }

    fun setParams(params: MutableMap<String, String>): UploadRequestBuilder {
        this.params = params
        return this
    }

    fun addParams(key: String, value: String): UploadRequestBuilder {
        if (params == null) {
            params = IdentityHashMap()
        }
        params!!.put(key, value)
        return this
    }

    fun headers(headers: MutableMap<String, String>): UploadRequestBuilder {
        this.headers = headers
        return this
    }

    fun addHeader(key: String, values: String): UploadRequestBuilder {
        if (headers == null) {
            headers = IdentityHashMap<String, String>()
        }
        headers!!.put(key, values)
        return this
    }

    fun tag(tag: Any): UploadRequestBuilder {
        this.tag = tag
        return this
    }

    /**
     * @param connectTimeOut unit is minute
     * *
     * @return
     */
    fun setConnectTimeOut(connectTimeOut: Int): UploadRequestBuilder {
        this.connectTimeOut = connectTimeOut
        return this
    }

    /**
     * @param writeTimeOut unit is minute
     * *
     * @return
     */
    fun setWriteTimeOut(writeTimeOut: Int): UploadRequestBuilder {
        this.writeTimeOut = writeTimeOut
        return this
    }

    /**
     * @param readTimeOut unit is minute
     * *
     * @return
     */
    fun setReadTimeOut(readTimeOut: Int): UploadRequestBuilder {
        this.readTimeOut = readTimeOut
        return this
    }

    fun start(uploadListener: UploadListener): Call {

        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        addParams(multipartBuilder, params)
        addFiles(multipartBuilder, file!!)

        val builder = Request.Builder()
        appendHeaders(builder, headers)
        val request = builder.url(url!!).post(BodyWrapper.addProgressRequestListener(multipartBuilder.build(), uploadListener)).build()

        val clientBuilder = OkHttpProxy.instance.newBuilder()

        if (connectTimeOut > 0) {
            clientBuilder.connectTimeout(connectTimeOut.toLong(), TimeUnit.MINUTES)
        } else {
            clientBuilder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.MINUTES)
        }

        if (writeTimeOut > 0) {
            clientBuilder.writeTimeout(writeTimeOut.toLong(), TimeUnit.MINUTES)
        } else {
            clientBuilder.writeTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.MINUTES)
        }

        if (readTimeOut > 0) {
            clientBuilder.readTimeout(readTimeOut.toLong(), TimeUnit.MINUTES)
        } else {
            clientBuilder.readTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.MINUTES)
        }

        val clone = clientBuilder.build()
        val call = clone.newCall(request)
        call.enqueue(uploadListener)
        return call
    }

    internal override fun enqueue(callback: Callback): Call? {
        return null
    }

    @Throws(IOException::class)
    internal override fun execute(): Response? {
        return null
    }

    companion object {

        private val DEFAULT_TIME_OUT = 30

        private fun addParams(builder: MultipartBody.Builder, params: Map<String, String>?) {
            if (params != null && !params.isEmpty()) {
                for (key in params.keys) {
                    builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                            RequestBody.create(null, params[key]))
                }
            }
        }

        private fun addFiles(builder: MultipartBody.Builder, vararg files: Pair<String, File>) {
            if (files != null) {
                var fileBody: RequestBody
                for (i in files.indices) {
                    val filePair = files[i]
                    val fileKeyName = filePair.first
                    val file = filePair.second
                    val fileName = file.name
                    fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file)
                    builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"$fileKeyName\"; filename=\"$fileName\""),
                            fileBody)
                }
            } else {
                throw IllegalArgumentException("File can not be null")
            }
        }

        private fun guessMimeType(path: String): String {
            val fileNameMap = URLConnection.getFileNameMap()
            var contentTypeFor: String? = fileNameMap.getContentTypeFor(path)
            if (contentTypeFor == null) {
                contentTypeFor = "application/octet-stream"
            }
            return contentTypeFor
        }
    }

}
