package com.like.thirdpartyloginandshare.share.params.image

import android.graphics.Bitmap

/**
 * @param bmp 内容大小不超过10MB
 */
data class WxImageParams(val bmp: Bitmap, val thumbBmp: Bitmap) : ImageParams