package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.like.retrofit.RequestConfig
import com.like.retrofit.RetrofitUtils
import com.like.retrofit.interceptor.BaseParamsInterceptor
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.SingletonHolder
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.jvm.functions.FunctionN

class WxLogin private constructor(activity: Activity) : LoginStrategy(activity) {
    companion object : SingletonHolder<WxLogin>(object : FunctionN<WxLogin> {
        override val arity: Int = 1 // number of arguments that must be passed to constructor

        override fun invoke(vararg args: Any?): WxLogin {
            return WxLogin(args[0] as Activity)
        }
    }) {
        var openId = ""
    }

    private val mRetrofitUtils: RetrofitUtils by lazy {
        RetrofitUtils(
            customRequestConfig = RequestConfig.Builder()
                .application(activity.application)
                .scheme(RequestConfig.SCHEME_HTTPS)
                .ip("api.weixin.qq.com")
                .interceptors(BaseParamsInterceptor())// 这里可以设置拦截器。库中默认已经实现了3个基础拦截器
                .build()
        )
    }

    private val mWxApi: IWXAPI by lazy { ApiFactory.createWxApi(applicationContext, ThirdPartyInit.wxInitParams.appId) }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy {
        return this
    }

    override fun login() {
        getCode()
    }

    override fun logout() {
    }

    private fun getCode() {
        // 获取授权码
        val req = SendAuth.Req()
        // 获取用户个人信息的授权作用域
        req.scope = "snsapi_userinfo"
        // 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
        req.state = "wechat_sdk_demo_test"
        mWxApi.sendReq(req)
    }

    fun onGetCodeSuccess(code: String) {
        // 从后台获取AccessToken
        GlobalScope.launch {
            val result = mRetrofitUtils.getService<RetrofitApi>()?.getAccessTokenByCode(
                mapOf("code" to code)
            )?.await()
            Log.e("WxLogin", result.toString())
        }
    }

    fun onGetCodeCancel() {

    }

    fun onGetCodeFailure(errCode: Int?) {

    }

}