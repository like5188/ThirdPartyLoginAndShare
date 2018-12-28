package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.APP_KEY
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.like.thirdpartyloginandshare.util.REDIRECT_URL
import com.like.thirdpartyloginandshare.util.SCOPE
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.*
import com.sina.weibo.sdk.auth.sso.SsoHandler


class WbLogin(activity: Activity) : LoginStrategy(activity) {
    private val mAuthInfo = AuthInfo(applicationContext, APP_KEY, REDIRECT_URL, SCOPE)
    private val mSsoHandler = SsoHandler(activity)
    private lateinit var mLoginListener: LoginListener

    init {
        WbSdk.install(applicationContext, mAuthInfo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    override fun setLoginListener(listener: OnLoginAndShareListener) {
        mLoginListener = LoginListener(listener)
    }

    override fun login(listener: OnLoginAndShareListener) {
        if (AccessTokenKeeper.readAccessToken(applicationContext).isSessionValid) {
            mLoginListener.onSuccess()
        } else {
            mSsoHandler.authorize(mLoginListener)
        }
    }

    override fun logout() {
        AccessTokenKeeper.clear(applicationContext)
    }

    inner class LoginListener(private val listener: OnLoginAndShareListener) : WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken?) {
            if (token != null) {
                if (token.isSessionValid) {
                    onSuccess()
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(applicationContext, token)
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

        fun onSuccess() {
            listener.onSuccess()
        }

        fun onFailure(errorMessage: String) {
            listener.onFailure(errorMessage)
        }
    }
}