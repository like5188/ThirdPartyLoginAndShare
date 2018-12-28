package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.like.thirdpartyloginandshare.share.params.app.AppParams
import com.like.thirdpartyloginandshare.share.params.image.ImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.ImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QZoneImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.multiimage.MultiImageParams
import com.like.thirdpartyloginandshare.share.params.music.MusicParams
import com.like.thirdpartyloginandshare.share.params.page.PageParams
import com.like.thirdpartyloginandshare.share.params.text.TextParams
import com.like.thirdpartyloginandshare.share.params.video.VideoParams
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.QQ_APP_ID
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError

/**
 * QQ空间分享工具类
 * QQ空间分享无需QQ登录
 */
class QZoneShare(activity: Activity) : ShareStrategy(activity) {
    private val mTencent = Tencent.createInstance(QQ_APP_ID, activity.applicationContext)
    private lateinit var mShareListener: ShareListener

    override fun setShareListener(listener: OnLoginAndShareListener): ShareStrategy {
        mShareListener = ShareListener(listener)
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener)
        }
    }

    override fun shareText(params: TextParams) {
        throw UnsupportedOperationException("QZONE不支持此操作")
    }

    override fun shareImage(params: ImageParams) {
        throw UnsupportedOperationException("QZONE不支持此操作")
    }

    override fun shareMultiImage(params: MultiImageParams) {
        throw UnsupportedOperationException("QZONE不支持此操作")
    }

    override fun shareImageAndText(params: ImageAndTextParams) {
        if (params !is QZoneImageAndTextParams) return
        with(Bundle()) {
            // 分享的类型。
            putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
            // 分享的标题，最多200个字符
            putString(QzoneShare.SHARE_TO_QQ_TITLE, params.title)
            // 分享的摘要，最多600字符
            putString(QzoneShare.SHARE_TO_QQ_SUMMARY, params.summary)
            // 需要跳转的链接，URL字符串
            putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, params.targetUrl)
            // 分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
            putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, params.imageUrl)
            mTencent.shareToQzone(activity, this, mShareListener)
        }
    }

    override fun shareMusic(params: MusicParams) {
        throw UnsupportedOperationException("QZONE不支持此操作")
    }

    override fun shareVideo(params: VideoParams) {
        throw UnsupportedOperationException("QZONE不支持此操作")
    }

    override fun shareApp(params: AppParams) {
        throw UnsupportedOperationException("QZONE不支持此操作")
    }

    override fun sharePage(params: PageParams) {
        throw UnsupportedOperationException("QZONE不支持此操作")
    }

    class ShareListener(private val listener: OnLoginAndShareListener) : IUiListener {
        override fun onComplete(response: Any?) {
            onSuccess()
        }

        override fun onError(e: UiError) {
            onFailure("分享失败 ${e.errorDetail}")
        }

        override fun onCancel() {
            onFailure("取消分享")
        }

        fun onSuccess() {
            listener.onSuccess()
        }

        fun onFailure(errorMessage: String) {
            listener.onFailure(errorMessage)
        }

    }

}