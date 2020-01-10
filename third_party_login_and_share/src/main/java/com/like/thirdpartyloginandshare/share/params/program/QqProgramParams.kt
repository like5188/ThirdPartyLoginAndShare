package com.like.thirdpartyloginandshare.share.params.program

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param title         分享的标题, 最长30个字符。
 * @param targetUrl     这条分享消息被好友点击后的跳转URL
 * @param summary       分享的消息摘要，最长40个字。
 * @param imageUrl      分享图片的URL或者本地路径
 * @param programAppId
 * @param programPath
 * @param programType
 */
data class QqProgramParams(
    val title: String,
    val targetUrl: String,
    val summary: String = "",
    val imageUrl: String = "",
    val programAppId: String = "",
    val programPath: String = "",
    val programType: String = ""
) : ShareParams