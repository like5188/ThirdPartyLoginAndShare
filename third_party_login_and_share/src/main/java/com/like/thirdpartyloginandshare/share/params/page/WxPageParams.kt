package com.like.thirdpartyloginandshare.share.params.page

import android.graphics.Bitmap

/**
 * @param webPageUrl    html链接。限制长度不超过10KB
 */
data class WxPageParams(
    val title: String,
    val description: String,
    val webPageUrl: String,
    val thumbBmp: Bitmap
) : PageParams