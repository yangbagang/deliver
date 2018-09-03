package com.ybg.app.base.utils

import android.os.AsyncTask

/**
 * Created by ybg on 17-2-21.
 */
object AsyncTaskUtil {

    fun startTask(task: () -> Unit, callback: () -> Unit) {
        Thread {
            task()
            callback()
        }.start()
    }
}