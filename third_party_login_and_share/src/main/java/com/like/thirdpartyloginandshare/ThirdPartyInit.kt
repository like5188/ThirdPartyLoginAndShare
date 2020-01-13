package com.like.thirdpartyloginandshare

import android.content.Context
import com.like.thirdpartyloginandshare.util.ApiFactory
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

    fun isQqInitialized() = isQqInitialized.get()
    fun isWxInitialized() = isWxInitialized.get()
    fun isWbInitialized() = isWbInitialized.get()

    fun initQq(context: Context, initParams: QqInitParams) {
        if (isQqInitialized.compareAndSet(false, true)) {
            qqInitParams = initParams
        }
    }

    fun initWx(context: Context, initParams: WxInitParams) {
        if (isWxInitialized.compareAndSet(false, true)) {
            wxInitParams = initParams
            ApiFactory.createWxApi(context, initParams.appId).registerApp(initParams.appId)
        }
    }

    fun initWb(context: Context, initParams: WbInitParams) {
        if (isWbInitialized.compareAndSet(false, true)) {
            wbInitParams = initParams
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
     * @param redirectUrl   当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     * @param scope         Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    data class WbInitParams(
        val appKey: String,
        val redirectUrl: String = "https://api.weibo.com/oauth2/default.html",
        val scope: String = ""
    )
}