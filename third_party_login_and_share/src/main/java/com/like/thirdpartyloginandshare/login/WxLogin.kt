package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.like.thirdpartyloginandshare.ThirdPartyInit
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.NetworkUtil
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

class WxLogin(private val activity: Activity) : LoginStrategy {
    companion object {
        private const val BASE_URL = "https://api.weixin.qq.com/"
        private lateinit var mLoginListener: OnLoginAndShareListener
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
                mLoginListener.onFailure("code is null or empty")
                return
            }
            val getTokenUrl =
                "${BASE_URL}sns/oauth2/access_token?appid=${ThirdPartyInit.wxInitParams.appId}&secret=${ThirdPartyInit.wxInitParams.secret}&code=$code&grant_type=authorization_code"
            NetworkUtil.sendWxAPI(handler, getTokenUrl, NetworkUtil.GET_TOKEN)
        }

        private fun checkToken(accessToken: String?, openId: String?) {
            if (accessToken.isNullOrEmpty() || openId.isNullOrEmpty()) {
                mLoginListener.onFailure("accessToken or openId is null or empty")
                return
            }
            val checkTokenUrl = "${BASE_URL}sns/auth?access_token=$accessToken&openid=$openId"
            NetworkUtil.sendWxAPI(handler, checkTokenUrl, NetworkUtil.CHECK_TOKEN)
        }

        private fun refreshToken(refreshToken: String?) {
            if (refreshToken.isNullOrEmpty()) {
                mLoginListener.onFailure("refreshToken is null or empty")
                return
            }
            val refreshTokenUrl =
                "${BASE_URL}sns/oauth2/refresh_token?appid=${ThirdPartyInit.wxInitParams.appId}&grant_type=refresh_token&refresh_token=$refreshToken"
            NetworkUtil.sendWxAPI(handler, refreshTokenUrl, NetworkUtil.REFRESH_TOKEN)
        }

        /**
         * 此接口用于获取用户个人信息。
         * 开发者可通过 OpenID 来获取用户基本信息。
         * 特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，可通过获取用户基本信息中的 unionid 来区分用户的唯一性，
         * 因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，用户的 unionid 是唯一的。
         * 换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid 是相同的。
         * 请注意，在用户修改微信头像后，旧的微信头像 URL 将会失效，
         * 因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像 URL 失效后的异常情况。
         */
        private fun getInfo(accessToken: String?, openId: String?) {
            if (accessToken.isNullOrEmpty() || openId.isNullOrEmpty()) {
                mLoginListener.onFailure("accessToken or openId is null or empty")
                return
            }
            val getInfoUrl = "${BASE_URL}sns/userinfo?access_token=$accessToken&openid=$openId"
            NetworkUtil.sendWxAPI(handler, getInfoUrl, NetworkUtil.GET_INFO)
        }

        private fun getEncode(str: String): String {
            val encodeArray = arrayOf("GB2312", "ISO-8859-1", "UTF-8", "GBK", "Big5", "UTF-16LE", "Shift_JIS", "EUC-JP")
            for (i in encodeArray.indices) {
                try {
                    val charset = Charset.forName(encodeArray[i])
                    if (str == String(str.toByteArray(charset), charset)) {
                        return encodeArray[i]
                    }
                } catch (e: Exception) {
                } finally {
                }
            }
            return ""
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
                            mLoginListener.onFailure(e.message ?: "get token error")
                        }
                    }
                    NetworkUtil.CHECK_TOKEN -> {
                        try {
                            val result = msg.data.getString("result") ?: ""
                            val json = JSONObject(result)
                            val errCode = json.getInt("errcode")
                            if (errCode == 0) {
                                getInfo(accessToken, openId)
                            } else {
                                refreshToken(refreshToken)
                            }
                        } catch (e: JSONException) {
                            mLoginListener.onFailure(e.message ?: "check token error")
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
                            getInfo(accessToken, openId)
                        } catch (e: JSONException) {
                            mLoginListener.onFailure(e.message ?: "refresh token error")
                        }
                    }
                    NetworkUtil.GET_INFO -> {
                        try {
                            val result = msg.data.getString("result") ?: ""
                            val json = JSONObject(result)
                            val userInfo = UserInfo()
                            userInfo.headimgurl = json.getString("headimgurl")
                            val encode = getEncode(json.getString("nickname"))
                            userInfo.nickname = String(json.getString("nickname").toByteArray(charset(encode)), Charset.forName("utf-8"))
                            userInfo.sex = json.getString("sex")
                            userInfo.province = json.getString("province")
                            userInfo.city = json.getString("city")
                            userInfo.country = json.getString("country")
                            userInfo.unionid = json.getString("unionid")
                            mLoginListener.onSuccess(userInfo)
                        } catch (e: JSONException) {
                            mLoginListener.onFailure(e.message ?: "get info error")
                        }
                    }
                }
            }
        }

        class UserInfo {
            var nickname = ""
            var sex = ""
            var province = ""
            var city = ""
            var country = ""
            /**
             * 用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空
             */
            var headimgurl = ""
            /**
             * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的。
             */
            var unionid = ""
        }
    }

    private val mWxApi: IWXAPI by lazy {
        ApiFactory.createWxApi(activity.applicationContext, ThirdPartyInit.wxInitParams.appId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy {
        mLoginListener = listener
        return this
    }

    /**
     * 授权并获取到用户个人信息才算登录成功
     */
    override fun login() {
        if (!mWxApi.isWXAppInstalled) {
            mLoginListener.onFailure("您的手机没有安装微信")
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

    internal fun onGetCodeSuccess(code: String?) {
        getToken(code)
    }

    internal fun onGetCodeCancel() {
        mLoginListener.onCancel()
    }

    internal fun onGetCodeFailure(errStr: String?) {
        mLoginListener.onFailure(errStr ?: "")
    }

}