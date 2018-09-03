package com.ybg.app.base.http.parser

import com.google.gson.internal.`$Gson$Types`

import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import okhttp3.Response

abstract class OkBaseJsonParser<T> : OkBaseParser<T>() {

    var mType: Type

    init {
        mType = getSuperclassTypeParameter(javaClass)
    }

    @Throws(IOException::class)
    abstract override fun parse(response: Response): T

    private fun getSuperclassTypeParameter(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameter = superclass as ParameterizedType
        return `$Gson$Types`.canonicalize(parameter.actualTypeArguments[0])
    }
}
