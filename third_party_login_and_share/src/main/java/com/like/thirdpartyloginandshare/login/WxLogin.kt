package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.WX_APP_ID
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WxLogin(activity: Activity) : LoginStrategy(activity) {
    val mWxApi: IWXAPI by lazy {
        WXAPIFactory.createWXAPI(applicationContext, WX_APP_ID, true)
    }

    init {
        mWxApi.registerApp(WX_APP_ID)
    }

    override fun login(listener: OnLoginListener) {
        getCode()
    }

    override fun logout() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    }

    fun onGetCodeFailure(errCode: Int?) {

    }

}