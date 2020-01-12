package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.share.params.image.WxImageParams
import com.like.thirdpartyloginandshare.share.params.music.WxMusicParams
import com.like.thirdpartyloginandshare.share.params.page.WxPageParams
import com.like.thirdpartyloginandshare.share.params.text.WxTextParams
import com.like.thirdpartyloginandshare.share.params.video.WxVideoParams
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline
import com.tencent.mm.opensdk.openapi.IWXAPI
import java.io.ByteArrayOutputStream

class WxShare(private val activity: Activity) : IShareStrategy {
    companion object {
        private var mTargetScene: Int = WXSceneSession
        private var mOnLoginAndShareListener: OnLoginAndShareListener? = null
    }

    private val mWxApi: IWXAPI by lazy {
        ApiFactory.createWxApi(activity.applicationContext, ThirdPartyInit.wxInitParams.appId)
    }

    fun setScene(scene: Int): IShareStrategy {
        mTargetScene = scene
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun setShareListener(listener: OnLoginAndShareListener): IShareStrategy {
        mOnLoginAndShareListener = listener
        return this
    }

    override fun share(params: ShareParams) {
        if (!mWxApi.isWXAppInstalled) {
            mOnLoginAndShareListener?.onFailure("您的手机没有安装微信")
            return
        }
        when (params) {
            is WxTextParams -> {
                shareText(params)
            }
            is WxImageParams -> {
                shareImage(params)
            }
            is WxMusicParams -> {
                shareMusic(params)
            }
            is WxVideoParams -> {
                shareVideo(params)
            }
            is WxPageParams -> {
                sharePage(params)
            }
            else -> when (mTargetScene) {
                WXSceneSession -> throw UnsupportedOperationException("WX不支持此操作")
                WXSceneTimeline -> throw UnsupportedOperationException("WX_CIRCLE不支持此操作")
            }
        }
    }

    internal fun onShareSuccess() {
        mOnLoginAndShareListener?.onSuccess()
    }

    internal fun onShareFailure(errStr: String?) {
        mOnLoginAndShareListener?.onFailure(errStr ?: "")
    }

    internal fun onCancel() {
        mOnLoginAndShareListener?.onCancel()
    }

    private fun sendMessageToWX(transaction: String, msg: WXMediaMessage) {
        val req = SendMessageToWX.Req()
        req.transaction = "$transaction${System.currentTimeMillis()}"
        req.message = msg
        req.scene = mTargetScene
        mWxApi.sendReq(req)
    }

    private fun shareText(params: WxTextParams) {
        val textObj = WXTextObject()
        textObj.text = params.text

        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = params.text

        sendMessageToWX("text", msg)
    }

    private fun shareImage(params: WxImageParams) {
        val imgObj = WXImageObject(params.bmp)
        params.bmp.recycle()

        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        msg.thumbData = bmpToByteArray(params.thumbBmp, true)

        sendMessageToWX("img", msg)
    }

    private fun shareMusic(params: WxMusicParams) {
        val music = WXMusicObject()
        music.musicUrl = params.musicUrl

        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = params.title
        msg.description = params.description
        msg.thumbData = bmpToByteArray(params.thumbBmp, true)

        sendMessageToWX("music", msg)
    }

    private fun shareVideo(params: WxVideoParams) {
        val video = WXVideoObject()
        video.videoUrl = params.videoUrl

        val msg = WXMediaMessage(video)
        msg.title = params.title
        msg.description = params.description
        msg.thumbData = bmpToByteArray(params.thumbBmp, true)

        sendMessageToWX("video", msg)
    }

    private fun sharePage(params: WxPageParams) {
        // 初始化一个WXWebpageObject，填写url
        val webpage = WXWebpageObject()
        webpage.webpageUrl = params.webPageUrl

        // 用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(webpage)
        msg.title = params.title
        msg.description = params.description
        msg.thumbData = bmpToByteArray(params.thumbBmp, true)

        sendMessageToWX("webpage", msg)
    }

    private fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}