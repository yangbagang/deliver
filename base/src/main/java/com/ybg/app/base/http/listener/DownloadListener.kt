package com.ybg.app.base.http.listener

import android.os.Handler

import com.ybg.app.base.http.Model.Progress
import com.ybg.app.base.http.handler.ProgressHandler
import com.ybg.app.base.http.handler.UIHandler

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response

abstract class DownloadListener(private val mDestFileDir: String, private val mDestFileName: String) : ProgressListener, Callback, UIProgressListener {

    private val mHandler = UIHandler(this)
    private var isFirst = true

    fun onResponse(response: Response) {

        var file: File? = null
        try {
            file = saveFile(response)
        } catch (e: IOException) {
            mHandler.post { onFailure(e) }
        }

        val newFile = file!!
        mHandler.post { onSuccess(newFile) }
    }

    fun onFailure(request: Request, e: IOException) {
        mHandler.post { onFailure(e) }
    }

    abstract fun onSuccess(file: File)

    abstract fun onFailure(e: Exception)

    @Throws(IOException::class)
    fun saveFile(response: Response): File {
        var `is`: InputStream? = null
        val buf = ByteArray(2048)
        var len: Int
        var fos: FileOutputStream? = null
        try {
            `is` = response.body().byteStream()
            val dir = File(mDestFileDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, mDestFileName)
            fos = FileOutputStream(file)
            len = `is`!!.read(buf)
            while (len != -1) {
                fos.write(buf, 0, len)
                len = `is`!!.read(buf)
            }
            fos.flush()
            return file
        } finally {
            try {
                if (`is` != null) `is`.close()
            } catch (e: IOException) {
            }

            try {
                if (fos != null) fos.close()
            } catch (e: IOException) {
            }

        }
    }

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
