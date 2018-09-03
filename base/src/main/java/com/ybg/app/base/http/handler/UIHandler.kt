package com.ybg.app.base.http.handler

import com.ybg.app.base.http.Model.Progress
import com.ybg.app.base.http.listener.UIProgressListener

class UIHandler(uiProgressListener: UIProgressListener) : ProgressHandler(uiProgressListener) {

    override fun start(listener: UIProgressListener) {
        listener.onUIStart()
    }

    override fun progress(uiProgressListener: UIProgressListener, progress: Progress) {
        uiProgressListener.onUIProgress(progress)
    }

    override fun finish(listener: UIProgressListener) {
        listener.onUIFinish()
    }
}
