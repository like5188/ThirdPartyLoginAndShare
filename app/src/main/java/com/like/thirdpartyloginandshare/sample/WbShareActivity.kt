package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.WbShare
import com.like.thirdpartyloginandshare.share.params.image.WbImageParams
import com.like.thirdpartyloginandshare.share.params.multiimage.WbMultiImageParams
import com.like.thirdpartyloginandshare.share.params.page.WbPageParams
import com.like.thirdpartyloginandshare.share.params.text.WbTextParams
import com.like.thirdpartyloginandshare.share.params.video.WbVideoParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import org.jetbrains.anko.toast
import java.io.File

class WbShareActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "WbShareActivity"
    }

    private val mThirdPartyShare: ThirdPartyShare by lazy { ThirdPartyShare() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wb_share)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mThirdPartyShare.onActivityResult(requestCode, resultCode, data)
    }

    fun image(view: View) {
        mThirdPartyShare
            .strategy(WbShare(this))
            .listener(object : OnLoginAndShareListener {
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
            .share(WbImageParams(BitmapFactory.decodeFile("${getExternalFilesDir(null)}/aaa.png")))
    }

    fun multiImage(view: View) {
        mThirdPartyShare
            .strategy(WbShare(this))
            .listener(object : OnLoginAndShareListener {
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
            .share(
                WbMultiImageParams(
                    arrayListOf(
                        Uri.fromFile(File(getExternalFilesDir(null), "/aaa.png")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/bbbb.jpg")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/ccc.JPG")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/ddd.jpg")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/fff.jpg")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/ggg.JPG")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/eee.jpg")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/hhhh.jpg")),
                        Uri.fromFile(File(getExternalFilesDir(null), "/kkk.JPG"))
                    )
                )
            )
    }

    fun page(view: View) {
        mThirdPartyShare
            .strategy(WbShare(this))
            .listener(object : OnLoginAndShareListener {
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
            .share(
                WbPageParams(
                    BitmapFactory.decodeFile("${getExternalFilesDir(null)}/aaa.png"),
                    "title",
                    "description",
                    "https://www.baidu.com/",
                    "defaultText"
                )
            )
    }

    fun text(view: View) {
        mThirdPartyShare
            .strategy(WbShare(this))
            .listener(object : OnLoginAndShareListener {
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
            .share(WbTextParams("hahahah"))
    }

    fun video(view: View) {
        mThirdPartyShare
            .strategy(WbShare(this))
            .listener(object : OnLoginAndShareListener {
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
            .share(
                WbVideoParams(
                    Uri.fromFile(File(getExternalFilesDir(null), "/eeee.mp4"))
                )
            )
    }
}
