package com.like.thirdpartyloginandshare.share

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import java.io.File

abstract class ShareStrategy(protected val activity: Activity) {
    protected val applicationContext = activity.applicationContext
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    abstract fun setShareListener(listener: OnLoginAndShareListener)
    /**
     * QQ分享图文消息
     *
     * @param title         分享的标题, 最长30个字符。
     * @param targetUrl     这条分享消息被好友点击后的跳转URL。
     * @param summary       分享的消息摘要，最长40个字。
     * @param imageUrl      分享图片的URL或者本地路径
     * @param arkStr        Ark JSON串
     */
    abstract fun shareImageAndText(
        title: String,
        targetUrl: String,
        summary: String = "",
        imageUrl: String = "",
        arkStr: String = ""
    )

    /**
     * QQ分享图片
     *
     * @param imageLocalUrl     需要分享的本地图片路径
     */
    abstract fun shareImage(imageLocalUrl: String)

    /**
     * QQ分享音乐
     *
     * @param title         分享的标题, 最长30个字符。
     * @param audioUrl      音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
     * @param targetUrl     这条分享消息被好友点击后的跳转URL。
     * @param summary       分享的消息摘要，最长40个字。
     * @param imageUrl      分享图片的URL或者本地路径
     */
    abstract fun shareAudio(
        title: String,
        audioUrl: String,
        targetUrl: String,
        summary: String = "",
        imageUrl: String = ""
    )

    /**
     * QQ分享应用
     *
     * @param title         分享的标题, 最长30个字符。
     * @param summary       分享的消息摘要，最长40个字。
     * @param imageUrl      分享图片的URL或者本地路径
     */
    abstract fun shareApp(
        title: String,
        summary: String = "",
        imageUrl: String = ""
    )

    /**
     * QQ空间分享图文消息
     *
     * @param title         分享的标题，最多200个字符
     * @param targetUrl     需要跳转的链接，URL字符串
     * @param summary       分享的摘要，最多600字符
     * @param imageUrl      分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
     */
    abstract fun shareImageAndText(
        title: String,
        targetUrl: String,
        summary: String = "",
        imageUrl: ArrayList<String>? = null
    )

    /**
     * QQ空间发表说说、上传图片
     *
     * @param summary           说说正文（传图和传视频接口会过滤第三方传过来的自带描述，目的为了鼓励用户自行输入有价值信息）
     * @param imageUrl          说说的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：<=9张图片为发表说说，>9张为上传图片到相册），只支持本地图片
     * @param scene             区分分享场景，用于异化feeds点击行为和小尾巴展示
     * @param callback          游戏自定义字段，点击分享消息回到游戏时回传给游戏
     */
    abstract fun publishMood(
        summary: String = "",
        imageUrl: ArrayList<String>? = null,
        scene: String = "",
        callback: String = ""
    )

    /**
     * QQ空间发表视频
     *
     * @param videoLocalPath    发表的视频，只支持本地地址，发表视频时必填；上传视频的大小最好控制在100M以内
     * @param scene             区分分享场景，用于异化feeds点击行为和小尾巴展示
     * @param callback          游戏自定义字段，点击分享消息回到游戏时回传给游戏
     */
    abstract fun publishVideo(
        videoLocalPath: String,
        scene: String = "",
        callback: String = ""
    )


    /**
     * 微博分享文本
     */
    abstract fun shareText(text: String, title: String, actionUrl: String)

    /**
     * 微博分享图片
     */
    abstract fun shareImage(bmp: Bitmap)

    /**
     * 微博分享多图
     *
     * @param images 本地图片文件
     */
    abstract fun shareMultiImage(images: List<File>)

    /**
     * 微博分享视频
     *
     * @param video 本地视频文件
     */
    abstract fun shareVideo(video: File)

    /**
     * 微博分享多媒体（网页）
     *
     * @param thumbBmp      缩略图。不得超过 32kb
     * @param title
     * @param description
     * @param actionUrl
     * @param defaultText   默认文案
     */
    abstract fun shareMedia(
        thumbBmp: Bitmap,
        title: String,
        description: String,
        actionUrl: String,
        defaultText: String
    )


    /**
     * 微信分享文本
     *
     * @param text      文本。长度需大于0且不超过10KB
     * @param scene     场景。发送到聊天界面：[com.tencent.mm.abstractsdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    abstract fun shareText(text: String, scene: Int)

    /**
     * 微信分享图片
     *
     * @param bmp       图片。内容大小不超过10MB
     * @param thumbBmp  缩略图
     * @param scene     场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    abstract fun shareImage(bmp: Bitmap, thumbBmp: Bitmap, scene: Int)

    /**
     * 微信分享音乐
     *
     * 分享至微信的音乐，直接点击好友会话或朋友圈下的分享内容会跳转至第三方 APP，
     * 点击会话列表顶部的音乐分享内容将跳转至微信原生音乐播放器播放。
     *
     * @param title         标题
     * @param description   描述
     * @param musicUrl      音频的URL地址。限制长度不超过10KB
     * @param thumbBmp      缩略图
     * @param scene         场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    abstract fun shareMusic(title: String, description: String, musicUrl: String, thumbBmp: Bitmap, scene: Int)

    /**
     * 微信分享视频
     *
     * @param title         标题
     * @param description   描述
     * @param videoUrl      视频的URL地址。限制长度不超过10KB
     * @param thumbBmp      缩略图
     * @param scene         场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    abstract fun shareVideo(title: String, description: String, videoUrl: String, thumbBmp: Bitmap, scene: Int)

    /**
     * 微信分享网页
     *
     * @param title         标题
     * @param description   描述
     * @param webPageUrl    html链接。限制长度不超过10KB
     * @param thumbBmp      缩略图
     * @param scene         场景。发送到聊天界面：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession]；发送到朋友圈：[com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline]
     */
    abstract fun shareWebpage(title: String, description: String, webPageUrl: String, thumbBmp: Bitmap, scene: Int)
}