package com.like.thirdpartyloginandshare.share.params.video

import android.net.Uri

/**
 * @param videoUri 本地视频文件
 */
data class WbVideoParams(val videoUri: Uri, val text: String = "") : VideoParams