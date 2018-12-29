package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.like.thirdpartyloginandshare.R
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.share.params.app.AppParams
import com.like.thirdpartyloginandshare.share.params.app.QqAppParams
import com.like.thirdpartyloginandshare.share.params.image.ImageParams
import com.like.thirdpartyloginandshare.share.params.image.QqImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.ImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QqImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.multiimage.MultiImageParams
import com.like.thirdpartyloginandshare.share.params.music.MusicParams
import com.like.thirdpartyloginandshare.share.params.music.QqMusicParams
import com.like.thirdpartyloginandshare.share.params.page.PageParams
import com.like.thirdpartyloginandshare.share.params.text.TextParams
import com.like.thirdpartyloginandshare.share.params.video.VideoParams
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError

/**
 * QQ分享工具类
 * QQ分享无需QQ登录
 */
class QqShare(activity: Activity) : ShareStrategy(activity) {
    private val mTencent by lazy { ApiFactory.createQqApi(applicationContext, ThirdPartyInit.qqInitParams.appId) }
    private lateinit var mShareListener: ShareListener

    override fun setShareListener(listener: OnLoginAndShareListener): ShareStrategy {
        mShareListener = ShareListener(listener)
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener)
        }
    }

    override fun shareText(params: TextParams) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareImage(params: ImageParams) {
        if (params !is QqImageParams) return
        with(Bundle()) {
            // 分享的类型。
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
            // 需要分享的本地图片路径
            putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, params.imageLocalPath)
            // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
            // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
            putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    override fun shareMultiImage(params: MultiImageParams) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareImageAndText(params: ImageAndTextParams) {
        if (params !is QqImageAndTextParams) return
        with(Bundle()) {
            // 分享的类型。图文分享(普通分享)
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
            // 分享的标题, 最长30个字符。
            putString(QQShare.SHARE_TO_QQ_TITLE, params.title)
            // 分享的消息摘要，最长40个字。
            putString(QQShare.SHARE_TO_QQ_SUMMARY, params.summary)
            // 这条分享消息被好友点击后的跳转URL。
            putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.targetUrl)
            // 分享图片的URL或者本地路径
            putString(QQShare.SHARE_TO_QQ_IMAGE_URL, params.imageUrl)
            // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
            // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
            putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE)
            // 携带ARK信息的同时，需要携带原本的图文信息，ARK信息只是作为可选项，传递的JSON串为空或者不规范，分享的仍然为原本的图文信息。
            putString(QQShare.SHARE_TO_QQ_ARK_INFO, params.arkStr)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    override fun shareMusic(params: MusicParams) {
        if (params !is QqMusicParams) return
        with(Bundle()) {
            // 分享的类型。图文分享(普通分享)
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO)
            // 分享的标题, 最长30个字符。
            putString(QQShare.SHARE_TO_QQ_TITLE, params.title)
            // 分享的消息摘要，最长40个字。
            putString(QQShare.SHARE_TO_QQ_SUMMARY, params.summary)
            // 音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
            putString(QQShare.SHARE_TO_QQ_AUDIO_URL, params.audioUrl)
            // 音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
            putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.targetUrl)
            // 分享图片的URL或者本地路径
            putString(QQShare.SHARE_TO_QQ_IMAGE_URL, params.imageUrl)
            // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
            // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
            putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    override fun shareVideo(params: VideoParams) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareApp(params: AppParams) {
        if (params !is QqAppParams) return
        with(Bundle()) {
            // 分享的类型。图文分享(普通分享)
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP)
            // 分享的标题, 最长30个字符。
            putString(QQShare.SHARE_TO_QQ_TITLE, params.title)
            // 分享的消息摘要，最长40个字。
            putString(QQShare.SHARE_TO_QQ_SUMMARY, params.summary)
            // 分享图片的URL或者本地路径
            putString(QQShare.SHARE_TO_QQ_IMAGE_URL, params.imageUrl)
            // 分享的app的链接
            putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.targetUrl)
            // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
            // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
            putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    override fun sharePage(params: PageParams) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    class ShareListener(private val listener: OnLoginAndShareListener) : IUiListener {
        override fun onComplete(response: Any?) {
            listener.onSuccess()
        }

        override fun onError(e: UiError) {
            listener.onFailure("分享失败 ${e.errorDetail}")
        }

        override fun onCancel() {
            listener.onCancel()
        }

    }

}