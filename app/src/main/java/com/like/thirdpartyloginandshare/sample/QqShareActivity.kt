package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.params.app.QqAppParams
import com.like.thirdpartyloginandshare.share.params.image.QqImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QqImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.music.QqMusicParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import org.jetbrains.anko.toast

class QqShareActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "QqShareActivity"
    }

    private val mThirdPartyShare: ThirdPartyShare by lazy { ThirdPartyShare(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qq_share)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mThirdPartyShare.onActivityResult(requestCode, resultCode, data)
    }

    fun app(view: View) {
        mThirdPartyShare
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
            .share(QqAppParams("title", "http://url.cn/424xgot"))
    }

    fun image(view: View) {
        mThirdPartyShare
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
            .share(QqImageParams("${getExternalFilesDir(null)}/aaa.png"))
    }

    fun imageAndText(view: View) {
        mThirdPartyShare
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
            .share(QqImageAndTextParams("title", "https://www.baidu.com/"))
    }

    fun music(view: View) {
        mThirdPartyShare
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
            .share(
                QqMusicParams(
                    "title",
                    "http://c.y.qq.com/v8/playsong.html?songid=109325260&songmid=000kuo2H2xJqfA&songtype=0&source=mqq&_wv=1",
                    "https://www.baidu.com"
                )
            )
    }

    fun program(view: View) {
        toast("没有添加例子")
    }

}
