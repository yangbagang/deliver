package com.szcloud8.app.deliver.activity.user

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.app.ShowApplication
import com.szcloud8.app.deliver.http.SendRequest
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.bean.UserInfo
import com.ybg.app.base.constants.MessageEvent
import com.ybg.app.base.http.callback.JsonCallback
import com.ybg.app.base.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_user.*
import org.greenrobot.eventbus.EventBus

class UserActivity : AppCompatActivity() {

    private val showApplication = ShowApplication.instance!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_update_button.setOnClickListener {
            attemptUpdate()
        }

        btn_logout_button.setOnClickListener {
            logout()
        }
    }

    override fun onStart() {
        super.onStart()
        if (showApplication.hasLogin()) {
            loadUserInfo()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadUserInfo() {
        SendRequest.getUserInfo(this, showApplication.token, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                val gson = Gson()
                val userInfo = gson.fromJson<UserInfo>(data, object : TypeToken<UserInfo>() {}.type)
                tv_user_name.text = "配送员：${userInfo.userName}"
                tv_user_mobile.text = "手机号：${userInfo.mobile}"
            }
        })
    }

    private fun attemptUpdate() {
        // Reset errors.
        et_old_pwd.error = null
        et_new_pwd.error = null

        // Store values at the time of the login attempt.
        val oldPwdStr = et_old_pwd.text.toString()
        val newPwdStr = et_new_pwd.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid name.
        if (TextUtils.isEmpty(oldPwdStr)) {
            et_old_pwd.error = getString(R.string.error_field_required)
            focusView = et_old_pwd
            cancel = true
        }

        if (TextUtils.isEmpty(newPwdStr)) {
            et_new_pwd.error = getString(R.string.error_field_required)
            focusView = et_new_pwd
            cancel = true
        }

        if (newPwdStr.length < 6) {
            et_new_pwd.error = getString(R.string.error_invalid_password)
            focusView = et_new_pwd
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            update(oldPwdStr, newPwdStr)
        }
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        update_progress.visibility = if (show) View.VISIBLE else View.GONE
        update_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        update_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    private fun update(oldPwd: String, newPwd: String) {
        SendRequest.updatePassword(this, showApplication.token, oldPwd, newPwd, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                showProgress(false)
                ToastUtil.show(showApplication, "操作完成")
                finish()
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                println("errorMsg: ${jsonBean.message}")
                if (jsonBean.message.contains("重新登录")) {
                    EventBus.getDefault().post(MessageEvent(MessageEvent.MESSAGE_USER_LOGOUT))
                    finish()
                } else {
                    ToastUtil.show(showApplication, jsonBean.message)
                }
                showProgress(false)
            }
        })
    }

    private fun logout() {
        SendRequest.userLogout(this, showApplication.token, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                logout()
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                logout()
            }

            fun logout() {
                showApplication.token = ""
                runOnUiThread {
                    EventBus.getDefault().post(MessageEvent(MessageEvent.MESSAGE_USER_LOGOUT))
                    finish()
                }
            }
        })
    }
}
