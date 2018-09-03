package com.ybg.app.base.utils

import android.os.Environment
import android.os.StatFs

import java.io.File

/**
 * 获取系统环境信息的工具类
 */
object EnvironmentStateUtil {

    /**
     * 判断设备是否存在外部存储设备
     *
     * @return
     */
    fun ExternalStorageIsAvailable() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /**
     * 获取外部存储设备的路径
     *
     * @return
     */
    val externalStorageDirectory: File
        get() = Environment.getExternalStorageDirectory()

    /**
     * 获取外部存储设备的可用容量
     *
     * @return
     */
    val availableExternalMemorySize: Long
        get() {
            if (ExternalStorageIsAvailable()) {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.path)
                val blockSize = stat.blockSize.toLong()
                val availableBlocks = stat.availableBlocks.toLong()
                return availableBlocks * blockSize
            } else {
                return -1
            }
        }

    /**
     * 获取外部存储设备的总容量

     * @return
     */
    val totalExternalMemorySize: Long
        get() {
            if (ExternalStorageIsAvailable()) {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.path)
                val blockSize = stat.blockSize.toLong()
                val totalBlocks = stat.blockCount.toLong()
                return totalBlocks * blockSize
            } else {
                return -1
            }
        }
}