package com.szcloud8.app.deliver.http

import com.szcloud8.app.deliver.BuildConfig

/**
 * 网络请求相关设置,配置请求地址及参数
 */
object HttpUrl {

    //全局定义
    //上传图片
    const val FILE_SERVER_PIC_UPLOAD = "http://139.196.126.148:8080/fileserver/file/upload3"
    //上传图片
    const val FILE_SERVER_VIDEO_UPLOAD = "http://139.196.126.148:8080/fileserver/file/upload"
    //预览
    private const val FILE_SERVER_PREVIEW = "http://139.196.126.148:8080/fileserver/file/preview"
    //下载
    private const val FILE_SERVER_DOWNLOAD = "http://139.196.126.148:8080/fileserver/file/download"

    private const val ROOT_URL = BuildConfig.API_SERVER_URI

    //第一部分，用户操作
    //用户登录
    val userLoginUrl: String
        get() = "$ROOT_URL/deliverUser/login"
    //用户退出
    val userLogoutUrl: String
        get() = "$ROOT_URL/deliverUser/logout"
    //修改密码
    val updatePasswordUrl: String
        get() = "$ROOT_URL/deliverUser/updatePassword"
    //更新推送
    val updateClientIdUrl: String
        get() = "$ROOT_URL/deliverUser/updateAppToken"
    //用户详情
    val userInfoUrl: String
        get() = "$ROOT_URL/deliverUser/getUserInfo"

    //第二部分，配送操作
    val deliverListUrl: String
        get() = "$ROOT_URL/deliverInfo/list"
    val deliverCountUrl: String
        get() = "$ROOT_URL/deliverInfo/getCount"
    val deliverAssignUrl: String
        get() = "$ROOT_URL/deliverInfo/assign"
    val deliverUnAssignUrl: String
        get() = "$ROOT_URL/deliverInfo/unAssign"
    val deliverDetailUrl: String
        get() = "$ROOT_URL/deliverInfo/detail"
    val deliverFinishUrl: String
        get() = "$ROOT_URL/deliverInfo/finish"

    //公共方法
    fun getImageUrl(fid: String): String {
        if (fid.startsWith("http:", true) || fid.startsWith("https:", true)) {
            return fid
        }
        //val path = Base64Util.getDecodeString(fid)
        //println("path=$path")
        return "$FILE_SERVER_PREVIEW/$fid"
    }

    fun getVideoUrl(fid: String): String {
        if (fid.startsWith("http:", true) || fid.startsWith("https:", true)) {
            return fid
        }
        //val path = Base64Util.getDecodeString(fid)
        //println("path=$path")
        return "$FILE_SERVER_DOWNLOAD/$fid"
    }

}
