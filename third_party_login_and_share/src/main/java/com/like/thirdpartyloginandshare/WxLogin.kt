package com.like.thirdpartyloginandshare

import android.content.Context
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlin.jvm.functions.FunctionN

class WxLogin(private val context: Context) {
    companion object : SingletonHolder<WxLogin>(object : FunctionN<WxLogin> {
        override val arity: Int = 1 // number of arguments that must be passed to constructor

        override fun invoke(vararg args: Any?): WxLogin {
            return WxLogin(args[0] as Context)
        }
    })

    val mWxApi: IWXAPI by lazy {
        WXAPIFactory.createWXAPI(context, WX_APP_ID, true)
    }

    init {
        mWxApi.registerApp(WX_APP_ID)
    }

    fun login() {
        // 获取授权码
        val req = SendAuth.Req()
        // 获取用户个人信息的授权作用域
        req.scope = "snsapi_userinfo"
        // 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
        req.state = "wechat_sdk_demo_test"
        mWxApi.sendReq(req)
    }

    fun onGetCodeSuccess(code: String) {

    }

    fun onGetCodeFailure(errCode: Int?) {

    }

}