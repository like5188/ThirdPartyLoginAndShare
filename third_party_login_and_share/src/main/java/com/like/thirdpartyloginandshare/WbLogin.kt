package com.like.thirdpartyloginandshare

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.APP_KEY
import com.like.thirdpartyloginandshare.util.REDIRECT_URL
import com.like.thirdpartyloginandshare.util.SCOPE
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.*
import com.sina.weibo.sdk.auth.sso.SsoHandler


class WbLogin(private val activity: Activity, private val mLoginListener: LoginListener) {
    private val mAuthInfo = AuthInfo(activity.applicationContext, APP_KEY, REDIRECT_URL, SCOPE)
    private val mSsoHandler = SsoHandler(activity)

    init {
        WbSdk.install(activity.applicationContext, mAuthInfo)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    fun login() {
        if (AccessTokenKeeper.readAccessToken(activity.applicationContext).isSessionValid) {
            mLoginListener.onSuccess()
        } else {
            mSsoHandler.authorize(mLoginListener)
        }
    }

    fun logout() {
        AccessTokenKeeper.clear(activity.applicationContext)
    }

    abstract inner class LoginListener : WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken?) {
            if (token != null) {
                if (token.isSessionValid) {
                    onSuccess()
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(activity.applicationContext, token)
                }
            } else {
                onFailure("登录失败 返回为空")
            }
        }

        override fun cancel() {
            onFailure("取消登录")
        }

        override fun onFailure(errorMessage: WbConnectErrorMessage) {
            onFailure("登录失败 ${errorMessage.errorMessage}")
        }

        abstract fun onSuccess()

        abstract fun onFailure(errorMessage: String)
    }
}