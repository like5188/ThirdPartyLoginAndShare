package com.like.thirdpartyloginandshare.login

import android.app.Activity
import android.content.Intent
import com.like.thirdpartyloginandshare.util.ApiFactory
import com.like.thirdpartyloginandshare.util.HttpUtils
import com.like.thirdpartyloginandshare.util.OnLoginAndShareListener
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage

class WbLogin(private val activity: Activity) : ILoginStrategy {
    private val mSsoHandler by lazy { ApiFactory.createWbApi(activity) }
    private var mLoginListener: LoginListener? = null
    private var mOnLoginAndShareListener: OnLoginAndShareListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    override fun setLoginListener(listener: OnLoginAndShareListener) {
        mOnLoginAndShareListener = listener
        mLoginListener = LoginListener(listener)
    }

    override fun login() {
        if (AccessTokenKeeper.readAccessToken(activity.applicationContext).isSessionValid) {
            mOnLoginAndShareListener?.onSuccess()
        } else {
            // 此种授权方式会根据手机是否安装微博客户端来决定使用sso授权还是网页授权，
            // 如果安装有微博客户端 则调用微博客户端授权，否则调用Web页面方式授权
            mSsoHandler.authorize(mLoginListener)
        }
    }

    override fun logout() {
        AccessTokenKeeper.clear(activity.applicationContext)
    }

    /**
     * 数据结构参考：https://open.weibo.com/wiki/2/users/show
     */
    override fun getData(dataType: DataType, params: Map<String, Any>?, onSuccess: (String) -> Unit, onError: ((String) -> Unit)?) {
        when (dataType) {
            USER_INFO -> {
                // 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
                val oauth2AccessToken = AccessTokenKeeper.readAccessToken(activity.applicationContext)
                if (!oauth2AccessToken.isSessionValid) {
                    onError?.invoke("尚未登录微博")
                    return
                }
                if (params.isNullOrEmpty()) {
                    onError?.invoke("参数错误，uid与screen_name二者必选其一，且只能选其一")
                    return
                }
                val first = params.entries.first()
                HttpUtils.requestAsync(
                    "https://api.weibo.com/2/users/show.json?access_token=${oauth2AccessToken.token}&${first.key}=${first.value}",
                    {
                        onSuccess(it ?: "")
                    },
                    onError
                )
            }
            UNION_ID -> {
                throw UnsupportedOperationException("WB不支持此操作")
            }
        }
    }

    inner class LoginListener(private val listener: OnLoginAndShareListener) : WbAuthListener {
        override fun onSuccess(token: Oauth2AccessToken?) {
            if (token != null) {
                if (token.isSessionValid) {
                    listener.onSuccess()
                    // 保存 Token 到 SharedPreferences
                    AccessTokenKeeper.writeAccessToken(activity.applicationContext, token)
                } else {
                    listener.onFailure("登录失败 token无效")
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