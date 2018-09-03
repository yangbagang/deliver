package com.ybg.app.base.utils

import android.app.Application
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.ybg.app.base.R

/**
 * ToastUtil
 */
object ToastUtil {

    fun show(application: Application, resId: Int) {
        show(application, application.resources.getText(resId), Toast.LENGTH_SHORT)
    }

    fun show(application: Application, resId: Int, duration: Int) {
        show(application, application.resources.getText(resId), duration)
    }

    fun toast(application: Application, text: CharSequence) {
        show(application, text, Toast.LENGTH_SHORT)
    }

    @JvmOverloads fun show(application: Application, text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val view = LayoutInflater.from(application).inflate(R.layout.layout_toast_center, null)
        val messageTv = view.findViewById(R.id.tv_title_toast) as TextView
        messageTv.text = text
        val toast = Toast(application)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.duration = duration
        toast.view = view
        toast.show()
    }

}
