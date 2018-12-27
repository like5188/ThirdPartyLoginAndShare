package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QzonePublish
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent

/**
 * QQ空间分享工具类
 * QQ空间分享无需QQ登录
 */
class QqZoneShare(private val activity: Activity, private val mShareListener: IUiListener) {
    private val mTencent = Tencent.createInstance(QQ_APP_ID, activity.applicationContext)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener)
        }
    }

    /**
     * 分享图文消息
     *
     * @param title         分享的标题，最多200个字符
     * @param targetUrl     需要跳转的链接，URL字符串
     * @param summary       分享的摘要，最多600字符
     * @param imageUrl      分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
     */
    fun shareImageAndText(
        title: String,
        targetUrl: String,
        summary: String = "",
        imageUrl: ArrayList<String>? = null
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
        mTencent.shareToQzone(activity, params, mShareListener)
    }

    /**
     * 发表说说、上传图片
     *
     * @param summary           说说正文（传图和传视频接口会过滤第三方传过来的自带描述，目的为了鼓励用户自行输入有价值信息）
     * @param imageUrl          说说的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：<=9张图片为发表说说，>9张为上传图片到相册），只支持本地图片
     * @param scene             区分分享场景，用于异化feeds点击行为和小尾巴展示
     * @param callback          游戏自定义字段，点击分享消息回到游戏时回传给游戏
     */
    fun publishMood(
        summary: String = "",
        imageUrl: ArrayList<String>? = null,
        scene: String = "",
        callback: String = ""
    ) {
        val params = Bundle()
        // 分享的类型
        // QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD（发表说说、上传图片）
        // QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO（发表视频）
        params.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD)
        // 说说正文（传图和传视频接口会过滤第三方传过来的自带描述，目的为了鼓励用户自行输入有价值信息）
        params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, summary)
        // 说说的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：<=9张图片为发表说说，>9张为上传图片到相册），只支持本地图片
        params.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imageUrl)

        val extParams = Bundle()
        // 区分分享场景，用于异化feeds点击行为和小尾巴展示
        extParams.putString(QzonePublish.HULIAN_EXTRA_SCENE, scene)
        // 游戏自定义字段，点击分享消息回到游戏时回传给游戏
        extParams.putString(QzonePublish.HULIAN_CALL_BACK, callback)
        params.putBundle(QzonePublish.PUBLISH_TO_QZONE_EXTMAP, extParams)

        mTencent.publishToQzone(activity, params, mShareListener)
    }

    /**
     * 发表视频
     *
     * @param videoLocalPath    发表的视频，只支持本地地址，发表视频时必填；上传视频的大小最好控制在100M以内
     * @param scene             区分分享场景，用于异化feeds点击行为和小尾巴展示
     * @param callback          游戏自定义字段，点击分享消息回到游戏时回传给游戏
     */
    fun publishVideo(
        videoLocalPath: String,
        scene: String = "",
        callback: String = ""
    ) {
        val params = Bundle()
        // 分享的类型
        // QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD（发表说说、上传图片）
        // QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO（发表视频）
        params.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO)
        // 发表的视频，只支持本地地址，发表视频时必填；上传视频的大小最好控制在100M以内
        // （因为QQ普通用户上传视频必须在100M以内，黄钻用户可上传1G以内视频，大于1G会直接报错。）
        params.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, videoLocalPath)

        val extParams = Bundle()
        // 区分分享场景，用于异化feeds点击行为和小尾巴展示
        extParams.putString(QzonePublish.HULIAN_EXTRA_SCENE, scene)
        // 游戏自定义字段，点击分享消息回到游戏时回传给游戏
        extParams.putString(QzonePublish.HULIAN_CALL_BACK, callback)
        params.putBundle(QzonePublish.PUBLISH_TO_QZONE_EXTMAP, extParams)

        mTencent.publishToQzone(activity, params, mShareListener)
    }

}