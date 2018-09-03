package com.szcloud8.app.deliver.activity.user

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.activity.home.MainActivity
import com.szcloud8.app.deliver.app.ShowApplication
import com.szcloud8.app.deliver.http.SendRequest
import com.ybg.app.base.bean.JSonResultBean
import com.ybg.app.base.http.callback.JsonCallback
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via mobile/password.
 */
class LoginActivity : AppCompatActivity() {

    private val mApplication = ShowApplication.instance!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        mobile_sign_in_button.setOnClickListener { attemptLogin() }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid mobile, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        mobile.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val mobileStr = mobile.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid mobile address.
        if (TextUtils.isEmpty(mobileStr)) {
            mobile.error = getString(R.string.error_field_required)
            focusView = mobile
            cancel = true
        } else if (!isEmailValid(mobileStr)) {
            mobile.error = getString(R.string.error_invalid_mobile)
            focusView = mobile
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
            login(mobileStr, passwordStr)
        }
    }

    private fun isEmailValid(mobile: String): Boolean {
        return mobile.length == 11
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    private fun login(mobileStr: String, passwordStr: String) {
        SendRequest.userLogin(this, mobileStr, passwordStr, object : JsonCallback() {
            override fun onJsonSuccess(data: String) {
                showProgress(false)
                mApplication.token = data
                //转向主页
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                finish()
            }

            override fun onJsonFail(jsonBean: JSonResultBean) {
                showProgress(false)
                password.error = getString(R.string.error_incorrect_password)
                password.requestFocus()
            }
        })
    }

}
