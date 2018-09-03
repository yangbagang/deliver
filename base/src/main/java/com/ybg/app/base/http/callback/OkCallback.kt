package com.ybg.app.base.http.callback

import android.os.Handler
import android.os.Looper
import com.ybg.app.base.http.parser.OkBaseParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

abstract class OkCallback<T>(private val mParser: OkBaseParser<T>?) : Callback {

    init {
        if (mParser == null) {
            throw IllegalArgumentException("Parser can't be null")
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        mHandler.post { onFailure(e) }
    }

    override fun onResponse(call: Call, response: Response) {
        val code = mParser!!.code
        try {
            val t = mParser.parseResponse(response)
            if (response.isSuccessful && t != null) {
                mHandler.post { onSuccess(code, t) }
            } else {
                mHandler.post { onFailure(Exception(response.toString())) }
            }
        } catch (e: Exception) {
            mHandler.post { onFailure(e) }
        }

    }

    abstract fun onSuccess(code: Int, response: T)

    abstract fun onFailure(e: Throwable)

    fun onStart() {
    }

    companion object {

        private val mHandler = Handler(Looper.getMainLooper())
    }

}