package com.like.thirdpartyloginandshare.share.params.app

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param title         分享的标题, 最长30个字符。
 * @param targetUrl     App链接
 * @param summary       分享的消息摘要，最长40个字。
 * @param imageUrl      分享图片的URL或者本地路径
 */
data class QqAppParams(
    val title: String,
    val targetUrl: String,
    val summary: String = "",
    val imageUrl: String = ""
) : ShareParams