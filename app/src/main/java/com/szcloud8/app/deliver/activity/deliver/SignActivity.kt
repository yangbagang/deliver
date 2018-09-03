package com.szcloud8.app.deliver.activity.deliver

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.app.ShowApplication
import com.szcloud8.app.deliver.http.SendRequest
import com.ybg.app.base.constants.AppConstant
import com.ybg.app.base.constants.IntentExtra
import com.ybg.app.base.http.callback.UploadCallback
import com.ybg.app.base.utils.BitmapUtils
import com.ybg.app.base.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_sign.*
import java.io.File

class SignActivity : AppCompatActivity() {

    private val showApplication = ShowApplication.instance!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_sign_clear.setOnClickListener {
            //清除签名
            signature_view.clearCanvas()
        }

        btn_sign_save.setOnClickListener {
            saveSignBmp()
        }
    }

    private fun saveSignBmp() {
        if (signature_view.isBitmapEmpty) {
            ToastUtil.show(showApplication, "您尚未进行签名")
            return
        }
        //展示进度条
        showProgress(true)
        //保存文件
        val fileName = "${System.currentTimeMillis()}.png"
        val file = File(AppConstant.IMAGE_SAVE_PATH, fileName)
        Thread {
            val signBmp = signature_view.signatureBitmap
            BitmapUtils.saveBitmap(signBmp, file)
            //开始上传
            uploadSign(file)
        }.start()
    }

    private fun uploadSign(file: File) {
        SendRequest.uploadPicFile(this, "sign", file, object : UploadCallback() {
            override fun onJsonFailure(message: String) {
                ToastUtil.show(showApplication, message)
                showProgress(false)
            }

            override fun onJsonSuccess(fid: String) {
                val intent = Intent()
                intent.putExtra("signFid", fid)
                setResult(IntentExtra.ResultCode.RESULT_CODE_SIGN, intent)
                showProgress(false)
                finish()
            }
        })
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

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        signature_view.visibility = if (show) View.GONE else View.VISIBLE
        signature_view.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        signature_view.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        save_progress.visibility = if (show) View.VISIBLE else View.GONE
        save_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        save_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }
}
