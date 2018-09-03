package com.ybg.app.base.http.parser

import android.os.Build

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.IOException
import java.lang.reflect.Modifier

import okhttp3.Response

abstract class OkJsonParser<T> : OkBaseJsonParser<T>() {

    protected var mGson: Gson

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val gsonBuilder = GsonBuilder().excludeFieldsWithModifiers(
                    Modifier.FINAL,
                    Modifier.TRANSIENT,
                    Modifier.STATIC)
            mGson = gsonBuilder.create()
        } else {
            mGson = Gson()
        }
    }

    @Throws(IOException::class)
    public override fun parse(response: Response): T {
        val body = response.body().string()
        return mGson.fromJson<T>(body, mType)
    }
}