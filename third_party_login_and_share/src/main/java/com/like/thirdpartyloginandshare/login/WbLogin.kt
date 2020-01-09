package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage

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
}