package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.params.image.WxImageParams
import com.like.thirdpartyloginandshare.share.params.music.WxMusicParams
import com.like.thirdpartyloginandshare.share.params.text.WxTextParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import org.jetbrains.anko.toast

class WxShareActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "WxShareActivity"
    }

    private val mThirdPartyShare: ThirdPartyShare by lazy { ThirdPartyShare(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wx_share)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mThirdPartyShare.onActivityResult(requestCode, resultCode, data)
    }

    fun image(view: View) {
        mThirdPartyShare
            .setPlatForm(PlatForm.WX)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微信分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信分享")
                }
            })
            .share(
                WxImageParams(
                    BitmapFactory.decodeFile("${getExternalFilesDir(null)}/aaa.png"),
                    BitmapFactory.decodeFile("${getExternalFilesDir(null)}/bbbb.jpg").let {
                        // 128kb以内
                        val thumbBmp = Bitmap.createScaledBitmap(it, 150, 150, true)
                        it.recycle()
                        thumbBmp
                    }
                )
            )
    }

    fun music(view: View) {
        mThirdPartyShare
            .setPlatForm(PlatForm.WX)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微信分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信分享")
                }
            })
            .share(
                WxMusicParams(
                    "title",
                    "description",
                    "http://c.y.qq.com/v8/playsong.html?songid=109325260&songmid=000kuo2H2xJqfA&songtype=0&source=mqq&_wv=1",
                    BitmapFactory.decodeFile("${getExternalFilesDir(null)}/aaa.png").let {
                        // 128kb以内
                        val thumbBmp = Bitmap.createScaledBitmap(it, 150, 150, true)
                        it.recycle()
                        thumbBmp
                    }
                )
            )
    }

    fun page(view: View) {
        toast("没有添加例子")
    }

    fun text(view: View) {
        mThirdPartyShare
            .setPlatForm(PlatForm.WX)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微信分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信分享")
                }
            })
            .share(WxTextParams("222"))
    }

    fun video(view: View) {
        toast("没有添加例子")
    }
}
