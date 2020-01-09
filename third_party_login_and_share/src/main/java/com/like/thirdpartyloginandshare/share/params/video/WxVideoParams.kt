package com.like.thirdpartyloginandshare.share.params.video

import android.graphics.Bitmap
import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param videoUrl  视频的URL地址。限制长度不超过10KB
 * @param thumbBmp  128kb以内
 */
data class WxVideoParams(
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbBmp: Bitmap
) : ShareParams