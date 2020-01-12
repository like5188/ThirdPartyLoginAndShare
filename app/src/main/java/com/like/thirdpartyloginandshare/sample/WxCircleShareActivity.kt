package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.params.text.WxTextParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import org.jetbrains.anko.toast

class WxCircleShareActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "WxCircleShareActivity"
    }

    private val mThirdPartyShare: ThirdPartyShare by lazy { ThirdPartyShare(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wxcircle_share)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mThirdPartyShare.onActivityResult(requestCode, resultCode, data)
    }

    fun image(view: View) {
        toast("没有添加例子")
    }

    fun music(view: View) {
        toast("没有添加例子")
    }

    fun page(view: View) {
        toast("没有添加例子")
    }

    fun text(view: View) {
        mThirdPartyShare
            .setPlatForm(PlatForm.WX_CIRCLE)
            .setShareListener(object : OnLoginAndShareListener {
                override fun onSuccess() {
                    toast("微信朋友圈分享成功")
                }

                override fun onFailure(errorMessage: String) {
                    toast("微信朋友圈分享失败：$errorMessage")
                }

                override fun onCancel() {
                    toast("取消微信朋友圈分享")
                }
            })
            .share(WxTextParams("222"))
    }

    fun video(view: View) {
        toast("没有添加例子")
    }
}
