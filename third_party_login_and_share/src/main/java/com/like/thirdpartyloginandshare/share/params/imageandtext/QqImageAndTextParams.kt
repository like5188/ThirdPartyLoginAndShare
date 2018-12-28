package com.like.thirdpartyloginandshare.share.params.imageandtext

/**
 * @param title         分享的标题, 最长30个字符。
 * @param targetUrl     这条分享消息被好友点击后的跳转URL
 * @param summary       分享的消息摘要，最长40个字。
 * @param imageUrl      分享图片的URL或者本地路径
 * @param arkStr        Ark JSON串
 */
data class QqImageAndTextParams(
    val title: String,
    val targetUrl: String,
    val summary: String = "",
    val imageUrl: String = "",
    val arkStr: String = ""
) : ImageAndTextParams