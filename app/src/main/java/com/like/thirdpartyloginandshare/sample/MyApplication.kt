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
            this, "5a12384aa40fa3551f0001d1", "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
        )//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        // 微信设置
        PlatformConfig.setWeixin("wxa9cce595f2c0b87b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setWXFileProvider("com.like.thirdpartyloginandshare.fileprovider");
        // QQ设置
        PlatformConfig.setQQZone("101540498", "5d63ae8858f1caab67715ccd6c18d7a5");
        PlatformConfig.setQQFileProvider("com.like.thirdpartyloginandshare.fileprovider");
        // 新浪微博设置
        PlatformConfig.setSinaWeibo("1929959086", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        PlatformConfig.setSinaFileProvider("com.like.thirdpartyloginandshare.fileprovider");

        // 设置每次登录拉取确认界面（目前SDK默认设置为在Token有效期内登录不进行二次授权）
        val config = UMShareConfig()
        config.isNeedAuthOnGetUserInfo(true)
        UMShareAPI.get(this).setShareConfig(config)
    }
}