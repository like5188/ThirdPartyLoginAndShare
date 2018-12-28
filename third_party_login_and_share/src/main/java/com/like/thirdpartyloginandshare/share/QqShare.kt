package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.like.thirdpartyloginandshare.R
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.QQ_APP_ID
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import java.io.File
import java.lang.UnsupportedOperationException


/**
 * QQ分享工具类
 * QQ分享无需QQ登录
 */
class QqShare(activity: Activity) : ShareStrategy(activity) {
    private val mTencent = Tencent.createInstance(QQ_APP_ID, activity.applicationContext)
    private lateinit var mShareListener: ShareListener

    override fun setShareListener(listener: OnLoginAndShareListener) {
        mShareListener = ShareListener(listener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener)
        }
    }

    /**
     * 分享图文消息
     *
     * @param title         分享的标题, 最长30个字符。
     * @param targetUrl     这条分享消息被好友点击后的跳转URL。
     * @param summary       分享的消息摘要，最长40个字。
     * @param imageUrl      分享图片的URL或者本地路径
     * @param arkStr        Ark JSON串
     */
    override fun shareImageAndText(
        title: String,
        targetUrl: String,
        summary: String,
        imageUrl: String,
        arkStr: String
    ) {
        val params = Bundle()
        // 分享的类型。图文分享(普通分享)
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        // 分享的标题, 最长30个字符。
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title)
        // 分享的消息摘要，最长40个字。
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary)
        // 这条分享消息被好友点击后的跳转URL。
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl)
        // 分享图片的URL或者本地路径
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl)
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
        // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN)
        // 携带ARK信息的同时，需要携带原本的图文信息，ARK信息只是作为可选项，传递的JSON串为空或者不规范，分享的仍然为原本的图文信息。
        params.putString(QQShare.SHARE_TO_QQ_ARK_INFO, arkStr)
        mTencent.shareToQQ(activity, params, mShareListener)
    }

    /**
     * 分享图片
     *
     * @param imageLocalUrl     需要分享的本地图片路径
     */
    override fun shareImage(imageLocalUrl: String) {
        val params = Bundle()
        // 分享的类型。
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
        // 需要分享的本地图片路径
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageLocalUrl)
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
        // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN)
        mTencent.shareToQQ(activity, params, mShareListener)
    }

    /**
     * 分享音乐
     *
     * @param title         分享的标题, 最长30个字符。
     * @param audioUrl      音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
     * @param targetUrl     这条分享消息被好友点击后的跳转URL。
     * @param summary       分享的消息摘要，最长40个字。
     * @param imageUrl      分享图片的URL或者本地路径
     */
    override fun shareAudio(
        title: String,
        audioUrl: String,
        targetUrl: String,
        summary: String,
        imageUrl: String
    ) {
        val params = Bundle()
        // 分享的类型。图文分享(普通分享)
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO)
        // 分享的标题, 最长30个字符。
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title)
        // 分享的消息摘要，最长40个字。
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary)
        // 这条分享消息被好友点击后的跳转URL。
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl)
        // 分享图片的URL或者本地路径
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl)
        // 音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl)
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
        // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN)
        mTencent.shareToQQ(activity, params, mShareListener)
    }

    /**
     * 分享应用
     *
     * @param title         分享的标题, 最长30个字符。
     * @param summary       分享的消息摘要，最长40个字。
     * @param imageUrl      分享图片的URL或者本地路径
     */
    override fun shareApp(
        title: String,
        summary: String,
        imageUrl: String,
        targetUrl: String
    ) {
        val params = Bundle()
        // 分享的类型。图文分享(普通分享)
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP)
        // 分享的标题, 最长30个字符。
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title)
        // 分享的消息摘要，最长40个字。
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary)
        // 分享图片的URL或者本地路径
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl)
        // 分享的app的链接
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl)
        // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
        // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
        // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN)
        mTencent.shareToQQ(activity, params, mShareListener)
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

    override fun shareImageAndText(title: String, targetUrl: String, summary: String, imageUrl: ArrayList<String>?) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun publishMood(summary: String, imageUrl: ArrayList<String>?, scene: String, callback: String) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun publishVideo(videoLocalPath: String, scene: String, callback: String) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareText(text: String, title: String, actionUrl: String) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareImage(bmp: Bitmap) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareMultiImage(images: List<File>) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareVideo(video: File) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareMedia(
        thumbBmp: Bitmap,
        title: String,
        description: String,
        actionUrl: String,
        defaultText: String
    ) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareText(text: String) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareImage(bmp: Bitmap, thumbBmp: Bitmap) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareMusic(title: String, description: String, musicUrl: String, thumbBmp: Bitmap) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareVideo(title: String, description: String, videoUrl: String, thumbBmp: Bitmap) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }

    override fun shareWebpage(title: String, description: String, webPageUrl: String, thumbBmp: Bitmap) {
        throw UnsupportedOperationException("QQ不支持此操作")
    }
}