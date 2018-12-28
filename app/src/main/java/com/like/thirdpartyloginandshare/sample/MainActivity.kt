package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.like.thirdpartyloginandshare.ThirdPartyLogin
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.params.image.QqImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QZoneImageAndTextParams
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

                override fun onCancel() {
                    toast("取消微博登录")
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

                override fun onCancel() {
                    toast("取消微博分享")
                }
            })
            .shareText(WbTextParams("内容", "标题", "https://www.baidu.com/"))
    }

    fun wxLogin(view: View) {

    }

    fun wxShare(view: View) {

    }

    fun wxCircleShare(view: View) {

    }

    fun qqLogin(view: View) {
        ThirdPartyLogin.with(this)
            .setPlatForm(PlatForm.QQ)
            .setLoginListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("QQ登录成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("QQ登录失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消QQ登录")
                }
            })
            .login()
    }

    fun qqShare(view: View) {
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.QQ)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("QQ分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("QQ分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消QQ分享")
                }
            })
            .shareImage(QqImageParams("$cacheDir/123.jpg"))
    }

    fun qzoneShare(view: View) {
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.QZONE)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("QZONE分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("QZONE分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消QZONE分享")
                }
            })
            .shareImageAndText(
                QZoneImageAndTextParams(
                    "title",
                    "https://www.baidu.com/",
                    "summary",
                    arrayListOf("$cacheDir/123.jpg")
                )
            )
    }

}
