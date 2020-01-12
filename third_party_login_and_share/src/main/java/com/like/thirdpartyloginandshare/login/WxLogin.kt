package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.HttpUtils
import com.like.thirdpartyloginandshare.util.NetworkUtil
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import org.json.JSONException
import org.json.JSONObject

class WxLogin(private val activity: Activity) : LoginStrategy {
    companion object {
        private const val BASE_URL = "https://api.weixin.qq.com/"
        private var mOnLoginAndShareListener: OnLoginAndShareListener? = null
        private val handler: MyHandler = MyHandler()
        /**
         * 授权用户唯一标识
         */
        private var openId: String? = null
        /**
         * 接口调用凭证
         *
         * access_token 有效期（目前为 2 个小时）较短，当 access_token 超时后，可以使用 refresh_token 进行刷新，
         * access_token 刷新结果有两种：
         * 1. 若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
         * 2. 若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
         * refresh_token 拥有较长的有效期（30 天），当 refresh_token 失效的后，需要用户重新授权。
         */
        private var accessToken: String? = null
        /**
         * 用户刷新 access_token
         */
        private var refreshToken: String? = null
        /**
         * 用户授权的作用域，使用逗号（,）分隔
         */
        private var scope: String? = null

        /**
         * @param code  code的超时时间为10分钟，一个code只能成功换取一次access_token即失效
         */
        private fun getToken(code: String?) {
            if (code.isNullOrEmpty()) {
                mOnLoginAndShareListener?.onFailure("code is null or empty")
                return
            }
            val getTokenUrl =
                "${BASE_URL}sns/oauth2/access_token?appid=${ThirdPartyInit.wxInitParams.appId}&secret=${ThirdPartyInit.wxInitParams.secret}&code=$code&grant_type=authorization_code"
            NetworkUtil.sendWxAPI(handler, getTokenUrl, NetworkUtil.GET_TOKEN)
        }

        private fun checkToken(accessToken: String?, openId: String?) {
            if (accessToken.isNullOrEmpty() || openId.isNullOrEmpty()) {
                mOnLoginAndShareListener?.onFailure("accessToken or openId is null or empty")
                return
            }
            val checkTokenUrl = "${BASE_URL}sns/auth?access_token=$accessToken&openid=$openId"
            NetworkUtil.sendWxAPI(handler, checkTokenUrl, NetworkUtil.CHECK_TOKEN)
        }

        private fun refreshToken(refreshToken: String?) {
            if (refreshToken.isNullOrEmpty()) {
                mOnLoginAndShareListener?.onFailure("refreshToken is null or empty")
                return
            }
            val refreshTokenUrl =
                "${BASE_URL}sns/oauth2/refresh_token?appid=${ThirdPartyInit.wxInitParams.appId}&grant_type=refresh_token&refresh_token=$refreshToken"
            NetworkUtil.sendWxAPI(handler, refreshTokenUrl, NetworkUtil.REFRESH_TOKEN)
        }

        private class MyHandler : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    NetworkUtil.GET_TOKEN -> {
                        try {
                            val result = msg.data.getString("result") ?: ""
                            val json = JSONObject(result)
                            openId = json.getString("openid")
                            accessToken = json.getString("access_token")
                            refreshToken = json.getString("refresh_token")
                            scope = json.getString("scope")
                            checkToken(accessToken, openId)
                        } catch (e: JSONException) {
                            mOnLoginAndShareListener?.onFailure(e.message ?: "get token error")
                        }
                    }
                    NetworkUtil.CHECK_TOKEN -> {
                        try {
                            val result = msg.data.getString("result") ?: ""
                            val json = JSONObject(result)
                            val errCode = json.getInt("errcode")
                            if (errCode == 0) {
                                mOnLoginAndShareListener?.onSuccess()
                            } else {
                                refreshToken(refreshToken)
                            }
                        } catch (e: JSONException) {
                            mOnLoginAndShareListener?.onFailure(e.message ?: "check token error")
                        }
                    }
                    NetworkUtil.REFRESH_TOKEN -> {
                        try {
                            val result = msg.data.getString("result") ?: ""
                            val json = JSONObject(result)
                            openId = json.getString("openid")
                            accessToken = json.getString("access_token")
                            refreshToken = json.getString("refresh_token")
                            scope = json.getString("scope")
                            mOnLoginAndShareListener?.onSuccess()
                        } catch (e: JSONException) {
                            mOnLoginAndShareListener?.onFailure(e.message ?: "refresh token error")
                        }
                    }
                }
            }
        }
    }

    private val mWxApi: IWXAPI by lazy {
        ApiFactory.createWxApi(activity.applicationContext, ThirdPartyInit.wxInitParams.appId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy {
        mOnLoginAndShareListener = listener
        return this
    }

    override fun login() {
        if (!mWxApi.isWXAppInstalled) {
            mOnLoginAndShareListener?.onFailure("您的手机没有安装微信")
            return
        }
        // 获取授权码
        val req = SendAuth.Req()
        // 应用授权作用域，如获取用户个人信息则填写 snsapi_userinfo
        req.scope = "snsapi_userinfo"
        // 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止 csrf 攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加 session 进行校验
        // 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用 sendReq 时传入，由微信终端回传，state 字符串长度不能超过 1K
        req.state = "wechat_sdk_demo_test"
        mWxApi.sendReq(req)
    }

    override fun logout() {
        mWxApi.unregisterApp()
    }

    override fun getData(dataType: DataType, onSuccess: (String) -> Unit, onError: ((String) -> Unit)?) {
        if (accessToken.isNullOrEmpty() || openId.isNullOrEmpty()) {
            onError?.invoke("尚未登录WX")
            return
        }
        when (dataType) {
            USER_INFO -> {
                /*
                 * 此接口用于获取用户个人信息。
                 * 开发者可通过 OpenID 来获取用户基本信息。
                 * 特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，可通过获取用户基本信息中的 unionid 来区分用户的唯一性，
                 * 因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，用户的 unionid 是唯一的。
                 * 换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid 是相同的。
                 * 请注意，在用户修改微信头像后，旧的微信头像 URL 将会失效，
                 * 因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像 URL 失效后的异常情况。
                 */
                HttpUtils.requestAsync(
                    "${BASE_URL}sns/userinfo?access_token=$accessToken&openid=$openId",
                    {
                        onSuccess(it ?: "")
                    },
                    onError
                )
            }
            UNION_ID -> {
                HttpUtils.requestAsync(
                    "${BASE_URL}sns/userinfo?access_token=$accessToken&openid=$openId",
                    {
                        onSuccess(it ?: "")
                    },
                    onError
                )
            }
        }
    }

    internal fun onGetCodeSuccess(code: String?) {
        getToken(code)
    }

    internal fun onGetCodeCancel() {
        mOnLoginAndShareListener?.onCancel()
    }

    internal fun onGetCodeFailure(errStr: String?) {
        mOnLoginAndShareListener?.onFailure(errStr ?: "")
    }

}