#### 最新版本

模块|ThirdPartyLoginAndShare
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/ThirdPartyLoginAndShare.svg)](https://jitpack.io/#like5188/ThirdPartyLoginAndShare)

## 功能介绍

1、集成了第三方登录和分享。

2、登录包括：QQ、微信、微博。

3、分享包括：QQ、QQ空间、微信、微信朋友圈、微博

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
        compile 'com.github.like5188:ThirdPartyLoginAndShare:版本号'
    }
```

2、QQ登录分享配置
```java
    defaultConfig {
        ...
        manifestPlaceholders = [tencentAuthId: "tencent123456"]// tencent+appid
    }
```

3、初始化
```java
    ThirdPartyInit.initQq(context, ThirdPartyInit.QqInitParams("appid"))
    ThirdPartyInit.initWx(context, ThirdPartyInit.WxInitParams("appid"))
    ThirdPartyInit.initWb(context, ThirdPartyInit.WbInitParams("appKey", "redirectUrl", "scope"))
```

4、登录、获取用户信息
```java
    private val mThirdPartyLogin: ThirdPartyLogin by lazy { ThirdPartyLogin() }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mThirdPartyLogin.onActivityResult(requestCode, resultCode, data)
    }

    // QQ登录，其它平台登录详见例子
    mThirdPartyLogin
        .strategy(QqLogin(this))
        .listener(object : OnLoginAndShareListener {
            override fun onSuccess() {
                toast("QQ登录成功")
            }

            override fun onFailure(errorMessage: String) {
                toast("QQ登录失败：$errorMessage")
            }

            override fun onCancel() {
                toast("取消QQ登录")
            }
        })
        .login()

    // QQ获取用户信息，其它平台获取用户详见例子
    mThirdPartyLogin.strategy(QqLogin(this)).getData(
         onSuccess = {
             Log.w(TAG, it)
         },
         onError = {
             Log.e(TAG, it)
         })
```

5、分享
```java
    private val mThirdPartyShare: ThirdPartyShare by lazy { ThirdPartyShare() }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mThirdPartyShare.onActivityResult(requestCode, resultCode, data)
    }

    // QQ分享，其它平台分享详见例子
    mThirdPartyShare
        .strategy(QqShare(this))
        .listener(object : OnLoginAndShareListener {
            override fun onSuccess() {
                toast("QQ分享成功")
            }

            override fun onFailure(errorMessage: String) {
                toast("QQ分享失败：$errorMessage")
            }

            override fun onCancel() {
                toast("取消QQ分享")
            }
        })
        .share(QqImageParams("${getExternalFilesDir(null)}/aaa.png"))
```