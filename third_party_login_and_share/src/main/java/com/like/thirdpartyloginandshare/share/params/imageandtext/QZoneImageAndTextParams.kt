package com.like.thirdpartyloginandshare.share.params.imageandtext

/**
 * @param title         分享的标题，最多200个字符
 * @param targetUrl     需要跳转的链接，URL字符串
 * @param imageUrls     分享的图片, 可以是本地或者网络图片。（注：图片最多支持9张图片，但是现在只能支持1张图片）。
 * @param summary       分享的摘要，最多600字符
 */
data class QZoneImageAndTextParams(
    val title: String,
    val targetUrl: String,
    val imageUrls: ArrayList<String>,
    val summary: String = ""
) : ImageAndTextParams