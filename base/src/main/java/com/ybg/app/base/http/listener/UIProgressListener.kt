package com.ybg.app.base.http.listener

import com.ybg.app.base.http.Model.Progress

interface UIProgressListener {

    fun onUIProgress(progress: Progress)

    fun onUIStart()

    fun onUIFinish()
}
