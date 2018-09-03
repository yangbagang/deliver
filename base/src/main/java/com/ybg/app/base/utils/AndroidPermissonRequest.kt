package com.ybg.app.base.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

/**
 * Created by yangbagang on 2016/10/26.
 */

object AndroidPermissonRequest {

    /**  外部存储  和相机 */
    private val REQUEST_STORAGE = 1000
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    /**  外部存储  和相机 */
    private val REQUEST_CAMERA = 2000
    private val PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA)


    /**
     * @param activity 动态申请相机权限
     */
    fun verifyStoragePermissions(activity: Activity) {
        val permission_storage = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission_storage != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_STORAGE)
        }
    }

    /**
     * @param activity 动态申请相机权限
     */
    fun verifyCameraPermissions(activity: Activity) {
        val permission_carera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        if (permission_carera != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA, REQUEST_CAMERA)
        }
    }

}
