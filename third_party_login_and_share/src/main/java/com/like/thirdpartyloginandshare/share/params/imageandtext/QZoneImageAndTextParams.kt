package com.like.thirdpartyloginandshare.share.params.imageandtext

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * @param title         分享的标题，最多200个字符
 * @param targetUrl     需要跳转的链接，URL字符串
 * @param imageUrls     分享的图片。（最多支持9张图片，多余的会被丢弃）（注意：QZone接口暂不支持发送多张图片的能力，若传入多张图片，则会自动选入第一张图片作为预览图。多图的能力将会在以后支持。）
 * @param summary       分享的摘要，最多600字符
 */
data class QZoneImageAndTextParams(
    val title: String,
    val targetUrl: String,
    val imageUrls: ArrayList<String>,
    val summary: String = ""
) : ShareParams