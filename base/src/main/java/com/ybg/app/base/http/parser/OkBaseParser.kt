package com.ybg.app.base.http.parser

import java.io.IOException

import okhttp3.Response

abstract class OkBaseParser<T> {

    var code: Int = 0
        protected set

    @Throws(IOException::class)
    protected abstract fun parse(response: Response): T

    @Throws(IOException::class)
    fun parseResponse(response: Response): T {
        code = response.code()
        return parse(response)
    }

}
