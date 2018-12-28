package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.like.thirdpartyloginandshare.ThirdPartyLogin
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.params.text.TextParams
import com.like.thirdpartyloginandshare.share.params.text.WbTextParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ThirdPartyLogin.with(this).onActivityResult(requestCode, resultCode, data)
        ThirdPartyShare.with(this).onActivityResult(requestCode, resultCode, data)
    }

    fun wbLogin(view: View) {
        ThirdPartyLogin.with(this)
            .setPlatForm(PlatForm.WB)
            .setLoginListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微博登录成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微博登录失败：$errorMessage")
                }
            })
            .login()
    }

    fun wbShare(view: View) {
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.WB)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微博分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微博分享失败：$errorMessage")
                }
            })
            .shareText(WbTextParams("内容", "标题", "https://www.baidu.com/"))
    }

    fun wxLogin(view: View) {

    }

    fun wxShare(view: View) {

    }

    fun wxCircleLogin(view: View) {

    }

    fun wxCircleShare(view: View) {

    }

    fun qqLogin(view: View) {

    }

    fun qqShare(view: View) {

    }

    fun qzoneLogin(view: View) {

    }

    fun qzoneShare(view: View) {

    }

}
