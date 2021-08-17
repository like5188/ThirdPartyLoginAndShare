#### 最新版本

模块|ThirdPartyLoginAndShare
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/ThirdPartyLoginAndShare.svg)](https://jitpack.io/#like5188/ThirdPartyLoginAndShare)

## 功能介绍

1、集成了友盟社会化分享 SDK。

## 使用方法：

1、引用

在Project的gradle中加入：
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
在Module的gradle中加入：
```groovy
    dependencies {
        implementation 'com.github.like5188:ThirdPartyLoginAndShare:版本号'
    }
```

2、创建第三方平台应用
大部分社交平台进行分享、登录操作首先需要在第三方平台创建应用并提交审核，
创建应用后，分享、登录操作时显示的应用icon、名称和对应开放平台设置有关，
必须要创建应用的平台为：微信、QQ、新浪、钉钉、企业微信、支付宝、Facebook、Kakao、LinkeIn、Twitter，
创建应用方法请参考：https://developer.umeng.com/docs/128606/detail/193651

3、QQ登录分享配置
```java
    defaultConfig {
        ...
        manifestPlaceholders = [qqappid: "101540498"]
    }
```

4、初始化
```java
    // 为保证您的在集成【友盟+】统计SDK之后，能够满足工信部相关合规要求，您应确保在App安装后首次冷启动时，在Applicaiton.onCreate函数中调用预初始化函数UMConfigure.preInit()，并弹出隐私授权弹窗给您的App用户。
    // 预初始化函数不会采集设备信息，也不会向友盟后台上报数据，同时preInit耗时极少，不会影响冷启动体验。所以务必在Applicaiton.onCreate函数中调用，否则统计日活是不准确的！
    UMConfigure.preInit(this, "592b8268e88bad10320009ca", "umeng")

    // 如果开发者调用kill或者exit之类的方法杀死进程，或者双击back键会杀死进程，请务必在此之前调用onKillProcess方法，用来保存统计数据。
    // 程序退出时，用于保存统计数据的API：public static void onKillProcess(Context context);

    /**
     * 确保App首次冷启动时，在用户阅读您的《隐私政策》并取得用户授权之后，
     * 才调用正式初始化函数UMConfigure.init()初始化统计SDK，此时SDK才会真正采集设备信息并上报数据。
     * 反之，如果用户不同意《隐私政策》授权，则不能调用UMConfigure.init()初始化函数。
     * 请注意调用正式初始化函数UMConfigure.init()之前，不要调用UMShareAPI接口类的任何API方法。
     */
    val isPrivacyPolicyAgreement = true
    if (isPrivacyPolicyAgreement) {
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
```

4、登录获取用户信息
```java
    UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, mUMAuthListener)
```

5、分享
```java
    ShareAction(this)
        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
        .setCallback(mUMShareListener)
        .withText("hello")
        .withMedia(UMImage(this, File(getExternalFilesDir(null), "aaa.png")))
        .open()
```