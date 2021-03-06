package com.like.thirdpartyloginandshare.share.params.page

import android.graphics.Bitmap
import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param thumbBmp      缩略图。不得超过 32kb
 * @param defaultText   默认文案
 */
data class WbPageParams(
    val thumbBmp: Bitmap? = null,
    val title: String = "",
    val description: String = "",
    val actionUrl: String = "",
    val defaultText: String = ""
) : ShareParams