package com.ybg.app.base.http.listener

import android.os.Handler

import com.ybg.app.base.http.Model.Progress
import com.ybg.app.base.http.handler.ProgressHandler
import com.ybg.app.base.http.handler.UIHandler

import java.io.IOException

import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response

abstract class UploadListener : ProgressListener, Callback, UIProgressListener {

    private val mHandler = UIHandler(this)
    private var isFirst = true

    fun onResponse(response: Response) {
        mHandler.post { onSuccess(response) }
    }

    fun onFailure(request: Request, e: IOException) {
        mHandler.post { onFailure(e) }
    }

    abstract fun onSuccess(response: Response)

    abstract fun onFailure(e: Exception)

    override fun onProgress(progress: Progress) {

        if (!isFirst) {
            isFirst = true
            mHandler.obtainMessage(ProgressHandler.START, progress).sendToTarget()
        }

        mHandler.obtainMessage(ProgressHandler.UPDATE,
                progress).sendToTarget()

        if (progress.isFinish) {
            mHandler.obtainMessage(ProgressHandler.FINISH,
                    progress).sendToTarget()
        }
    }

    abstract override fun onUIProgress(progress: Progress)

    override fun onUIStart() {
    }

    override fun onUIFinish() {
    }
}
