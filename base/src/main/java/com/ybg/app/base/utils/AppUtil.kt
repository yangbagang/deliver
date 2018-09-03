package com.ybg.app.base.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import com.ybg.app.base.R

import com.ybg.app.base.constants.AppConstant
import com.ybg.app.base.preference.AppPreferences

/**
 * Created by yangbagang on 16/8/9.
 */
object AppUtil {

    private val preference = AppPreferences.instance

    var isFirstUse: Boolean
        get() = preference.getBoolean(AppConstant.IS_FIRST_USE, true)
        set(isFirstUse) = preference.setBoolean(AppConstant.IS_FIRST_USE, isFirstUse)

    // 操作系统
    val sysName: String
        get() = android.os.Build.DEVICE

    // 操作系统版本
    val sysVersion: String
        get() = android.os.Build.VERSION.RELEASE

    // 客户端或浏览器版本
    fun getAppVersion(context: Context, packageName: String): String {
        var versionName = ""
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                    packageName, 0)
            versionName = packageInfo.versionName
            if (TextUtils.isEmpty(versionName)) {
                return ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return versionName
    }

    // 品牌
    val brandInfo: String
        get() = android.os.Build.BRAND

    // 型号
    val modelInfo: String
        get() = android.os.Build.MODEL

    // 进网号
    fun getImeiNo(context: Context): String {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.deviceId
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }

    fun showMessage(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun checkPermission(activity: Activity, permission: String, message: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                AlertDialog.Builder(activity)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok) { dialog, which ->
                            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
                        }.setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show()
                return false
            }
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
            return false
        }
        return true
    }

    fun getProgressDialog(context: Context, message: String): ProgressDialog {
        val dialog = ProgressDialog(context, R.style.tmDialog)
        dialog.setMessage(message)
        val window = dialog.window
        val lp = window!!.attributes
        lp.alpha = 0.6f
        window.attributes = lp
        return dialog
    }

}
