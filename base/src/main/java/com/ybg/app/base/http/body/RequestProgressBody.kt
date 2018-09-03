package com.ybg.app.base.http.body

import com.ybg.app.base.http.Model.Progress
import com.ybg.app.base.http.listener.ProgressListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

class RequestProgressBody(private val requestBody: RequestBody, private val progressListener: ProgressListener?) : RequestBody() {
    private var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink))
        }
        requestBody.writeTo(bufferedSink)
        bufferedSink!!.flush()
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            internal var bytesWritten = 0L
            internal var contentLength = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    contentLength = contentLength()
                }
                bytesWritten += byteCount
                progressListener?.onProgress(Progress(bytesWritten, contentLength, bytesWritten == contentLength))
            }
        }
    }

}