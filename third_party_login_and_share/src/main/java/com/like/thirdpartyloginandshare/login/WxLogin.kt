package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI

class WxLogin(private val activity: Activity) : LoginStrategy {
    private lateinit var mListener: OnLoginAndShareListener
    private val mWxApi: IWXAPI by lazy {
        ApiFactory.createWxApi(activity.applicationContext, ThirdPartyInit.wxInitParams.appId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy {
        this.mListener = listener
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
        // 应用授权作用域，如获取用户个人信息则填写 snsapi_userinfo
        req.scope = "snsapi_userinfo"
        // 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止 csrf 攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加 session 进行校验
        // 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用 sendReq 时传入，由微信终端回传，state 字符串长度不能超过 1K
        req.state = "wechat_sdk_demo_test"
        mWxApi.sendReq(req)
    }

    fun onGetCodeSuccess(code: String) {
        mListener.onSuccess(code)
    }

    fun onGetCodeCancel() {
        mListener.onCancel()
    }

    fun onGetCodeFailure(errStr: String?) {
        mListener.onFailure(errStr ?: "")
    }

}