package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.like.thirdpartyloginandshare.share.*
import com.like.thirdpartyloginandshare.share.params.app.AppParams
import com.like.thirdpartyloginandshare.share.params.image.ImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.ImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.multiimage.MultiImageParams
import com.like.thirdpartyloginandshare.share.params.music.MusicParams
import com.like.thirdpartyloginandshare.share.params.page.PageParams
import com.like.thirdpartyloginandshare.share.params.text.TextParams
import com.like.thirdpartyloginandshare.share.params.video.VideoParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.PlatForm
import com.like.thirdpartyloginandshare.util.SingletonHolder
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import kotlin.jvm.functions.FunctionN

class ThirdPartyShare private constructor(activity: Activity) : ShareStrategy(activity) {
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

    override fun shareText(params: TextParams) {
        checkParams()
        mStrategy.shareText(params)
    }

    override fun shareImage(params: ImageParams) {
        checkParams()
        mStrategy.shareImage(params)
    }

    override fun shareMultiImage(params: MultiImageParams) {
        checkParams()
        mStrategy.shareMultiImage(params)
    }

    override fun shareImageAndText(params: ImageAndTextParams) {
        checkParams()
        mStrategy.shareImageAndText(params)
    }

    override fun shareMusic(params: MusicParams) {
        checkParams()
        mStrategy.shareMusic(params)
    }

    override fun shareVideo(params: VideoParams) {
        checkParams()
        mStrategy.shareVideo(params)
    }

    override fun shareApp(params: AppParams) {
        checkParams()
        mStrategy.shareApp(params)
    }

    override fun sharePage(params: PageParams) {
        checkParams()
        mStrategy.sharePage(params)
    }

    private fun checkParams() {
        if (!::mStrategy.isInitialized) {
            throw UnsupportedOperationException("请先调用setPlatForm(platForm: PlatForm)方法设置对应的第三方分享平台")
        }
    }
}