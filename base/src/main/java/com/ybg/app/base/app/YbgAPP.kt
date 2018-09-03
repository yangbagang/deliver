package com.ybg.app.base.app

import android.app.Activity
import android.app.Application
import com.ybg.app.base.constants.AppConstant
import com.ybg.app.base.constants.AppConstant.PREFERENCE_FILE_NAME
import com.ybg.app.base.picasso.OkHttp3Downloader
import com.ybg.app.base.picasso.Picasso
import com.ybg.app.base.preference.AppPreferences
import java.io.File

/**
 * Created by yangbagang on 16/8/3.
 */
open class YbgAPP : Application() {

    protected val preference: AppPreferences = AppPreferences.instance

    var longitude = 0.0

    var latitude = 0.0

    fun hasLogin(): Boolean {
        return "" != token
    }

    var token: String
        get() = preference.getString("token", "")
        set(token) = preference.setString("token", token)

    override fun onCreate() {
        super.onCreate()

        if (!preference.hasInit()) {
            preference.init(getSharedPreferences(
                    PREFERENCE_FILE_NAME, Activity.MODE_PRIVATE))
        }

        initImgTool()
    }

    override fun onTerminate() {
        super.onTerminate()

        container.clear()
    }

    fun checkNeedLogin(message: String) = message.contains("重新登录")

    private fun initImgTool() {
        //初始化picasso
        val picasso = Picasso.Builder(applicationContext).downloader(OkHttp3Downloader(File(AppConstant.IMAGE_CACHE_PATH))).build()
        Picasso.setSingletonInstance(picasso)
    }

    private val container = mutableMapOf<String, Any>()

    fun storeData(key: String, data: Any) {
        container.put(key, data)
    }

    fun removeData(key: String) {
        container.remove(key)
    }

    fun retrieveData(key: String): Any? {
        return container.get(key)
    }

    fun checkData(key: String): Boolean {
        return container.containsKey(key)
    }

}
