package com.like.thirdpartyloginandshare.share.params.music

/**
 * 分享至微信的音乐，直接点击好友会话或朋友圈下的分享内容会跳转至第三方 APP，
 * 点击会话列表顶部的音乐分享内容将跳转至微信原生音乐播放器播放。
 *
 * @param title     分享的标题, 最长30个字符。
 * @param audioUrl  音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
 * @param targetUrl 这条分享消息被好友点击后的跳转URL。
 * @param summary   分享的消息摘要，最长40个字。
 * @param imageUrl  分享图片的URL或者本地路径
 */
data class QqMusicParams(
    val title: String,
    val audioUrl: String,
    val targetUrl: String,
    val summary: String = "",
    val imageUrl: String = ""
) : MusicParams