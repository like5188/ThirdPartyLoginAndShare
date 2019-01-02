package com.like.thirdpartyloginandshare.share.params.video

import android.graphics.Bitmap

/**
 * @param videoUrl  视频的URL地址。限制长度不超过10KB
 */
data class WxVideoParams(
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbBmp: Bitmap,
    val openId: String
) : VideoParams