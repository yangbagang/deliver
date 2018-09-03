package com.szcloud8.app.deliver.http

import android.content.Context
import android.util.Pair
import com.ybg.app.base.http.OkHttpProxy
import com.ybg.app.base.http.callback.OkCallback
import com.ybg.app.base.http.listener.UploadListener
import java.io.File

object SendRequest {

    //第一部分，用户操作

    /**
     * 1.1 用户登录接口
     *
     * @param mobile 手机号
     * @param password 密码
     */
    fun userLogin(tag: Context, mobile: String, password: String, callback: OkCallback<*>) {
        val params = mapOf("mobile" to mobile, "password" to password)
        OkHttpProxy.post(HttpUrl.userLoginUrl, tag, params, callback)
    }

    fun userLogout(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf("token" to token)
        OkHttpProxy.post(HttpUrl.userLogoutUrl, tag, params, callback)
    }

    fun getUserInfo(tag: Context, token: String, callback: OkCallback<*>) {
        val params = mapOf("token" to token)
        OkHttpProxy.post(HttpUrl.userInfoUrl, tag, params, callback)
    }

    /**
     * 1.2 修改密码
     *
     * @param token
     */
    fun updatePassword(tag: Context, token: String, oldPassword: String, newPassword: String,
                       callback: OkCallback<*>) {
        val params = mapOf("token" to token, "oldPassword" to oldPassword, "newPassword" to newPassword)
        OkHttpProxy.post(HttpUrl.updatePasswordUrl, tag, params, callback)
    }

    fun updateAppToken(tag: Context, userToken: String, appToken: String, callback: OkCallback<*>) {
        val params = mapOf("userToken" to userToken, "appToken" to appToken)
        OkHttpProxy.post(HttpUrl.updateClientIdUrl, tag, params, callback)
    }


    //第二部分，配送操作

    fun getDeliverList(tag: Context, token: String, pageNum: Int, pageSize: Int, status: Int,
                       orderNo: String, callback: OkCallback<*>) {
        val params = mapOf("token" to token, "page" to "$pageNum",
                "limit" to "$pageSize", "status" to "$status", "orderNo" to orderNo)
        OkHttpProxy.post(HttpUrl.deliverListUrl, tag, params, callback)
    }

    fun getDeliverCount(tag: Context, token: String, status: Int, callback: OkCallback<*>) {
        val params = mapOf("token" to token, "status" to "$status")
        OkHttpProxy.post(HttpUrl.deliverCountUrl, tag, params, callback)
    }

    fun assignDeliver(tag: Context, token: String, deliverInfoId: Long, callback: OkCallback<*>) {
        val params = mapOf("token" to token, "deliverInfoId" to "$deliverInfoId")
        OkHttpProxy.post(HttpUrl.deliverAssignUrl, tag, params, callback)
    }

    fun unAssignDeliver(tag: Context, token: String, deliverInfoId: Long, callback: OkCallback<*>) {
        val params = mapOf("token" to token, "deliverInfoId" to "$deliverInfoId")
        OkHttpProxy.post(HttpUrl.deliverUnAssignUrl, tag, params, callback)
    }

    fun getDeliverInfo(tag: Context, deliverInfoId: Long, callback: OkCallback<*>) {
        val params = mapOf("deliverInfoId" to deliverInfoId)
        OkHttpProxy.post(HttpUrl.deliverDetailUrl, tag, params, callback)
    }

    fun finishDeliver(tag: Context, token: String, deliverInfoId: Long, receiverUser: String,
                      signId: String, orderNo: String, callback: OkCallback<*>) {
        val params = mapOf("token" to token, "deliverInfoId" to "$deliverInfoId", "receiverUser"
                to receiverUser, "signId" to signId, "orderNo" to orderNo)
        OkHttpProxy.post(HttpUrl.deliverFinishUrl, tag, params, callback)
    }

    /**
     * 上传图片文件
     */
    fun uploadPicFile(tag: Context, folder: String, file: File, uploadListener: UploadListener) {
        try {
            val uploadBuilder = OkHttpProxy.upload().url(HttpUrl.FILE_SERVER_PIC_UPLOAD).tag(tag)
            uploadBuilder.addParams("folder", folder)
                    .file(Pair("Filedata", file))
                    .start(uploadListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 上传视频文件
     */
    fun uploadVideoFile(tag: Context, folder: String, file: File, uploadListener: UploadListener) {
        try {
            val uploadBuilder = OkHttpProxy.upload().url(HttpUrl.FILE_SERVER_VIDEO_UPLOAD).tag(tag)
            uploadBuilder.addParams("folder", folder)
                    .file(Pair("Filedata", file))
                    .start(uploadListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
