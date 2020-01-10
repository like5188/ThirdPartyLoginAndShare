package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.HttpUtils
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import org.json.JSONObject

class WbLogin(private val activity: Activity) : LoginStrategy {
    private val mSsoHandler by lazy { ApiFactory.createWbApi(activity) }
    private var mLoginListener: LoginListener? = null
    private var mOnLoginAndShareListener: OnLoginAndShareListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    override fun setLoginListener(listener: OnLoginAndShareListener): LoginStrategy {
        mOnLoginAndShareListener = listener
        mLoginListener = LoginListener(listener)
        return this
    }

    override fun login() {
        if (!WbSdk.isWbInstall(activity.applicationContext)) {
            mOnLoginAndShareListener?.onFailure("您的手机没有安装微博")
            return
        }
        if (AccessTokenKeeper.readAccessToken(activity.applicationContext).isSessionValid) {
            mOnLoginAndShareListener?.onSuccess()
        } else {
            mSsoHandler.authorize(mLoginListener)
        }
    }

    override fun logout() {
        AccessTokenKeeper.clear(activity.applicationContext)
    }

    fun getUserInfo(onSuccess: (UserInfo) -> Unit, onError: ((String) -> Unit)? = null) {
        val oauth2AccessToken = AccessTokenKeeper.readAccessToken(activity.applicationContext)
        if (oauth2AccessToken.isSessionValid) {
            HttpUtils.requestAsync(
                "https://api.weibo.com/2/users/show.json?access_token=${oauth2AccessToken.token}",
                {
                    if (it.isNullOrEmpty()) {
                        onError?.invoke("用户信息为空")
                    } else {
                        try {
                            val jsonObject = JSONObject(it)
                            val userInfo = UserInfo()
                            userInfo.screen_name = jsonObject.getString("screen_name")
                            userInfo.gender = jsonObject.getString("gender")
                            userInfo.name = jsonObject.getString("name")
                            userInfo.profile_image_url = jsonObject.getString("profile_image_url")
                            userInfo.avatar_large = jsonObject.getString("avatar_large")
                            userInfo.avatar_hd = jsonObject.getString("avatar_hd")
                            onSuccess(userInfo)
                        } catch (e: Exception) {
                            onError?.invoke(e.message ?: "")
                        }
                    }
                },
                onError
            )
        } else {
            onError?.invoke("获取用户信息失败 尚未登录")
        }
    }

    inner class LoginListener(private val listener: OnLoginAndShareListener) : WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken?) {
            if (token != null) {
                if (token.isSessionValid) {
                    listener.onSuccess()
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(activity.applicationContext, token)
                }
            } else {
                listener.onFailure("登录失败 返回为空")
            }
        }

        override fun onFailure(errorMessage: WbConnectErrorMessage) {
            listener.onFailure("登录失败 ${errorMessage.errorMessage}")
        }

        override fun cancel() {
            listener.onCancel()
        }

        fun onSuccess() {
            listener.onSuccess()
        }
    }

    class UserInfo {
        /**
         * 用户昵称
         */
        var screen_name = ""
        /**
         * 性别，m：男、f：女、n：未知
         */
        var gender = ""
        var name = ""
        /**
         * 用户头像地址（中图），50×50像素
         */
        var profile_image_url = ""
        /**
         * 用户头像地址（大图），180×180像素
         */
        var avatar_large = ""
        /**
         * 用户头像地址（高清），高清头像原图
         */
        var avatar_hd = ""
    }
}