package com.like.thirdpartyloginandshare.share.params.imageandtext

/**
 * @param title         分享的标题，最多200个字符
 * @param targetUrl     需要跳转的链接，URL字符串
 * @param summary       分享的摘要，最多600字符
 * @param imageUrl      分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
 */
data class QZoneImageAndTextParams(
    val title: String,
    val targetUrl: String,
    val summary: String = "",
    val imageUrl: ArrayList<String>? = null
) : ImageAndTextParams