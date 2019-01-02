package com.like.thirdpartyloginandshare.sample.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.login.WxLogin
import com.like.thirdpartyloginandshare.share.WxShare
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * 如果你的程序需要接收微信发送的请求，或者接收发送到微信请求的响应结果
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {
    private val mWxApi: IWXAPI by lazy { ApiFactory.createWxApi(applicationContext, ThirdPartyInit.wxInitParams.appId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        try {
            // 第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，
            // 如果返回值为false，则说明入参不合法未被SDK处理，
            // 应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
            if (!mWxApi.handleIntent(intent, this)) {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    override fun onResp(resp: BaseResp?) {
        if (resp?.type == 2) {// 分享
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> WxShare.getInstance(this).onShareSuccess()
                BaseResp.ErrCode.ERR_USER_CANCEL -> WxShare.getInstance(this).onCancel()
                else -> WxShare.getInstance(this).onShareFailure(resp.errCode)
            }
        } else if (resp?.type == 1) {// 登录
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> WxLogin.getInstance(this).onGetCodeSuccess((resp as SendAuth.Resp).code)// 获取授权码
                BaseResp.ErrCode.ERR_USER_CANCEL -> WxLogin.getInstance(this).onGetCodeCancel()
                else -> WxLogin.getInstance(this).onGetCodeFailure(resp.errCode)
            }
        }
        finish()
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(p0: BaseReq?) {
    }
}