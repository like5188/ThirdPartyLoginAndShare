package com.like.thirdpartyloginandshare.share.params.multiimage

import android.net.Uri
import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param imageUris    本地图片文件
 */
data class WbMultiImageParams(val imageUris: ArrayList<Uri>, val text: String = "") : ShareParams