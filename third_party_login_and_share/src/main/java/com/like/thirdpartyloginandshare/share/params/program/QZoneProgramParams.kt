package com.like.thirdpartyloginandshare.share.params.program

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * QQ小程序
 * 可以从外部app分享到手Q空间为一个QQ小程序，并可以指定预览图、文案、小程序路径
 *
 * @param title         分享的标题, 最长30个字符。如果不填，默认使用小程序名称作为标题
 * @param targetUrl     兼容低版本的网页连接
 * @param summary       分享的消息摘要，最长40个字。如果不填，默认使用小程序后台注册的描述作为摘要
 * @param imageUrls     分享的图片。（最多支持9张图片，多余的会被丢弃）（注意：QZone接口暂不支持发送多张图片的能力，若传入多张图片，则会自动选入第一张图片作为预览图。多图的能力将会在以后支持。）
 * @param programAppId  分享的小程序appId，小程序与当前应用必须为同一个主体
 * @param programPath   分享的小程序页面路径，如果不需要指定，请填主页路径
 * @param programType   3表示正式版，1表示体验版
 */
data class QZoneProgramParams(
    val title: String = "",
    val targetUrl: String,
    val summary: String = "",
    val imageUrls: ArrayList<String>,
    val programAppId: String,
    val programPath: String,
    val programType: String = ""
) : ShareParams