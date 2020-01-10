package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.like.thirdpartyloginandshare.R
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.share.params.app.QqAppParams
import com.like.thirdpartyloginandshare.share.params.image.QqImageParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QqImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.music.QqMusicParams
import com.like.thirdpartyloginandshare.share.params.program.QqProgramParams
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
class QqShare(private val activity: Activity) : ShareStrategy {
    private val mTencent by lazy {
        ApiFactory.createQqApi(activity.applicationContext, ThirdPartyInit.qqInitParams.appId)
    }
    private var mShareListener: ShareListener? = null
    private var mOnLoginAndShareListener: OnLoginAndShareListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener)
        }
    }

    override fun setShareListener(listener: OnLoginAndShareListener): ShareStrategy {
        mOnLoginAndShareListener = listener
        mShareListener = ShareListener(listener)
        return this
    }

    override fun share(params: ShareParams) {
        if (!mTencent.isQQInstalled(activity.applicationContext)) {
            mOnLoginAndShareListener?.onFailure("您的手机没有安装QQ")
            return
        }
        when (params) {
            is QqImageParams -> {
                shareImage(params)
            }
            is QqImageAndTextParams -> {
                shareImageAndText(params)
            }
            is QqMusicParams -> {
                shareMusic(params)
            }
            is QqAppParams -> {
                shareApp(params)
            }
            is QqProgramParams -> {
                shareProgram(params)
            }
            else -> throw UnsupportedOperationException("QQ不支持此操作")
        }
    }

    private fun shareImage(params: QqImageParams) {
        with(Bundle()) {
            // 分享的类型。
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
            // 需要分享的本地图片路径
            putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, params.imageLocalUrl)
            // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
            putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.resources.getString(R.string.app_name))
            // 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
            // Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
            putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    private fun shareImageAndText(params: QqImageAndTextParams) {
        with(Bundle()) {
            // 分享的类型。
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

    private fun shareMusic(params: QqMusicParams) {
        with(Bundle()) {
            // 分享的类型。
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
            // 携带ARK信息的同时，需要携带原本的图文信息，ARK信息只是作为可选项，传递的JSON串为空或者不规范，分享的仍然为原本的图文信息。
            putString(QQShare.SHARE_TO_QQ_ARK_INFO, params.arkStr)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    private fun shareApp(params: QqAppParams) {
        with(Bundle()) {
            // 分享的类型。
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
            // 携带ARK信息的同时，需要携带原本的图文信息，ARK信息只是作为可选项，传递的JSON串为空或者不规范，分享的仍然为原本的图文信息。
            putString(QQShare.SHARE_TO_QQ_ARK_INFO, params.arkStr)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    private fun shareProgram(params: QqProgramParams) {
        with(Bundle()) {
            // 分享的类型。
            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_MINI_PROGRAM)
            // 分享的标题, 最长30个字符。
            putString(QQShare.SHARE_TO_QQ_TITLE, params.title)
            // 分享的消息摘要，最长40个字。
            putString(QQShare.SHARE_TO_QQ_SUMMARY, params.summary)
            // 分享图片的URL或者本地路径
            putString(QQShare.SHARE_TO_QQ_IMAGE_URL, params.imageUrl)
            // 分享的app的链接
            putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.targetUrl)
            putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID, params.programAppId)
            putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH, params.programPath)
            putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE, params.programType)
            mTencent.shareToQQ(activity, this, mShareListener)
        }
    }

    class ShareListener(private val listener: OnLoginAndShareListener) : IUiListener {
        override fun onComplete(response: Any?) {
            listener.onSuccess()
        }

        override fun onError(e: UiError) {
            listener.onFailure(e.errorDetail)
        }

        override fun onCancel() {
            listener.onCancel()
        }

    }

}