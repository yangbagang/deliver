package com.ybg.app.base.utils

import android.text.TextUtils
import android.util.Log

/**
 * Created by yangbagang on 16/8/9.
 */
object LogUtil {

    val customTagPrefix = "YBG_"

    var isDebug = true

    private fun generateTag(): String {
        val caller = Throwable().stackTrace[2]
        var tag = "%s.%s(L:%d)"
        var callerClazzName = caller.getClassName()
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1)
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber())
        tag = if (TextUtils.isEmpty(customTagPrefix)) tag else customTagPrefix + ":" + tag
        return tag
    }

    fun d(content: String) {
        if (!isDebug) return
        val tag = generateTag()

        Log.d(tag, content)
    }

    fun d(content: String, tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.d(tag, content, tr)
    }

    fun e(content: String) {
        if (!isDebug) return
        val tag = generateTag()

        Log.e(tag, content)
    }

    fun e(content: String, tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.e(tag, content, tr)
    }

    fun i(content: String) {
        if (!isDebug) return
        val tag = generateTag()

        Log.i(tag, content)
    }

    fun i(tag: String, content: String) {
        if (!isDebug) return

        Log.i(tag, content)
    }

    fun i(content: String, tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.i(tag, content, tr)
    }

    fun v(content: String) {
        if (!isDebug) return
        val tag = generateTag()

        Log.v(tag, content)
    }

    fun v(content: String, tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.v(tag, content, tr)
    }

    fun w(content: String) {
        if (!isDebug) return
        val tag = generateTag()

        Log.w(tag, content)
    }

    fun w(content: String, tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.w(tag, content, tr)
    }

    fun w(tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.w(tag, tr)
    }


    fun wtf(content: String) {
        if (!isDebug) return
        val tag = generateTag()

        Log.wtf(tag, content)
    }

    fun wtf(content: String, tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.wtf(tag, content, tr)
    }

    fun wtf(tr: Throwable) {
        if (!isDebug) return
        val tag = generateTag()

        Log.wtf(tag, tr)
    }

}
