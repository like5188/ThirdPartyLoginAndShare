package com.like.thirdpartyloginandshare.util

// QQ
const val QQ_APP_ID = "101540498"

// 微信
const val WX_APP_ID = "2"
const val WX_OPEN_ID = "3"

// 微博
const val APP_KEY = "1929959086"
/**
 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
 */
const val REDIRECT_URL = "https://api.weibo.com/oauth2/default.html"
/**
 * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
 * 详情请查看 Demo 中对应的注释。
 */
const val SCOPE = "email,direct_messages_read,direct_messages_write," +
        "friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
        "follow_app_official_microblog," +
        "invitation_write"