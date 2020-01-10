package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.share.params.ShareParams
import com.like.thirdpartyloginandshare.share.params.imageandtext.QZoneImageAndTextParams
import com.like.thirdpartyloginandshare.share.params.program.QZoneProgramParams
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError

/**
 * QQ空间分享工具类
 * QQ空间分享无需QQ登录
 */
class QZoneShare(private val activity: Activity) : ShareStrategy {
    private val mTencent by lazy {
        ApiFactory.createQqApi(activity.applicationContext, ThirdPartyInit.qqInitParams.appId)
    }
    private var mShareListener: ShareListener? = null
    private var mOnLoginAndShareListener: OnLoginAndShareListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_QZONE_SHARE) {
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
            is QZoneImageAndTextParams -> {
                shareImageAndText(params)
            }
            is QZoneProgramParams -> {
                shareProgram(params)
            }
            else -> throw UnsupportedOperationException("QZONE不支持此操作")
        }
    }

    private fun shareImageAndText(params: QZoneImageAndTextParams) {
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
            putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, params.imageUrls)
            mTencent.shareToQzone(activity, this, mShareListener)
        }
    }

    private fun shareProgram(params: QZoneProgramParams) {
        with(Bundle()) {
            // 分享的类型。
            putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_MINI_PROGRAM)
            // 分享的标题, 最长30个字符。
            putString(QQShare.SHARE_TO_QQ_TITLE, params.title)
            // 分享的消息摘要，最长40个字。
            putString(QQShare.SHARE_TO_QQ_SUMMARY, params.summary)
            // 分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
            putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, params.imageUrls)
            // 分享的app的链接
            putString(QQShare.SHARE_TO_QQ_TARGET_URL, params.targetUrl)
            putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID, params.programAppId)
            putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH, params.programPath)
            putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE, params.programType)
            mTencent.shareToQzone(activity, this, mShareListener)
        }
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