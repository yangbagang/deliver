package com.ybg.app.base.http.listener

import com.ybg.app.base.http.Model.Progress

interface ProgressListener {
    fun onProgress(progress: Progress)
}