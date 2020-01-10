package com.like.thirdpartyloginandshare.share.params.music

import com.like.thirdpartyloginandshare.share.params.ShareParams

/**
 * 音乐。
 * 分享后，发送方和接收方在聊天窗口中点击消息气泡即可开始播放音乐
 *
 * @param title     分享的标题, 最长30个字符。
 * @param audioUrl  音乐文件的远程链接, 以URL的形式传入, 不支持本地音乐。
 * @param targetUrl 这条分享消息被好友点击后的跳转URL。
 * @param summary   分享的消息摘要，最长40个字。
 * @param imageUrl  分享图片的URL或者本地路径
 * @param arkStr    ARK JSON格式字符串。
 * app:ARK应用名称，com.tencent.map
 * view:ARK应用对应展示的视图
 * meta:ARK应用元数据，view视图对应需要的JSON字符串元数据
 * {"app":"com.dianping.dpscope","view":"RestaurantShare","meta":{"ShopData":{"shopId":"2511961"}}}
 */
data class QqMusicParams(
    val title: String,
    val audioUrl: String,
    val targetUrl: String,
    val summary: String = "",
    val imageUrl: String = "",
    val arkStr: String = ""
) : ShareParams
