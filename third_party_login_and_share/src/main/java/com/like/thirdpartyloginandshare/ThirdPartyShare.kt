package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.like.thirdpartyloginandshare.share.*
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import com.like.thirdpartyloginandshare.util.SingletonHolder
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import kotlin.jvm.functions.FunctionN

class ThirdPartyShare private constructor(private val activity: Activity) : ShareStrategy {
    companion object : SingletonHolder<ThirdPartyShare>(object : FunctionN<ThirdPartyShare> {
        override val arity: Int = 1 // number of arguments that must be passed to constructor

        override fun invoke(vararg args: Any?): ThirdPartyShare {
            return ThirdPartyShare(args[0] as Activity)
        }
    }) {
        fun with(activity: Activity): ThirdPartyShare = getInstance(activity)

        fun with(fragment: Fragment): ThirdPartyShare = getInstance(fragment.activity)
    }

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
                mStrategy = WxShare.getInstance(activity).setSence(SendMessageToWX.Req.WXSceneSession)
            }
            PlatForm.WX_CIRCLE -> {
                mStrategy = WxShare.getInstance(activity).setSence(SendMessageToWX.Req.WXSceneTimeline)
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
     * @param params    对应平台的对应参数。
     *
     * - [com.like.thirdpartyloginandshare.util.PlatForm.QQ]：
     * [com.like.thirdpartyloginandshare.share.params.app.QqAppParams]、
     * [com.like.thirdpartyloginandshare.share.params.image.QqImageParams]、
     * [com.like.thirdpartyloginandshare.share.params.imageandtext.QqImageAndTextParams]、
     * [com.like.thirdpartyloginandshare.share.params.music.QqMusicParams]
     *
     * - [com.like.thirdpartyloginandshare.util.PlatForm.QZONE]：
     * [com.like.thirdpartyloginandshare.share.params.imageandtext.QZoneImageAndTextParams]
     *
     * - [com.like.thirdpartyloginandshare.util.PlatForm.WX]、[com.like.thirdpartyloginandshare.util.PlatForm.WX_CIRCLE]：
     * [com.like.thirdpartyloginandshare.share.params.image.WxImageParams]、
     * [com.like.thirdpartyloginandshare.share.params.music.WxMusicParams]、
     * [com.like.thirdpartyloginandshare.share.params.page.WxPageParams]、
     * [com.like.thirdpartyloginandshare.share.params.text.WxTextParams]、
     * [com.like.thirdpartyloginandshare.share.params.video.WxVideoParams]
     *
     * - [com.like.thirdpartyloginandshare.util.PlatForm.WB]：
     * [com.like.thirdpartyloginandshare.share.params.image.WbImageParams]、
     * [com.like.thirdpartyloginandshare.share.params.multiimage.WbMultiImageParams]、
     * [com.like.thirdpartyloginandshare.share.params.page.WbPageParams]、
     * [com.like.thirdpartyloginandshare.share.params.text.WbTextParams]、
     * [com.like.thirdpartyloginandshare.share.params.video.WbVideoParams]
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