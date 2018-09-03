package com.ybg.app.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager


/**
 * Created by ybg on 17-2-28.
 */
object NetworkUtil {

    /**
     * 获取当前网络连接类型
     * @param context
     * *
     * @return
     */
    fun getNetworkState(context: Context): NetworkType {
        //获取系统的网络服务
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return NetworkType.NONE

        //获取当前网络类型，如果为空，返回无网络
        val activeNetInfo = connManager.activeNetworkInfo
        if (activeNetInfo == null || !activeNetInfo.isAvailable) {
            return NetworkType.NONE
        }

        // 判断是不是连接的是不是wifi
        val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (null != wifiInfo) {
            val state = wifiInfo.state
            if (null != state)
                if (state === NetworkInfo.State.CONNECTED || state === NetworkInfo.State.CONNECTING) {
                    return NetworkType.WIFI
                }
        }

        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        if (null != networkInfo) {
            val state = networkInfo.state
            val strSubTypeName = networkInfo.subtypeName
            if (null != state)
                if (state === NetworkInfo.State.CONNECTED || state === NetworkInfo.State.CONNECTING) {
                    when (activeNetInfo.subtype) {
                    //如果是2g类型
                        TelephonyManager.NETWORK_TYPE_GPRS // 联通2g
                            , TelephonyManager.NETWORK_TYPE_CDMA // 电信2g
                            , TelephonyManager.NETWORK_TYPE_EDGE // 移动2g
                            , TelephonyManager.NETWORK_TYPE_1xRTT
                            , TelephonyManager.NETWORK_TYPE_IDEN -> return NetworkType.Mobile_2G
                    //如果是3g类型
                        TelephonyManager.NETWORK_TYPE_EVDO_A // 电信3g
                            , TelephonyManager.NETWORK_TYPE_UMTS
                            , TelephonyManager.NETWORK_TYPE_EVDO_0
                            , TelephonyManager.NETWORK_TYPE_HSDPA
                            , TelephonyManager.NETWORK_TYPE_HSUPA
                            , TelephonyManager.NETWORK_TYPE_HSPA
                            , TelephonyManager.NETWORK_TYPE_EVDO_B
                            , TelephonyManager.NETWORK_TYPE_EHRPD
                            , TelephonyManager.NETWORK_TYPE_HSPAP -> return NetworkType.Mobile_3G
                    //如果是4g类型
                        TelephonyManager.NETWORK_TYPE_LTE -> return NetworkType.Mobile_4G
                        else ->
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equals("TD-SCDMA", ignoreCase = true) || strSubTypeName.equals("WCDMA", ignoreCase = true) || strSubTypeName.equals("CDMA2000", ignoreCase = true)) {
                                return NetworkType.Mobile_3G
                            } else {
                                return NetworkType.Mobile
                            }
                    }
                }
        }
        return NetworkType.NONE
    }
}

enum class NetworkType {
    NONE, WIFI, Mobile_2G, Mobile_3G, Mobile_4G, Mobile
}