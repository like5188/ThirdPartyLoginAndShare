package com.like.thirdpartyloginandshare.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.like.thirdpartyloginandshare.ThirdPartyShare
import com.like.thirdpartyloginandshare.share.params.imageandtext.QZoneImageAndTextParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import org.jetbrains.anko.toast

class QZoneShareActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "QZoneShareActivity"
    }

    private val mThirdPartyShare: ThirdPartyShare by lazy { ThirdPartyShare(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qzone_share)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mThirdPartyShare.onActivityResult(requestCode, resultCode, data)
    }

    fun imageAndText(view: View) {
        mThirdPartyShare
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
            .share(
                QZoneImageAndTextParams(
                    "title",
                    "https://www.baidu.com/",
                    arrayListOf("${getExternalFilesDir(null)}/aaa.png", "${getExternalFilesDir(null)}/bbbb.jpg"),
                    "summary"
                )
            )
    }

    fun program(view: View) {
        toast("没有添加例子")
    }

}
