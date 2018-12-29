package com.like.thirdpartyloginandshare.share.params.multiimage

import android.net.Uri

/**
 * @param imageUris    本地图片文件
 */
data class WbMultiImageParams(val imageUris: ArrayList<Uri>? = null) : MultiImageParams