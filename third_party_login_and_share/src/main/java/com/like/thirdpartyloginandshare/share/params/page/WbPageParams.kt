package com.like.thirdpartyloginandshare.share.params.page

import android.graphics.Bitmap

/**
 * @param thumbBmp      缩略图。不得超过 32kb
 * @param defaultText   默认文案
 */
data class WbPageParams(
    val thumbBmp: Bitmap,
    val title: String,
    val description: String,
    val actionUrl: String,
    val defaultText: String
) : PageParams