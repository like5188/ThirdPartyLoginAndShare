package com.like.thirdpartyloginandshare.sample

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.ThirdPartyLogin
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.params.app.QqAppParams
import com.like.thirdpartyloginandshare.share.params.image.WbImageParams
import com.like.thirdpartyloginandshare.share.params.image.WxImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QZoneImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.multiimage.WbMultiImageParams
import com.like.thirdpartyloginandshare.share.params.page.WbPageParams
import com.like.thirdpartyloginandshare.share.params.text.WbTextParams
import com.like.thirdpartyloginandshare.share.params.text.WxTextParams
import com.like.thirdpartyloginandshare.share.params.video.WbVideoParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ThirdPartyInit.initQq(this, ThirdPartyInit.QqInitParams("101540498"))
        ThirdPartyInit.initWx(
            this,
            ThirdPartyInit.WxInitParams("wxa9cce595f2c0b87b")
        )
        ThirdPartyInit.initWb(
            this, ThirdPartyInit.WbInitParams(
                "1929959086",
                "https://api.weibo.com/oauth2/default.html",
                "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write"
            )
        )
        copyFile("eeee.mp4")
        copyFile("aaa.png")
        copyFile("bbbb.jpg")
        copyFile("ccc.JPG")
        copyFile("eee.jpg")
        copyFile("ddd.jpg")
        copyFile("fff.jpg")
        copyFile("ggg.JPG")
        copyFile("hhhh.jpg")
        copyFile("kkk.JPG")
    }

    private fun copyFile(fileName: String) {
        val file = File(getExternalFilesDir(null), fileName)
        if (!file.exists()) {
            GlobalScope.launch {
                try {
                    assets.open(fileName).use { inputStream ->
                        FileOutputStream(file).use { outputStream ->
                            outputStream.write(inputStream.readBytes())
                        }
                    }
                    Log.d("MainActivity", "copyFile成功：$fileName")
                } catch (e: Exception) {
                    Log.d("MainActivity", "copyFile失败：${e.message}")
                }
            }
        } else {
            Log.w("MainActivity", "文件已经存在：$fileName")
        }
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
            .setPlatForm(PlatForm.WB)
            .setLoginListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
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
            .setPlatForm(PlatForm.WB)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
                    toast("微博分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微博分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微博分享")
                }
            })
//            .shareMultiImage(
//                WbMultiImageParams(
//                    arrayListOf(
//                        Uri.fromFile(File(getExternalFilesDir(null), "/aaa.png")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/bbbb.jpg")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/ccc.JPG")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/ddd.jpg")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/fff.jpg")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/ggg.JPG")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/eee.jpg")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/hhhh.jpg")),
//                        Uri.fromFile(File(getExternalFilesDir(null), "/kkk.JPG"))
//                    )
//                )
//            )
            .shareVideo(
                WbVideoParams(
                    Uri.fromFile(File(getExternalFilesDir(null), "/eeee.mp4"))
                )
            )
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
        ThirdPartyLogin.with(this)
            .setPlatForm(PlatForm.WX)
            .setLoginListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
                    toast("微信登录获取code成功：$content")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信登录失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信登录")
                }
            })
            .login()
    }

    fun wxShare(view: View) {
        isLogin = false
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.WX)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
                    toast("微信分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信分享")
                }
            })
            .shareImage(
                WxImageParams(
                    BitmapFactory.decodeFile("$cacheDir/123.jpg"),
                    BitmapFactory.decodeFile("$cacheDir/123.jpg"),
                    "123"
                )
            )
    }

    fun wxCircleShare(view: View) {
        isLogin = false
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.WX_CIRCLE)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
                    toast("微信朋友圈分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信朋友圈分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信朋友圈分享")
                }
            })
            .shareText(WxTextParams("222"))
    }

    fun qqLogin(view: View) {
        isLogin = true
        ThirdPartyLogin.with(this)
            .setPlatForm(PlatForm.QQ)
            .setLoginListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
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
            .setPlatForm(PlatForm.QQ)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
                    toast("QQ分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("QQ分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消QQ分享")
                }
            })
            .shareApp(QqAppParams("title", "http://url.cn/424xgot"))
//            .shareMusic(QqMusicParams("title", "http://c.y.qq.com/v8/playsong.html?songid=109325260&songmid=000kuo2H2xJqfA&songtype=0&source=mqq&_wv=1"))
//            .shareImageAndText(QqImageAndTextParams("title","https://www.baidu.com/"))
//            .shareImage(QqImageParams("/storage/emulated/0/Pictures/Screenshots/S81224-20082955.jpg"))
    }

    fun qzoneShare(view: View) {
        isLogin = false
        ThirdPartyShare.with(this)
            .setPlatForm(PlatForm.QZONE)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess(content: String) {
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
                    arrayListOf("/storage/emulated/0/Pictures/Screenshots/S81224-20082955.jpg"),
                    "summary"
                )
            )
    }

}
