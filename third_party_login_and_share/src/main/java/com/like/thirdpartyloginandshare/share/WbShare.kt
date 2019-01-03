package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.share.params.image.WbImageParams
import com.like.thirdpartyloginandshare.share.params.multiimage.WbMultiImageParams
import com.like.thirdpartyloginandshare.share.params.page.WbPageParams
import com.like.thirdpartyloginandshare.share.params.text.WbTextParams
import com.like.thirdpartyloginandshare.share.params.video.WbVideoParams
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.sina.weibo.sdk.api.*
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.utils.Utility

class WbShare(private val activity: Activity) : ShareStrategy {
    private val shareHandler by lazy { ApiFactory.createWbShareApi(activity) }
    private lateinit var mShareListener: ShareListener

    init {
        shareHandler.registerApp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        shareHandler.doResultIntent(data, mShareListener)
    }

    override fun setShareListener(listener: OnLoginAndShareListener): ShareStrategy {
        mShareListener = ShareListener(listener)
        return this
    }

    override fun share(params: ShareParams) {
        when (params) {
            is WbTextParams -> {
                shareText(params)
            }
            is WbImageParams -> {
                shareImage(params)
            }
            is WbMultiImageParams -> {
                shareMultiImage(params)
            }
            is WbVideoParams -> {
                shareVideo(params)
            }
            is WbPageParams -> {
                sharePage(params)
            }
            else -> throw UnsupportedOperationException("WB不支持此操作")
        }
    }

    private fun shareText(params: WbTextParams) {
        val textObject = TextObject()
        textObject.text = params.text

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.textObject = textObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    private fun shareImage(params: WbImageParams) {
        val imageObject = ImageObject()
        imageObject.setImageObject(params.bmp)

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.imageObject = imageObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    private fun shareMultiImage(params: WbMultiImageParams) {
        // 分享多图时，必须添加TextObject，否则会报错java.lang.SecurityException: No permission to write APN settings
        val textObject = TextObject()
        textObject.text = params.text

        val multiImageObject = MultiImageObject()
        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
        multiImageObject.setImageList(params.imageUris)

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.textObject = textObject
        weiboMessage.multiImageObject = multiImageObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    private fun shareVideo(params: WbVideoParams) {
        // 分享视频时，必须添加TextObject，否则会报错java.lang.SecurityException: No permission to write APN settings
        val textObject = TextObject()
        textObject.text = params.text

        //获取视频
        val videoSourceObject = VideoSourceObject()
        videoSourceObject.videoPath = params.videoUri

        val weiboMessage = WeiboMultiMessage()
        weiboMessage.textObject = textObject
        weiboMessage.videoSourceObject = videoSourceObject
        shareHandler.shareMessage(weiboMessage, false)
    }

    private fun sharePage(params: WbPageParams) {
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