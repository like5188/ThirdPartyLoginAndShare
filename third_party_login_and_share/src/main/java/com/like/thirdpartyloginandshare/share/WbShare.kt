package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.sina.weibo.sdk.api.*
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import com.sina.weibo.sdk.utils.Utility
import java.io.File

class WbShare(activity: Activity) : ShareStrategy(activity) {
    private val shareHandler = WbShareHandler(activity)
    private lateinit var mShareListener: ShareListener

    override fun setShareListener(listener: OnLoginAndShareListener) {
        mShareListener = ShareListener(listener)
    }

    init {
        shareHandler.registerApp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        shareHandler.doResultIntent(data, mShareListener)
    }

    /**
     * 分享文本
     */
    override fun shareText(text: String, title: String, actionUrl: String) {
        val textObject = TextObject()
        textObject.text = text
        textObject.title = title
        textObject.actionUrl = actionUrl

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.textObject = textObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    /**
     * 分享图片
     */
    override fun shareImage(bmp: Bitmap) {
        val imageObject = ImageObject()
        imageObject.setImageObject(bmp)

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.imageObject = imageObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    /**
     * 分享多图
     *
     * @param images 本地图片文件
     */
    override fun shareMultiImage(images: List<File>) {
        val multiImageObject = MultiImageObject()
        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
        val pathList = ArrayList<Uri>()
        images.mapTo(pathList) { Uri.fromFile(it) }
        multiImageObject.setImageList(pathList)

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.multiImageObject = multiImageObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    /**
     * 分享视频
     *
     * @param video 本地视频文件
     */
    override fun shareVideo(video: File) {
        //获取视频
        val videoSourceObject = VideoSourceObject()
        videoSourceObject.videoPath = Uri.fromFile(video)

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.videoSourceObject = videoSourceObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    /**
     * 分享多媒体（网页）
     *
     * @param thumbBmp      缩略图。不得超过 32kb
     * @param title
     * @param description
     * @param actionUrl
     * @param defaultText   默认文案
     */
    override fun shareMedia(
        thumbBmp: Bitmap,
        title: String,
        description: String,
        actionUrl: String,
        defaultText: String
    ) {
        val mediaObject = WebpageObject()
        mediaObject.identify = Utility.generateGUID()
        mediaObject.title = title
        mediaObject.description = description
        // 设置 Bitmap 类型的图片到视频对象里
        // 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(thumbBmp)
        mediaObject.actionUrl = actionUrl
        mediaObject.defaultText = defaultText

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.mediaObject = mediaObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    class ShareListener(private val listener: OnLoginAndShareListener) : WbShareCallback {
        override fun onWbShareFail() {
            onFailure("分享失败")
        }

        override fun onWbShareCancel() {
            onFailure("取消分享")
        }

        override fun onWbShareSuccess() {
            onSuccess()
        }

        fun onSuccess() {
            listener.onSuccess()
        }

        fun onFailure(errorMessage: String) {
            listener.onFailure(errorMessage)
        }

    }

    override fun shareImageAndText(
        title: String,
        targetUrl: String,
        summary: String,
        imageUrl: String,
        arkStr: String
    ) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareImage(imageLocalUrl: String) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareAudio(title: String, audioUrl: String, targetUrl: String, summary: String, imageUrl: String) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareApp(title: String, summary: String, imageUrl: String) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareImageAndText(title: String, targetUrl: String, summary: String, imageUrl: ArrayList<String>?) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun publishMood(summary: String, imageUrl: ArrayList<String>?, scene: String, callback: String) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun publishVideo(videoLocalPath: String, scene: String, callback: String) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareText(text: String, scene: Int) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareImage(bmp: Bitmap, thumbBmp: Bitmap, scene: Int) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareMusic(title: String, description: String, musicUrl: String, thumbBmp: Bitmap, scene: Int) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareVideo(title: String, description: String, videoUrl: String, thumbBmp: Bitmap, scene: Int) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareWebpage(title: String, description: String, webPageUrl: String, thumbBmp: Bitmap, scene: Int) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }
}