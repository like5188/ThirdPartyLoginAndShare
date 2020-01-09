package com.like.thirdpartyloginandshare

import android.content.Context
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.PlatForm
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import java.util.concurrent.atomic.AtomicBoolean

object ThirdPartyInit {
    private val isQqInitialized = AtomicBoolean(false)
    private val isWxInitialized = AtomicBoolean(false)
    private val isWbInitialized = AtomicBoolean(false)
    internal lateinit var qqInitParams: QqInitParams
    internal lateinit var wxInitParams: WxInitParams
    internal lateinit var wbInitParams: WbInitParams

    internal fun checkInit(platForm: PlatForm) {
        when (platForm) {
            PlatForm.QQ -> {
                if (!isQqInitialized.get()) throw IllegalArgumentException("必须先调用initQq()方法初始化QQ")
            }
            PlatForm.QZONE -> {
                if (!isQqInitialized.get()) throw IllegalArgumentException("必须先调用initQq()方法初始化QQ")
            }
            PlatForm.WX -> {
                if (!isWxInitialized.get()) throw IllegalArgumentException("必须先调用initWx()方法初始化微信")
            }
            PlatForm.WX_CIRCLE -> {
                if (!isWxInitialized.get()) throw IllegalArgumentException("必须先调用initWx()方法初始化微信")
            }
            PlatForm.WB -> {
                if (!isWbInitialized.get()) throw IllegalArgumentException("必须先调用initWb()方法初始化微博")
            }
        }
    }

    fun initQq(context: Context, initParams: QqInitParams) {
        qqInitParams = initParams
        if (isQqInitialized.compareAndSet(false, true)) {

        }
    }

    fun initWx(context: Context, initParams: WxInitParams) {
        wxInitParams = initParams
        if (isWxInitialized.compareAndSet(false, true)) {
            ApiFactory.createWxApi(context, initParams.appId).registerApp(initParams.appId)
        }
    }

    fun initWb(context: Context, initParams: WbInitParams) {
        wbInitParams = initParams
        if (isWbInitialized.compareAndSet(false, true)) {
            WbSdk.install(
                context.applicationContext,
                AuthInfo(context.applicationContext, initParams.appKey, initParams.redirectUrl, initParams.scope)
            )
        }
    }

    data class QqInitParams(val appId: String)

    /**
     * @param appId     应用唯一标识，在微信开放平台提交应用审核通过后获得
     * @param secret    应用密钥 AppSecret，在微信开放平台提交应用审核通过后获得
     */
    data class WxInitParams(val appId: String, val secret: String)

    /**
     * @param redirectUrl   当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * @param scope         WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。详情请查看 Demo 中对应的注释。
     */
    data class WbInitParams(val appKey: String, val redirectUrl: String, val scope: String)
}