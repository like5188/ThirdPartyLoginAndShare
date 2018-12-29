package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.like.thirdpartyloginandshare.share.params.app.AppParams
import com.like.thirdpartyloginandshare.share.params.image.ImageParams
import com.like.thirdpartyloginandshare.share.params.image.WbImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.ImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.multiimage.MultiImageParams
import com.like.thirdpartyloginandshare.share.params.multiimage.WbMultiImageParams
import com.like.thirdpartyloginandshare.share.params.music.MusicParams
import com.like.thirdpartyloginandshare.share.params.page.PageParams
import com.like.thirdpartyloginandshare.share.params.page.WbPageParams
import com.like.thirdpartyloginandshare.share.params.text.TextParams
import com.like.thirdpartyloginandshare.share.params.text.WbTextParams
import com.like.thirdpartyloginandshare.share.params.video.VideoParams
import com.like.thirdpartyloginandshare.share.params.video.WbVideoParams
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.sina.weibo.sdk.api.*
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import com.sina.weibo.sdk.utils.Utility

class WbShare(activity: Activity) : ShareStrategy(activity) {
    private val shareHandler by lazy { ApiFactory.createWbShareApi(activity) }
    private lateinit var mShareListener: ShareListener

    override fun setShareListener(listener: OnLoginAndShareListener): ShareStrategy {
        mShareListener = ShareListener(listener)
        return this
    }

    init {
        shareHandler.registerApp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        shareHandler.doResultIntent(data, mShareListener)
    }

    override fun shareText(params: TextParams) {
        if (params !is WbTextParams) return
        val textObject = TextObject()
        textObject.text = params.text

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.textObject = textObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    override fun shareImage(params: ImageParams) {
        if (params !is WbImageParams) return
        val imageObject = ImageObject()
        imageObject.setImageObject(params.bmp)

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.imageObject = imageObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    override fun shareMultiImage(params: MultiImageParams) {
        if (params !is WbMultiImageParams) return
        val multiImageObject = MultiImageObject()
        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
        val pathList = ArrayList<Uri>()
        params.images?.mapTo(pathList) { Uri.fromFile(it) }
        multiImageObject.setImageList(pathList)

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.multiImageObject = multiImageObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    override fun shareImageAndText(params: ImageAndTextParams) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareMusic(params: MusicParams) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun shareVideo(params: VideoParams) {
        if (params !is WbVideoParams) return
        //获取视频
        val videoSourceObject = VideoSourceObject()
        videoSourceObject.videoPath = params.videoUri

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.videoSourceObject = videoSourceObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    override fun shareApp(params: AppParams) {
        throw UnsupportedOperationException("SINA不支持此操作")
    }

    override fun sharePage(params: PageParams) {
        if (params !is WbPageParams) return
        val mediaObject = WebpageObject()
        mediaObject.identify = Utility.generateGUID()
        mediaObject.title = params.title
        mediaObject.description = params.description
        // 设置 Bitmap 类型的图片到视频对象里
        // 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(params.thumbBmp)
        mediaObject.actionUrl = params.actionUrl
        mediaObject.defaultText = params.defaultText

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.mediaObject = mediaObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    class ShareListener(private val listener: OnLoginAndShareListener) : WbShareCallback {
        override fun onWbShareSuccess() {
            listener.onSuccess()
        }

        override fun onWbShareFail() {
            listener.onFailure("分享失败")
        }

        override fun onWbShareCancel() {
            listener.onCancel()
        }

    }

}