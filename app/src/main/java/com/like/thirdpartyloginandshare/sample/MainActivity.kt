package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.like.thirdpartyloginandshare.ThirdPartyLogin
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.init.params.QqInitParams
import com.like.thirdpartyloginandshare.init.params.WbInitParams
import com.like.thirdpartyloginandshare.share.params.image.QqImageParams
import com.like.thirdpartyloginandshare.share.params.image.WbImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QZoneImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.multiimage.MultiImageParams
import com.like.thirdpartyloginandshare.share.params.multiimage.WbMultiImageParams
import com.like.thirdpartyloginandshare.share.params.page.WbPageParams
import com.like.thirdpartyloginandshare.share.params.page.WxPageParams
import com.like.thirdpartyloginandshare.share.params.text.WbTextParams
import com.like.thirdpartyloginandshare.share.params.video.WbVideoParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : AppCompatActivity() {
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (isLogin) {
            ThirdPartyLogin.with(this).onActivityResult(requestCode, resultCode, data)
        } else {
            ThirdPartyShare.with(this).onActivityResult(requestCode, resultCode, data)
        }
    }

    fun wbLogin(view: View) {
        isLogin = true
        ThirdPartyLogin.with(this)
            .setPlatForm(
                PlatForm.WB,
                WbInitParams(
                    "1929959086",
                    "https://api.weibo.com/oauth2/default.html",
                    "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"
                )
            )
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
        isLogin = false
        ThirdPartyShare.with(this)
            .setPlatForm(
                PlatForm.WB,
                WbInitParams(
                    "1929959086",
                    "https://api.weibo.com/oauth2/default.html",
                    "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"
                )
            )
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
            .shareVideo(WbVideoParams(Uri.fromFile(File(cacheDir, "222.mp4"))))
//            .shareMultiImage(WbMultiImageParams(listOf(File(cacheDir, "123.jpg"), File(cacheDir, "123.jpg"))))
//            .sharePage(
//                WbPageParams(
//                    BitmapFactory.decodeFile("$cacheDir/123.jpg"),
//                    "title",
//                    "description",
//                    "https://www.baidu.com/",
//                    "defaultText"
//                )
//            )
//            .shareImage(WbImageParams(BitmapFactory.decodeFile("$cacheDir/123.jpg")))
//            .shareText(WbTextParams("hahahah"))
    }

    fun wxLogin(view: View) {
        isLogin = true

    }

    fun wxShare(view: View) {
        isLogin = false

    }

    fun wxCircleShare(view: View) {
        isLogin = false

    }

    fun qqLogin(view: View) {
        isLogin = true
        ThirdPartyLogin.with(this)
            .setPlatForm(PlatForm.QQ, QqInitParams("101540498"))
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
        isLogin = false
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.QQ, QqInitParams("101540498"))
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
        isLogin = false
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.QQ, QqInitParams("101540498"))
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
