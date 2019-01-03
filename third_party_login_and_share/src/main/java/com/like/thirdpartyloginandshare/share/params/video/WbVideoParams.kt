package com.like.thirdpartyloginandshare.share.params.video

import android.net.Uri
import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param videoUri 本地视频文件
 */
data class WbVideoParams(val videoUri: Uri, val text: String = "") : ShareParams