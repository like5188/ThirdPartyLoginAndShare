package com.like.thirdpartyloginandshare.share.params.music

import android.graphics.Bitmap

/**
 * 分享至微信的音乐，直接点击好友会话或朋友圈下的分享内容会跳转至第三方 APP，
 * 点击会话列表顶部的音乐分享内容将跳转至微信原生音乐播放器播放。
 *
 * @param musicUrl  音频的URL地址。限制长度不超过10KB
 */
data class WxMusicParams(
    val title: String,
    val description: String,
    val musicUrl: String,
    val thumbBmp: Bitmap,
    val openId: String
) : MusicParams