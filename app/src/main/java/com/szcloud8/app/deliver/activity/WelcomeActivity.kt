package com.szcloud8.app.deliver.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.activity.home.MainActivity
import com.szcloud8.app.deliver.activity.user.LoginActivity
import com.szcloud8.app.deliver.app.ShowApplication

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //检查当前是否己登录
        if (ShowApplication.instance!!.hasLogin()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //关闭本窗口
        finish()
    }

}
