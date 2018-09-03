package com.ybg.app.base.preference

import android.content.SharedPreferences

/**
 * @author 杨拔纲
 */
class AppPreferences private constructor() {

    private var sharedPreferences: SharedPreferences? = null

    private var editor: SharedPreferences.Editor? = null

    private var init = false

    fun init(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
        editor = this.sharedPreferences!!.edit()
        init = true
    }

    fun hasInit(): Boolean {
        return init
    }

    fun getString(key: String, defValue: String): String {
        return sharedPreferences!!.getString(key, defValue)
    }

    fun setString(key: String, value: String) {
        editor!!.putString(key, value)
        editor!!.commit()
    }

    fun getInt(key: String, defValue: Int): Int {
        return sharedPreferences!!.getInt(key, defValue)
    }

    fun setInt(key: String, value: Int) {
        editor!!.putInt(key, value)
        editor!!.commit()
    }

    fun getLong(key: String, defValue: Long): Long {
        return sharedPreferences!!.getLong(key, defValue)
    }

    fun setLong(key: String, value: Long) {
        editor!!.putLong(key, value)
        editor!!.commit()
    }

    fun getFloat(key: String, defValue: Float): Float {
        return sharedPreferences!!.getFloat(key, defValue)
    }

    fun setFloat(key: String, value: Float) {
        editor!!.putFloat(key, value)
        editor!!.commit()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        editor!!.putBoolean(key, value)
        editor!!.commit()
    }

    companion object {

        private var preference: AppPreferences? = null

        val instance: AppPreferences
            get() {
                if (preference == null) {
                    preference = AppPreferences()
                }
                return preference!!
            }
    }

}
