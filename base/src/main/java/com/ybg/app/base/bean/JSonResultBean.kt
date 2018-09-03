package com.ybg.app.base.bean

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by yangbagang on 2016/10/27.
 */
class JSonResultBean : Serializable {

    var success: Boolean = false

    var message: String = ""

    var errorCode: String = ""

    var data: String = ""

    override fun toString(): String {
        return "JSonResultBean{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", data='" + data + '\'' +
                '}'
    }

    companion object {

        private val serialVersionUID = -788465282702822219L

        fun fromJSON(jsonStr: String): JSonResultBean? {
            try {
                val json = JSONObject(jsonStr)
                val jsonBean = JSonResultBean()
                jsonBean.success = json.getBoolean("success")
                jsonBean.message = json.getString("message")
                jsonBean.errorCode = json.getString("errorCode")
                jsonBean.data = json.getString("data")
                return jsonBean
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}
