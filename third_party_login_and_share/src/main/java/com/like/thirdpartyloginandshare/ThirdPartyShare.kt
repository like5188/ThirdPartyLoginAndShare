package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.share.*
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX

class ThirdPartyShare(private val activity: Activity) : ShareStrategy {
    private lateinit var mStrategy: ShareStrategy

    fun setPlatForm(platForm: PlatForm): ThirdPartyShare {
        ThirdPartyInit.checkInit(platForm)
        when (platForm) {
            PlatForm.QQ -> {
                mStrategy = QqShare(activity)
            }
            PlatForm.QZONE -> {
                mStrategy = QZoneShare(activity)
            }
            PlatForm.WX -> {
                mStrategy = WxShare(activity).setScene(SendMessageToWX.Req.WXSceneSession)
            }
            PlatForm.WX_CIRCLE -> {
                mStrategy = WxShare(activity).setScene(SendMessageToWX.Req.WXSceneTimeline)
            }
            PlatForm.WB -> {
                mStrategy = WbShare(activity)
            }
        }
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        checkParams()
        mStrategy.onActivityResult(requestCode, resultCode, data)
    }

    override fun setShareListener(listener: OnLoginAndShareListener): ThirdPartyShare {
        checkParams()
        mStrategy.setShareListener(listener)
        return this
    }

    /**
     * 分享
     * @param params    对应平台的对应参数。参数在[com.like.thirdpartyloginandshare.share.params]包下。
     */
    override fun share(params: ShareParams) {
        checkParams()
        mStrategy.share(params)
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法设置对应的第三方分享平台")
        }
    }
}