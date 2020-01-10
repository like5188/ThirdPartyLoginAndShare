package com.like.thirdpartyloginandshare.share.params.image

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * 纯图片消息
 *
 * @param imageLocalUrl     需要分享的本地图片路径，图片不能大于5M
 */
data class QqImageParams(val imageLocalUrl: String) : ShareParams