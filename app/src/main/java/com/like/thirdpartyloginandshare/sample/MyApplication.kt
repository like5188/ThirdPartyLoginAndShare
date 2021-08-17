package com.like.thirdpartyloginandshare.sample

import android.app.Application
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI

import com.umeng.socialize.UMShareConfig


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        UMConfigure.init(
            this, "592b8268e88bad10320009ca", "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
        )//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        // 微信设置
        PlatformConfig.setWeixin("wxa9cce595f2c0b87b", "979610c4b66148e4dd7a46f25f261058");
        PlatformConfig.setWXFileProvider("com.like.thirdpartyloginandshare.fileprovider");
        // QQ设置
        PlatformConfig.setQQZone("101540498", "e957a6694ea10bead40ebc7301275eca");
        PlatformConfig.setQQFileProvider("com.like.thirdpartyloginandshare.fileprovider");
        // 新浪微博设置
        PlatformConfig.setSinaWeibo("1929959086", "376628131f4e598e7b13b682bab35f91", "https://api.weibo.com/oauth2/default.html");
        PlatformConfig.setSinaFileProvider("com.like.thirdpartyloginandshare.fileprovider");

        // 设置每次登录拉取确认界面（目前SDK默认设置为在Token有效期内登录不进行二次授权）
        val config = UMShareConfig()
        config.isNeedAuthOnGetUserInfo(true)
        UMShareAPI.get(this).setShareConfig(config)
    }
}