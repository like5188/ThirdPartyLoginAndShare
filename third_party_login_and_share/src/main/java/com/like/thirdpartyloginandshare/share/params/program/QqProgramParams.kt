package com.like.thirdpartyloginandshare.share.params.program

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * QQ小程序
 * 可以从外部app分享到手Q为一个QQ小程序，并可以指定预览图、文案、小程序路径
 *
 * @param title         分享的标题, 最长30个字符。如果不填，默认使用小程序名称作为标题
 * @param targetUrl     兼容低版本的网页连接
 * @param summary       分享的消息摘要，最长40个字。如果不填，默认使用小程序后台注册的描述作为摘要
 * @param imageUrl      分享预览封面图片的URL或者本地路径
 * @param programAppId  分享的小程序appId，小程序与当前应用必须为同一个主体
 * @param programPath   分享的小程序页面路径，如果不需要指定，请填主页路径
 * @param programType   3表示正式版，1表示体验版
 */
data class QqProgramParams(
    val title: String = "",
    val targetUrl: String,
    val summary: String = "",
    val imageUrl: String,
    val programAppId: String,
    val programPath: String,
    val programType: String = ""
) : ShareParams