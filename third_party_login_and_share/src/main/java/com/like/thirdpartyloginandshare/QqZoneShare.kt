package com.like.thirdpartyloginandshare

import android.app.Activity
import android.os.Bundle
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent


/**
 * QQ空间分享工具类
 * QQ空间分享无需QQ登录
 */
class QqZoneShare() {

    /**
     * 分享图文消息
     *
     * @param activity
     * @param title         分享的标题，最多200个字符
     * @param targetUrl     需要跳转的链接，URL字符串
     * @param summary       分享的摘要，最多600字符
     * @param imageUrl      分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
     * @param listener
     */
    fun shareImageAndText(
        activity: Activity,
        title: String,
        targetUrl: String,
        summary: String = "",
        imageUrl: ArrayList<String>? = null,
        listener: IUiListener? = null
    ) {
        val params = Bundle()
        // 分享的类型。
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
        // 分享的标题，最多200个字符
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title)
        // 分享的摘要，最多600字符
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary)
        // 需要跳转的链接，URL字符串
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl)
        // 分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrl)
        Tencent.createInstance(QQ_APP_ID, activity.applicationContext)?.shareToQzone(activity, params, listener)
    }

}