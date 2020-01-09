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

class WxShare(private val activity: Activity) : ShareStrategy {
    private var scene: Int = WXSceneSession
    private val mWxApi: IWXAPI by lazy {
        ApiFactory.createWxApi(activity.applicationContext, ThirdPartyInit.wxInitParams.appId)
    }
    private lateinit var mListener: OnLoginAndShareListener

    fun setScene(scene: Int): ShareStrategy {
        this.scene = scene
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun setShareListener(listener: OnLoginAndShareListener): ShareStrategy {
        this.mListener = listener
        return this
    }

    override fun share(params: ShareParams) {
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
            else -> when (scene) {
                WXSceneSession -> throw UnsupportedOperationException("WX不支持此操作")
                WXSceneTimeline -> throw UnsupportedOperationException("WX_CIRCLE不支持此操作")
            }
        }
    }

    fun onShareSuccess() {
        mListener.onSuccess()
    }

    fun onShareFailure(errStr: String?) {
        mListener.onFailure(errStr ?: "")
    }

    fun onCancel() {
        mListener.onCancel()
    }

    private fun shareText(params: WxTextParams) {
        // 初始化一个 WXTextObject 对象，填写分享的文本内容
        val textObj = WXTextObject()
        textObj.text = params.text

        // 初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = params.text

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = scene

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    private fun shareImage(params: WxImageParams) {
        // 初始化一个 WXImageObject 对象
        val imgObj = WXImageObject(params.bmp)
        params.bmp.recycle()

        // 初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        msg.thumbData = bmpToByteArray(params.thumbBmp, true)

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = scene
        req.userOpenId = params.openId

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    private fun shareMusic(params: WxMusicParams) {
        // 初始化一个WXMusicObject，填写url
        val music = WXMusicObject()
        music.musicUrl = params.musicUrl

        // 用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = params.title
        msg.description = params.description
        msg.thumbData = bmpToByteArray(params.thumbBmp, true)

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("music")
        req.message = msg
        req.scene = scene
        req.userOpenId = params.openId

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    private fun shareVideo(params: WxVideoParams) {
        // 初始化一个WXVideoObject，填写url
        val video = WXVideoObject()
        video.videoUrl = params.videoUrl

        // 用 WXVideoObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(video)
        msg.title = params.title
        msg.description = params.description
        msg.thumbData = bmpToByteArray(params.thumbBmp, true)

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("video")
        req.message = msg
        req.scene = scene
        req.userOpenId = params.openId

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
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

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = scene
        req.userOpenId = params.openId

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    private fun buildTransaction(str: String): String? = "$str${System.currentTimeMillis()}"

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