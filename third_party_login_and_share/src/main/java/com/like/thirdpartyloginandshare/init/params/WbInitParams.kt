package com.like.thirdpartyloginandshare.init.params

/**
 * @param redirectUrl   当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。建议使用默认回调页：https://api.weibo.com/oauth2/default.html
 * @param scope         WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。详情请查看 Demo 中对应的注释。
 */
data class WbInitParams(
    val appKey: String,
    val redirectUrl: String,
    val scope: String
) : InitParams