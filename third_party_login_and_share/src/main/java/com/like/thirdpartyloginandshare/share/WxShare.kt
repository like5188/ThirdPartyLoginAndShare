package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import com.like.thirdpartyloginandshare.login.WxLogin
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.WX_OPEN_ID
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import java.io.ByteArrayOutputStream
import java.io.File


class WxShare(activity: Activity) : ShareStrategy(activity) {
    private val mWxApi: IWXAPI by lazy {
        WxLogin.getInstance(activity).mWxApi
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun setShareListener(listener: OnLoginAndShareListener) {
    }

    /**
     * 分享文本
     *
     * @param text      文本。长度需大于0且不超过10KB
     * @param scene     场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    override fun shareText(text: String, scene: Int) {
        // 初始化一个 WXTextObject 对象，填写分享的文本内容
        val textObj = WXTextObject()
        textObj.text = text

        // 初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = text

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = scene

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    /**
     * 分享图片
     *
     * @param bmp       图片。内容大小不超过10MB
     * @param thumbBmp  缩略图
     * @param scene     场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    override fun shareImage(bmp: Bitmap, thumbBmp: Bitmap, scene: Int) {
        // 初始化一个 WXImageObject 对象
        val imgObj = WXImageObject(bmp)
        bmp.recycle()

        // 初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        msg.thumbData = bmpToByteArray(thumbBmp, true)

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = scene
        req.userOpenId = WX_OPEN_ID

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    /**
     * 分享音乐
     *
     * 分享至微信的音乐，直接点击好友会话或朋友圈下的分享内容会跳转至第三方 APP，
     * 点击会话列表顶部的音乐分享内容将跳转至微信原生音乐播放器播放。
     *
     * @param title         标题
     * @param description   描述
     * @param musicUrl      音频的URL地址。限制长度不超过10KB
     * @param thumbBmp      缩略图
     * @param scene         场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    override fun shareMusic(title: String, description: String, musicUrl: String, thumbBmp: Bitmap, scene: Int) {
        //初始化一个WXMusicObject，填写url
        val music = WXMusicObject()
        music.musicUrl = musicUrl

        // 用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = title
        msg.description = description
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("music")
        req.message = msg
        req.scene = scene
        req.userOpenId = WX_OPEN_ID

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    /**
     * 分享视频
     *
     * @param title         标题
     * @param description   描述
     * @param videoUrl      视频的URL地址。限制长度不超过10KB
     * @param thumbBmp      缩略图
     * @param scene         场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    override fun shareVideo(title: String, description: String, videoUrl: String, thumbBmp: Bitmap, scene: Int) {
        // 初始化一个WXVideoObject，填写url
        val video = WXVideoObject()
        video.videoUrl = videoUrl

        // 用 WXVideoObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(video)
        msg.title = title
        msg.description = description
        msg.thumbData = bmpToByteArray(thumbBmp, true)

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("video")
        req.message = msg
        req.scene = scene
        req.userOpenId = WX_OPEN_ID

        // 调用api接口，发送数据到微信
        mWxApi.sendReq(req)
    }

    /**
     * 分享网页
     *
     * @param title         标题
     * @param description   描述
     * @param webPageUrl    html链接。限制长度不超过10KB
     * @param thumbBmp      缩略图
     * @param scene         场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    override fun shareWebpage(title: String, description: String, webPageUrl: String, thumbBmp: Bitmap, scene: Int) {
        // 初始化一个WXWebpageObject，填写url
        val webpage = WXWebpageObject()
        webpage.webpageUrl = webPageUrl

        // 用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(webpage)
        msg.title = title
        msg.description = description
        msg.thumbData = bmpToByteArray(thumbBmp, true)

        // 构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = scene
        req.userOpenId = WX_OPEN_ID

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

    override fun shareImageAndText(
        title: String,
        targetUrl: String,
        summary: String,
        imageUrl: String,
        arkStr: String
    ) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareImage(imageLocalUrl: String) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareAudio(title: String, audioUrl: String, targetUrl: String, summary: String, imageUrl: String) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareApp(title: String, summary: String, imageUrl: String) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareImageAndText(title: String, targetUrl: String, summary: String, imageUrl: ArrayList<String>?) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun publishMood(summary: String, imageUrl: ArrayList<String>?, scene: String, callback: String) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun publishVideo(videoLocalPath: String, scene: String, callback: String) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareText(text: String, title: String, actionUrl: String) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareImage(bmp: Bitmap) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareMultiImage(images: List<File>) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareVideo(video: File) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }

    override fun shareMedia(
        thumbBmp: Bitmap,
        title: String,
        description: String,
        actionUrl: String,
        defaultText: String
    ) {
        throw UnsupportedOperationException("WEIXIN不支持此操作")
    }
}