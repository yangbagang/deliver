package com.ybg.app.base.constants

import com.ybg.app.base.utils.EnvironmentStateUtil

/**
 * @author 杨拔纲
 */
object AppConstant {

    // 是否首次使用
    const val IS_FIRST_USE = "isFirstUse"
    // 属性文件
    const val PREFERENCE_FILE_NAME = "ybg_deliver_preference.dat"

    // 外部存储设备的根路径
    private val ExternalStorageRootPath = EnvironmentStateUtil.externalStorageDirectory.path
    private val BasePath = "$ExternalStorageRootPath/deliver_cache/"

    // 文件存放路径
    val FILE_CACHE_PATH = BasePath + "fileCache/"
    // 视频存放路径
    val VIDEO_CACHE_PATH = BasePath + "videoCache/"
    //缓存的图片
    val IMAGE_CACHE_PATH = BasePath + "imageCache/"

    val VIDEO_SAVE_PATH = BasePath + "video/"
    // 保存图片
    val IMAGE_SAVE_PATH = BasePath + "photos/"
    // 下载存储地址
    val DOWNLOAD_PATH = BasePath + "download/"
}
