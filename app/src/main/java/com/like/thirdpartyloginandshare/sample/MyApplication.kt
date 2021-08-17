package com.like.thirdpartyloginandshare.sample

import android.app.Application
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI

import com.umeng.socialize.UMShareConfig


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         *
         * 参数解释：
         * 1、appkey和channl必须保持和预初始化一致！！！
         * 2、deviceType：设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子
         * 3、pushSecret：Push推送业务的secret
         */
        UMConfigure.init(
            this, "592b8268e88bad10320009ca", "umeng", UMConfigure.DEVICE_TYPE_PHONE, ""
        )

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